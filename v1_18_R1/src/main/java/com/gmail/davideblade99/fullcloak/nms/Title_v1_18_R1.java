/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.nms;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Title_v1_18_R1 implements Title {

    @Override
    public void sendTitle(@NotNull final Player player, @NotNull final String msgTitle, @NotNull final String msgSubTitle, final int ticks) {
        // When tested, Player#sendTitle() did not work properly

        final ClientboundSetTitlesAnimationPacket times = new ClientboundSetTitlesAnimationPacket(20, ticks, 20);
        ((CraftPlayer) player).getHandle().b.a(times);

        final IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + msgTitle + "\"}");
        final ClientboundSetTitleTextPacket title = new ClientboundSetTitleTextPacket(chatTitle);
        ((CraftPlayer) player).getHandle().b.a(title);

        final IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"" + msgSubTitle + "\"}");
        final ClientboundSetSubtitleTextPacket subtitle = new ClientboundSetSubtitleTextPacket(chatSubTitle);
        ((CraftPlayer) player).getHandle().b.a(subtitle);
    }
}