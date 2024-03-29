/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.event.player;

import com.gmail.davideblade99.fullcloak.user.User;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class BecomeInvisibleEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();


    public BecomeInvisibleEvent(@NotNull final User user) {
        super(user);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}