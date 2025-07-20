
/* Class457 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;

public class Class457 {
    GraphicsDevice graphicsDevice;
    DisplayMode aDisplayMode5667;

    public void method5960() {
        try {
            if (this.aDisplayMode5667 != null) {
                this.graphicsDevice.setDisplayMode(this.aDisplayMode5667);
                if (!this.graphicsDevice.getDisplayMode().equals(this.aDisplayMode5667))
                    throw new RuntimeException("");
                this.aDisplayMode5667 = null;
            }
            method5963(null, -2144003881);
        } catch (RuntimeException runtimeexception) {
            throw Class346.method4175(runtimeexception, new StringBuilder().append("sv.exit(").append(')').toString());
        }
    }

    public int[] method5961() {
        try {
            DisplayMode[] displaymodes = this.graphicsDevice.getDisplayModes();
            int[] is = new int[displaymodes.length << 2];
            for (int i = 0; i < displaymodes.length; i++) {
                is[i << 2] = displaymodes[i].getWidth();
                is[(i << 2) + 1] = displaymodes[i].getHeight();
                is[2 + (i << 2)] = displaymodes[i].getBitDepth();
                is[3 + (i << 2)] = displaymodes[i].getRefreshRate();
            }
            return is;
        } catch (RuntimeException runtimeexception) {
            throw Class346.method4175(runtimeexception, new StringBuilder().append("sv.listmodes(").append(')').toString());
        }
    }

    public void method5962(Frame frame, int i, int i_0_, int i_1_, int i_2_) {
        try {
            this.aDisplayMode5667 = this.graphicsDevice.getDisplayMode();
            if (null == this.aDisplayMode5667)
                throw new NullPointerException();
            frame.setUndecorated(true);
            frame.enableInputMethods(false);
            method5963(frame, -2144003881);
            if (0 == i_2_) {
                int i_3_ = this.aDisplayMode5667.getRefreshRate();
                DisplayMode[] displaymodes = this.graphicsDevice.getDisplayModes();
                boolean bool = false;
                for (int i_4_ = 0; i_4_ < displaymodes.length; i_4_++) {
                    if (displaymodes[i_4_].getWidth() == i && displaymodes[i_4_].getHeight() == i_0_ && displaymodes[i_4_].getBitDepth() == i_1_) {
                        int i_5_ = displaymodes[i_4_].getRefreshRate();
                        if (!bool || Math.abs(i_5_ - i_3_) < Math.abs(i_2_ - i_3_)) {
                            i_2_ = i_5_;
                            bool = true;
                        }
                    }
                }
                if (!bool)
                    i_2_ = i_3_;
            }
            this.graphicsDevice.setDisplayMode(new DisplayMode(i, i_0_, i_1_, i_2_));
        } catch (RuntimeException runtimeexception) {
            throw Class346.method4175(runtimeexception, new StringBuilder().append("sv.enter(").append(')').toString());
        }
    }

    void method5963(Frame frame, int i) {
        try {
        boolean changed = false;
        try {
            Field field = sun.awt.Win32GraphicsDevice.class.getDeclaredField("valid");
            field.setAccessible(true);

            if ((boolean) field.get(graphicsDevice)) {
                field.set(graphicsDevice, false);
                changed = true;
            }
        } catch (Throwable t) {
            // Ignore - field might not exist or not accessible
        }

        try {
            graphicsDevice.setFullScreenWindow(frame);
        } catch (RuntimeException e) {
            if (changed) {
                try {
                    Field field = sun.awt.Win32GraphicsDevice.class.getDeclaredField("valid");
                    field.set(graphicsDevice, true);
                } catch (Throwable t) { /* ignore */ }
            }
            throw e;
        }

        if (changed) {
            try {
                Field field = sun.awt.Win32GraphicsDevice.class.getDeclaredField("valid");
                field.set(graphicsDevice, true);
            } catch (Throwable t) { /* ignore */
            }
        }
        } catch (RuntimeException runtimeexception) {
            throw Class346.method4175(runtimeexception, new StringBuilder().append("sv.a(").append(')').toString());
        }
    }

    public Class457() throws Exception {
        GraphicsEnvironment graphicsenvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.graphicsDevice = graphicsenvironment.getDefaultScreenDevice();
        if (!this.graphicsDevice.isFullScreenSupported()) {
            GraphicsDevice[] graphicsdevices = graphicsenvironment.getScreenDevices();
            GraphicsDevice[] graphicsdevices_7_ = graphicsdevices;
            for (int i = 0; i < graphicsdevices_7_.length; i++) {
                GraphicsDevice graphicsdevice = graphicsdevices_7_[i];
                if (null != graphicsdevice && graphicsdevice.isFullScreenSupported()) {
                    this.graphicsDevice = graphicsdevice;
                    return;
                }
            }
            throw new Exception();
        }
    }
}
