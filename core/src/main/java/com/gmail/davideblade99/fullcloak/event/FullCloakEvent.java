/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.event;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class FullCloakEvent extends Event {

    protected FullCloakEvent() {
        super(false);
    }

    @NotNull
    @Override
    public final String getEventName() {
        return getClass().getSimpleName();
    }
}