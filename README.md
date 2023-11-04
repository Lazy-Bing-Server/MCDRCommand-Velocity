# MCDRCommand-Velocity
Make velocity more compatible with MCDReforged

Splitted from [lls-manager(LBS fork)](https://github.com/Lazy-Bing-Server/lls-manager)

MCDR command suggestion compatible with [Minecraft Command Register](https://github.com/AnzhiZhang/MCDReforgedPlugins/tree/master/minecraft_command_register)

And this plugin will print chat message to velocity console to make MCDReforged custom handlers available to parse commands in chat.

## Config
No configuration is required

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