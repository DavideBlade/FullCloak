/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Title_v1_16_R1 implements Title {

    @Override
    public void sendTitle(@NotNull final Player player, @NotNull final String msgTitle, @NotNull final String msgSubTitle, final int ticks) {
        player.sendTitle(msgTitle, msgSubTitle, 20, ticks, 20);
    }
}