# buildPalettes
A Minecraft Spigot plugin that allows players to manage/share custom block palettes.

## Commands
Commands such as load, and delete, have additional ".other" permissions. These control whether a player can load palettes belonging to other players or not.

| Command | description | permission |
|-------- | ----------- | ---------- |
| /palette save \<paletteName> | save a palette with the given name | palette.save |
| /palette load \<paletteName> | load a palette with the given name | palette.load, palettes.load.other |
| /palette delete \<paletteName> | delete a palette with the given name | palette.delete, palettes.delete.other|
| /palette help | view plugin commands & info | palette.help |
| /palette reload | reload plugin config | palette.reload |
| /palette list| list personal palettes | palette.list |
| /palette list public | list all public palettes | palette.list.public |
| /palette list player \<playerName> | list all palettes belonging to player | palette.list.player |
| /palette edit \<paletteName> <name\|rename> <newName> | edit a specific palette's name | palette.edit.name, palette.edit.other |
| /palette edit \<paletteName> privacy <privacySetting> | edit a specific palette's privacy | palette.edit.privacy, palette.edit.other |

## Config
```YAML
palette_limit: 3
blacklist:
- BARRIER
```

## Palette Config
```YAML
# in the event that palettes need to be edited directly, the format is as follows
# text in <angled brackets> refers to a variable

# <paletteName>:
#  owner: <UUID of player>
#  items:
#  - <block>
#  - <block>
#  - <block>
#  - <block>
#  - <block>
#  - <block>
#  - <block>
#  - <block>
#  - <block>

coolPalette:
  owner: 2645e025-e900-441b-8d02-074a36680777
  items:
  - STONE
  - OAK_PLANKS
```