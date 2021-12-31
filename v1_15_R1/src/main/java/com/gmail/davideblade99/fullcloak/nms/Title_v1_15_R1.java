/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import org.bukkit.entity.Player;

public final class Title_v1_15_R1 implements Title {

    @Override
    public void sendTitle(final Player player, final String msgTitle, final String msgSubTitle, final int ticks) {
        player.sendTitle(msgTitle, msgSubTitle, 20, ticks, 20);
    }
}