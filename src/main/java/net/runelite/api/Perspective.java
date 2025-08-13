/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.api;


import com.jagex.GameClient;
import com.jagex.maths.JagexScale;
import net.runelite.api.coords.LocalPoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static net.runelite.api.Constants.TILE_FLAG_BRIDGE;

/**
 * A utility class containing methods to help with conversion between
 * in-game features to canvas areas.
 */
public class Perspective
{
	public static final double UNIT = Math.PI / 1024d; // How much of the circle each unit of SINE/COSINE is

	public static final int LOCAL_COORD_BITS = JagexScale.TILE_BITS;
	public static final int LOCAL_TILE_SIZE = 1 << LOCAL_COORD_BITS; // 128 - size of a tile in local coordinates
	public static final int LOCAL_HALF_TILE_SIZE = LOCAL_TILE_SIZE / 2;

	public static final int SCENE_SIZE = Constants.SCENE_SIZE; // in tiles

	public static final int[] SINE = new int[2048]; // sine angles for each of the 2048 units, * 65536 and stored as an int
	public static final int[] COSINE = new int[2048]; // cosine

	static
	{
		for (int i = 0; i < 2048; ++i)
		{
			SINE[i] = (int) (65536.0D * Math.sin((double) i * UNIT));
			COSINE[i] = (int) (65536.0D * Math.cos((double) i * UNIT));
		}
	}

	/**
	 * Translates two-dimensional ground coordinates within the 3D world to
	 * their corresponding coordinates on the game screen.
	 *
	 * @param client the game client
	 * @param point ground coordinate
	 * @param plane ground plane on the z axis
	 * @return a {@link Point} on screen corresponding to the position in
	 * 3D-space
	 */
	@Nullable
	public static Point localToCanvas(@Nonnull GameClient client, @Nonnull LocalPoint point, int plane)
	{
		return localToCanvas(client, point, plane, 0);
	}

	/**
	 * Translates two-dimensional ground coordinates within the 3D world to
	 * their corresponding coordinates on the game screen.
	 *
	 * @param client the game client
	 * @param point ground coordinate
	 * @param plane ground plane on the z axis
	 * @param zOffset distance from ground on the z axis
	 * @return a {@link Point} on screen corresponding to the position in
	 * 3D-space
	 */
	@Nullable
	public static Point localToCanvas(@Nonnull GameClient client, @Nonnull LocalPoint point, int plane, int zOffset)
	{
		final int tileHeight = getTileHeight(client, point, plane);
		return localToCanvas(client, point.getX(), point.getY(), tileHeight - zOffset);
	}

	/**
	 * Translates three-dimensional local coordinates within the 3D world to
	 * their corresponding coordinates on the game screen.
	 *
	 * @param client the game client
	 * @param x ground coordinate on the x axis
	 * @param y ground coordinate on the y axis
	 * @param z
	 * @return a {@link Point} on screen corresponding to the position in
	 * 3D-space
	 */
	public static Point localToCanvas(@Nonnull GameClient client, int x, int y, int z)
	{
	/*	TODO GameClient.instance.unprojectAbsolute(x, z, y, Viewport.center_x, Viewport.center_y, Scene.fov, Scene.fov);
		if (client.spriteDrawX > -1) {
			return new Point(client.spriteDrawX, client.spriteDrawY);
		}*/
		return null;
	}

	/**
	 * Translates a model's vertices into 2d space
	 */
	public static void modelToCanvas(GameClient client,  int end, int x3dCenter, int y3dCenter, int z3dCenter, int rotate, int[] x3d, int[] y3d, int[] z3d, int[] x2d, int[] y2d)
	{
		for (int i = 0; i < end; i++)
		{
			int x = x3d[i];
			int y = y3d[i];
			int z = z3d[i];

			x += x3dCenter;
			y += y3dCenter;
			z += z3dCenter;
			//TODO
/*
			client.unproject(x, y, z, Viewport.center_x, Viewport.center_y, Scene.fov, Scene.fov);

			if (client.spriteDrawX > -1) {
				x2d[i] = client.spriteDrawX;
				y2d[i] = client.spriteDrawY;
			} else {
				x2d[i] = Integer.MIN_VALUE;
				y2d[i] = Integer.MIN_VALUE;
			}*/
		}
	}


	/**
	 * Calculates the above ground height of a tile point.
	 *
	 * @param client the game client
	 * @param point the local ground coordinate
	 * @param plane the client plane/ground level
	 * @return the offset from the ground of the tile
	 */
	public static int getTileHeight(@Nonnull GameClient client, @Nonnull LocalPoint point, int plane)
	{
		int sceneX = point.getSceneX();
		int sceneY = point.getSceneY();
		if (sceneX >= 0 && sceneY >= 0 && sceneX < SCENE_SIZE && sceneY < SCENE_SIZE)
		{
			byte[][][] tileSettings = client.getTileSettings();
			int[][][] tileHeights = client.getTileHeights();

			int z1 = plane;
			if (plane < Constants.MAX_Z - 1 && (tileSettings[1][sceneX][sceneY] & TILE_FLAG_BRIDGE) == TILE_FLAG_BRIDGE)
			{
				z1 = plane + 1;
			}

			int x = point.getX() & (LOCAL_TILE_SIZE - 1);
			int y = point.getY() & (LOCAL_TILE_SIZE - 1);
			int var8 = x * tileHeights[z1][sceneX + 1][sceneY] + (LOCAL_TILE_SIZE - x) * tileHeights[z1][sceneX][sceneY] >> LOCAL_COORD_BITS;
			int var9 = tileHeights[z1][sceneX][sceneY + 1] * (LOCAL_TILE_SIZE - x) + x * tileHeights[z1][sceneX + 1][sceneY + 1] >> LOCAL_COORD_BITS;
			return (LOCAL_TILE_SIZE - y) * var8 + y * var9 >> LOCAL_COORD_BITS;
		}

		return 0;
	}

	/**
	 * Get the height of a location, in local coordinates. Interpolates the height from the adjacent tiles.
	 * Does not account for bridges.
	 * @param client
	 * @param localX
	 * @param localY
	 * @param plane
	 * @return
	 */
	private static int getHeight(@Nonnull GameClient client, int localX, int localY, int plane)
	{
		int sceneX = localX >> LOCAL_COORD_BITS;
		int sceneY = localY >> LOCAL_COORD_BITS;
		if (sceneX >= 0 && sceneY >= 0 && sceneX < SCENE_SIZE && sceneY < SCENE_SIZE)
		{
			int[][][] tileHeights = client.getTileHeights();

			int x = localX & (LOCAL_TILE_SIZE - 1);
			int y = localY & (LOCAL_TILE_SIZE - 1);
			int var8 = x * tileHeights[plane][sceneX + 1][sceneY] + (LOCAL_TILE_SIZE - x) * tileHeights[plane][sceneX][sceneY] >> LOCAL_COORD_BITS;
			int var9 = tileHeights[plane][sceneX][sceneY + 1] * (LOCAL_TILE_SIZE - x) + x * tileHeights[plane][sceneX + 1][sceneY + 1] >> LOCAL_COORD_BITS;
			return (LOCAL_TILE_SIZE - y) * var8 + y * var9 >> LOCAL_COORD_BITS;
		}

		return 0;
	}

	/**
	 * Calculates a tile polygon from offset worldToScreen() points.
	 *
	 * @param client the game client
	 * @param localLocation local location of the tile
	 * @return a {@link Polygon} on screen corresponding to the given
	 * localLocation.
	 */
	public static Polygon getCanvasTilePoly(@Nonnull GameClient client, @Nonnull LocalPoint localLocation)
	{
		return getCanvasTileAreaPoly(client, localLocation, 1);
	}

	/**
	 * Calculates a tile polygon from offset worldToScreen() points.
	 *
	 * @param client the game client
	 * @param localLocation local location of the tile
	 * @param zOffset offset from ground plane
	 * @return a {@link Polygon} on screen corresponding to the given
	 * localLocation.
	 */
	public static Polygon getCanvasTilePoly(@Nonnull GameClient client, @Nonnull LocalPoint localLocation, int zOffset)
	{
		return getCanvasTileAreaPoly(client, localLocation, 1, 1, client.getPlane(), zOffset);
	}

	/**
	 * Returns a polygon representing an area.
	 *
	 * @param client the game client
	 * @param localLocation the center location of the AoE
	 * @param size the size of the area (ie. 3x3 AoE evaluates to size 3)
	 * @return a polygon representing the tiles in the area
	 */
	public static Polygon getCanvasTileAreaPoly(@Nonnull GameClient client, @Nonnull LocalPoint localLocation, int size)
	{
		return getCanvasTileAreaPoly(client, localLocation, size, size, client.getPlane(), 0);
	}

	/**
	 * Returns a polygon representing an area.
	 *
	 * @param client the game client
	 * @param localLocation the center location of the AoE
	 * @param sizeX the size of the area in tiles on the x axis
	 * @param sizeY the size of the area in tiles on the y axis
	 * @param plane the plane of the area
	 * @param zOffset offset from ground plane
	 * @return a polygon representing the tiles in the area
	 */
	public static Polygon getCanvasTileAreaPoly(
		@Nonnull GameClient client,
		@Nonnull LocalPoint localLocation,
		int sizeX,
		int sizeY,
		int plane,
		int zOffset)
	{
		if (!localLocation.isInScene())
		{
			return null;
		}

		final byte[][][] tileSettings = client.getTileSettings();
		final int sceneX = localLocation.getSceneX();
		final int sceneY = localLocation.getSceneY();

		int tilePlane = plane;
		if (plane < Constants.MAX_Z - 1 && (tileSettings[1][sceneX][sceneY] & TILE_FLAG_BRIDGE) == TILE_FLAG_BRIDGE)
		{
			tilePlane = plane + 1;
		}

		final int swX = localLocation.getX() - (sizeX * LOCAL_TILE_SIZE / 2);
		final int swY = localLocation.getY() - (sizeY * LOCAL_TILE_SIZE / 2);

		final int neX = localLocation.getX() + (sizeX * LOCAL_TILE_SIZE / 2);
		final int neY = localLocation.getY() + (sizeY * LOCAL_TILE_SIZE / 2);

		final int seX = swX;
		final int seY = neY;

		final int nwX = neX;
		final int nwY = swY;

		final int swHeight = getHeight(client, swX, swY, tilePlane) - zOffset;
		final int nwHeight = getHeight(client, nwX, nwY, tilePlane) - zOffset;
		final int neHeight = getHeight(client, neX, neY, tilePlane) - zOffset;
		final int seHeight = getHeight(client, seX, seY, tilePlane) - zOffset;

		Point p1 = localToCanvas(client, swX, swY, swHeight);
		Point p2 = localToCanvas(client, nwX, nwY, nwHeight);
		Point p3 = localToCanvas(client, neX, neY, neHeight);
		Point p4 = localToCanvas(client, seX, seY, seHeight);

		if (p1 == null || p2 == null || p3 == null || p4 == null)
		{
			return null;
		}

		Polygon poly = new Polygon();
		poly.addPoint(p1.getX(), p1.getY());
		poly.addPoint(p2.getX(), p2.getY());
		poly.addPoint(p3.getX(), p3.getY());
		poly.addPoint(p4.getX(), p4.getY());

		return poly;
	}

	/**
	 * Calculates text position and centers depending on string length.
	 *
	 * @param client the game client
	 * @param graphics the game graphics
	 * @param localLocation local location of the tile
	 * @param text string for width measurement
	 * @param zOffset offset from ground plane
	 * @return a {@link Point} on screen corresponding to the given
	 * localLocation.
	 */
	public static Point getCanvasTextLocation(
		@Nonnull GameClient client,
		@Nonnull Graphics2D graphics,
		@Nonnull LocalPoint localLocation,
		@Nullable String text,
		int zOffset)
	{
		if (text == null)
		{
			return null;
		}

		int plane = client.getPlane();

		Point p = localToCanvas(client, localLocation, plane, zOffset);

		if (p == null)
		{
			return null;
		}

		FontMetrics fm = graphics.getFontMetrics();
		Rectangle2D bounds = fm.getStringBounds(text, graphics);
		int xOffset = p.getX() - (int) (bounds.getWidth() / 2);

		return new Point(xOffset, p.getY());
	}

	/**
	 * Calculates image position and centers depending on image size.
	 *
	 * @param client the game client
	 * @param localLocation local location of the tile
	 * @param image image for size measurement
	 * @param zOffset offset from ground plane
	 * @return a {@link Point} on screen corresponding to the given
	 * localLocation.
	 */
	public static Point getCanvasImageLocation(
		@Nonnull GameClient client,
		@Nonnull LocalPoint localLocation,
		@Nonnull BufferedImage image,
		int zOffset)
	{
		int plane = client.getPlane();

		Point p = localToCanvas(client, localLocation, plane, zOffset);

		if (p == null)
		{
			return null;
		}

		int xOffset = p.getX() - image.getWidth() / 2;
		int yOffset = p.getY() - image.getHeight() / 2;

		return new Point(xOffset, yOffset);
	}

	/**
	 * Calculates sprite position and centers depending on sprite size.
	 *
	 * @param client the game client
	 * @param localLocation local location of the tile
	 * @param sprite SpritePixel for size measurement
	 * @param zOffset offset from ground plane
	 * @return a {@link Point} on screen corresponding to the given localLocation.
	 */
	public static Point getCanvasSpriteLocation(
		@Nonnull GameClient client,
		@Nonnull LocalPoint localLocation,
		@Nonnull Object sprite, //client
		int zOffset)
	{
		int plane = client.getPlane();

		Point p = localToCanvas(client, localLocation, plane, zOffset);

		if (p == null)
		{
			return null;
		}

		/*int xOffset = p.getX() - sprite.getWidth() / 2;
		int yOffset = p.getY() - sprite.getHeight() / 2;

		return new Point(xOffset, yOffset);*/
		return new Point(0, 0); //TODO
	}

	public static Point localToMinimap(GameClient instance, LocalPoint localLocation) {
		// FIXME(RuneLite):
		return null;
	}
}
