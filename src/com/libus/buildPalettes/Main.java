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

    private File ConfigFile;
    private FileConfiguration pluginConfig;

    private File LangFile;
    private FileConfiguration langConfig;

    @Override
    public void onEnable()
    {
        createPluginConfig();
        createPaletteConfig();
        getServer().getConsoleSender().sendMessage("buildPalettes enabled");
        getCommand("palette").setExecutor(new paletteCommands(this));
    }

    @Override
    public void onDisable()
    {

    }

    // Palette storage config setup
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

    // Plugin config setup
    public FileConfiguration getPluginConfig() {
        return this.pluginConfig;
    }

    private void createPluginConfig() {
        ConfigFile = new File(getDataFolder(), "config.yml");
        if (!ConfigFile.exists()){
            ConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        pluginConfig = new YamlConfiguration();
        try {
            pluginConfig.load(ConfigFile);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void savePluginConfig() {
        try {
            getPluginConfig().save(ConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConfigFile = new File(getDataFolder(), "config.yml");
        pluginConfig = YamlConfiguration.loadConfiguration(ConfigFile);
    }

    public void reloadPluginConfig() {
        ConfigFile = new File(getDataFolder(), "config.yml");
        pluginConfig = YamlConfiguration.loadConfiguration(ConfigFile);
    }

    // Lang config setup
    public FileConfiguration getLangConfig() {
        return this.langConfig;
    }

    private void createLangConfig() {
        LangFile = new File(getDataFolder(), "lang.yml");
        if (!LangFile.exists()){
            LangFile.getParentFile().mkdirs();
            saveResource("lang.yml", false);
        }

        langConfig = new YamlConfiguration();
        try {
            langConfig.load(LangFile);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveLangConfig() {
        try {
            getPluginConfig().save(LangFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LangFile = new File(getDataFolder(), "lang.yml");
        langConfig = YamlConfiguration.loadConfiguration(LangFile);
    }

    public void reloadLangConfig() {
        LangFile = new File(getDataFolder(), "lang.yml");
        langConfig = YamlConfiguration.loadConfiguration(LangFile);
    }
}
