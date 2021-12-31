/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.listener.player;

import com.gmail.davideblade99.fullcloak.Messages;
import com.gmail.davideblade99.fullcloak.user.UserManager;
import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PotionSplashEvent;

import static org.bukkit.GameMode.CREATIVE;

public final class PlayerHit implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        final Entity target = event.getEntity();
        if (!(target instanceof Player)) // If the hit entity isn't a player
            return;

        // Who hit
        final Entity attacker = event.getCause() == DamageCause.PROJECTILE ? (Entity) ((Projectile) event.getDamager()).getShooter() : event.getDamager();
        if (!(attacker instanceof Player))
            return;

        final Player attackerPlayer = (Player) attacker;
        if (attackerPlayer.equals(target)) // If the player hit himself
            return;

        if (UserManager.getUser(attackerPlayer).isInvisible()) {
            event.setCancelled(true);

            MessageUtil.sendChatMessage(attackerPlayer, Messages.getMessage("No hit when invisible"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPotionSplash(final PotionSplashEvent e) {
        final Entity attacker = (Entity) e.getPotion().getShooter();
        if (!(attacker instanceof Player))
            return;

        final Player attackerPlayer = (Player) attacker;
        if (!UserManager.getUser(attackerPlayer).isInvisible())
            return;

        for (LivingEntity target : e.getAffectedEntities()) {
            if (!(target instanceof Player)) // If the affected entity is not a player
                continue;
            if (attackerPlayer.equals(target)) // If the player hit himself
                continue;

            e.setCancelled(true);

            /*
             * If the player has consumed the potion (so he is not in creative) and
             * still has a free slot in the inventory (didn't pick up any items from the ground in the meantime)
             */
            if (attackerPlayer.getGameMode() != CREATIVE && attackerPlayer.getInventory().firstEmpty() != -1)
                attackerPlayer.getInventory().addItem(e.getPotion().getItem()); // Returns the thrown potion

            MessageUtil.sendChatMessage(attackerPlayer, Messages.getMessage("No hit when invisible"));
            break;
        }
    }
}