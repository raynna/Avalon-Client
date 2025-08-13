package net.runelite.client.ui;

import lombok.Getter;

import java.awt.*;
import java.awt.image.*;
import java.util.Arrays;
import java.util.Hashtable;

public class OverlayUiBuffer {

    @Getter
    private Object sprite;
    private int width;
    private int height;
    private int[] pixels;
    private int[] flipped_pixels;
    private Graphics2D graphics;
    private boolean began;

    public OverlayUiBuffer(int width, int height) {
        resize(width, height);
     //   sprite = GLManager.toolkit.create_sprite(pixels, 0, width, width, height, false);
    }

    public Graphics2D begin() {
        began = true;
        clear();
        return graphics;
    }

    public void end() {
        if (!began) {
            return;
        }
        draw();
        began = false;
    }

    private void draw() {
    /*    if (sprite.trimmed_width != width || sprite.trimmed_height != height) {
            sprite.set_pixels(width, height, pixels);
        } else {
            for (int flip_y = 0; flip_y < height; flip_y++) {
                int flip_pixel = flip_y * width;
                int src_pixel = (height - flip_y - 1) * width;
                System.arraycopy(pixels, src_pixel, flipped_pixels, flip_pixel, width);
            }
            sprite.set_pixels(flipped_pixels, false);
        }
        sprite.drawSprite(0, 0);*/
    }

    private void clear() {
        Arrays.fill(pixels, 0);
    }

    public void resize(int width, int height) {
        if (this.width == width && this.height == height) {
            return;
        }
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        flipped_pixels = new int[width * height];
        if (width > 0 && height > 0) {
            DataBufferInt pixelBuffer = new DataBufferInt(pixels, pixels.length);
            DirectColorModel colorModel = new DirectColorModel(32, 0xff0000, 0xff00, 0xff, 0xff000000);
            WritableRaster imageRaster = Raster.createWritableRaster(colorModel.createCompatibleSampleModel(width, height), pixelBuffer, null);
            BufferedImage image = new BufferedImage(colorModel, imageRaster, false, new Hashtable<>());
            graphics = (Graphics2D) image.getGraphics();
        }
    }

    public Graphics2D getGraphics() {
        return graphics;
    }
}
