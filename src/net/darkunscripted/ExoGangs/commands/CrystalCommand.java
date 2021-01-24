package net.darkunscripted.ExoGangs.commands;

import net.darkunscripted.ExoGangs.Main;
import net.darkunscripted.ExoGangs.data.CurrencyData;
import net.darkunscripted.ExoGangs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CrystalCommand implements Listener, CommandExecutor {

    Main plugin = Main.getPlugin(Main.class);

    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(s instanceof Player){
            Player p = (Player) s;
            if(args.length == 0){
                p.sendMessage(Utils.chat("&b------Crystals-----"));
                p.sendMessage(Utils.chat("&e/crystals help"));
                p.sendMessage(Utils.chat("&e/crystals <set|give|remove> <name>"));
                p.sendMessage(Utils.chat("&e/crystals <name>"));
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("help")){
                    p.sendMessage(Utils.chat("&b------Crystals-----"));
                    p.sendMessage(Utils.chat("&e/crystals help"));
                    p.sendMessage(Utils.chat("&e/crystals <set|give|remove> <name> <amount>"));
                    p.sendMessage(Utils.chat("&e/crystals <name>"));
                }else if(args[0].equalsIgnoreCase("shop")) {
                    Inventory CrystalShop = Bukkit.createInventory(null, 9, Utils.chat("&b&lCrystal Shop"));
                    ItemStack item = new ItemStack(Material.GOLD_NUGGET, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(Utils.chat("&6Buy GangPoints"));
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(Utils.chat("&bBuy 1 for " + plugin.getConfig().getInt("settings.crystal-for-gangpoints") + " crystal"));
                    lore.add(Utils.chat("&bShift Click = 10x"));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    CrystalShop.setItem(4, item);
                    p.openInventory(CrystalShop);
                }else{
                    boolean checker = true;
                    for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()){
                        if(player.getName().equalsIgnoreCase(args[0])){
                            checker = false;
                            Player user = (Player) player;
                            if(CurrencyData.crystals.containsKey(user)){
                                p.sendMessage(Utils.chat("&b&lCrystals &7>> &a"+user.getName()+" has " + CurrencyData.crystals.get(user) + " crystals!"));
                            }else{
                                p.sendMessage(Utils.chat("&b&lCrystals &7>> &a"+user.getName()+" has 0 crystals!"));
                            }
                        }
                    }
                    if(checker){
                        p.sendMessage(Utils.chat("&b&lCrystals &7>> &cPlayer not found!"));
                    }
                }
            }else if(args.length == 3){
                if(args[0].equalsIgnoreCase("set")){
                    if(p.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                                if (player.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    Player user = (Player) player;
                                    CurrencyData.crystals.put(user, amount);
                                    p.sendMessage(Utils.chat("&b&lCrystals &7>> setted " + user.getName() + " to " + amount + " crystals!"));
                                }
                            }
                            if (checker) {
                                p.sendMessage(Utils.chat("&b&lCrystals &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(Utils.chat("&b&lCrystals &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("remove")){
                    if(p.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                                if (player.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    Player user = (Player) player;
                                    if (CurrencyData.crystals.containsKey(user)) {
                                        int balance = CurrencyData.crystals.get(user);
                                        if (balance < amount) {
                                            p.sendMessage(Utils.chat("&b&lCrystals &7>> &cBalance can not be negative!"));
                                        } else {
                                            CurrencyData.crystals.put(user, balance - amount);
                                        }
                                        p.sendMessage(Utils.chat("&b&lCrystals &7>> removed " + amount + " crystals from " + user.getName() + "!"));
                                    } else {
                                        p.sendMessage(Utils.chat("&b&lCrystals &7>> &cBalance can not be negative!"));
                                    }
                                }
                            }
                            if (checker) {
                                p.sendMessage(Utils.chat("&b&lCrystals &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(Utils.chat("&b&lCrystals &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("give")){
                    if(p.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                                if (player.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    Player user = (Player) player;
                                    if (CurrencyData.crystals.containsKey(user)) {
                                        int balance = CurrencyData.crystals.get(user);
                                        CurrencyData.crystals.put(user, balance + amount);
                                    } else {
                                        CurrencyData.crystals.put(user, amount);
                                    }
                                    p.sendMessage(Utils.chat("&b&lCrystals &7>> gave " + user.getName() + " " + amount + " crystals!"));
                                }
                            }
                            if (checker) {
                                p.sendMessage(Utils.chat("&b&lCrystals &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(Utils.chat("&b&lCrystals &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else{
                    p.sendMessage(Utils.chat("&b------Crystals-----"));
                    p.sendMessage(Utils.chat("&e/crystals help"));
                    p.sendMessage(Utils.chat("&e/crystals <set|give|remove> <name> <amount>"));
                    p.sendMessage(Utils.chat("&e/crystals <name>"));
                }
            }else{
                p.sendMessage(Utils.chat("&b------Crystals-----"));
                p.sendMessage(Utils.chat("&e/crystals help"));
                p.sendMessage(Utils.chat("&e/crystals <set|give|remove> <name> <amount>"));
                p.sendMessage(Utils.chat("&e/crystals <name>"));
            }
        }else{
            if(args.length == 0){
                s.sendMessage(Utils.chat("&b------Crystals-----"));
                s.sendMessage(Utils.chat("&e/crystals help"));
                s.sendMessage(Utils.chat("&e/crystals <set|give|remove> <name>"));
                s.sendMessage(Utils.chat("&e/crystals <name>"));
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("help")){
                    s.sendMessage(Utils.chat("&b------Crystals-----"));
                    s.sendMessage(Utils.chat("&e/crystals help"));
                    s.sendMessage(Utils.chat("&e/crystals <set|give|remove> <name> <amount>"));
                    s.sendMessage(Utils.chat("&e/crystals <name>"));
                }else{
                    boolean checker = true;
                    for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()){
                        if(player.getName().equalsIgnoreCase(args[0])){
                            checker = false;
                            Player user = (Player) player;
                            if(CurrencyData.crystals.containsKey(user)){
                                s.sendMessage(Utils.chat("&b&lCrystals &7>> &a"+user.getName()+" has " + CurrencyData.crystals.get(user) + " crystals!"));
                            }else{
                                s.sendMessage(Utils.chat("&b&lCrystals &7>> &a"+user.getName()+" has 0 crystals!"));
                            }
                        }
                    }
                    if(checker){
                        s.sendMessage(Utils.chat("&b&lCrystals &7>> &cPlayer not found!"));
                    }
                }
            }else if(args.length == 3){
                if(args[0].equalsIgnoreCase("set")){
                    if(s.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                                if (player.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    Player user = (Player) player;
                                    CurrencyData.crystals.put(user, amount);
                                }
                            }
                            if (checker) {
                                s.sendMessage(Utils.chat("&b&lCrystals &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            s.sendMessage(Utils.chat("&b&lCrystals &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("remove")){
                    if(s.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                                if (player.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    Player user = (Player) player;
                                    if (CurrencyData.crystals.containsKey(user)) {
                                        int balance = CurrencyData.crystals.get(user);
                                        if (balance < amount) {
                                            s.sendMessage(Utils.chat("&b&lCrystals &7>> &cBalance can not be negative!"));
                                        } else {
                                            CurrencyData.crystals.put(user, balance - amount);
                                        }
                                    } else {
                                        s.sendMessage(Utils.chat("&b&lCrystals &7>> &cBalance can not be negative!"));
                                    }
                                }
                            }
                            if (checker) {
                                s.sendMessage(Utils.chat("&b&lCrystals &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            s.sendMessage(Utils.chat("&b&lCrystals &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("give")){
                    if(s.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                                if (player.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    Player user = (Player) player;
                                    if (CurrencyData.crystals.containsKey(user)) {
                                        int balance = CurrencyData.crystals.get(user);
                                        CurrencyData.crystals.put(user, balance + amount);
                                    } else {
                                        CurrencyData.crystals.put(user, amount);
                                    }
                                }
                            }
                            if (checker) {
                                s.sendMessage(Utils.chat("&b&lCrystals &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            s.sendMessage(Utils.chat("&b&lCrystals &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else{
                    s.sendMessage(Utils.chat("&b------Crystals-----"));
                    s.sendMessage(Utils.chat("&e/crystals help"));
                    s.sendMessage(Utils.chat("&e/crystals <set|give|remove> <name> <amount>"));
                    s.sendMessage(Utils.chat("&e/crystals <name>"));
                }
            }else{
                s.sendMessage(Utils.chat("&b------Crystals-----"));
                s.sendMessage(Utils.chat("&e/crystals help"));
                s.sendMessage(Utils.chat("&e/crystals <set|give|remove> <name> <amount>"));
                s.sendMessage(Utils.chat("&e/crystals <name>"));
            }
        }
        return false;
    }

}
