/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.event.player;

import com.gmail.davideblade99.fullcloak.event.FullCloakEvent;
import com.gmail.davideblade99.fullcloak.user.User;

abstract class PlayerEvent extends FullCloakEvent {

    private final User user;

    PlayerEvent(final User user) {
        super();

        this.user = user;
    }

    public final User getUser() {
        return user;
    }
}