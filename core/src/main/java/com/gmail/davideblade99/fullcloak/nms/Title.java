/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Title {
    void sendTitle(@NotNull final Player player, @NotNull final String msgTitle, @NotNull final String msgSubTitle, final int ticks); // Title is the message sended in the middle of minecraft's window.
}