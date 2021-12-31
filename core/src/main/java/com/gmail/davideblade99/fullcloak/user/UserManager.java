/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.user;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final static Map<String, User> USERS = new HashMap<>();

    private UserManager() {
        throw new IllegalAccessError();
    }

    public static User getUser(final Player player) {
        return USERS.get(player.getName());
    }

    public static void addPlayer(final Player player) {
        USERS.put(player.getName(), new User(player));
    }

    public static void removePlayer(final Player player) {
        USERS.remove(player.getName());
    }
}
