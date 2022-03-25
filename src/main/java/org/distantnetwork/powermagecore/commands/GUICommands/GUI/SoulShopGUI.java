package org.distantnetwork.powermagecore.commands.GUICommands.GUI;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.distantnetwork.powermagecore.builders.InventoryBuilder;
import org.distantnetwork.powermagecore.builders.ItemBuilder;
import org.distantnetwork.powermagecore.utils.Config.ConfigurationManager;
import org.distantnetwork.powermagecore.utils.Config.Hashmap.PlayerSouls;
import org.distantnetwork.powermagecore.utils.WeaponItem;

import static org.distantnetwork.powermagecore.utils.Config.ConfigurationManager.getFileFile;

public class SoulShopGUI extends InventoryBuilder {
    public SoulShopGUI(Player p) {
        super((ConfigurationManager.getFilesAmountInFolder(ConfigurationManager.getWeaponsFolder()) % 9 == 0 ? ConfigurationManager.getFilesAmountInFolder(ConfigurationManager.getWeaponsFolder()) : ConfigurationManager.getFilesAmountInFolder(ConfigurationManager.getWeaponsFolder()) + (9 - (ConfigurationManager.getFilesAmountInFolder(ConfigurationManager.getWeaponsFolder()) % 9)))+9, String.format("%sPowermage Soul Shop", ChatColor.AQUA));
        for (int i = 0; i < getInventory().getSize(); i++) setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).toItem());
        String[] list = ConfigurationManager.getWeaponsFolder().list();
        if (ConfigurationManager.getFilesAmountInFolder(ConfigurationManager.getWeaponsFolder()) <= 0 || list == null) {
            setItem(4, new ItemBuilder(Material.BARRIER).setName(ChatColor.RED + "No weapons found!").toItem());
        } else {
            int i = 0;
            for (String name : list) {
                WeaponItem weaponItem = new WeaponItem(getFileFile(name));
                setItem(i, weaponItem.getItem(), (player -> {
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(ChatColor.RED + "Your inventory is full!");
                    }
                    if (PlayerSouls.getSouls(player.getUniqueId()) < weaponItem.getPrice()) {
                        player.sendMessage(ChatColor.RED + "You don't have enough souls!");
                    }
                    PlayerSouls.removeSouls(player.getUniqueId(), weaponItem.getPrice());
                    player.getInventory().addItem(weaponItem.getItem());
                    player.sendMessage(ChatColor.GREEN + "You bought " + weaponItem.getName() + " for " + weaponItem.getPrice() + " souls!");
                }));
            }
        }
        setItem(getInventory().getSize() - 9, new ItemBuilder(Material.ARROW).setName(String.format("%sBack to Main Menu", ChatColor.GRAY)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).toItem(), player -> new MenuGUI(player).open(player));
        setItem(getInventory().getSize() - 5, new ItemBuilder(Material.BARRIER).setName(String.format("%sClose Menu", org.bukkit.ChatColor.RED)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).toItem(), HumanEntity::closeInventory);
    }
}
