# PvPSwitch
[![GitHub version](https://img.shields.io/badge/release-1.0.1-blue)](https://github.com/LoreSchaeffer/PvPSwitch)
[![GitHub stars](https://img.shields.io/github/stars/LoreSchaeffer/PvPSwitch)](https://github.com/LoreSchaeffer/PvPSwitch)
[![GitHub issues](https://img.shields.io/github/issues/LoreSchaeffer/PvPSwitch)](https://github.com/LoreSchaeffer/PvPSwitch/issues)

## Description
This is a simple plugin to toggle PvP for players.

## Features
- Easy to use. Just put the plugin in your plugins directory and give the permissions to your players.
- Both players must have the PvP on to attack each other.
- You can see others PvP state.
- Staff can toggle others PvP state.
- Lightweight.
- Customizable messages with rich formatting. [Click here to see more.](https://github.com/MultiCoreNetwork/MBCore/blob/dev/README.md#chat-format)
- PlaceholderAPI support. (_%pvpswitch_pvp_status%_)

## Commands & Permissions
- /pvp <on|off|toggle> - (_pvpswitch.pvp_) - Toggle PvP for yourself.
- /pvp <status> [player] - (_pvpswitch.pvp_) - Check the PvP status of a player.
- /pvp <on|off|toggle> <player> (_pvpswitch.pvp.others_) - Toggle PvP for a player.
- /pvp <reload> - (_pvpswitch.reload_) - Reload the plugin.

_pvpswitch.ignore-cooldown_ - Ignore the PvP cooldown.

## Default config.json
```json
{
  "pvp_default": true,
  "world_list": [],
  "blacklist": true,
  "cooldown": 0
}
```

### Explanation:
- **pvp_default**: This is the default state of PvP when a player join the server (PvP will be reverted to this every time a player join the server).
- **world_list**: You can use the following list as a blacklist or whitelist of worlds in which you want the plugin enabled (if whitelist) or disabled (if blacklist).
- **blacklist**: If true, the world_list will be used as a blacklist, if false, the world_list will be used as a whitelist.
- **cooldown**: The cooldown in seconds to toggle PvP.

## Default messages.json
```json
{
  "command_usage": "&cIncorrect usage! &eUse /pvp <enable|disable|toggle|status|reload>",
  "cooldown": "&cYou can use this command once every {cooldown} seconds.",
  "insufficient_perms": "&cYou don't have permission to use this command!",
  "not_player": "&cYou must be a player to use this command!",
  "player_not_found": "&cPlayer not found!",
  "pvp_disabled": "&cPvP is disabled!",
  "pvp_disabled_striker": "&cYou have PvP disabled!",
  "pvp_disabled_other": "&cPvP is disabled for &e{name}&c!",
  "pvp_disabled_target": "&c{name} has PvP disabled!",
  "pvp_enabled": "&aPvP is enabled!",
  "pvp_enabled_other": "&aPvP is enabled for &e{name}&a!",
  "pvp_status_enabled": "&aPvP enabled",
  "pvp_status_disabled": "&cPvP disabled",
  "reload": "&hPvPSwitch has been reloaded!",
  "world_blacklisted": "&cYou can't toggle PvP in this world!"
}
```

## Contributing
To contribute to this repository just fork this repository make your changes or add your code and make a pull request.

## License
PvPSwitch is released under "The 3-Clause BSD License". You can find a copy [here](https://github.com/LoreSchaeffer/PvPSwitch/blob/master/LICENSE)