/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.util;

import com.gmail.davideblade99.fullcloak.FullCloak;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MessageUtil {

    private final static char COLOR_CHAR = '&';
    private final static String PREFIX = "&8[FullCloak] ";

    private MessageUtil() {
        throw new IllegalAccessError();
    }

    public static void sendMessage(final Player receiver, final String message) {
        final FullCloak plugin = FullCloak.getInstance();

        switch (plugin.getSettings().getMessageType()) {
            case CHAT:
                sendChatMessage(receiver, message);
                break;

            case TITLE:
                plugin.getTitle().sendTitle(receiver, "", colour(message), 60);
                break;

            case ACTIONBAR:
                plugin.getActionbar().sendActionBar(receiver, colour(message));
                break;
        }
    }

    public static void sendChatMessage(final CommandSender receiver, final String message) {
        sendChatMessage(receiver, message, true);
    }

    public static void sendChatMessage(final CommandSender receiver, final String[] messages, final boolean prefix) {
        for (String message : messages)
            sendChatMessage(receiver, message, prefix);
    }

    public static void sendChatMessage(final CommandSender receiver, String message, final boolean prefix) {
        if (message == null || message.isEmpty())
            return;

        if (prefix)
            message = PREFIX + message;

        receiver.sendMessage(colour(message));
    }

    public static void sendMessageToConsole(final String message) {
        sendMessageToConsole(message, true);
    }

    public static void sendMessageToConsole(final String message, final boolean prefix) {
        sendChatMessage(Bukkit.getConsoleSender(), message, prefix);
    }

    /**
     * Returns the singular or plural form of a noun depending on the amount
     *
     * @param amount   The amount of the noun
     * @param singular The singular form of the noun
     * @param plural   The plural form of the noun
     * @return The correctly formed noun
     */
    public static String plural(final long amount, final String singular, final String plural) {
        return amount == 1 ? singular : plural;
    }

    public static String colour(final String toTranslate) {
        return ChatColor.translateAlternateColorCodes(COLOR_CHAR, toTranslate);
    }
}