// instead of just pulling strings, create a Palette object type
// this allows not just the name, but the palette contents to be grabbed
// allowing functions such as showing the palette blocks upon hover

//method of pulling UUID of offline players queries the API online, which can be slow

package com.libus.buildPalettes.commands;

import com.libus.buildPalettes.Main;
import com.libus.buildPalettes.Palette;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class paletteCommands implements CommandExecutor {

    private final Main plugin;

    public paletteCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        String playerUUID = player.getUniqueId().toString();

        if (args.length == 0) { player.sendMessage("No args given"); return true;}
        else {
            //region SAVE PALETTE
            //save inventory
            if (args[0].equalsIgnoreCase("save")) {
                if (!(checkPermission(player, "palettes.save"))) { return true; }

                //check how many palettes user already has
                int paletteLimit = plugin.getPluginConfig().getInt("palette_limit");
                if(getPalettesByPlayer(playerUUID).size() >= paletteLimit) {
                    player.sendMessage("Palette limit reached! (" + paletteLimit + ")");
                    return true;
                }

                //require palette name
                if (args.length == 1){
                    player.sendMessage("Please provide a name for your new palette.");
                    return true;
                }
                //loop through inventory and grab non-air items
                String paletteName = args[1];
                List<String> paletteItems = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    if (player.getInventory().getItem(i) != null) {
                        String itemName = player.getInventory().getItem(i).getType().name();
                        paletteItems.add(itemName);
                        if (plugin.getPluginConfig().getStringList("blacklist").contains(itemName)){
                            player.sendMessage("Palette cannot contain blacklisted item " + itemName);
                            return true;
                        }
                    }
                }
                plugin.getPaletteConfig().set(paletteName + ".owner", playerUUID);
                plugin.getPaletteConfig().set(paletteName + ".privacy", "private");
                plugin.getPaletteConfig().set(paletteName + ".items", paletteItems);
                plugin.savePaletteConfig();
                player.sendMessage("Palette \"" + paletteName + "\" saved");
            }
            //endregion

            //region LOAD PALETTE
            else if (args[0].equalsIgnoreCase("load")) {
                if (!(checkPermission(player, "palettes.load"))) { return true; }

                //require palette name
                if (args.length == 1){
                    player.sendMessage("Please provide a palette name to load");
                    return true;
                }
                String paletteName = args[1];
                //check if palette with given name exists
                if(!(plugin.getPaletteConfig().isSet(paletteName)))
                {
                    player.sendMessage("Palette \"" + paletteName + "\" not found");
                    return true;
                }

                //check if palette belongs to player
                //if does not belong, check if private/public
                //if private, cancel
                //if public, check if player has perm to load other players' palettes
                if(!(playerUUID.equalsIgnoreCase(plugin.getPaletteConfig().get(paletteName + ".owner").toString())))
                {
                    String privacy = plugin.getPaletteConfig().getString(paletteName + ".privacy");
                    if (privacy.equalsIgnoreCase("private")) {
                        player.sendMessage("This palette is private");
                        return true;
                    }
                    if (privacy.equalsIgnoreCase("public")) {
                        if (!(checkPermission(player, "palettes.load.other"))) { return true; }
                    }
                }

                List<String> paletteItems = plugin.getPaletteConfig().getStringList(paletteName + ".items");
                for (String item : paletteItems) {
                    ItemStack paletteItem = new ItemStack(Material.getMaterial(item));
                    player.getInventory().addItem(paletteItem);
                }
                player.sendMessage("Palette \"" + paletteName + "\" loaded");
            }
            //endregion

            //region DELETE PALETTE
            else if (args[0].equalsIgnoreCase("delete")) {
                if (!(checkPermission(player, "palettes.delete"))) { return true; }

                if (args.length == 1){
                    player.sendMessage("Please provide a palette name to delete");
                    return true;
                }
                String paletteName = args[1];
                //check if palette with given name exists
                if(!(plugin.getPaletteConfig().isSet(paletteName)))
                {
                    player.sendMessage("Palette \"" + paletteName + "\" not found");
                    return true;
                }

                if(!(playerUUID.equalsIgnoreCase(plugin.getPaletteConfig().get(paletteName + ".owner").toString()) && checkPermission(player, "palettes.delete.other")))
                {
                    player.sendMessage("You do not have permission to delete \"" + paletteName + "\"");
                    return true;
                }
                plugin.getPaletteConfig().set(paletteName, null);
                plugin.savePaletteConfig();
                player.sendMessage("Palette \"" + paletteName + "\" deleted");
            }
            //endregion

            //region RELOAD/HELP
            else if (args[0].equalsIgnoreCase("reload")) {
                if (!(checkPermission(player, "palettes.reload"))) { return true; }
                plugin.reloadPluginConfig();
            }

            else if (args[0].equalsIgnoreCase("help")) {
                if (!(checkPermission(player, "palettes.help"))) { return true; }
                player.sendMessage("/palette <save|load|delete> <paletteName> - save/load/delete palette\n/palette reload - reload plugin config");
            }
            //endregion

            //region LIST PALETTES
            else if (args[0].equalsIgnoreCase("list")) {
                if (!(checkPermission(player, "palettes.list"))) { return true; }
                List<Palette> palettes = new ArrayList<>();

                int page = 0;

                // get all personal palettes
                // /palette list
                if (args.length == 1 || isNumeric(args[1])) {
                    palettes = getPalettesByPlayer(playerUUID);
                    player.sendMessage("List palettes (" + player.getName() + "):");
                    if (args.length > 1) {
                        page = Integer.parseInt(args[1]);
                    }
                }
                else {
                    // get all public palettes
                    // /palette list public
                    if (args[1].equalsIgnoreCase("public")) {
                        if (!(checkPermission(player, "palettes.list.public"))) { return true; }
                        palettes = getPalettesByPrivacy("public");
                        player.sendMessage("List palettes (Public):");
                        if (args.length > 2) {
                            page = Integer.parseInt(args[2]);
                        }
                    }
                    // get all palettes belonging to player
                    // /palette list player <playerName>
                    else if (args[1].equalsIgnoreCase("player")) {
                        if (!(checkPermission(player, "palettes.list.player"))) { return true; }
                        if (args.length < 3) { player.sendMessage("Please specify player name"); return true; }
                        String targetUUID = Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString();
                        palettes = getPalettesByPlayer(targetUUID);

                        player.sendMessage("List palettes (" + args[2] + "):");
                        if (args.length > 3) {
                            page = Integer.parseInt(args[3]);
                        }
                    }
                }
                if(palettes.size() == 0) { player.sendMessage("No palettes found!"); return true; }

                int maxDisplay = 6;
                if (page > 0) page--;
                for(int i = page * maxDisplay; i < page * maxDisplay + maxDisplay; i++) {
                    String name = palettes.get(i).getName();
                    String privacy = palettes.get(i).getPrivacy();
                    List<String> blocks = palettes.get(i).getBlocks();
                    player.sendMessage("- " + name);
                    if(i + 1 >= palettes.size()) break;
                }
                player.sendMessage("page " + (page+1));
            }
            //endregion

            //region EDIT PALETTE
            else if(args[0].equalsIgnoreCase("edit")) {
                if (!(checkPermission(player, "palettes.edit"))) { return true; }
                //require palette name
                if (args.length == 1){
                    player.sendMessage("Please provide a palette name to edit");
                    return true;
                }
                String paletteName = args[1];

                if(!(playerUUID.equalsIgnoreCase(plugin.getPaletteConfig().get(paletteName + ".owner").toString())))
                {
                    if (!(checkPermission(player, "palettes.edit.other"))) { return true; }
                }


                if (args.length < 3) {
                    player.sendMessage("Please provide a parameter to edit");
                    return true;
                }
                // edit name
                // /palette edit <paletteName> name <newPaletteName>
                if (args[2].equalsIgnoreCase("name") || args[2].equalsIgnoreCase("rename")) {
                    if (args.length < 4) {
                        player.sendMessage("Please enter a new name");
                        return true;
                    }
                    String newPaletteName = args[3];
                    for (String s : plugin.getPaletteConfig().getConfigurationSection(paletteName).getKeys(false)) {
                        plugin.getPaletteConfig().set(newPaletteName + "." + s, plugin.getPaletteConfig().get(paletteName + "." + s));
                    }
                    plugin.savePaletteConfig();
                    plugin.getPaletteConfig().set(paletteName, null);
                    plugin.savePaletteConfig();

                    player.sendMessage("Renamed palette \"" + paletteName + "\" to \"" + newPaletteName + "\"");
                }
                // edit privacy
                // /palette edit <paletteName> privacy <newPrivacy>
                else if(args[2].equalsIgnoreCase("privacy")) {
                    if (args.length < 4) {
                        player.sendMessage("Please enter a privacy setting");
                        return true;
                    }
                    String newPrivacy = args[3];
                    if (!(newPrivacy.equalsIgnoreCase("public") || newPrivacy.equalsIgnoreCase("private"))) {
                        player.sendMessage("Please enter a valid privacy setting (public/private)");
                        return true;
                    }
                    plugin.getPaletteConfig().set(paletteName + ".privacy", newPrivacy);
                    plugin.savePaletteConfig();
                    player.sendMessage("Palette \"" + paletteName + "\" privacy set to " + newPrivacy);
                }

            }
            //endregion

            else { player.sendMessage("Command not recognised");}
        }

        return true;
    }








    public boolean checkPermission(Player p, String perm) {
        if(p.hasPermission(perm)) { return true; }
        else {
            p.sendMessage("You lack permission \"" + perm + "\"");
            return false;
        }
    }

    public List<Palette> getPalettesByPlayer(String playerUUID) {
        List<Palette> palettes = new ArrayList<>();
        for(String key : plugin.getPaletteConfig().getKeys(false)) {
            if(playerUUID.equalsIgnoreCase(plugin.getPaletteConfig().get(key + ".owner").toString())) {
                Palette pal = new Palette(key, plugin.getPaletteConfig().get(key + ".privacy").toString(), plugin.getPaletteConfig().getStringList(key + ".items"));
                palettes.add(pal);
            }
        }
        return palettes;
    }

    public List<Palette> getPalettesByPrivacy(String privacyLevel) {
        List<Palette> palettes = new ArrayList<>();
        for(String key : plugin.getPaletteConfig().getKeys(false)) {
            if(plugin.getPaletteConfig().get(key + ".privacy").toString().equalsIgnoreCase(privacyLevel)) {
                Palette pal = new Palette(key, privacyLevel, plugin.getPaletteConfig().getStringList(key + ".items"));
                palettes.add(pal);
            }
        }
        return palettes;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


}


