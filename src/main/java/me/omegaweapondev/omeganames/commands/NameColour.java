package me.omegaweapondev.omeganames.commands;

import me.omegaweapondev.omeganames.OmegaNames;
import me.omegaweapondev.omeganames.utilities.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.commands.PlayerCommand;
import org.bukkit.entity.Player;

public class NameColour extends PlayerCommand {

  @Override
  protected void execute(Player player, String[] strings) {
    if(Utilities.checkPermission(player, true, "omeganames.namecolours.open")) {
      OmegaNames.getInstance().getNameColourGUI().openInventory(player);
    } else {
      Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.noPermission());
    }
  }
}
