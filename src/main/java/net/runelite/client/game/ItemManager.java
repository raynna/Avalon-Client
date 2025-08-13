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
package net.runelite.client.game;

import com.jagex.GameClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.util.AsyncBufferedImage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Slf4j
public class ItemManager {

    public int getItemPrice(int id) {
        return 1; // FIXME(RuneLite):
    }

    @Value
    private static class ImageKey {
        private final int itemId;
        private final int itemQuantity;
        private final boolean stackable;
    }

    @Value
    private static class OutlineKey {
        private final int itemId;
        private final int itemQuantity;
        private final Color outlineColor;
    }

    private final GameClient client;
    private final ClientThread clientThread;
    private final RuneLiteConfig runeLiteConfig;

    private final LoadingCache<ImageKey, AsyncBufferedImage> itemImages;
    private final LoadingCache<OutlineKey, BufferedImage> itemOutlines;

    @Inject
    public ItemManager(GameClient client, ScheduledExecutorService scheduledExecutorService, ClientThread clientThread,
                       EventBus eventBus, RuneLiteConfig runeLiteConfig) {
        this.client = client;
        this.clientThread = clientThread;
        this.runeLiteConfig = runeLiteConfig;

        itemImages = CacheBuilder.newBuilder()
                .maximumSize(128L)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build(new CacheLoader<ImageKey, AsyncBufferedImage>() {
                    @Override
                    public AsyncBufferedImage load(ImageKey key) throws Exception {
                        return loadImage(key.itemId, key.itemQuantity, key.stackable);
                    }
                });

        itemOutlines = CacheBuilder.newBuilder()
                .maximumSize(128L)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build(new CacheLoader<OutlineKey, BufferedImage>() {
                    @Override
                    public BufferedImage load(OutlineKey key) throws Exception {
                        return loadItemOutline(key.itemId, key.itemQuantity, key.outlineColor);
                    }
                });

        eventBus.register(this);
    }

    /**
     * Search for tradeable items based on item name
     *
     * @param itemName item name
     * @return
     */
    public List<Object> search(String itemName) {
        itemName = itemName.toLowerCase();
/*
        List<ItemDef> result = new ArrayList<>();
        for (int id = 0; id < ObjTypeList.count; id++) {

            ItemDef def = ObjTypeList.forID(id);
            if (def == null || def.name == null) {
                continue;
            }
            final String name = def.name;
            if (name.toLowerCase().contains(itemName)) {
                result.add(def);
            }
        }*/
        return null;
    }

    /**
     * Look up an item's composition
     *
     * @param itemId item id
     * @return item composition
     */
    @Nonnull
    public Object getItemComposition(int itemId) {
      /*  assert client.isClientThread() : "getItemComposition must be called on client thread";
        return ObjTypeList.forID(itemId);*/
        return null;
    }

    /**
     * Get an item's un-noted, un-placeholdered ID
     */
    public int canonicalize(int itemID) {
       /* ItemDef itemComposition = getItemComposition(itemID);

        if (itemComposition.cert_template != -1) {
            return itemComposition.cert_link;
        }
        if (itemComposition.lent_template != -1) {
            return itemComposition.lent_link;
        }
        if (itemComposition.bought_template != -1) {
            return itemComposition.bought_link;
        }
        if (itemComposition.shard_template != -1) {
            return itemComposition.shard_link;
        }

        return itemID;//WORN_ITEMS.getOrDefault(itemID, itemID);*/
        return -1;
    }

    /**
     * Loads item sprite from game, makes transparent, and generates image
     *
     * @param itemId
     * @return
     */
    private AsyncBufferedImage loadImage(int itemId, int quantity, boolean stackable) {
        return null;
    }

    /**
     * Get item sprite image as BufferedImage.
     * <p>
     * This method may return immediately with a blank image if not called on the game thread.
     * The image will be filled in later. If this is used for a UI label/button, it should be added
     * using AsyncBufferedImage::addTo to ensure it is painted properly
     *
     * @param itemId
     * @return
     */
    public AsyncBufferedImage getImage(int itemId) {
        return getImage(itemId, 1, false);
    }

    /**
     * Get item sprite image as BufferedImage.
     * <p>
     * This method may return immediately with a blank image if not called on the game thread.
     * The image will be filled in later. If this is used for a UI label/button, it should be added
     * using AsyncBufferedImage::addTo to ensure it is painted properly
     *
     * @param itemId
     * @param quantity
     * @return
     */
    public AsyncBufferedImage getImage(int itemId, int quantity, boolean stackable) {
        try {
            return itemImages.get(new ImageKey(itemId, quantity, stackable));
        } catch (ExecutionException ex) {
            return null;
        }
    }

    /**
     * Create item sprite and applies an outline.
     *
     * @param itemId       item id
     * @param itemQuantity item quantity
     * @param outlineColor outline color
     * @return image
     */
    private BufferedImage loadItemOutline(final int itemId, final int itemQuantity, final Color outlineColor) {
      /*  final JavaSprite itemSprite = createItemSprite(itemId, itemQuantity, 1, 0, 0, false, CLIENT_DEFAULT_ZOOM);
        return itemSprite.toBufferedOutline(outlineColor);*/
        return null;
    }
    /**
     * Get item outline with a specific color.
     *
     * @param itemId       item id
     * @param itemQuantity item quantity
     * @param outlineColor outline color
     * @return image
     */
    public BufferedImage getItemOutline(final int itemId, final int itemQuantity, final Color outlineColor) {
        try {
            return itemOutlines.get(new OutlineKey(itemId, itemQuantity, outlineColor));
        } catch (ExecutionException e) {
            return null;
        }
    }

    private Object createItemSprite(int itemId, int quantity, int border, int shadowColor, int stackable, boolean noted, int scale)
    {
        assert client.isClientThread();
        // int zoom = get3dZoom();
        //set3dZoom(scale);
        try
        {
            return null;
        }
        finally
        {
            //set3dZoom(zoom);
        }
    }
}
