/*
 * Copyright (c) 2018 Abex
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
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
package net.runelite.client.plugins.info;

import com.jagex.GameClient;
import com.google.common.base.MoreObjects;
import com.google.inject.Inject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.runelite.client.RuneLiteProperties;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

@Singleton
public class InfoPanel extends PluginPanel {

    private static final ImageIcon ARROW_RIGHT_ICON;
    private static final ImageIcon DISCORD_ICON;
    private static final ImageIcon FORUMS_ICON;
    private static final ImageIcon RULES_ICON;
    private static final ImageIcon STORE_ICON;
    private static final ImageIcon VOTE_ICON;
    private static final ImageIcon WIKI_ICON;

    private JPanel actionsContainer;

    @Inject
    @Nullable
    private GameClient client;

    @Inject
    private ScheduledExecutorService executor;

    @Inject
    private ConfigManager configManager;

    @Inject
    @Named("runelite.version")
    private String runeliteVersion;

    @Inject
    @Named("runelite.discord.link")
    private String discordLink;

    @Inject
    @Named("runelite.forums.link")
    private String forumsLink;

    @Inject
    @Named("runelite.rules.link")
    private String rulesLink;

    @Inject
    @Named("runelite.store.link")
    private String storeLink;
    @Inject
    @Named("runelite.vote.link")
    private String voteLink;

    @Inject
    @Named("runelite.wiki.link")
    private String wikiLink;

    static {
        ARROW_RIGHT_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "/util/arrow_right.png"));
        DISCORD_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "discord_icon.png"));
        FORUMS_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "forums_icon.png"));
        RULES_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "rules_icon.png"));
        STORE_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "store_icon.png"));
        VOTE_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "vote_icon.png"));
        WIKI_ICON = new ImageIcon(ImageUtil.loadImageResource(InfoPanel.class, "wiki_icon.png"));
    }

    void init() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel versionPanel = new JPanel();
        versionPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        versionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        versionPanel.setLayout(new GridLayout(0, 1));

        final Font smallFont = FontManager.getRunescapeSmallFont();

        JLabel version = new JLabel(htmlLabel("Client version: ", runeliteVersion));
        version.setFont(smallFont);

        JLabel revision = new JLabel();
        revision.setFont(smallFont);

        String engineVer = "1.0";

        revision.setText(htmlLabel("Server version: ", engineVer));

        JLabel launcher = new JLabel(htmlLabel("Launcher version: ", MoreObjects
                .firstNonNull(RuneLiteProperties.getLauncherVersion(), "1.0")));
        launcher.setFont(smallFont);

        versionPanel.add(version);
        versionPanel.add(revision);
        versionPanel.add(launcher);
        versionPanel.add(Box.createGlue());

        actionsContainer = new JPanel();
        actionsContainer.setBorder(new EmptyBorder(10, 0, 0, 0));
        actionsContainer.setLayout(new GridLayout(0, 1, 0, 10));

        actionsContainer.add(buildLinkPanel(FORUMS_ICON, "Forums", forumsLink));
        actionsContainer.add(buildLinkPanel(DISCORD_ICON, "Discord", discordLink));
        actionsContainer.add(buildLinkPanel(VOTE_ICON, "Vote", voteLink));
        actionsContainer.add(buildLinkPanel(STORE_ICON, "Store", storeLink));

        add(versionPanel, BorderLayout.NORTH);
        add(actionsContainer, BorderLayout.CENTER);
    }

    /**
     * Builds a link panel with a given icon, text and url to redirect to.
     */
    private static JPanel buildLinkPanel(ImageIcon icon, String topText, String url) {
        return buildLinkPanel(icon, topText, () -> LinkBrowser.browse(url));
    }

    /**
     * Builds a link panel with a given icon, text and callable to call.
     */
    private static JPanel buildLinkPanel(ImageIcon icon, String topText, Runnable callback) {
        JPanel container = new JPanel();
        container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(10, 10, 10, 10));

        final Color hoverColor = ColorScheme.DARKER_GRAY_HOVER_COLOR;
        final Color pressedColor = ColorScheme.DARKER_GRAY_COLOR.brighter();

        JLabel iconLabel = new JLabel(icon);
        container.add(iconLabel, BorderLayout.WEST);

        JPanel textContainer = new JPanel();
        textContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        textContainer.setLayout(new GridLayout(1, 1));
        textContainer.setBorder(new EmptyBorder(5, 10, 5, 10));

        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                container.setBackground(pressedColor);
                textContainer.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                callback.run();
                container.setBackground(hoverColor);
                textContainer.setBackground(hoverColor);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                container.setBackground(hoverColor);
                textContainer.setBackground(hoverColor);
                container.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                textContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                container.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        JLabel topLine = new JLabel(topText);
        topLine.setForeground(Color.WHITE);
        topLine.setFont(FontManager.getRunescapeSmallFont());

        textContainer.add(topLine);
        //textContainer.add(bottomLine);

        container.add(textContainer, BorderLayout.CENTER);

        JLabel arrowLabel = new JLabel(ARROW_RIGHT_ICON);
        container.add(arrowLabel, BorderLayout.EAST);

        return container;
    }

    private static String htmlLabel(String key, String value) {
        return "<html><body style = 'color:#a5a5a5'>" + key + "<span style = 'color:white'>" + value + "</span></body></html>";
    }
}
