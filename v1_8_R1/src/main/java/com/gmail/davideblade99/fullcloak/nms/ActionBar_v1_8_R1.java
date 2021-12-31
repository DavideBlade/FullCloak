/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class ActionBar_v1_8_R1 implements ActionBar {

    @Override
    public void sendActionBar(final Player player, final String message) {
        final IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        final PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);
    }
}