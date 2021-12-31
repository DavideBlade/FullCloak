/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.event;

import org.bukkit.event.Event;

public abstract class FullCloakEvent extends Event {

    protected FullCloakEvent() {
        super(false);
    }

    @Override
    public final String getEventName() {
        return getClass().getSimpleName();
    }
}