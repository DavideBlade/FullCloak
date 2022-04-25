/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak;

import com.gmail.davideblade99.fullcloak.Messages.Language;
import com.gmail.davideblade99.fullcloak.Messages.MessageType;
import com.gmail.davideblade99.fullcloak.inventory.Item;
import com.gmail.davideblade99.fullcloak.inventory.Menu;
import com.gmail.davideblade99.fullcloak.util.EnumUtil;
import com.gmail.davideblade99.fullcloak.util.FileUtil;
import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmail.davideblade99.fullcloak.util.FileUtil.CONFIG_FILE;

public final class Settings {

    /*
     * General settings
     */
    private final Language lang;
    private final boolean checkUpdate;
    private final boolean disableWithOnePlayer;
    private final short delayBeforeInvisible;
    private final short cooldownTime;
    private final boolean sendCooldownMessage;
    private final MessageType messageType;
    private final boolean canMoveWhenInvisible;
    private final float invisibleSpeed;
    private final short maxTimeInvisible;
    private final Effect defaultParticles;
    private final boolean playParticlesOnPlayerMove;
    private final boolean messageWhenBecomeInvisible;
    private final boolean messageWhenBecomeVisible;
    private final List<World> enabledWorlds;
    private final boolean canHideWhenTagged;
    private final boolean invisibleToMobs;
    private final boolean hideViaCommand;
    private final boolean hideViaShift;
    private final boolean canHitWhenInvisible;
    private final boolean disableInvisibilityOnHit;

    /*
     * Menu settings
     */
    private final Map<String, Menu> menus = new HashMap<>();

    Settings() {
        if (!CONFIG_FILE.exists()) {
            if (System.getProperty("FullCloakReloaded") == null) // If the plugin has not been reloaded with /fulcloak reload
                FileUtil.copyFile("config.yml", CONFIG_FILE);
            else {
                /*
                 * When the plugin is reloaded with /fullcloak reload the files within
                 * the .jar are not detected. So if the files have been deleted from the
                 * plugin's folder, there is no way to recreate them.
                 * The entire server must be reloaded or restarted.
                 */
                MessageUtil.sendMessageToConsole("&cThe config.yml file has not been found: /fullcloak reload is not designed to recreate files from scratch but only to reload some updated parameters.");
                MessageUtil.sendMessageToConsole("&cIf you want to recreate them, you need to reload/restart the server.");
                MessageUtil.sendMessageToConsole("&cThe configuration file will not be created but default values will be used.");
            }
        }

        /*
         * Without reloadConfig(), when reloading the single plugin,
         * the config would not be reloaded but the old configuration
         * would be used.
         * If the whole server is reloaded, this method is unnecessary.
         */
        FullCloak.getInstance().reloadConfig();
        final FileConfiguration config = FullCloak.getInstance().getConfig();

        // Check the language
        Language locale = EnumUtil.getEnumIgnoreCase(config.getString("Locale", null), Language.class, null);
        if (locale == null) {
            MessageUtil.sendMessageToConsole("&cThe language specified in the \"Locale\" line (in the config) does not exist!");
            MessageUtil.sendMessageToConsole("&cThe default language (English) will be used.");

            locale = Language.EN;
        }


        // Check the message type
        MessageType messageType = EnumUtil.getEnumIgnoreCase(config.getString("Message type", null), MessageType.class, null);
        if (messageType == null) {
            MessageUtil.sendMessageToConsole("&cThe type of message specified in the \"Message type\" line (in the config) does not exist!");
            MessageUtil.sendMessageToConsole("&cMessages will be shown on chat by default.");

            messageType = MessageType.CHAT;
        }


        // Check if effect in config is correct
        Effect defaultParticles = EnumUtil.getEnumIgnoreCase(config.getString("Default particles", null), Effect.class, null);
        if (defaultParticles == null) {
            MessageUtil.sendMessageToConsole("&cThe effect specified in the \"Default particles\" line (in the config) does not exist!");
            MessageUtil.sendMessageToConsole("&cThe default effect \"ENDER_SIGNAL\" will be used.");

            defaultParticles = Effect.ENDER_SIGNAL;
        }


        // Check if there are any enabled worlds
        final List<World> enabledWorlds = new ArrayList<>();
        for (String world : config.getStringList("Worlds")) {
            final World w = Bukkit.getWorld(world);
            if (w != null)
                enabledWorlds.add(w);
        }
        if (enabledWorlds.isEmpty())
            MessageUtil.sendMessageToConsole("&cWarning: there are no enabled worlds!");


        // Global settings
        this.lang = locale;
        this.checkUpdate = config.getBoolean("Check update", true);
        this.disableWithOnePlayer = config.getBoolean("Disable with only a player", false);
        this.delayBeforeInvisible = (short) config.getInt("Delay before invisible", 0);
        this.cooldownTime = (short) config.getInt("Cooldown time", 5);
        this.sendCooldownMessage = config.getBoolean("Cooldown message", true);
        this.messageType = messageType;
        this.canMoveWhenInvisible = config.getBoolean("Can move when invisible", true);
        this.invisibleSpeed = (float) config.getDouble("Speed when invisible", 0.1F);
        this.maxTimeInvisible = (short) config.getInt("Max second invisible", 5);
        this.defaultParticles = defaultParticles;
        this.playParticlesOnPlayerMove = config.getBoolean("Particles when player move", true);
        this.messageWhenBecomeInvisible = config.getBoolean("Message when become invisible", true);
        this.messageWhenBecomeVisible = config.getBoolean("Message when become visible", true);
        this.enabledWorlds = enabledWorlds;
        this.canHideWhenTagged = config.getBoolean("Can hide when tagged", true);
        this.invisibleToMobs = config.getBoolean("Invisible to mobs", false);
        this.hideViaCommand = config.getBoolean("Invisible via command", true);
        this.hideViaShift = config.getBoolean("Invisible via Shift", true);
        this.canHitWhenInvisible = config.getBoolean("Allow invisible player hit", false);
        this.disableInvisibilityOnHit = config.getBoolean("Disable invisibility on hit", false);


        // Menu settings
        loadMenuSettings(config);
    }

    @NotNull
    Language getLanguage() {
        return lang;
    }

    boolean checkUpdate() {
        return checkUpdate;
    }

    public boolean isDisabledWithOnePlayer() {
        return disableWithOnePlayer;
    }

    public short getDelayBeforeInvisible() {
        return delayBeforeInvisible;
    }

    public short getCooldownTime() {
        return cooldownTime;
    }

    public boolean sendCooldownMessage() {
        return sendCooldownMessage;
    }

    @NotNull
    public MessageType getMessageType() {
        return messageType;
    }

    public boolean canMoveWhenInvisible() {
        return canMoveWhenInvisible;
    }

    public float getInvisibleSpeed() {
        return invisibleSpeed;
    }

    public short getMaxTimeInvisible() {
        return maxTimeInvisible;
    }

    @NotNull
    public Effect getDefaultParticles() {
        return defaultParticles;
    }

    public boolean playParticlesOnPlayerMove() {
        return playParticlesOnPlayerMove;
    }

    public boolean messageWhenBecomeInvisible() {
        return messageWhenBecomeInvisible;
    }

    public boolean messageWhenBecomeVisible() {
        return messageWhenBecomeVisible;
    }

    public boolean isDisabledWorld(final World world) {
        return !enabledWorlds.contains(world);
    }

    public boolean canHideWhenTagged() {
        return canHideWhenTagged;
    }

    boolean isInvisibleToMobs() {
        return invisibleToMobs;
    }

    public boolean canHideViaCommand() {
        return hideViaCommand;
    }

    boolean canHideViaShift() {
        return hideViaShift;
    }

    public boolean canHitWhenInvisible() {
        return canHitWhenInvisible;
    }

    public boolean disableInvisibilityOnHit() {
        return disableInvisibilityOnHit;
    }

    @NotNull
    public Map<String, Menu> getMenus() {
        return menus;
    }

    @Nullable
    public Menu getMenu(@NotNull final String menuName) {
        return menus.get(menuName.toLowerCase());
    }

    private void loadMenuSettings(@NotNull final FileConfiguration config) {
        final ConfigurationSection menus = config.getConfigurationSection("Menus");

        // Check if there are menus
        if (menus == null || menus.getKeys(false).isEmpty()) {
            MessageUtil.sendMessageToConsole("&cWarning: the menu list is empty!");
            return;
        }

        // Load all menus
        for (String menuName : menus.getKeys(false)) {
            byte size = (byte) menus.getInt(menuName + ".Settings.Row", -1);
            String title = menus.getString(menuName + ".Settings.Title", null);

            if (size < 1 || size > 6) {
                MessageUtil.sendMessageToConsole("&cThe number of rows of the '" + menuName + "' menu isn't correct. The menu will be ignored.");
                continue;
            }
            size *= 9; // Total number of slots

            if (title == null) {
                MessageUtil.sendMessageToConsole("&cTitle of '" + menuName + "' menu is missing. The menu will be ignored.");
                continue;
            }
            title = MessageUtil.colour(title);


            /*
             * Load all items for current menu
             */
            final ConfigurationSection itemsConf = menus.getConfigurationSection(menuName + ".Items");

            // Check if there are no items
            if (itemsConf == null || itemsConf.getKeys(false).isEmpty()) {
                MessageUtil.sendMessageToConsole("&cWarning: the '" + menuName + "' menu has no items!");
                MessageUtil.sendMessageToConsole("&cThis menu will not be created.");
                continue;
            }

            final List<Item> items = new ArrayList<>();
            for (String itemName : itemsConf.getKeys(false)) {
                final String type = itemsConf.getString(itemName + ".Item", null);
                final short data = (short) itemsConf.getInt(itemName + ".Data", 0);
                String displayName = itemsConf.getString(itemName + ".Name", null);
                String lore = itemsConf.getString(itemName + ".Lore", null);

                final byte slot = (byte) itemsConf.getInt(itemName + ".Slot", -1);
                String command = itemsConf.getString(itemName + ".Command", null);
                final boolean keepOpen = itemsConf.getBoolean(itemName + ".Keep open", false);

                if (type == null) {
                    MessageUtil.sendMessageToConsole("&cMissing item under '" + itemName + "' in '" + menuName + "' menu. This item will be ignored.");
                    continue;
                }

                final Material material = Material.matchMaterial(type);
                if (material == null) {
                    MessageUtil.sendMessageToConsole("&cUnknown material '" + type + "' in '" + menuName + "' menu. This item will be ignored.");
                    continue;
                }

                if (slot < 0 || slot >= size) {
                    MessageUtil.sendMessageToConsole("&cThe slot of '" + itemName + "' item in '" + menuName + "' menu is incorrect. The item will be ignored.");
                    continue;
                }

                if (displayName != null)
                    displayName = MessageUtil.colour(displayName);
                if (lore != null)
                    lore = MessageUtil.colour(lore);
                if (command != null)
                    command = MessageUtil.colour(command).replace("%default", this.defaultParticles.toString());

                items.add(new Item(material, data, displayName, lore, slot, command, keepOpen));
            }

            this.menus.put(menuName.toLowerCase(), new Menu(title, size, items));
        }
    }
}