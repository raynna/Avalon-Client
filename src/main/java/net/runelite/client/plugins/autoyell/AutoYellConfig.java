package net.runelite.client.plugins.autoyell;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("autoyell")
public interface AutoYellConfig extends Config {
    @ConfigItem(
            keyName = "interval",
            name = "Interval",
            description = "The amount of seconds between each message",
            position = 1
    )
    default int interval() {
        return 300;
    }

    @ConfigItem(
            keyName = "mode",
            name = "Mode",
            description = "The message selection strategy mode",
            position = 2
    )
    default AutoYellMode mode() {
        return AutoYellMode.ORDERED;
    }

    @ConfigItem(
            keyName = "messages",
            name = "",
            description = "",
            position = 3
    )
    default String messages() {
        return "";
    }
}
