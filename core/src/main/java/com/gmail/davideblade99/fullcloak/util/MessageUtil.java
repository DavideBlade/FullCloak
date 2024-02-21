/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.util;

import com.gmail.davideblade99.fullcloak.FullCloak;
import com.gmail.davideblade99.fullcloak.Messages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MessageUtil {

    private final static char COLOR_CHAR = '&';
    private final static String PREFIX = "&8[FullCloak]&r ";

    private MessageUtil() {
        throw new IllegalAccessError();
    }

    public static void sendMessage(@NotNull final Player receiver, @NotNull final String message) {
        final FullCloak plugin = FullCloak.getInstance();

        switch (plugin.getSettings().getMessageType()) {
            case CHAT:
                sendChatMessage(receiver, message);
                break;

            case TITLE:
                receiver.sendTitle("", colour(message), 20, 60, 20);
                break;

            case ACTIONBAR:
                receiver.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colour(message)));
                break;
        }
    }

    public static void sendChatMessage(@NotNull final CommandSender receiver, @Nullable final String message) {
        sendChatMessage(receiver, message, true);
    }

    public static void sendChatMessage(@NotNull final CommandSender receiver, @NotNull final String[] messages, final boolean prefix) {
        for (String message : messages)
            sendChatMessage(receiver, message, prefix);
    }

    public static void sendChatMessage(@NotNull final CommandSender receiver, @Nullable String message, final boolean prefix) {
        if (message == null || message.isEmpty())
            return;

        if (prefix)
            message = PREFIX + message;

        receiver.sendMessage(colour(message));
    }

    public static void sendMessageToConsole(@Nullable final String message) {
        sendMessageToConsole(message, true);
    }

    public static void sendMessageToConsole(@Nullable final String message, final boolean prefix) {
        sendChatMessage(Bukkit.getConsoleSender(), message, prefix);
    }

    @NotNull
    public static String colour(@NotNull final String toTranslate) {
        return ChatColor.translateAlternateColorCodes(COLOR_CHAR, toTranslate);
    }

    /**
     * Gets the message from the configuration and replaces the placeholder
     * {@link PlaceholderUtil#PLAYER_PLACEHOLDER} with the player's name
     *
     * @param message    Message in the message file containing the placeholder
     * @param playerName Player name
     *
     * @return Message with the player's name instead of the placeholder
     */
    @NotNull
    public static String replacePlayer(@NotNull final String message, @NotNull final String playerName) {
        return PlaceholderUtil.replacePlayer(Messages.getMessage(message), playerName);
    }

    /**
     * Gets the message from the configuration and replaces the placeholder
     * {@link PlaceholderUtil#PLAYER_PLACEHOLDER} with the player's name
     *
     * @param message Message in the message file containing the placeholder
     * @param player  Player to replace the placeholder
     *
     * @return Message with the player's name in place of the placeholder
     */
    @NotNull
    public static String replacePlayer(@NotNull final String message, @NotNull final Player player) {
        return replacePlayer(message, player.getName());
    }

    /**
     * Gets the message from the configuration and replaces the placeholder
     * {@link PlaceholderUtil#EFFECT_PLACEHOLDER} with the effect
     *
     * @param message Message in the message file containing the placeholder
     * @param effect  Effect to be added to the message
     *
     * @return Message with effect in place of placeholder
     */
    @NotNull
    public static String replaceEffect(@NotNull final String message, @NotNull final Effect effect) {
        return PlaceholderUtil.replaceEffect(Messages.getMessage(message), effect);
    }

    /**
     * Gets the message from the configuration and replaces the placeholder
     * {@link PlaceholderUtil#SECONDS_PLACEHOLDER} with the number of seconds
     * and the placeholder {@link PlaceholderUtil#PLURAL_PLACEHOLDER}
     * with the singular or plural form of the noun depending on the seconds
     *
     * @param message Message in the message file containing the placeholder
     * @param seconds Seconds to be added to the message
     *
     * @return Message with the number of seconds and the noun (singular or plural) replacing the placeholders
     */
    @NotNull
    public static String replaceSeconds(@NotNull final String message, final long seconds) {
        return PlaceholderUtil.replacePlural(PlaceholderUtil.replaceSeconds(Messages.getMessage(message), seconds), Messages.getMessage(plural(seconds, "Second", "Seconds")));
    }

    /**
     * Returns the singular or plural form of a noun depending on the amount
     *
     * @param amount   The amount of the noun
     * @param singular The singular form of the noun
     * @param plural   The plural form of the noun
     *
     * @return The correctly formed noun
     */
    @NotNull
    private static String plural(final long amount, @NotNull final String singular, @NotNull final String plural) {
        return amount == 1 ? singular : plural;
    }

    private static class PlaceholderUtil {

        // Placeholder
        private final static String PLAYER_PLACEHOLDER = "%player";
        private final static String EFFECT_PLACEHOLDER = "%effect";
        private final static String SECONDS_PLACEHOLDER = "%seconds";
        private final static String PLURAL_PLACEHOLDER = "%type";

        /**
         * Replaces the {@link PlaceholderUtil#PLAYER_PLACEHOLDER} placeholder with the name of the player
         *
         * @param message    Message containing the placeholder
         * @param playerName Player name
         *
         * @return Message with the player's name instead of the placeholder
         */
        @NotNull
        private static String replacePlayer(@NotNull final String message, @NotNull final String playerName) {
            return message.replace(PLAYER_PLACEHOLDER, playerName);
        }

        /**
         * Replaces the placeholder {@link PlaceholderUtil#EFFECT_PLACEHOLDER} with the effect
         *
         * @param message Message containing the placeholder
         * @param effect  Effect to be added to the message
         *
         * @return Message with effect in place of placeholder
         */
        @NotNull
        private static String replaceEffect(@NotNull final String message, @NotNull final Effect effect) {
            return message.replace(EFFECT_PLACEHOLDER, effect.toString());
        }

        /**
         * Replaces the placeholder {@link PlaceholderUtil#SECONDS_PLACEHOLDER} with the number of seconds
         *
         * @param message Message containing the placeholder
         * @param seconds Seconds to be added to the message
         *
         * @return Message with the number of seconds replacing the placeholder
         */
        @NotNull
        private static String replaceSeconds(@NotNull final String message, final long seconds) {
            return message.replace(SECONDS_PLACEHOLDER, String.valueOf(seconds));
        }

        /**
         * Replaces the placeholder {@link PlaceholderUtil#PLURAL_PLACEHOLDER} with {@code noun}
         *
         * @param message Message containing the placeholder
         * @param noun    Name to be placed in the message
         *
         * @return Message with the noun (singular or plural) replacing the placeholder
         */
        @NotNull
        private static String replacePlural(@NotNull final String message, @NotNull final String noun) {
            return message.replace(PLURAL_PLACEHOLDER, noun);
        }
    }
}