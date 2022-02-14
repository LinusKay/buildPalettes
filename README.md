# buildPalettes
A Minecraft Spigot plugin that allows players to manage/share custom block palettes.

## Commands

| Command | description | permission |
|-------- | ----------- | ---------- |
| /palette save \<paletteName> | save a palette with the given name | palette.save |
| /palette load \<paletteName> | load a palette with the given name | palette.load |
| /palette delete \<paletteName> | delete a palette with the given name | palette.delete|
| /palette help | view plugin commands & info | palette.help |
| /palette reload | reload plugin config | palette.reload |

# Config
``` YAML
# blacklist particular blocks - MUST BE CAPITALISED, VALID BLOCK NAMES
blacklist:
- BARRIER
```

# Palette Config
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