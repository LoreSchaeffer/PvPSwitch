# PvPSwitch
[![GitHub version](https://img.shields.io/badge/release-1.0.0-blue)](https://github.com/LoreSchaeffer/PvPSwitch)
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
- MVdWPlaceholderAPI support. (_{pvpswitch_pvp_status}_)

## Commands & Permissions
- /pvp <on|off|toggle> - (_pvpswitch.pvp_) - Toggle PvP for yourself.
- /pvp <status> [player] - (_pvpswitch.pvp_) - Check the PvP status of a player.
- /pvp <on|off|toggle> <player> (_pvpswitch.pvp.others_) - Toggle PvP for a player.
- /pvp <reload> - (_pvpswitch.reload_) - Reload the plugin.

_pvpswitch.ignore-cooldown_ - Ignore the PvP cooldown.

## Default config
```yaml
# This is the default state of PvP when a player join the server.
# PvP will be reverted to this every time a player join the server.
pvp-default-enabled: true

# You can use the following list as a blacklist or whitelist of worlds.
# In this list you can add the worlds in which you want the plugin enabled (if whitelist) or disabled (if blacklist).
world-list: [ ]
is-world-list-blacklist: true

# The pvp command can be used only every x seconds.
pvp-command-cooldown: 0

# The messages used by the plugin. You can change them as you like.
# You can use MBCore rich formatting. For more info read this: https://github.com/MultiCoreNetwork/MBCore/blob/dev/README.md#chat-format.
# The strings between the curly braces are tags replaced by the plugin.
# {name} tag can be replaced by {displayname}.
messages:
  command-usage: "&cIncorrect usage! &eUse /pvp <enable|disable|toggle|status|reload>"
  cooldown: "&cYou can use this command once every {cooldown} seconds."
  insufficient-perms: "&cYou don't have permission to use this command!"
  not-player: "&cYou must be a player to use this command!"
  player-not-found: "&cPlayer not found!"
  pvp-disabled: "&cPvP is disabled!"
  pvp-disabled-damager: "&cYou have PvP disabled!"
  pvp-disabled-other: "&cPvP is disabled for &e{name}&c!"
  pvp-disabled-target: "&c{name} has PvP disabled!"
  pvp-enabled: "&aPvP is enabled!"
  pvp-enabled-other: "&aPvP is enabled for &e{name}&a!"
  pvp-status-enabled: "&aPvP enabled"
  pvp-status-disabled: "&cPvP disabled"
  reload: "&hPvPSwitch has been reloaded!"
  world-blacklisted: "&cYou can't toggle PvP in this world!"
```

## Contributing
To contribute to this repository just fork this repository make your changes or add your code and make a pull request.

## License
PvPSwitch is released under "The 3-Clause BSD License". You can find a copy [here](https://github.com/LoreSchaeffer/PvPSwitch/blob/master/LICENSE)