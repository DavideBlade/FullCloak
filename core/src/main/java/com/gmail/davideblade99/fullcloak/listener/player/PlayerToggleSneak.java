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
import com.gmail.davideblade99.fullcloak.user.UserManager;
import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerToggleSneak implements Listener {

    private final FullCloak plugin;

    public PlayerToggleSneak(@NotNull final FullCloak instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerToggleSneak(final PlayerToggleSneakEvent e) {
        final Player player = e.getPlayer();
        final Settings settings = plugin.getSettings();

        if (settings.isDisabledWorld(player.getWorld()) || !player.hasPermission(Permissions.HIDE))
            return;
        if (settings.isDisabledWithOnePlayer() && !(Bukkit.getOnlinePlayers().size() >= 1))
            return;

        final User fcPlayer = UserManager.getUser(player);
        if (fcPlayer.hasPluginDisabled()) // If player has disabled the plugin
            return;

        if (!player.isSneaking()) // When player press shift to crunch
        {
            if (fcPlayer.isInvisible()) // If player is already invisible (has already run the /fullcloak hide command)
                return;
            if (fcPlayer.isWaitingToHide()) {
                MessageUtil.sendChatMessage(player, Messages.getMessage("Already waiting delay"));
                return;
            }

            if (!settings.canHideWhenTagged() // If player can't hide while is tagged
                    && plugin.getCombatTagPlugin().getTagManager().isTagged(player.getUniqueId())) // If player is tagged
            {
                MessageUtil.sendChatMessage(player, Messages.getMessage("Cannot hide"));
                e.setCancelled(true);
                return;
            }

            final long secondsLeft = fcPlayer.getRemainingCooldown();
            if (secondsLeft > 0) { // Cooldown to becoming invisible again is not over yet
                if (settings.sendCooldownMessage())
                    MessageUtil.sendMessage(player, MessageUtil.replaceSeconds("Time left", secondsLeft));

                return;
            }

            final short delay = settings.getDelayBeforeInvisible();
            if (delay > 0) {
                MessageUtil.sendMessage(player, MessageUtil.replaceSeconds("Wait for delay", delay));

                final int taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getPluginManager().callEvent(new BecomeInvisibleEvent(fcPlayer)), 20 * delay).getTaskId();
                fcPlayer.setDelayTask(taskId);
            } else
                Bukkit.getPluginManager().callEvent(new BecomeInvisibleEvent(fcPlayer));
        } else {
            // When the player gets up during the delay
            if (fcPlayer.isWaitingToHide()) {
                fcPlayer.cancelDelayTask();
                MessageUtil.sendChatMessage(player, Messages.getMessage("Delay cancelled"));
                return;
            }

            // If the player gets up when he is already back visible (maximum invisibility time reached)
            if (!fcPlayer.isInvisible())
                return;

            // If the player gets up before reaching (if set) the maximum invisibility time
            if (fcPlayer.hasMaxInvisibilityTime())
                fcPlayer.cancelMaxInvisibilityTimeTask();

            Bukkit.getPluginManager().callEvent(new BecomeVisibleEvent(fcPlayer, false));
        }
    }
}