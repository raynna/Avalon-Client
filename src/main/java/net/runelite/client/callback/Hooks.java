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
package net.runelite.client.callback;

import com.jagex.GameClient;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.GameTick;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.Notifier;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.task.Scheduler;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayRenderer;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.DeferredEventBus;
import net.runelite.client.util.RSTimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * This class contains field required for mixins and runelite hooks to work.
 * All remaining method hooks in this class are performance-critical or contain client-specific logic and so they
 * can't just be placed in mixins or sent through event bus.
 */
@Singleton
@Slf4j
public class Hooks implements Callbacks {

    private static final long CHECK = RSTimeUnit.GAME_TICKS.getDuration().toNanos(); // ns - how often to run checks
    private static final GameTick GAME_TICK = new GameTick();
    private static final BeforeRender BEFORE_RENDER = new BeforeRender();

    private final GameClient client;
    private final OverlayRenderer renderer;
    private final EventBus eventBus;
    private final DeferredEventBus deferredEventBus;
    private final Scheduler scheduler;
    private final InfoBoxManager infoBoxManager;
    //    private final ChatMessageManager chatMessageManager;
    private final MouseManager mouseManager;
    private final KeyManager keyManager;
    private final ClientThread clientThread;
    private final DrawManager drawManager;
    private final Notifier notifier;
    private final ClientUI clientUi;

    private long lastCheck;
    private boolean ignoreNextNpcUpdate;
    private boolean shouldProcessGameTick;

    private static Object lastMainBufferProvider;
    private static Graphics2D lastGraphics;

    private Graphics2D beginGraphics() {
     /*   if (GLManager.isEnabled()) {
            return renderer.getBuffer().begin();
        }*/
        // FIXME(Walied): fullGameScreen probably doesn't work well in fixed?
      //  RSImageProducer bufferProvider = client.viewportImageProducer;
        return getGraphics(null);
    }

    private void endGraphics() {
      /*  if (GLManager.isEnabled()) {
            renderer.getBuffer().end();
        }*/
    }

    /**
     * Get the Graphics2D for the MainBufferProvider image
     * This caches the Graphics2D instance so it can be reused
     *
     * @param mainBufferProvider
     * @return
     */
    private static Graphics2D getGraphics(Object mainBufferProvider) {
        if (lastGraphics == null || lastMainBufferProvider != mainBufferProvider) {
            if (lastGraphics != null) {
                log.debug("Graphics reset!");
                lastGraphics.dispose();
            }

            lastMainBufferProvider = mainBufferProvider;
           // lastGraphics = (Graphics2D) mainBufferProvider.getImage().getGraphics();
        }
        return lastGraphics;
    }

    @Inject
    private Hooks(
            GameClient client,
            OverlayRenderer renderer,
            EventBus eventBus,
            DeferredEventBus deferredEventBus,
            Scheduler scheduler,
            InfoBoxManager infoBoxManager,
//            ChatMessageManager chatMessageManager,
            MouseManager mouseManager,
            KeyManager keyManager,
            ClientThread clientThread,
            DrawManager drawManager,
            Notifier notifier,
            ClientUI clientUi
    ) {
        this.client = client;
        this.renderer = renderer;
        this.eventBus = eventBus;
        this.deferredEventBus = deferredEventBus;
        this.scheduler = scheduler;
        this.infoBoxManager = infoBoxManager;
//        this.chatMessageManager = chatMessageManager;
        this.mouseManager = mouseManager;
        this.keyManager = keyManager;
        this.clientThread = clientThread;
        this.drawManager = drawManager;
        this.notifier = notifier;
        this.clientUi = clientUi;
        eventBus.register(this);
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }

    @Override
    public void postDeferred(Object event) {
        deferredEventBus.post(event);
    }

    @Override
    public void clientMainLoop() {
        if (shouldProcessGameTick) {
            shouldProcessGameTick = false;

            deferredEventBus.replay();

            eventBus.post(GAME_TICK);

           /* int tick = client.getTickCount();
            client.setTickCount(tick + 1);*/
        }

        eventBus.post(BEFORE_RENDER);

        clientThread.invoke();

        long now = System.nanoTime();

        if (now - lastCheck < CHECK) {
            return;
        }

        lastCheck = now;

        try {
            // tick pending scheduled tasks
            scheduler.tick();

            // cull infoboxes
            infoBoxManager.cull();

            //chatMessageManager.process();

            //checkWorldMap();
        } catch (Exception ex) {
            log.warn("error during main loop tasks", ex);
        }
    }

    @Override
    public void updateNpcs() {
        if (ignoreNextNpcUpdate) {
            // After logging in an NPC update happens outside of the normal game tick, which
            // is sent prior to skills and vars being bursted, so ignore it.
            ignoreNextNpcUpdate = false;
            log.debug("Skipping login updateNpc");
        } else {
            // The NPC update event seem to run every server tick,
            // but having the game tick event after all packets
            // have been processed is typically more useful.
            shouldProcessGameTick = true;
        }

        // Replay deferred events, otherwise if two npc
        // update packets get processed in one client tick, a
        // despawn event could be published prior to the
        // spawn event, which is deferred
        deferredEventBus.replay();
    }

    public void drawInterface(int interfaceId, List<WidgetItem> widgetItems)
    {
        Graphics2D graphics2d = beginGraphics();

        try
        {
            renderer.renderAfterInterface(graphics2d, interfaceId, widgetItems);
        }
        catch (Exception ex)
        {
            log.warn("Error during overlay rendering", ex);
        }
        endGraphics();
    }

    public void drawLayer(Object layer, List<WidgetItem> widgetItems)
    {
        Graphics2D graphics2d = beginGraphics();

        try
        {
            renderer.renderAfterLayer(graphics2d, layer, widgetItems);
        }
        catch (Exception ex)
        {
            log.warn("Error during overlay rendering", ex);
        }
        endGraphics();
    }

    /**
     * Copy an image
     * @param src
     * @return
     */
    private static Image copy(Image src)
    {
        final int width = src.getWidth(null);
        final int height = src.getHeight(null);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.drawImage(src, 0, 0, width, height, null);
        graphics.dispose();
        return image;
    }

    @Override
    public void drawScene() {
        Graphics2D graphics2d = beginGraphics();

        try {
            renderer.renderOverlayLayer(graphics2d, OverlayLayer.ABOVE_SCENE);
        } catch (Exception ex) {
            log.warn("Error during overlay rendering", ex);
        }
        endGraphics();
    }

    @Override
    public void drawAboveOverheads() {
        Graphics2D graphics2d = beginGraphics();

        try {
            renderer.renderOverlayLayer(graphics2d, OverlayLayer.UNDER_WIDGETS);
        } catch (Exception ex) {
            log.warn("Error during overlay rendering", ex);
        }
        endGraphics();
    }

    @Override
    public MouseEvent mousePressed(MouseEvent mouseEvent) {
        return mouseManager.processMousePressed(mouseEvent);
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent mouseEvent) {
        return mouseManager.processMouseReleased(mouseEvent);
    }

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent) {
        return mouseManager.processMouseClicked(mouseEvent);
    }

    @Override
    public MouseEvent mouseEntered(MouseEvent mouseEvent) {
        return mouseManager.processMouseEntered(mouseEvent);
    }

    @Override
    public MouseEvent mouseExited(MouseEvent mouseEvent) {
        return mouseManager.processMouseExited(mouseEvent);
    }

    @Override
    public MouseEvent mouseDragged(MouseEvent mouseEvent) {
        return mouseManager.processMouseDragged(mouseEvent);
    }

    @Override
    public MouseEvent mouseMoved(MouseEvent mouseEvent) {
        return mouseManager.processMouseMoved(mouseEvent);
    }

    @Override
    public MouseWheelEvent mouseWheelMoved(MouseWheelEvent event) {
        return mouseManager.processMouseWheelMoved(event);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        keyManager.processKeyPressed(keyEvent);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        keyManager.processKeyReleased(keyEvent);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        keyManager.processKeyTyped(keyEvent);
    }

    public void draw(Object mainBufferProvider, Graphics graphics, int x, int y) {
        if (graphics == null) {
            return;
        }

        final Graphics2D graphics2d = beginGraphics();

        try {
            renderer.renderOverlayLayer(graphics2d, OverlayLayer.ALWAYS_ON_TOP);
        } catch (Exception ex) {
            log.warn("Error during overlay rendering", ex);
        }

        notifier.processFlash(graphics2d);

        // Draw clientUI overlays
        clientUi.paintOverlays(graphics2d);

        endGraphics();

        /*if (GLManager.isEnabled()) {
            // processDrawComplete gets called on GPU by the gpu plugin at the end of its
            // drawing cycle, which is later on.
            return;
        }

        // Stretch the game image if the user has that enabled
        Image image = mainBufferProvider.getImage();
        final Image finalImage = image;

        // Draw the image onto the game canvas
        graphics.drawImage(finalImage, 0, 0, client.getCanvas());

        // finalImage is backed by the client buffer which will change soon. make a copy
        // so that callbacks can safely use it later from threads.
        drawManager.processDrawComplete(() -> copy(finalImage));*/
    }
}