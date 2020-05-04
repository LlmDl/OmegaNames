package me.omegaweapondev.omeganames;

import me.omegaweapondev.omeganames.commands.MainCommand;
import me.omegaweapondev.omeganames.commands.NameColour;
import me.omegaweapondev.omeganames.events.MenuListener;
import me.omegaweapondev.omeganames.events.PlayerListener;
import me.omegaweapondev.omeganames.menus.NameColours;
import me.omegaweapondev.omeganames.utilities.Placeholders;
import me.ou.library.Utilities;
import me.ou.library.configs.ConfigCreator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OmegaNames extends JavaPlugin {
  private static final ConfigCreator configFile = new ConfigCreator("config.yml");
  private static final ConfigCreator messagesFile = new ConfigCreator("messages.yml");
  private static final ConfigCreator playerData = new ConfigCreator("playerData.yml");
  private static OmegaNames instance;

  // Declaring the GUI's
  private NameColours nameColourGUI;

  @Override
  public void onEnable() {
    // Set the plugin & OULibrary Instances
    instance = this;
    Utilities.setInstance(instance);

    // Send a message to console once the plugin has enabled
    Utilities.logInfo(true, "OmegaNames has been enabled!");

    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
      new Placeholders(instance).register();
    }

    // Create the files
    getConfigFile().createConfig();
    getMessagesFile().createConfig();
    getPlayerData().createConfig();

    // Give the playerData.yml file a header
    getPlayerData().getConfig().options().header(
      " -------------------------------------------------------------------------------------------\n" +
        " \n" +
        " Welcome to OmegaNames' Player Data file.\n" +
        " \n" +
        " This file contains all the uuids and namecolour colours\n" +
        " for all the players who have the permission omeganames.login\n" +
        " \n" +
        " -------------------------------------------------------------------------------------------"
    );

    // Create the GUI's
    nameColourGUI = new NameColours();

    // Register the commands and events
    Utilities.registerCommand("omeganames", new MainCommand());
    Utilities.registerCommand("namecolour", new NameColour());

    Utilities.registerEvents(new PlayerListener(), new MenuListener());
  }

  @Override
  public void onDisable() {
    // Set the instance to null when the plugin is disabled
    instance = null;
    super.onDisable();
  }

  public void onReload() {
    // Reload the Name Colour GUI
    new BukkitRunnable() {
      @Override
      public void run() {
        nameColourGUI = new NameColours();
      }
    }.runTaskLaterAsynchronously(getInstance(), 20);

    // Reload the files
    configFile.reloadConfig();
    messagesFile.reloadConfig();
    playerData.reloadConfig();
  }

  // Method to reload just the GUI's
  public void onGUIReload() {
    new BukkitRunnable() {
      @Override
      public void run() {
        nameColourGUI = new NameColours();
      }
    }.runTaskLaterAsynchronously(getInstance(), 20);
  }

  public static ConfigCreator getConfigFile() {
    return configFile;
  }

  public static ConfigCreator getMessagesFile() {
    return messagesFile;
  }

  public static ConfigCreator getPlayerData() {
    return playerData;
  }

  public NameColours getNameColourGUI() {
    return nameColourGUI;
  }

  public static OmegaNames getInstance() {
    return instance;
  }
}
