# MCDRCommand-Velocity
Make velocity more compatible with MCDReforged

Splitted from [lls-manager(LBS fork)](https://github.com/Lazy-Bing-Server/lls-manager)

MCDR command suggestion compatible with [Minecraft Command Register](https://github.com/AnzhiZhang/MCDReforgedPlugins/tree/master/minecraft_command_register)

Player chat event parsing compatible with [LBSVelocityHandler](https://github.com/Lazy-Bing-Server/VelocityHandler-MCDR/)

And this plugin will print chat message to velocity console to make MCDReforged custom handlers available to parse commands in chat.

## Config

Config path: `plugins/VelocityMCDRCommand/config.json`

1. `enableMCDRCommandSuggestion`

Enable `mcdr` command to register MCDR command tree to Velocity and provides command suggestion

Default: `true`

2. `enablePrintPlayerChat`

Enable printing chat message to server console

Default: `true`

3. `playerChatLogFormat`

Log format of printing chat message

Default: `[{0}] <{1}> {2}` (0: sub-server, 1: player name, 2: message)

4. `enableAlertRaw`

Enable `alertraw` command to send raw json to server players

Default: `true`


## Command

1. `mcdr <serialized_mcdr_command_tree>`

Register MCDReforged command tree to Velocity, do not register on your own, use [Minecraft Command Register](https://github.com/AnzhiZhang/MCDReforgedPlugins/tree/master/minecraft_command_register) to do this

For example, `!!MCDR` will be registered as `/!!MCDR`

Can only be executed in console

2. `/alertraw <target> <raw_json>`

Send text in raw json like vanilla `tellraw` command

Permission: `velocitymcdrcommand.command.alertraw`

## Credits

Thanks [AnzhiZhang](https://github.com/AnzhiZhang) and [ZhuRuoLing](https://github.com/ZhuRuoLing) for their [MCDRCommandFarbic](https://github.com/AnzhiZhang/MCDRCommandFabric) and [Minecraft Command Register](https://github.com/AnzhiZhang/MCDReforgedPlugins/tree/master/minecraft_command_register)