/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.event.player;

import com.gmail.davideblade99.fullcloak.user.User;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class BecomeVisibleEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    // True if the player is becoming visible again because he has reached the maximum invisibility time
    private final boolean maxTimeReached;

    public BecomeVisibleEvent(@NotNull final User user, final boolean maxTimeReached) {
        super(user);

        this.maxTimeReached = maxTimeReached;
    }

    public boolean isMaxTimeReached() {
        return maxTimeReached;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}