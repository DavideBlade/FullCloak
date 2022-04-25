/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ActionBar {

    void sendActionBar(@NotNull final Player player, @NotNull final String message); // Action bar is the bar over the hotbar
}