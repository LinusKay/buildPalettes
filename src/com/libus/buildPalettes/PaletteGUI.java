package com.libus.buildPalettes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class PaletteGUI implements Listener {

    private final Inventory inv;
    private final Main plugin;

    public PaletteGUI(Main plugin, int size, String name) {
        inv = Bukkit.createInventory(null, size, name);
        this.plugin = plugin;
        getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openInventory(final HumanEntity ent) { ent.openInventory(inv); }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        String paletteName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        final Player player = (Player) e.getWhoClicked();

        List<String> blocks = plugin.getPaletteConfig().getStringList(paletteName + ".items");
        for(int i = 0; i < 9; i++) player.getInventory().setItem(i, new ItemStack(Material.AIR));
        for(String blockName : blocks) {
            player.getInventory().addItem(new ItemStack(Material.getMaterial(blockName)));
        }
        player.sendMessage("Loaded palette " + paletteName);

        //////////
    }

    /**
     * Disallow dragging from GUI inventory
     *
     * @param e InventoryDragEvent
     */
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
            e.setCancelled(true);
        }
    }

    public void addPalettes(List<Palette> palettes) {
        int slotCount = 0;
        for (Palette palette : palettes) {
            List<String> blocks = palette.getBlocks();
            String ownerName = palette.getOwner();
            String privacy = palette.getPrivacy();
            for (String blockName : blocks) {
                ItemStack block = new ItemStack(Material.getMaterial(blockName));
                ItemMeta meta = block.getItemMeta();
                meta.setDisplayName("§r§l" + palette.getName());
                meta.setLore(Arrays.asList("", "§r§fOwner: " + ownerName, "§r§fPrivacy: " + privacy, "§r§f" + block.getType().name()));
                block.setItemMeta(meta);
                inv.setItem(slotCount, block);
                slotCount++;
            }
            if (blocks.size() < 9) {
                for(int i = 0; i < 9 - blocks.size(); i++) {
                    inv.setItem(slotCount, new ItemStack(Material.AIR));
                    slotCount++;
                }
            }
        }
    }
}
