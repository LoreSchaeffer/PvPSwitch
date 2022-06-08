package it.multicoredev.pvps;

import it.multicoredev.mbcore.spigot.chat.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Copyright © 2022 by Lorenzo Magni
 * This file is part of PvPSwitch.
 * PvPSwitch is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class PvpCommand implements CommandExecutor, TabCompleter {
    private final PvPSwitch plugin;

    public PvpCommand(PvPSwitch plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("pvpswitch.pvp")) {
            Chat.send(plugin.config.getString("messages.insufficient-perms"), sender);
            return true;
        }

        if (args.length == 0) {
            Chat.send(plugin.config.getString("messages.command-usage"), sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("on")) {
            if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    Chat.send(plugin.config.getString("messages.not-player"), sender);
                    return true;
                }

                Player player = (Player) sender;

                if (plugin.hasCooldown(player) && !player.hasPermission("pvpswitch.ignore-cooldown")) {
                    Chat.send(plugin.config.getString("messages.cooldown").replace("{cooldown}", String.valueOf(plugin.config.getInt("pvp-command-cooldown"))), sender);
                    return true;
                }

                if (!worldCheck(player, player)) return true;

                plugin.setPvPStatus(player, true);
                Chat.send(plugin.config.getString("messages.pvp-enabled"), sender);

                if (plugin.config.getInt("pvp-command-cooldown") > 0 && !player.hasPermission("pvpswitch.pvp.ignore-cooldown")) plugin.setCooldown(player);
            } else {
                if (!sender.hasPermission("pvpswitch.pvp.others")) {
                    Chat.send(plugin.config.getString("messages.insufficient-perms"), sender);
                    return true;
                }

                if (plugin.hasCooldown(sender) && !sender.hasPermission("pvpswitch.ignore-cooldown")) {
                    Chat.send(plugin.config.getString("messages.cooldown").replace("{cooldown}", String.valueOf(plugin.config.getInt("pvp-command-cooldown"))), sender);
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Chat.send(plugin.config.getString("messages.player-not-found"), sender);
                    return true;
                }

                if (!worldCheck(target, sender)) return true;

                plugin.setPvPStatus(target, true);
                Chat.send(plugin.config.getString("messages.pvp-enabled"), target);
                Chat.send(plugin.config.getString("messages.pvp-enabled-other").replace("{name}", target.getName()).replace("{displayname}", target.getDisplayName()), sender);
            }
        } else if (args[0].equalsIgnoreCase("off")) {
            if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    Chat.send(plugin.config.getString("messages.not-player"), sender);
                    return true;
                }

                Player player = (Player) sender;

                if (!worldCheck(player, player)) return true;

                plugin.setPvPStatus(player, false);
                Chat.send(plugin.config.getString("messages.pvp-disabled"), sender);

                if (plugin.config.getInt("pvp-command-cooldown") > 0 && !player.hasPermission("pvpswitch.pvp.ignore-cooldown")) plugin.setCooldown(player);
            } else {
                if (!sender.hasPermission("pvpswitch.pvp.others")) {
                    Chat.send(plugin.config.getString("messages.insufficient-perms"), sender);
                    return true;
                }

                if (plugin.hasCooldown(sender) && !sender.hasPermission("pvpswitch.ignore-cooldown")) {
                    Chat.send(plugin.config.getString("messages.cooldown").replace("{cooldown}", String.valueOf(plugin.config.getInt("pvp-command-cooldown"))), sender);
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Chat.send(plugin.config.getString("messages.player-not-found"), sender);
                    return true;
                }

                if (!worldCheck(target, sender)) return true;

                plugin.setPvPStatus(target, false);
                Chat.send(plugin.config.getString("messages.pvp-disabled"), target);
                Chat.send(plugin.config.getString("messages.pvp-disabled-other").replace("{name}", target.getName()).replace("{displayname}", target.getDisplayName()), sender);
            }
        } else if (args[0].equalsIgnoreCase("toggle")) {
            if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    Chat.send(plugin.config.getString("messages.not-player"), sender);
                    return true;
                }

                Player player = (Player) sender;

                if (!worldCheck(player, player)) return true;

                boolean enabled = !plugin.hasPvPEnabled(player);
                plugin.setPvPStatus(player, enabled);
                if (enabled) Chat.send(plugin.config.getString("messages.pvp-enabled"), sender);
                else Chat.send(plugin.config.getString("messages.pvp-disabled"), sender);

                if (plugin.config.getInt("pvp-command-cooldown") > 0 && !player.hasPermission("pvpswitch.pvp.ignore-cooldown")) plugin.setCooldown(player);
            } else {
                if (!sender.hasPermission("pvpswitch.pvp.others")) {
                    Chat.send(plugin.config.getString("messages.insufficient-perms"), sender);
                    return true;
                }

                if (plugin.hasCooldown(sender) && !sender.hasPermission("pvpswitch.ignore-cooldown")) {
                    Chat.send(plugin.config.getString("messages.cooldown").replace("{cooldown}", String.valueOf(plugin.config.getInt("pvp-command-cooldown"))), sender);
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Chat.send(plugin.config.getString("messages.player-not-found"), sender);
                    return true;
                }

                if (!worldCheck(target, sender)) return true;

                boolean enabled = !plugin.hasPvPEnabled(target);
                plugin.setPvPStatus(target, enabled);
                if (enabled) {
                    Chat.send(plugin.config.getString("messages.pvp-enabled"), target);
                    Chat.send(plugin.config.getString("messages.pvp-enabled-other").replace("{name}", target.getName()).replace("{displayname}", target.getDisplayName()), sender);
                } else {
                    Chat.send(plugin.config.getString("messages.pvp-disabled"), target);
                    Chat.send(plugin.config.getString("messages.pvp-disabled-other").replace("{name}", target.getName()).replace("{displayname}", target.getDisplayName()), sender);
                }
            }
        } else if (args[0].equalsIgnoreCase("status")) {
            if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    Chat.send(plugin.config.getString("messages.not-player"), sender);
                    return true;
                }

                Player player = (Player) sender;

                if (!worldCheck(player, player)) return true;

                if (plugin.hasPvPEnabled(player)) Chat.send(plugin.config.getString("messages.pvp-enabled"), sender);
                else Chat.send(plugin.config.getString("messages.pvp-disabled"), sender);
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    Chat.send(plugin.config.getString("messages.player-not-found"), sender);
                    return true;
                }

                if (!worldCheck(target, sender)) return true;

                if (!plugin.hasPvPEnabled(target)) {
                    Chat.send(plugin.config.getString("messages.pvp-enabled"), target);
                    Chat.send(plugin.config.getString("messages.pvp-enabled-other").replace("{name}", target.getName()).replace("{displayname}", target.getDisplayName()), sender);
                } else {
                    Chat.send(plugin.config.getString("messages.pvp-disabled"), target);
                    Chat.send(plugin.config.getString("messages.pvp-disabled-other").replace("{name}", target.getName()).replace("{displayname}", target.getDisplayName()), sender);
                }
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("pvpswitch.reload")) {
                Chat.send(plugin.config.getString("messages.insufficient-perms"), sender);
                return true;
            }

            plugin.onDisable();
            plugin.onEnable();
            Chat.send(plugin.config.getString("messages.reload"), sender);
        } else {
            Chat.send(plugin.config.getString("messages.command-usage"), sender);
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("pvpswitch.pvp")) return null;

        if (args.length == 1) {
            return TabCompleterUtil.getCompletions(args[0], "on", "off", "toggle", "status", "reload");
        } else if (args.length == 2 && sender.hasPermission("pvpswitch.pvp.others")) {
            String sub = args[0];
            if (sub.equalsIgnoreCase("off") || sub.equalsIgnoreCase("on") || sub.equalsIgnoreCase("toggle") || sub.equalsIgnoreCase("status")) {
                return TabCompleterUtil.getPlayers(args[1]);
            }
        }

        return null;
    }

    private boolean worldCheck(Player target, CommandSender sender) {
        if (plugin.config.getBoolean("is-world-list-blacklist")) {
            if (plugin.isWorldListed(target)) {
                Chat.send(plugin.config.getString("messages.world-blacklisted"), sender);
                return false;
            }
        } else {
            if (!plugin.isWorldListed(target)) {
                Chat.send(plugin.config.getString("messages.world-blacklisted"), sender);
                return false;
            }
        }

        return true;
    }
}
