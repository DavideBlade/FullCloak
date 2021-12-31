/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.listener.player;

import com.gmail.davideblade99.fullcloak.FullCloak;
import com.gmail.davideblade99.fullcloak.Messages;
import com.gmail.davideblade99.fullcloak.Permissions;
import com.gmail.davideblade99.fullcloak.Settings;
import com.gmail.davideblade99.fullcloak.event.player.BecomeInvisibleEvent;
import com.gmail.davideblade99.fullcloak.event.player.BecomeVisibleEvent;
import com.gmail.davideblade99.fullcloak.user.User;
import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class BecomeInvisible implements Listener {

    private final FullCloak plugin;

    public BecomeInvisible(final FullCloak instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBecomeInvisible(final BecomeInvisibleEvent event) {
        final User fcPlayer = event.getUser();
        final Player player = fcPlayer.getPlayer();
        final Settings settings = plugin.getSettings();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission(Permissions.SEE_HIDDEN))
                p.hidePlayer(player);
        }

        player.setFlySpeed(settings.getInvisibleSpeed());
        player.setWalkSpeed(settings.getInvisibleSpeed());

        player.getWorld().playEffect(player.getLocation(), fcPlayer.getEffect(), 5);

        if (settings.messageWhenBecomeInvisible())
            MessageUtil.sendChatMessage(player, Messages.getMessage("Hide player"));

        fcPlayer.setDelayTask(-1);
        fcPlayer.setInvisible(true);

        final short maxTimeInvisible;
        if (player.hasPermission(Permissions.HIDE + ".60"))
            maxTimeInvisible = 60;
        else if (player.hasPermission(Permissions.HIDE + ".50"))
            maxTimeInvisible = 50;
        else if (player.hasPermission(Permissions.HIDE + ".40"))
            maxTimeInvisible = 40;
        else if (player.hasPermission(Permissions.HIDE + ".30"))
            maxTimeInvisible = 30;
        else if (player.hasPermission(Permissions.HIDE + ".20"))
            maxTimeInvisible = 20;
        else if (player.hasPermission(Permissions.HIDE + ".10"))
            maxTimeInvisible = 10;
        else
            maxTimeInvisible = settings.getMaxTimeInvisible();
        if (maxTimeInvisible <= 0)
            return;

        final int taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getPluginManager().callEvent(new BecomeVisibleEvent(fcPlayer, true)), 20 * maxTimeInvisible).getTaskId();
        fcPlayer.setMaxInvisibilityTimeTask(taskId);
    }
}