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