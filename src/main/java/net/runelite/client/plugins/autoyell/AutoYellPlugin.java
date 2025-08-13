package net.runelite.client.plugins.autoyell;

import com.jagex.GameClient;
import com.google.inject.Provides;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.ClientToolbar;

import javax.inject.Inject;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@PluginDescriptor(
        name = "Auto Yell",
        description = "Automatically yell a select amount of messages",
        staffPlugin = true,
        enabledByDefault = false
)
public final class AutoYellPlugin extends Plugin {

    private final List<String> messages = new ArrayList<>();

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private GameClient client;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private AutoYellConfig config;

    private long lastYellMs;
    private long intervalMs;
    private int messageCounter;

    @Provides
    AutoYellConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(AutoYellConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        cacheData();
    }

    @Schedule(period = 1, unit = ChronoUnit.SECONDS)
    public void process() {
       /* if (client.getGameState() == GameState.LOGGED_IN) {
            long elapsed = System.currentTimeMillis() - lastYellMs;
            if (elapsed >= intervalMs) {
                submitYell();
                lastYellMs = System.currentTimeMillis();
            }
        }*/
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged changed) {
        if (!"autoyell".equals(changed.getGroup())) {
            return;
        }
        cacheData();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
            lastYellMs = System.currentTimeMillis();
        }
    }

    private void submitYell() {
        if (messages.isEmpty()) {
            return;
        }
        String message;
        switch (config.mode()) {
            case RANDOM:
               // message = messages.get(RandomUtils.nextInt(0, messages.size()));
                break;
            case ORDERED:
                if (messageCounter >= messages.size()) {
                    messageCounter = 0;
                }
                message = messages.get(messageCounter++);
                break;
            default:
                return;
        }
  //      Chat.sendPublic(message, 2);
    }

    private void cacheData() {
        intervalMs = TimeUnit.SECONDS.toMillis(config.interval());
        messages.clear();
        messages.addAll(Arrays.asList(config.messages().split("[\r\n]+")));
    }
}
