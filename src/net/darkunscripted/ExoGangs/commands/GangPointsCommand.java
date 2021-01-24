package net.darkunscripted.ExoGangs.commands;

import net.darkunscripted.ExoGangs.Main;
import net.darkunscripted.ExoGangs.data.CurrencyData;
import net.darkunscripted.ExoGangs.data.GangData;
import net.darkunscripted.ExoGangs.managers.Gang;
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

public class GangPointsCommand implements CommandExecutor, Listener {

    Main plugin = Main.getPlugin(Main.class);

    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(s instanceof Player){
            Player p = (Player) s;
            if(args.length == 0){
                p.sendMessage(Utils.chat("&b------GangPoints-----"));
                p.sendMessage(Utils.chat("&e/gangpoints help"));
                p.sendMessage(Utils.chat("&e/gangpoints <set|give|remove> <name>"));
                p.sendMessage(Utils.chat("&e/gangpoints <name>"));
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("help")){
                    p.sendMessage(Utils.chat("&b------GangPoints-----"));
                    p.sendMessage(Utils.chat("&e/gangpoints help"));
                    p.sendMessage(Utils.chat("&e/gangpoints <set|give|remove> <name> <amount>"));
                    p.sendMessage(Utils.chat("&e/gangpoints <name>"));
                }else{
                    boolean checker = true;
                    for(Gang gang : GangData.gangs){
                        if(gang.getName().equalsIgnoreCase(args[0])){
                            checker = false;
                            if(CurrencyData.gangPoints.containsKey(gang)){
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &a"+gang.getName()+" has " + CurrencyData.gangPoints.get(gang) + " GangPoints!"));
                            }else{
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &a"+gang.getName()+" has 0 GangPoints!"));
                            }
                        }
                    }
                    if(checker){
                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cGang not found!"));
                    }
                }
            }else if(args.length == 3){
                if(args[0].equalsIgnoreCase("set")){
                    if(p.hasPermission("exoprison.admin")){
                        try{
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for(Gang gang : GangData.gangs){
                                if(gang.getName().equalsIgnoreCase(args[1])){
                                    checker = false;
                                    CurrencyData.gangPoints.put(gang, amount);
                                    p.sendMessage(Utils.chat("&b&lCrystals &7>> setted " + gang.getName() + " to " + amount + " gangpoints!"));
                                    try{
                                        plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                        plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                    }catch (IOException exception){
                                        p.sendMessage(Utils.chat("&cERROR Could not save to file!"));
                                    }
                                }
                            }
                            if(checker){
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &cGang not found!"));
                            }
                        }catch (NumberFormatException e){
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("remove")){
                    if(p.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (Gang gang : GangData.gangs) {
                                if (gang.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    if (CurrencyData.gangPoints.containsKey(gang)) {
                                        int balance = CurrencyData.gangPoints.get(gang);
                                        if (balance < amount) {
                                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cBalance can not be negative!"));
                                        } else {
                                            CurrencyData.gangPoints.put(gang, balance - amount);
                                            p.sendMessage(Utils.chat("&b&lCrystals &7>> removed " + amount + " of gangpoints from " + gang.getName() + "!"));
                                            try{
                                                plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                                plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                            }catch (IOException exception){
                                                p.sendMessage(Utils.chat("&cERROR Could not save to file!"));
                                            }
                                        }
                                    } else {
                                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cBalance can not be negative!"));
                                    }
                                }
                            }
                            if (checker) {
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &cGang not found!"));
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("give")){
                    if(p.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (Gang gang : GangData.gangs) {
                                if (gang.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    if (CurrencyData.gangPoints.containsKey(gang)) {
                                        int balance = CurrencyData.gangPoints.get(gang);
                                        CurrencyData.gangPoints.put(gang, balance + amount);
                                        p.sendMessage(Utils.chat("&b&lCrystals &7>> gave " + amount + " of gangpoints to " + gang.getName() + "!"));
                                        try{
                                            plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                            plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                        }catch (IOException exception){
                                            p.sendMessage(Utils.chat("&cERROR Could not save to file!"));
                                        }
                                    } else {
                                        CurrencyData.gangPoints.put(gang, amount);
                                        p.sendMessage(Utils.chat("&b&lCrystals &7>> gave " + amount + " of gangpoints to " + gang.getName() + "!"));
                                        try{
                                            plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                            plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                        }catch (IOException exception){
                                            p.sendMessage(Utils.chat("&cERROR Could not save to file!"));
                                        }
                                    }
                                }
                            }
                            if (checker) {
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &cGang not found!"));
                            }
                        } catch (NumberFormatException e) {
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else{
                    p.sendMessage(Utils.chat("&b------GangPoints-----"));
                    p.sendMessage(Utils.chat("&e/gangpoints help"));
                    p.sendMessage(Utils.chat("&e/gangpoints <set|give|remove> <name> <amount>"));
                    p.sendMessage(Utils.chat("&e/gangpoints <name>"));
                }
            }else{
                p.sendMessage(Utils.chat("&b------GangPoints-----"));
                p.sendMessage(Utils.chat("&e/gangpoints help"));
                p.sendMessage(Utils.chat("&e/gangpoints <set|give|remove> <name> <amount>"));
                p.sendMessage(Utils.chat("&e/gangpoints <name>"));
            }
        }else{
            if(args.length == 0){
                s.sendMessage(Utils.chat("&b------GangPoints-----"));
                s.sendMessage(Utils.chat("&e/gangpoints help"));
                s.sendMessage(Utils.chat("&e/gangpoints <set|give|remove> <name>"));
                s.sendMessage(Utils.chat("&e/gangpoints <name>"));
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("help")){
                    s.sendMessage(Utils.chat("&b------GangPoints-----"));
                    s.sendMessage(Utils.chat("&e/gangpoints help"));
                    s.sendMessage(Utils.chat("&e/gangpoints <set|give|remove> <name> <amount>"));
                    s.sendMessage(Utils.chat("&e/gangpoints <name>"));
                }else{
                    boolean checker = true;
                    for(Gang gang : GangData.gangs){
                        if(gang.getName().equalsIgnoreCase(args[0])){
                            checker = false;
                            if(CurrencyData.gangPoints.containsKey(gang)){
                                s.sendMessage(Utils.chat("&b&lGangs &7>> &a"+gang.getName()+" has " + CurrencyData.gangPoints.get(gang) + " GangPoints!"));
                            }else{
                                s.sendMessage(Utils.chat("&b&lGangs &7>> &a"+gang.getName()+" has 0 GangPoints!"));
                            }
                        }
                    }
                    if(checker){
                        s.sendMessage(Utils.chat("&b&lGangs &7>> &cGang not found!"));
                    }
                }
            }else if(args.length == 3){
                if(args[0].equalsIgnoreCase("set")){
                    if(s.hasPermission("exoprison.admin")){
                        try{
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for(Gang gang : GangData.gangs){
                                if(gang.getName().equalsIgnoreCase(args[1])){
                                    checker = false;
                                    CurrencyData.gangPoints.put(gang, amount);
                                    try{
                                        plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                        plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                    }catch (IOException exception){
                                        s.sendMessage(Utils.chat("&cERROR Could not save to file!"));
                                    }
                                }
                            }
                            if(checker){
                                s.sendMessage(Utils.chat("&b&lGangs &7>> &cGang not found!"));
                            }
                        }catch (NumberFormatException e){
                            s.sendMessage(Utils.chat("&b&lGangs &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("remove")){
                    if(s.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (Gang gang : GangData.gangs) {
                                if (gang.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    if (CurrencyData.gangPoints.containsKey(gang)) {
                                        int balance = CurrencyData.gangPoints.get(gang);
                                        if (balance < amount) {
                                            s.sendMessage(Utils.chat("&b&lGangs &7>> &cBalance can not be negative!"));
                                        } else {
                                            CurrencyData.gangPoints.put(gang, balance - amount);
                                            try{
                                                plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                                plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                            }catch (IOException exception){
                                                s.sendMessage(Utils.chat("&cERROR Could not save to file!"));
                                            }
                                        }
                                    } else {
                                        s.sendMessage(Utils.chat("&b&lGangs &7>> &cBalance can not be negative!"));
                                    }
                                }
                            }
                            if (checker) {
                                s.sendMessage(Utils.chat("&b&lGangs &7>> &cGang not found!"));
                            }
                        } catch (NumberFormatException e) {
                            s.sendMessage(Utils.chat("&b&lGangs &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("give")){
                    if(s.hasPermission("exoprison.admin")) {
                        try {
                            int amount = Integer.parseInt(args[2]);
                            boolean checker = true;
                            for (Gang gang : GangData.gangs) {
                                if (gang.getName().equalsIgnoreCase(args[1])) {
                                    checker = false;
                                    if (CurrencyData.gangPoints.containsKey(gang)) {
                                        int balance = CurrencyData.gangPoints.get(gang);
                                        CurrencyData.gangPoints.put(gang, balance + amount);
                                        try{
                                            plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                            plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                        }catch (IOException exception){
                                            s.sendMessage(Utils.chat("&cERROR Could not save to file!"));
                                        }
                                    } else {
                                        CurrencyData.gangPoints.put(gang, amount);
                                        try{
                                            plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                            plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                        }catch (IOException exception){
                                            s.sendMessage(Utils.chat("&cERROR Could not save to file!"));
                                        }
                                    }
                                }
                            }
                            if (checker) {
                                s.sendMessage(Utils.chat("&b&lGangs &7>> &cGang not found!"));
                            }
                        } catch (NumberFormatException e) {
                            s.sendMessage(Utils.chat("&b&lGangs &7>> &cAmount needs to be a number!"));
                        }
                    }
                }else{
                    s.sendMessage(Utils.chat("&b------GangPoints-----"));
                    s.sendMessage(Utils.chat("&e/gangpoints help"));
                    s.sendMessage(Utils.chat("&e/gangpoints <set|give|remove> <name> <amount>"));
                    s.sendMessage(Utils.chat("&e/gangpoints <name>"));
                }
            }else{
                s.sendMessage(Utils.chat("&b------GangPoints-----"));
                s.sendMessage(Utils.chat("&e/gangpoints help"));
                s.sendMessage(Utils.chat("&e/gangpoints <set|give|remove> <name> <amount>"));
                s.sendMessage(Utils.chat("&e/gangpoints <name>"));
            }
        }
        return false;
    }

}
