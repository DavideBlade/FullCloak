/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Title {

    /**
     * The title is a message sent to the centre of the Minecraft window
     *
     * @param player      Player to show title to
     * @param msgTitle    Title text
     * @param msgSubTitle Subtitle text
     * @param ticks       Title duration (how long it should remain on the screen)
     */
    default void sendTitle(@NotNull final Player player, @NotNull final String msgTitle, @NotNull final String msgSubTitle, final int ticks) {
        /*
         * As of 1.19, working APIs for titles and action bars have been introduced,
         * so it is no longer necessary to use NMS for later versions.
         * In other words, it is no longer necessary to create a separate implementation for each new version,
         * but it is sufficient to use this default method.
         */

        player.sendTitle(msgTitle, msgSubTitle, 20, ticks, 20);
    }
}