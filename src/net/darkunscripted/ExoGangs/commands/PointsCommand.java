package net.darkunscripted.ExoGangs.commands;

import net.darkunscripted.ExoGangs.Main;
import net.darkunscripted.ExoGangs.data.CurrencyData;
import net.darkunscripted.ExoGangs.utils.Utils;
import net.minecraft.server.v1_13_R2.CommandExecute;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.IOException;

public class PointsCommand implements CommandExecutor, Listener {

    Main plugin = Main.getPlugin(Main.class);

    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(s instanceof Player){
            Player p = (Player) s;
            if(args.length == 0){
                p.sendMessage(Utils.chat("&b------Points-----"));
                p.sendMessage(Utils.chat("&e/points help"));
                p.sendMessage(Utils.chat("&e/points <set|give|remove> <name>"));
                p.sendMessage(Utils.chat("&e/points <name>"));
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("help")){
                    p.sendMessage(Utils.chat("&b------Points-----"));
                    p.sendMessage(Utils.chat("&e/points help"));
                    p.sendMessage(Utils.chat("&e/points <set|give|remove> <name> <amount>"));
                    p.sendMessage(Utils.chat("&e/points <name>"));
                }else{
                    boolean checker = true;
                    for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()){
                        if(player.getName().equalsIgnoreCase(args[0])){
                            checker = false;
                            Player user = (Player) player;
                            if(CurrencyData.crystals.containsKey(user)){
                                p.sendMessage(Utils.chat("&b&lPoints &7>> &a"+user.getName()+" has " + CurrencyData.points.get(user) + " points!"));
                            }else{
                                p.sendMessage(Utils.chat("&b&lPoints &7>> &a"+user.getName()+" has 0 points!"));
                            }
                        }
                    }
                    if(checker){
                        p.sendMessage(Utils.chat("&b&lPoints&7>> &cPlayer not found!"));
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
                                    CurrencyData.points.put(user, amount);
                                    p.sendMessage(Utils.chat("&b&lCrystals &7>> setted " + user.getName() + " points to " + amount + "!"));
                                }
                            }
                            if (checker) {
                                p.sendMessage(Utils.chat("&b&lPoints &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(Utils.chat("&b&lPoints &7>> &cAmount needs to be a number!"));
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
                                    if (CurrencyData.points.containsKey(user)) {
                                        int balance = CurrencyData.points.get(user);
                                        if (balance < amount) {
                                            p.sendMessage(Utils.chat("&b&lPoints &7>> &cBalance can not be negative!"));
                                        } else {
                                            CurrencyData.points.put(user, balance - amount);
                                            p.sendMessage(Utils.chat("&b&lCrystals &7>> removed " + amount + " of points from " + user.getName() + "!"));
                                        }
                                    } else {
                                        p.sendMessage(Utils.chat("&b&lPoints &7>> &cBalance can not be negative!"));
                                    }
                                }
                            }
                            if (checker) {
                                p.sendMessage(Utils.chat("&b&lPoints &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(Utils.chat("&b&lPoints &7>> &cAmount needs to be a number!"));
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
                                    if (CurrencyData.points.containsKey(user)) {
                                        int balance = CurrencyData.points.get(user);
                                        CurrencyData.points.put(user, balance + amount);
                                    } else {
                                        CurrencyData.points.put(user, amount);
                                    }
                                    p.sendMessage(Utils.chat("&b&lCrystals &7>> gave " + amount + " of points to " + user.getName() + "!"));
                                }
                            }
                            if (checker) {
                                p.sendMessage(Utils.chat("&b&lPoints &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(Utils.chat("&b&lPoints &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else{
                    p.sendMessage(Utils.chat("&b------Points-----"));
                    p.sendMessage(Utils.chat("&e/points help"));
                    p.sendMessage(Utils.chat("&e/points <set|give|remove> <name> <amount>"));
                    p.sendMessage(Utils.chat("&e/points <name>"));
                }
            }else{
                p.sendMessage(Utils.chat("&b------Points-----"));
                p.sendMessage(Utils.chat("&e/points help"));
                p.sendMessage(Utils.chat("&e/points <set|give|remove> <name> <amount>"));
                p.sendMessage(Utils.chat("&e/points <name>"));
            }
        }else{
            if(args.length == 0){
                s.sendMessage(Utils.chat("&b------Points-----"));
                s.sendMessage(Utils.chat("&e/points help"));
                s.sendMessage(Utils.chat("&e/points <set|give|remove> <name>"));
                s.sendMessage(Utils.chat("&e/points <name>"));
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("help")){
                    s.sendMessage(Utils.chat("&b------Points-----"));
                    s.sendMessage(Utils.chat("&e/points help"));
                    s.sendMessage(Utils.chat("&e/points <set|give|remove> <name> <amount>"));
                    s.sendMessage(Utils.chat("&e/points <name>"));
                }else{
                    boolean checker = true;
                    for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()){
                        if(player.getName().equalsIgnoreCase(args[0])){
                            checker = false;
                            Player user = (Player) player;
                            if(CurrencyData.crystals.containsKey(user)){
                                s.sendMessage(Utils.chat("&b&lPoints &7>> &a"+user.getName()+" has " + CurrencyData.points.get(user) + " points!"));
                            }else{
                                s.sendMessage(Utils.chat("&b&lPoints &7>> &a"+user.getName()+" has 0 points!"));
                            }
                        }
                    }
                    if(checker){
                        s.sendMessage(Utils.chat("&b&lPoints&7>> &cPlayer not found!"));
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
                                    CurrencyData.points.put(user, amount);
                                }
                            }
                            if (checker) {
                                s.sendMessage(Utils.chat("&b&lPoints &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            s.sendMessage(Utils.chat("&b&lPoints &7>> &cAmount needs to be a number!"));
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
                                    if (CurrencyData.points.containsKey(user)) {
                                        int balance = CurrencyData.points.get(user);
                                        if (balance < amount) {
                                            s.sendMessage(Utils.chat("&b&lPoints &7>> &cBalance can not be negative!"));
                                        } else {
                                            CurrencyData.points.put(user, balance - amount);
                                        }
                                    } else {
                                        s.sendMessage(Utils.chat("&b&lPoints &7>> &cBalance can not be negative!"));
                                    }
                                }
                            }
                            if (checker) {
                                s.sendMessage(Utils.chat("&b&lPoints &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            s.sendMessage(Utils.chat("&b&lPoints &7>> &cAmount needs to be a number!"));
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
                                    if (CurrencyData.points.containsKey(user)) {
                                        int balance = CurrencyData.points.get(user);
                                        CurrencyData.points.put(user, balance + amount);
                                    } else {
                                        CurrencyData.points.put(user, amount);
                                    }
                                }
                            }
                            if (checker) {
                                s.sendMessage(Utils.chat("&b&lPoints &7>> &cPlayer not found!"));
                            }
                        } catch (NumberFormatException e) {
                            s.sendMessage(Utils.chat("&b&lPoints &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else{
                    s.sendMessage(Utils.chat("&b------Points-----"));
                    s.sendMessage(Utils.chat("&e/points help"));
                    s.sendMessage(Utils.chat("&e/points <set|give|remove> <name> <amount>"));
                    s.sendMessage(Utils.chat("&e/points <name>"));
                }
            }else{
                s.sendMessage(Utils.chat("&b------Points-----"));
                s.sendMessage(Utils.chat("&e/points help"));
                s.sendMessage(Utils.chat("&e/points <set|give|remove> <name> <amount>"));
                s.sendMessage(Utils.chat("&e/points <name>"));
            }
        }
        return false;
    }
}
