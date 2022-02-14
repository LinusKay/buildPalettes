package com.libus.buildPalettes.commands;

import com.libus.buildPalettes.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Array;
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
                //require palette name
                if (args.length == 1){
                    player.sendMessage("Please provide a name for your palette");
                    return true;
                }
                //loop through inventory and grab non-air items
                String paletteName = args[1];
                List<String> paletteItems = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    if (player.getInventory().getItem(i) != null) {
                        String itemName = player.getInventory().getItem(i).getType().name();
                        player.sendMessage(itemName);
                        paletteItems.add(itemName);
                    }
                }
                plugin.getPaletteConfig().set(paletteName + ".owner", playerUUID);
                plugin.getPaletteConfig().set(paletteName + ".items", paletteItems);
                plugin.savePaletteConfig();
            }
            //endregion

            //region LOAD PALETTE
            else if (args[0].equalsIgnoreCase("load")) {
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

                List<String> paletteItems = plugin.getPaletteConfig().getStringList(paletteName + ".items");
                for(int i = 0; i < paletteItems.size(); i++)
                {
                    ItemStack paletteItem = new ItemStack(Material.getMaterial(paletteItems.get(i)));
                    player.getInventory().addItem(paletteItem);
                }
                player.sendMessage("Palette \"" + paletteName + "\" loaded");
            }
            //endregion

            //region DELETE PALETTE
            else if (args[0].equalsIgnoreCase("delete")) {
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

                if(!(playerUUID == plugin.getPaletteConfig().get(paletteName + ".owner")))
                {
                    player.sendMessage("You do not have permission to delete \"" + paletteName + "\"");
                    return true;
                }
                plugin.getPaletteConfig().set(paletteName, null);
                plugin.savePaletteConfig();
            }
            //endregion
        }

        return true;
    }
}
