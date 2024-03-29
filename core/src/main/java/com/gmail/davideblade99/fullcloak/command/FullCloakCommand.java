/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.command;

import com.gmail.davideblade99.fullcloak.FullCloak;
import com.gmail.davideblade99.fullcloak.Messages;
import com.gmail.davideblade99.fullcloak.Permissions;
import com.gmail.davideblade99.fullcloak.Settings;
import com.gmail.davideblade99.fullcloak.event.player.BecomeInvisibleEvent;
import com.gmail.davideblade99.fullcloak.event.player.BecomeVisibleEvent;
import com.gmail.davideblade99.fullcloak.inventory.Menu;
import com.gmail.davideblade99.fullcloak.user.User;
import com.gmail.davideblade99.fullcloak.user.UserManager;
import com.gmail.davideblade99.fullcloak.util.EnumUtil;
import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.bukkit.ChatColor.RED;

public final class FullCloakCommand implements CommandExecutor, TabCompleter {

    private final FullCloak plugin;

    public FullCloakCommand(@NotNull final FullCloak instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command cmd, @NotNull final String label, @NotNull final String[] args) {
        try {
            execute(sender, args);
        }
        catch (final CommandException ex) {
            if (ex.getMessage() != null && !ex.getMessage().isEmpty())
                MessageUtil.sendChatMessage(sender, RED + ex.getMessage());
        }

        return true;
    }

    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command cmd, @NotNull final String alias, @NotNull final String[] args) {
        final List<String> completions = new ArrayList<>();

        switch (args.length) {
            case 1:
                StringUtil.copyPartialMatches(args[0], Arrays.asList("help", "off", "disable", "enable", "cooldown", "check", "open", "effect", "hide", "reload"), completions);
                break;

            case 2:
                final List<String> arg = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], Arrays.asList("check", "open", "effect"), arg);

                if (arg.size() != 1)
                    return completions;

                if (arg.contains("check")) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        completions.add(p.getName());
                }

                if (arg.contains("open"))
                    completions.addAll(plugin.getSettings().getMenus().keySet());

                if (arg.contains("effect")) {
                    for (Effect effect : Effect.values())
                        completions.add(effect.toString());
                }
                break;

            default:
                return completions;
        }

        Collections.sort(completions);
        return completions;
    }

    private void execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (args.length == 0) {
            final String[] message = {
                    "&8------------[&cFullCloak&8]------------",
                    "&8Developer: &cDavideBlade",
                    "&8Version: &c" + plugin.getDescription().getVersion(),
                    "&8Command aliases: &c/fullcloak, /fc",
                    "&8Help: &c/fullcloak help"
            };
            MessageUtil.sendChatMessage(sender, message, false);
            return;
        }


        if (args[0].equalsIgnoreCase("help")) {
            final String[] message = {
                    "&8--------[&cFullCloak commands&8]--------",
                    "&8/fullcloak - &cPlugin information.",
                    "&8/fc off - &cDisables totally the plugin.",
                    "&8/fc disable - &cDisable invisibility.",
                    "&8/fc enable - &cEnable invisibility.",
                    "&8/fc cooldown - &cCheck remaining time in cooldown.",
                    "&8/fc check <player> - &cCheck if the player is hidden.",
                    "&8/fc open <menu> - &cOpen the selected plugin menu.",
                    "&8/fc effect <effect name> - &cSelect an effect.",
                    "&8/fc hide - &cBecome invisible.",
                    "&8/fc reload - &cReload plugin."
            };
            MessageUtil.sendChatMessage(sender, message, false);
            return;
        }


        if (args[0].equalsIgnoreCase("off")) {
            CommandValidator.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "off"), Messages.getMessage("No permission"));


            plugin.disablePlugin();
            MessageUtil.sendChatMessage(sender, Messages.getMessage("Disabled by command"));
            return;
        }


        if (args[0].equalsIgnoreCase("check")) {
            CommandValidator.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "check"), Messages.getMessage("No permission"));
            CommandValidator.minLength(args, 2, Messages.getMessage("Check command usage"));


            final Player target = Bukkit.getPlayer(args[1]);
            CommandValidator.notNull(target, MessageUtil.replacePlayer("Player not online", args[1]));

            if (UserManager.getUser(target).isInvisible())
                MessageUtil.sendChatMessage(sender, MessageUtil.replacePlayer("Invisible player", target));
            else
                MessageUtil.sendChatMessage(sender, MessageUtil.replacePlayer("Visible player", target));
            return;
        }


        if (args[0].equalsIgnoreCase("disable")) {
            CommandValidator.isTrue(sender instanceof Player, Messages.getMessage("Command only for player"));
            CommandValidator.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "disable"), Messages.getMessage("No permission"));

            final Player player = (Player) sender;
            final User fcPlayer = UserManager.getUser(player);

            if (!fcPlayer.hasPluginDisabled()) {
                fcPlayer.setPluginDisabled(true);

                // If the player is hidden, he must become visible again
                if (fcPlayer.isInvisible())
                    Bukkit.getPluginManager().callEvent(new BecomeVisibleEvent(fcPlayer, false));

                MessageUtil.sendChatMessage(player, Messages.getMessage("Plugin disabled for player"));
            } else
                MessageUtil.sendChatMessage(player, Messages.getMessage("Already disabled"));
            return;
        }


        if (args[0].equalsIgnoreCase("enable")) {
            CommandValidator.isTrue(sender instanceof Player, Messages.getMessage("Command only for player"));
            CommandValidator.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "enable"), Messages.getMessage("No permission"));

            final Player player = (Player) sender;
            final User fcPlayer = UserManager.getUser(player);

            if (fcPlayer.hasPluginDisabled()) {
                fcPlayer.setPluginDisabled(false);
                MessageUtil.sendChatMessage(player, Messages.getMessage("Plugin enabled for player"));
            } else
                MessageUtil.sendChatMessage(player, Messages.getMessage("Already enabled"));
            return;
        }


        if (args[0].equalsIgnoreCase("open")) {
            CommandValidator.isTrue(sender instanceof Player, Messages.getMessage("Command only for player"));
            CommandValidator.minLength(args, 2, Messages.getMessage("Open command usage"));
            CommandValidator.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "open"), Messages.getMessage("No permission"));


            final Menu menu = FullCloak.getInstance().getSettings().getMenu(args[1]);
            if (menu != null) {
                ((Player) sender).openInventory(menu.getInventory());
            } else
                MessageUtil.sendChatMessage(sender, Messages.getMessage("Menu not found"));
            return;
        }


        if (args[0].equalsIgnoreCase("effect")) {
            CommandValidator.isTrue(sender instanceof Player, Messages.getMessage("Command only for player"));
            CommandValidator.minLength(args, 2, Messages.getMessage("Effect command usage"));
            CommandValidator.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "effect"), Messages.getMessage("No permission"));

            final Effect effect = EnumUtil.getEnumIgnoreCase(args[1], Effect.class, null);
            CommandValidator.notNull(effect, Messages.getMessage("Effect not found"));


            UserManager.getUser((Player) sender).setEffect(effect);
            MessageUtil.sendChatMessage(sender, MessageUtil.replaceEffect("Particles selected", effect));
            return;
        }


        final Settings settings = plugin.getSettings();

        if (args[0].equalsIgnoreCase("hide")) {
            CommandValidator.isTrue(sender instanceof Player, Messages.getMessage("Command only for player"));
            CommandValidator.isTrue(settings.canHideViaCommand(), Messages.getMessage("Command disable"));
            CommandValidator.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "hide"), Messages.getMessage("No permission"));
            CommandValidator.isTrue(sender.hasPermission(Permissions.HIDE), Messages.getMessage("No permission"));

            final Player player = (Player) sender;
            final User fcPlayer = UserManager.getUser(player);

            if (settings.isDisabledWorld(player.getWorld()))
                return;
            if (settings.isDisabledWithOnePlayer() && !(Bukkit.getOnlinePlayers().size() >= 1))
                return;
            if (fcPlayer.hasPluginDisabled()) // If player has disabled the plugin (/fullcloak disable)
                return;

            // A player is already invisible if he has already run the /fullcloak hide command
            CommandValidator.isTrue(!fcPlayer.isInvisible(), Messages.getMessage("Already invisible"));
            CommandValidator.isTrue(!fcPlayer.isWaitingToHide(), Messages.getMessage("Already waiting delay"));

            // If not allowed, check that players trying to hide are not in combat (tagged)
            CommandValidator.isTrue(settings.canHideWhenTagged() || !plugin.getCombatTagPlugin().getTagManager().isTagged(player.getUniqueId()), Messages.getMessage("Cannot hide"));

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

            return;
        }


        if (args[0].equalsIgnoreCase("cooldown")) {
            CommandValidator.isTrue(sender instanceof Player, Messages.getMessage("Command only for player"));
            CommandValidator.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "cooldown"), Messages.getMessage("No permission"));

            final Player player = (Player) sender;
            final User fcPlayer = UserManager.getUser(player);

            if (fcPlayer.isCooldownEnded()) {
                MessageUtil.sendChatMessage(player, Messages.getMessage("Finished cooldown"));
                return;
            }

            final long secondsLeft = fcPlayer.getRemainingCooldown();
            final String message = secondsLeft <= 0 ? Messages.getMessage("Finished cooldown") : MessageUtil.replaceSeconds("Time left", secondsLeft);

            MessageUtil.sendMessage(player, message);
            return;
        }


        if (args[0].equalsIgnoreCase("reload")) {
            CommandValidator.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "reload"), Messages.getMessage("No permission"));

            FullCloak.getInstance().reloadPlugin();

            MessageUtil.sendChatMessage(sender, Messages.getMessage("Plugin reloaded"));
            return;
        }


        MessageUtil.sendChatMessage(sender, "&cUnknown sub-command \"" + args[0] + "\". Use /fullcloak help.");
    }

    private static final class CommandException extends RuntimeException {

        CommandException(@Nullable final String msg) {
            super(msg);
        }
    }

    private static final class CommandValidator {

        static void notNull(@Nullable final Object o, @Nullable final String msg) {
            if (o == null)
                throw new CommandException(msg);
        }

        static void isTrue(final boolean b, @Nullable final String msg) {
            if (!b)
                throw new CommandException(msg);
        }

        static void minLength(@NotNull final Object[] array, final int minLength, @Nullable final String msg) {
            if (array.length < minLength)
                throw new CommandException(msg);
        }
    }
}