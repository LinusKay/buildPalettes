package com.libus.buildPalettes;

import com.libus.buildPalettes.commands.paletteCommands;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    private File PaletteFile;
    private FileConfiguration paletteConfig;
    private FileConfiguration config;

    @Override
    public void onEnable()
    {
        createPaletteConfig();
        getServer().getConsoleSender().sendMessage("buildPalettes enabled");
        getCommand("palette").setExecutor(new paletteCommands(this));
    }

    @Override
    public void onDisable()
    {

    }

    public FileConfiguration getPaletteConfig() {
        return this.paletteConfig;
    }

    private void createPaletteConfig() {
        PaletteFile = new File(getDataFolder(), "palettes.yml");
        if (!PaletteFile.exists()) {
            PaletteFile.getParentFile().mkdirs();
            saveResource("palettes.yml", false);
        }

        paletteConfig = new YamlConfiguration();
        try {
            paletteConfig.load(PaletteFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void savePaletteConfig() {
        try {
            getPaletteConfig().save(PaletteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // reassign variables
        PaletteFile = new File(getDataFolder(), "palettes.yml");
        paletteConfig = YamlConfiguration.loadConfiguration(PaletteFile);
    }
}
