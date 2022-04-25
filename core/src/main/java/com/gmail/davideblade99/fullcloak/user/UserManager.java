/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.user;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private final static Map<String, User> USERS = new HashMap<>();

    private UserManager() {
        throw new IllegalAccessError();
    }

    @Nullable
    public static User getUser(@NotNull final Player player) {
        return USERS.get(player.getName());
    }

    public static void addPlayer(@NotNull final Player player) {
        USERS.put(player.getName(), new User(player));
    }

    public static void removePlayer(@NotNull final Player player) {
        USERS.remove(player.getName());
    }
}
