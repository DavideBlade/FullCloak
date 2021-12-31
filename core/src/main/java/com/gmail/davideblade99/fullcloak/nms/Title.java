/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import org.bukkit.entity.Player;

public interface Title {
    void sendTitle(final Player player, final String msgTitle, final String msgSubTitle, final int ticks); // Title is the message sended in the middle of minecraft's window.
}