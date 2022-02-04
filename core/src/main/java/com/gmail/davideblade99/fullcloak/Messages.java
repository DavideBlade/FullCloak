/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak;

import com.gmail.davideblade99.fullcloak.util.FileUtil;
import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class Messages {

    private final static FileConfiguration CONFIGURATION = new YamlConfiguration();
    private final static String[] MESSAGES = {
            "Hide player",
            "Show player",
            "Time left",
            "Finished cooldown",
            "No hit when invisible",
            "No move when invisible",
            "No permission",
            "Disabled by command",
            "Max time reached",
            "Command only for player",
            "Plugin disabled for player",
            "Plugin enabled for player",
            "Already disabled",
            "Already enabled",
            "Invisible player",
            "Visible player",
            "Player not online",
            "Check command usage",
            "Open command usage",
            "Effect command usage",
            "Menu not found",
            "Effect not found",
            "Particles selected",
            "Cannot hide",
            "Command disable",
            "Already waiting delay",
            "Delay cancelled",
            "Already invisible",
            "Wait for delay",
            "Seconds",
            "Second",
            "Plugin reloaded"
    };

    private Messages() {
        throw new IllegalAccessError();
    }

    public static String getMessage(final String string) {
        return CONFIGURATION.getString(string);
    }

    static void checkMessages() throws InvalidConfigurationException {
        final FullCloak pluginInstance = FullCloak.getInstance();

        final String extension = pluginInstance.getSettings().getLanguage().toString();
        final File messagesFile = new File(pluginInstance.getDataFolder() + "/messages", "messages_" + extension + ".yml");

        if (!messagesFile.exists())
            FileUtil.copyFile("messages_" + extension + ".yml", messagesFile);

        loadMessages(messagesFile);

        if (!findAllMessages()) {
            final File broken = new File(pluginInstance.getDataFolder() + "/messages", "messages_" + extension + ".broken." + System.currentTimeMillis());

            messagesFile.renameTo(broken);

            FileUtil.copyFile("messages_" + extension + ".yml", messagesFile);

            loadMessages(messagesFile);

            MessageUtil.sendMessageToConsole("&cNot found all messages in messages_" + extension + ".");
            MessageUtil.sendMessageToConsole("&cA new file has been created.");
            MessageUtil.sendMessageToConsole("&cThe old file has been renamed to \"" + broken.getName() + "\".");
        }
    }

    private static void loadMessages(final File messagesFile) throws InvalidConfigurationException {
        try {
            CONFIGURATION.load(messagesFile);
        } catch (final Exception e) {
            e.printStackTrace();

            MessageUtil.sendMessageToConsole("&cFailed to load " + messagesFile.getName() + ".");

            throw new InvalidConfigurationException();
        }
    }

    private static boolean findAllMessages() {
        for (String msg : MESSAGES)
            if (!find(msg))
                return false;

        return true;
    }

    private static boolean find(final String path) {
        return CONFIGURATION.isSet(path);
    }

    public enum Language {
        EN, IT;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum MessageType {
        CHAT, TITLE, ACTIONBAR
    }
}