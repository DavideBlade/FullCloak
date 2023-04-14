/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ActionBar {

    /**
     * Action bar is the bar over the hotbar
     *
     * @param player  Player to whom the action bar should be shown
     * @param message Message to be displayed in the action bar
     */
    default void sendActionBar(@NotNull final Player player, @NotNull final String message) {
        /*
         * As of 1.19, working APIs for titles and action bars have been introduced,
         * so it is no longer necessary to use NMS for later versions.
         * In other words, it is no longer necessary to create a separate implementation for each new version,
         * but it is sufficient to use this default method.
         */

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}