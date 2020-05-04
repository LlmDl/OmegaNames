package me.omegaweapondev.omeganames.events;

import me.omegaweapondev.omeganames.OmegaNames;
import me.omegaweapondev.omeganames.UpdateChecker;
import me.omegaweapondev.omeganames.utilities.MessageHandler;
import me.ou.library.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
    Player player = playerJoinEvent.getPlayer();

    // Set the players namecolours
    for(String groupName : OmegaNames.getConfigFile().getConfig().getConfigurationSection("Group_Name_Colour.Groups").getKeys(false)) {
      if(Utilities.checkPermission(player, true, "omeganames.namecolours.groups." + groupName.toLowerCase())) {
        Utilities.colourise(groupNameColour(player, groupName));

        // Format the tablist
        Bukkit.getScheduler().scheduleSyncRepeatingTask(OmegaNames.getInstance(), () -> {
          player.setPlayerListName(Utilities.colourise(groupNameColour(player, groupName) + player.getName()));
        }, 20L * 5L, 20L * 60L);
      } else {
        player.setDisplayName(Utilities.colourise(playerNameColour(player) + player.getName()));

        // Format the tablist
        Bukkit.getScheduler().scheduleSyncRepeatingTask(OmegaNames.getInstance(), () -> {
          player.setPlayerListName(Utilities.colourise(playerNameColour(player) + player.getName()));
        }, 20L * 5L, 20L * 60L);
      }
    }

    // Call gui reload method, so item lore is refreshed for each player, as it checks for permissions
    // to decide which lore messages should be applied
    new BukkitRunnable() {
      @Override
      public void run() {
        OmegaNames.getInstance().onGUIReload();
      }
    }.runTaskLaterAsynchronously(OmegaNames.getInstance(), 40);

    // Send the player a message on join if there is an update for the plugin
    if(Utilities.checkPermission(player, true, "omeganames.update")) {
      new UpdateChecker(OmegaNames.getInstance(), 73013).getVersion(version -> {
        if (!OmegaNames.getInstance().getDescription().getVersion().equalsIgnoreCase(version)) {
          PluginDescriptionFile pdf = OmegaNames.getInstance().getDescription();
          Utilities.message(player,
            "&bA new version of &c" + pdf.getName() + " &bis avaliable!",
            "&bCurrent Version: &c" + pdf.getVersion() + " &b> New Version: &c" + version,
            "&bGrab it here:&c https://spigotmc.org/resources/73013"
          );
        }
      });
    }
  }

  public static String groupNameColour(final Player player, final String groupName) {
    final boolean isconfigGroupNameColour = OmegaNames.getConfigFile().getConfig().isBoolean("Group_Name_Colour.Enabled");
    final String configGroupNameColour = OmegaNames.getConfigFile().getConfig().getString("Group_Name_Colour.Groups." + groupName);

    final boolean isPlayerDataNameColour = OmegaNames.getPlayerData().getConfig().isSet(player.getUniqueId().toString() + ".Name_Colour");
    final String playerDataNameColour = OmegaNames.getPlayerData().getConfig().getString(player.getUniqueId().toString() + ".Name_Colour");

    String finalNameColour;

    // Check if a custom player namecolour has been set
    if(isPlayerDataNameColour) {
      finalNameColour = playerDataNameColour;

      return finalNameColour;
    }

    // Checks if config group namecolours have been set
    if(Utilities.checkPermission(player, true,"omeganames.namecolours.groups." + groupName.toLowerCase()) && isconfigGroupNameColour) {
      finalNameColour = configGroupNameColour;

      return finalNameColour;
    } else {
      return playerNameColour(player);
    }
  }

  private static String playerNameColour(final Player player) {
    final boolean isPlayerDataNameColour = OmegaNames.getPlayerData().getConfig().isSet(player.getUniqueId().toString() + ".Name_Colour");
    final String playerDataNameColour = OmegaNames.getPlayerData().getConfig().getString(player.getUniqueId().toString() + ".Name_Colour");

    final String configNameColour = MessageHandler.defaultNameColour();

    String finalNameColour;

    // Check if a custom player namecolour has been set
    if(isPlayerDataNameColour) {
      finalNameColour = playerDataNameColour;
      return finalNameColour;
    }

    // No custom namecolour, so uses the default namecolour
    finalNameColour = configNameColour;
    return finalNameColour;
  }
}
