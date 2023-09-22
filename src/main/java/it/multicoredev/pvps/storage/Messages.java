package it.multicoredev.pvps.storage;

import com.google.gson.annotations.SerializedName;
import it.multicoredev.mclib.json.JsonConfig;

/**
 * Copyright © 2023 by Lorenzo Magni
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
public class Messages extends JsonConfig {
    @SerializedName("command_usage")
    private String commandUsage;
    private String cooldown;
    @SerializedName("insufficient_perms")
    private String insufficientPerms;
    @SerializedName("not_player")
    private String notPlayer;
    @SerializedName("player_not_found")
    private String playerNotFound;
    @SerializedName("pvp_disabled")
    private String pvpDisabled;
    @SerializedName("pvp_disabled_striker")
    private String pvpDisabledStriker;
    @SerializedName("pvp_disabled_other")
    private String pvpDisabledOther;
    @SerializedName("pvp_disabled_target")
    private String pvpDisabledTarget;
    @SerializedName("pvp_enabled")
    private String pvpEnabled;
    @SerializedName("pvp_enabled_other")
    private String pvpEnabledOther;
    @SerializedName("pvp_status_enabled")
    private String pvpStatusEnabled;
    @SerializedName("pvp_status_disabled")
    private String pvpStatusDisabled;
    private String reload;
    @SerializedName("world_blacklisted")
    private String worldBlacklisted;

    @Override
    public Messages init() {
        if (commandUsage == null) commandUsage = "&cIncorrect usage! &eUse /pvp <enable|disable|toggle|status|reload>";
        if (cooldown == null) cooldown = "&cYou can use this command once every {cooldown} seconds.";
        if (insufficientPerms == null) insufficientPerms = "&cYou don't have permission to use this command!";
        if (notPlayer == null) notPlayer = "&cYou must be a player to use this command!";
        if (playerNotFound == null) playerNotFound = "&cPlayer not found!";
        if (pvpDisabled == null) pvpDisabled = "&cPvP is disabled!";
        if (pvpDisabledStriker == null) pvpDisabledStriker = "&cYou have PvP disabled!";
        if (pvpDisabledOther == null) pvpDisabledOther = "&cPvP is disabled for &e{name}&c!";
        if (pvpDisabledTarget == null) pvpDisabledTarget = "&c{name} has PvP disabled!";
        if (pvpEnabled == null) pvpEnabled = "&aPvP is enabled!";
        if (pvpEnabledOther == null) pvpEnabledOther = "&aPvP is enabled for &e{name}&a!";
        if (pvpStatusEnabled == null) pvpStatusEnabled = "&aPvP enabled";
        if (pvpStatusDisabled == null) pvpStatusDisabled = "&cPvP disabled";
        if (reload == null) reload = "&hPvPSwitch has been reloaded!";
        if (worldBlacklisted == null) worldBlacklisted = "&cYou can't toggle PvP in this world!";
        return this;
    }

    public String getCommandUsage() {
        return commandUsage;
    }

    public String getCooldown() {
        return cooldown;
    }

    public String getInsufficientPerms() {
        return insufficientPerms;
    }

    public String getNotPlayer() {
        return notPlayer;
    }

    public String getPlayerNotFound() {
        return playerNotFound;
    }

    public String getPvpDisabled() {
        return pvpDisabled;
    }

    public String getPvpDisabledStriker() {
        return pvpDisabledStriker;
    }

    public String getPvpDisabledOther() {
        return pvpDisabledOther;
    }

    public String getPvpDisabledTarget() {
        return pvpDisabledTarget;
    }

    public String getPvpEnabled() {
        return pvpEnabled;
    }

    public String getPvpEnabledOther() {
        return pvpEnabledOther;
    }

    public String getPvpStatusEnabled() {
        return pvpStatusEnabled;
    }

    public String getPvpStatusDisabled() {
        return pvpStatusDisabled;
    }

    public String getReload() {
        return reload;
    }

    public String getWorldBlacklisted() {
        return worldBlacklisted;
    }
}
