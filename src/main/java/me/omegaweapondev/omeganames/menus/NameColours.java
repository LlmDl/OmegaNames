package me.omegaweapondev.omeganames.menus;

import me.omegaweapondev.omeganames.OmegaNames;
import me.omegaweapondev.omeganames.utilities.GUIPermissionsChecker;
import me.omegaweapondev.omeganames.utilities.MessageHandler;
import me.ou.library.Utilities;
import me.ou.library.menus.MenuCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NameColours extends MenuCreator {

  public NameColours() {
    super(5, OmegaNames.getMessagesFile().getConfig().getString("Name_Colour_GUI.GUI_Title"), "&6&lNameColours");

    createItem(10, "RED_WOOL", "Dark Red", "&4");
    createItem(11, "RED_WOOL", "Red", "&c");
    createItem(12, "ORANGE_WOOL", "Gold", "&6");
    createItem(13, "YELLOW_WOOL", "Yellow", "&e");
    createItem(14, "GREEN_WOOL", "Green", "&2");
    createItem(15, "GREEN_WOOL", "Lime", "&a");
    createItem(16, "LIGHT_BLUE_WOOL", "Aqua", "&b");

    createItem(19, "LIGHT_BLUE_WOOL", "Dark Aqua", "&3");
    createItem(20, "BLUE_WOOL", "Dark Blue", "&1");
    createItem(21, "BLUE_WOOL", "Blue", "&9");
    createItem(22, "PINK_WOOL", "Pink", "&d");
    createItem(23, "MAGENTA_WOOL", "Purple", "&5");
    createItem(24, "WHITE_WOOL", "White", "&f");
    createItem(25, "LIGHT_GRAY_WOOL","Light Gray", "&7");

    createItem(29, "GRAY_WOOL","Gray","&8");
    createItem(30, "BLACK_WOOL", "Black", "&0");

    setItem(32, createItemStack("SPONGE", Utilities.colourise("&cCurrent"), loreMessages(Arrays.asList("&cClick here to view", "&cyour current name colour"))), player -> {
      Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.currentNameColour(player.getDisplayName()));
    });

    setItem(33, createItemStack("BARRIER", Utilities.colourise("&cReset"), loreMessages(Arrays.asList("&cClick here to remove", "&cyour name colour completely"))), player -> {
      player.setDisplayName(player.getName());
      player.setPlayerListName(player.getName());
      OmegaNames.getPlayerData().getConfig().set(player.getUniqueId().toString() + ".Name_Colour", OmegaNames.getConfigFile().getConfig().getString("Name_Colour.Default_Colour"));
      OmegaNames.getPlayerData().saveConfig();
      Utilities.message(player, MessageHandler.prefix() + " " + MessageHandler.nameColourRemoved());
    });
  }

  private void createItem(final Integer slot, final String material, final String name, final String colour) {
    for(Player online : Bukkit.getOnlinePlayers()) {
      if(Utilities.checkPermission(online, true, "omeganames.namecolours.open")) {
        setItem(slot, createItemStack(material, Utilities.colourise(colour + name), nameColourDenied(online, name)), player -> {
          GUIPermissionsChecker.nameColourPermsCheck(player, name, colour);
        });
      }
    }
  }

  private ItemStack createItemStack(final String material, final String name, final List<String> itemLore) {
    final ItemStack item =  new ItemStack(Material.valueOf(material), 1);
    final ItemMeta itemMeta = item.getItemMeta();

    itemMeta.setDisplayName(Utilities.colourise(name));

    if(OmegaNames.getConfigFile().getConfig().getBoolean("Per_Name_Colour_Permissions")) {
      for(Player online : Bukkit.getOnlinePlayers()) {
        itemMeta.setLore(nameColourDenied(online, name));
      }
    } else {
      itemMeta.setLore(itemLore);
    }
    item.setItemMeta(itemMeta);

    return item;
  }

  private List<String> loreMessages(final List<String> lore) {
    List<String> colouredLore = new ArrayList<>();

    for(String string : lore) {
      colouredLore.add(Utilities.colourise(string));
    }

    return colouredLore;
  }

  private List<String> nameColourDenied(final Player player, final String colourName) {
    List<String> customLore = MessageHandler.nameColourItemLore(colourName + player.getName());
    List<String> deniedAccess = new ArrayList<>();

    if(OmegaNames.getConfigFile().getConfig().getBoolean("Per_Name_Colour_Permissions")) {
      if(!player.hasPermission("omeganames.namecolours.colours." + colourName.replace(" ", "").toLowerCase()) && !player.isOp()) {
        for(String string : MessageHandler.nameColourNoPermissionLore()) {
          deniedAccess.add(Utilities.colourise(string));
        }
        return deniedAccess;
      } else {
        return loreMessages(customLore);
      }
    } else {
      return loreMessages(customLore);
    }
  }
}
