/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class Title_v1_9_R1 implements Title {
    @Override
    public void sendTitle(final Player player, final String msgTitle, final String msgSubTitle, final int ticks) {
        final IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + msgTitle + "\"}");
        final IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"" + msgSubTitle + "\"}");
        final PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
        final PacketPlayOutTitle p2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p2);
        sendTime(player, ticks);
    }

    private void sendTime(final Player player, final int ticks) {
        final PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, 20, ticks, 20);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
    }
}