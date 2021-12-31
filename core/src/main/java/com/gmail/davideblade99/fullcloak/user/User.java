/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.user;

import com.gmail.davideblade99.fullcloak.FullCloak;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

public final class User {

    private final Player player; // Referred player
    private Effect effect; // Particle effect selected
    private boolean invisible; // True if player is invisible
    private boolean pluginDisabled; // True if player has plugin disabled
    private long cooldownEndTime; // The instant (in milliseconds) when the user can hide again
    private int delayTaskId; // Task fired after delay time (makes the player invisible)
    private int maxInvisibilityTimeTaskId; // Task fired when maximum invisibility time is reached

    User(final Player player) {
        this(player, FullCloak.getInstance().getSettings().getDefaultParticles());
    }

    private User(final Player player, final Effect effect) {
        this.player = player;
        this.effect = effect;
        this.invisible = false;
        this.pluginDisabled = false;
        this.cooldownEndTime = 0;
        this.delayTaskId = this.maxInvisibilityTimeTaskId = -1;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public void setEffect(final Effect effect) {
        this.effect = effect;
    }

    public boolean isInvisible() {
        return this.invisible;
    }

    public void setInvisible(final boolean invisible) {
        this.invisible = invisible;
    }

    public boolean hasPluginDisabled() {
        return this.pluginDisabled;
    }

    public void setPluginDisabled(final boolean disabled) {
        this.pluginDisabled = disabled;
    }

    public void setCooldownEndTime(final long time) {
        this.cooldownEndTime = time;
    }

    public long getRemainingCooldown() {
        return (this.cooldownEndTime / 1000 - System.currentTimeMillis() / 1000);
    }

    public boolean isCooldownEnded() {
        return this.getRemainingCooldown() <= 0;
    }

    /**
     * @return True if the player is waiting for the delay to become invisible
     */
    public boolean isWaitingToHide() {
        return this.delayTaskId != -1;
    }

    public void setDelayTask(final int taskId) {
        this.delayTaskId = taskId;
    }

    public void cancelDelayTask() {
        Bukkit.getScheduler().cancelTask(this.delayTaskId);

        this.setDelayTask(-1);
    }

    public boolean hasMaxInvisibilityTime() {
        return this.maxInvisibilityTimeTaskId != -1;
    }

    public void setMaxInvisibilityTimeTask(final int taskId) {
        this.maxInvisibilityTimeTaskId = taskId;
    }

    public void cancelMaxInvisibilityTimeTask() {
        Bukkit.getScheduler().cancelTask(this.maxInvisibilityTimeTaskId);

        this.setMaxInvisibilityTimeTask(-1);
    }
}