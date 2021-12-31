/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class ActionBar_v1_9_R1 implements ActionBar {

    @Override
    public void sendActionBar(final Player player, final String message) {
        final IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        final PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
    }
}