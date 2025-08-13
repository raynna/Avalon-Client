/*
 * Copyright (c) 2018, Lotto <https://github.com/devLotto>
 * Copyright (c) 2019, Alexsuperfly <https://github.com/Alexsuperfly>
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
package net.runelite.client.util;

import com.jagex.GameClient;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Singleton
public class ImageCapture
{
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	private final GameClient client;

	@Inject
	private ImageCapture(
		final GameClient client
	)
	{
		this.client = client;
	}

	/**
	 * Saves a screenshot of the client window to the screenshot folder as a PNG,
	 * and optionally uploads it to an image-hosting service.
	 *
	 * @param screenshot BufferedImage to capture.
	 * @param fileName Filename to use, without file extension.
	 * @param subDir Directory within the player screenshots dir to store the captured screenshot to.
	 */
	public void takeScreenshot(BufferedImage screenshot, String fileName, @Nullable String subDir)
	{
		/*if (client.getGameState() == GameState.LOGIN_SCREEN)
		{
			// Prevent the screenshot from being captured
			log.info("Login screenshot prevented");
			return;
		}*/

		/*File playerFolder;
		if (client.getLocalPlayer() != null && client.getLocalPlayer().getName() != null)
		{
			String playerDir = client.getLocalPlayer().getName();

			if (!Strings.isNullOrEmpty(subDir))
			{
				playerDir += File.separator + subDir;
			}

			playerFolder = new File(SCREENSHOT_DIR, playerDir);
		}
		else
		{
			playerFolder = SCREENSHOT_DIR;
		}

		playerFolder.mkdirs();

		fileName += (fileName.isEmpty() ? "" : " ") + format(new Date());

		try
		{
			File screenshotFile = new File(playerFolder, fileName + ".png");

			// To make sure that screenshots don't get overwritten, check if file exists,
			// and if it does create file with same name and suffix.
			int i = 1;
			while (screenshotFile.exists())
			{
				screenshotFile = new File(playerFolder, fileName + String.format("(%d)", i++) + ".png");
			}

			ImageIO.write(screenshot, "PNG", screenshotFile);
		}
		catch (IOException ex)
		{
			log.warn("error writing screenshot", ex);
		}*/
	}

	/**
	 * Saves a screenshot of the client window to the screenshot folder as a PNG,
	 * and optionally uploads it to an image-hosting service.
	 *
	 * @param screenshot BufferedImage to capture.
	 * @param fileName Filename to use, without file extension.
	 */
	public void takeScreenshot(BufferedImage screenshot, String fileName)
	{
		takeScreenshot(screenshot, fileName, null);
	}

	private static String format(Date date)
	{
		synchronized (TIME_FORMAT)
		{
			return TIME_FORMAT.format(date);
		}
	}
}
