package me.omegaweapondev.omeganames.events;

import me.omegaweapondev.omeganames.OmegaNames;
import me.omegaweapondev.omeganames.utilities.MessageHandler;
import me.ou.library.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
    Player player = playerJoinEvent.getPlayer();

    // If the player has permission and Name_Colour_Login is true, set the players namecolour everytime they log in. Otherwise remove it.
    if(Utilities.checkPermission(player, true, "omeganames.namecolour.login") && OmegaNames.getConfigFile().getConfig().getBoolean("Name_Colour_Login")) {
      for(String groupName : OmegaNames.getConfigFile().getConfig().getStringList("Group_Name_Colour.Groups")) {
        player.setDisplayName(groupNameColour(player, groupName) + player.getName());
      }
    } else {
      player.setDisplayName(player.getName());
    }

    // Call gui reload method, so item lore is refreshed for each player, as it checks for permissions
    // to decide which lore messages should be applied
    new BukkitRunnable() {
      @Override
      public void run() {
        OmegaNames.getInstance().onGUIReload();
      }
    }.runTaskLaterAsynchronously(OmegaNames.getInstance(), 40);
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

    final boolean isconfigNameColour = OmegaNames.getConfigFile().getConfig().isBoolean("Name_Colour.Enabled");
    final String configNameColour = MessageHandler.defaultNameColour();

    String finalNameColour;

    // Check if a custom player namecolour has been set
    if(isPlayerDataNameColour) {
      finalNameColour = playerDataNameColour;
      return finalNameColour;
    }

    // No custom namecolour, so uses the default namecolour
    if(isconfigNameColour) {
      finalNameColour = configNameColour;
      return finalNameColour;
    } else {
      return "";
    }
  }
}
