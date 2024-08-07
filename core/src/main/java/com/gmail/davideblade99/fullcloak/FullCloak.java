/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak;

import com.gmail.davideblade99.fullcloak.Updater.ResponseHandler;
import com.gmail.davideblade99.fullcloak.command.FullCloakCommand;
import com.gmail.davideblade99.fullcloak.inventory.MenuInventoryHolder;
import com.gmail.davideblade99.fullcloak.listener.inventory.MenuClick;
import com.gmail.davideblade99.fullcloak.listener.player.*;
import com.gmail.davideblade99.fullcloak.user.UserManager;
import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import net.minelink.ctplus.CombatTagPlus;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public final class FullCloak extends JavaPlugin {

    private final static String[] SUPPORTED_VERSIONS = {"1.11", "1.12", "1.13", "1.14", "1.15", "1.16", "1.17", "1.18", "1.19", "1.20", "1.21"};

    private static FullCloak instance;

    private CombatTagPlus combatTagPlus;
    private Settings settings;

    @Override
    public void onEnable() {
        instance = this;

        if (!checkVersion()) {
            MessageUtil.sendMessageToConsole("&cThis version of FullCloak only supports the following versions:" + String.join(", ", SUPPORTED_VERSIONS));
            disablePlugin();
            return;
        }

        this.settings = new Settings();

        final String pluginVersion = getDescription().getVersion();
        new Updater(this).checkForUpdate(new ResponseHandler() {
            @Override
            public void onUpdateFound(@Nullable final String newVersion) {
                final String currentVersion = pluginVersion.contains(" ") ? pluginVersion.split(" ")[0] : pluginVersion;

                MessageUtil.sendMessageToConsole("&bFound a new version: " + newVersion + " (Yours: v" + currentVersion + ")");
                MessageUtil.sendMessageToConsole("&bDownload it on spigot:");
                MessageUtil.sendMessageToConsole("&bspigotmc.org/resources/98851");
            }
        });

        try {
            Messages.checkMessages();
        } catch (final InvalidConfigurationException ignored) {
            disablePlugin();
            return;
        }

        if (!checkHooks()) {
            disablePlugin();
            return;
        }

        registerListeners();
        registerCommands();

        for (Player player : Bukkit.getOnlinePlayers())
            UserManager.addPlayer(player);


        MessageUtil.sendMessageToConsole("&bFullCloak has been enabled! (Version: " + pluginVersion + ")", false);
    }

    @Override
    public void onDisable() {
        // Necessary in case of server reload
        closeAllOpenMenu();
        makePlayersVisible();

        // Generic shutdown
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

        instance = null;
        combatTagPlus = null;
        settings = null;

        MessageUtil.sendMessageToConsole("&bFullCloak has been disabled! (Version: " + getDescription().getVersion() + ")", false);
    }

    public Settings getSettings() {
        return settings;
    }

    public CombatTagPlus getCombatTagPlugin() {
        return combatTagPlus;
    }

    public void disablePlugin() {
        if (isEnabled()) {
            // Notify that the plugin will be disabled
            Bukkit.getPluginManager().callEvent(new PluginDisableEvent(this));

            // Disable the plugin
            setEnabled(false);
        }
    }

    /**
     * Reload the plugin safely by performing the whole disabling phase and then the enabling phase. It is not designed to
     * allow loading a new .jar (and never will be). In that case the whole server must be restarted/reloaded to avoid
     * problems. More specifically, it is necessary to restart the whole server as this allows Spigot to unload the current
     * .jar file from the JVM and load the updated one. If the .jar is replaced and then the plugin is reloaded, nothing will
     * happen: the old .jar will be the one used as if it had never been replaced.
     */
    public void reloadPlugin() {
        System.setProperty("FullCloakReloaded", "1"); // This is used to determine whether the /fullcloak reload command has been used

        setEnabled(false);
        setEnabled(true);

        System.clearProperty("FullCloakReloaded");
    }

    private boolean checkHooks() {
        if (!settings.canHideWhenTagged()) {
            final Plugin combatTagPlus = Bukkit.getPluginManager().getPlugin("CombatTagPlus");
            if (combatTagPlus == null) {
                MessageUtil.sendMessageToConsole("&cCombatTagPlus wasn't found.");
                return false;
            }

            if (combatTagPlus.isEnabled())
                this.combatTagPlus = (CombatTagPlus) combatTagPlus;
            else {
                MessageUtil.sendMessageToConsole("&cCombatTagPlus wasn't enabled.");
                return false;
            }
        }

        return true;
    }

    /**
     * @return true if the Minecraft server version is supported, otherwise false
     */
    private boolean checkVersion() {
        final String serverVersion = Bukkit.getVersion();

        for (String version : SUPPORTED_VERSIONS)
            if (serverVersion.contains(version))
                return true;

        return false;
    }

    private void registerListeners() {
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerHit(this), this);
        pm.registerEvents(new BecomeInvisible(this), this);
        pm.registerEvents(new BecomeVisible(this), this);
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerMove(this), this);
        pm.registerEvents(new PlayerQuit(), this);
        pm.registerEvents(new MenuClick(), this);

        if (settings.canHideViaShift())
            pm.registerEvents(new PlayerToggleSneak(this), this);

        if (settings.isInvisibleToMobs())
            pm.registerEvents(new MobTargetPlayer(), this);
    }

    private void registerCommands() {
        final PluginCommand command = getCommand("FullCloak");

        if (command == null) {
            MessageUtil.sendMessageToConsole("&cFullCloak was unable to register the command \"fullcloak\".", false);
            MessageUtil.sendMessageToConsole("&cThis can be caused by edits to plugin.yml or other plugins.", false);
            disablePlugin();
            return;
        }

        command.setExecutor(new FullCloakCommand(this));
        command.setTabCompleter(new FullCloakCommand(this));
    }

    private void closeAllOpenMenu() {
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenuInventoryHolder)
                player.closeInventory();
    }

    private void makePlayersVisible() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!UserManager.getUser(player).isInvisible())
                continue;

            // Show hidden player
            for (Player p : Bukkit.getOnlinePlayers())
                p.showPlayer(player);

            // Notify
            if (settings.messageWhenBecomeVisible())
                MessageUtil.sendChatMessage(player, Messages.getMessage("Show player"));
        }
    }

    public static FullCloak getInstance() {
        return instance;
    }
}