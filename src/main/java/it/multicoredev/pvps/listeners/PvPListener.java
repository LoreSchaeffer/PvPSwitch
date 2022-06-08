package it.multicoredev.pvps.listeners;

import it.multicoredev.mbcore.spigot.chat.Chat;
import it.multicoredev.pvps.PvPSwitch;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

import static org.bukkit.potion.PotionEffectType.*;

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
public class PvPListener implements Listener {
    private static final PotionEffectType[] BAD_POTION_EFFECTS = {SLOW, SLOW_DIGGING, CONFUSION, HARM, INVISIBILITY, BLINDNESS, HUNGER, WEAKNESS, POISON, WITHER, GLOWING, LEVITATION, UNLUCK, BAD_OMEN};
    private final PvPSwitch plugin;

    public PvPListener(PvPSwitch plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player target = (Player) event.getEntity();

        if (plugin.config.getBoolean("is-world-list-blacklist")) {
            if (plugin.isWorldListed(target)) return;
        } else {
            if (!plugin.isWorldListed(target)) return;
        }

        if (event.getDamager() instanceof Player) {
            if (checkPvP(target, (Player) event.getDamager())) event.setCancelled(true);
        } else if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();

            if (!(projectile.getShooter() instanceof Player)) return;

            if (checkPvP(target, (Player) projectile.getShooter())) event.setCancelled(true);
        } else if (event.getDamager() instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion) event.getDamager();

            if (!containsBadEffects(potion.getEffects())) return;
            if (!(potion.getShooter() instanceof Player)) return;

            if (checkPvP(target, (Player) potion.getShooter())) event.setCancelled(true);
        } else if (event.getDamager() instanceof LightningStrike && event.getDamager().getMetadata("TRIDENT").size() > 0) {
            if (!plugin.hasPvPEnabled(target)) event.setCancelled(true);
        } else if (event.getDamager() instanceof Firework) {
            if (!plugin.hasPvPEnabled(target)) event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFlameArrowDamage(EntityCombustByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getCombuster() instanceof Arrow)) return;

        Player target = (Player) event.getEntity();

        if (plugin.config.getBoolean("is-world-list-blacklist")) {
            if (plugin.isWorldListed(target)) return;
        } else {
            if (!plugin.isWorldListed(target)) return;
        }

        Arrow arrow = (Arrow) event.getCombuster();
        if (!(arrow.getShooter() instanceof Player)) {
            if (checkPvP(target, (Player) arrow.getShooter())) event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getPotion().getShooter() instanceof Player)) return;

        Player target = (Player) event.getEntity();

        if (plugin.config.getBoolean("is-world-list-blacklist")) {
            if (plugin.isWorldListed(target)) return;
        } else {
            if (!plugin.isWorldListed(target)) return;
        }

        ThrownPotion potion = event.getPotion();

        if (!containsBadEffects(potion.getEffects())) return;

        Player damager = (Player) event.getPotion().getShooter();

        if (checkPvP(target, damager)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPotionCloudEffect(AreaEffectCloudApplyEvent event) {
        AreaEffectCloud cloud = event.getEntity();

        event.getAffectedEntities().forEach(entity -> {
            if (!(entity instanceof Player)) return;
            Player target = (Player) entity;

            if (plugin.config.getBoolean("is-world-list-blacklist")) {
                if (plugin.isWorldListed(target)) return;
            } else {
                if (!plugin.isWorldListed(target)) return;
            }

            if (!(event.getEntity().getSource() instanceof Player)) return;
            if (cloud.getMetadata("POTION").size() > 0) {
                Collection<PotionEffect> effects = (Collection<PotionEffect>) cloud.getMetadata("POTION").get(0).value();
                if (effects != null && !containsBadEffects(effects)) return;
            }

            Player damager = (Player) event.getEntity().getSource();

            if (checkPvP(target, damager)) event.setCancelled(true);
        });
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerFishing(PlayerFishEvent event) {
        if (!(event.getCaught() instanceof Player)) return;

        Player target = (Player) event.getCaught();

        if (plugin.config.getBoolean("is-world-list-blacklist")) {
            if (plugin.isWorldListed(target)) return;
        } else {
            if (!plugin.isWorldListed(target)) return;
        }

        Player damager = event.getPlayer();

        if (checkPvP(target, damager)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onLightningStrike(LightningStrikeEvent event) {
        if (event.getCause() == LightningStrikeEvent.Cause.TRIDENT) {
            event.getLightning().setMetadata("TRIDENT", new FixedMetadataValue(plugin, event.getLightning().getLocation()));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onLingeringPotion(LingeringPotionSplashEvent event) {
        event.getAreaEffectCloud().setMetadata("POTION", new FixedMetadataValue(plugin, event.getEntity().getEffects()));
    }

    private boolean checkPvP(Player target, Player damager) {
        if (!plugin.hasPvPEnabled(target)) {
            Chat.send(plugin.config.getString("messages.pvp-disabled-target").replace("{name}", target.getName()).replace("{displayname}", target.getDisplayName()), damager);
            return true;
        }

        if (!plugin.hasPvPEnabled(damager)) {
            Chat.send(plugin.config.getString("messages.pvp-disabled-damager"), damager);
            return true;
        }

        return false;
    }

    private boolean containsBadEffects(Collection<PotionEffect> effects) {
        for (PotionEffect effect : effects) {
            for (PotionEffectType badEffect : BAD_POTION_EFFECTS) {
                if (effect.getType().equals(badEffect)) return true;
            }
        }

        return false;
    }
}
