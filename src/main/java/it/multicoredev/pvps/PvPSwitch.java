package it.multicoredev.pvps;

import it.multicoredev.mbcore.spigot.chat.Chat;
import it.multicoredev.mclib.yaml.Configuration;
import it.multicoredev.pvps.listeners.JoinQuitListener;
import it.multicoredev.pvps.listeners.PvPListener;
import it.multicoredev.pvps.placeholders.MVdWPlaceholders;
import it.multicoredev.pvps.placeholders.PAPIPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
public class PvPSwitch extends JavaPlugin {
    public Configuration config;
    private final Map<Player, Boolean> pvpStatus = new HashMap<>();
    private final Map<Player, Long> cmdCooldown = new HashMap<>();
    private BukkitTask cleaner;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists() || !getDataFolder().isDirectory()) {
            if (!getDataFolder().mkdir()) {
                Chat.severe("&4Could not create plugin directory!");
                onDisable();
                return;
            }
        }

        config = new Configuration(new File(getDataFolder(), "config.yml"), getResource("config.yml"));
        try {
            config.autoload();
        } catch (IOException e) {
            Chat.severe("&4Could not load/create config.yml!");
            onDisable();
            return;
        }

        getServer().getPluginManager().registerEvents(new JoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PvPListener(this), this);

        PvpCommand cmd = new PvpCommand(this);
        getCommand("pvp").setExecutor(cmd);
        getCommand("pvp").setTabCompleter(cmd);

        if (config.getInt("pvp-command-cooldown") > 0) {
            cleaner = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
                long now = new Date().getTime();
                int cooldown = config.getInt("pvp-command-cooldown");

                cmdCooldown.forEach((player, time) -> {
                    if (time - now > cooldown) cmdCooldown.remove(player);
                });
            }, 6000, 6000);
        }

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PAPIPlaceholders(this).register();
        }

        if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            MVdWPlaceholders.registerMVdWPlaceholders(this);
        }

        Chat.info("&2PvPSwitch enabled!");
    }

    @Override
    public void onDisable() {
        if (cleaner != null) {
            if (!cleaner.isCancelled()) cleaner.cancel();
            cleaner = null;
        }

        HandlerList.unregisterAll(this);

        Chat.info("&4PvPSwitch disabled!");
    }

    public boolean hasPvPEnabled(Player player) {
        if (pvpStatus.containsKey(player)) return pvpStatus.get(player);
        return config.getBoolean("pvp-default-enabled");
    }

    public boolean hasPvPEnabled(OfflinePlayer player) {
        for (Player p : pvpStatus.keySet()) {
            if (p.getUniqueId().equals(player.getUniqueId())) return pvpStatus.get(p);
        }

        return config.getBoolean("pvp-default-enabled");
    }

    public void setPvPStatus(Player player, boolean enabled) {
        pvpStatus.put(player, enabled);
    }

    public void resetPvPStatus(Player player) {
        pvpStatus.remove(player);
    }

    public boolean hasCooldown(CommandSender sender) {
        if (!(sender instanceof Player)) return false;

        if (cmdCooldown.containsKey(sender)) {
            return new Date().getTime() - cmdCooldown.get(sender) / 1000 < config.getInt("pvp-cooldown");
        }

        return false;
    }

    public void setCooldown(CommandSender sender) {
        if (!(sender instanceof Player)) return;
        cmdCooldown.put((Player) sender, new Date().getTime());
    }

    public void deleteCooldown(Player player) {
        cmdCooldown.remove(player);
    }

    public boolean isWorldListed(Player player) {
        String playerWorld = player.getWorld().getName();
        return config.getStringList("world-list").stream().anyMatch(world -> world.equalsIgnoreCase(playerWorld));
    }
}
