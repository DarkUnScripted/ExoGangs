package net.darkunscripted.ExoGangs.commands;

import net.darkunscripted.ExoGangs.Main;
import net.darkunscripted.ExoGangs.data.CurrencyData;
import net.darkunscripted.ExoGangs.data.GangData;
import net.darkunscripted.ExoGangs.managers.Gang;
import net.darkunscripted.ExoGangs.utils.Utils;
import net.minecraft.server.v1_13_R2.CommandExecute;
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
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.UUID;

public class GangCommand implements CommandExecutor, Listener {

    private Main plugin = Main.getPlugin(Main.class);

    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(s instanceof Player){
            Player p = (Player) s;
            if(args.length == 0){
                p.sendMessage(Utils.chat("&b&lGangs &7>> &eThis plugin is made by DarkUnScripted"));
                p.sendMessage(Utils.chat("&eDiscord: DarkUnScripted#1001"));
                p.sendMessage(Utils.chat("&eFor Help do: /gang help"));
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("help")){
                    p.sendMessage(Utils.chat("&b------Gangs------"));
                    p.sendMessage(Utils.chat("&e/gang help"));
                    p.sendMessage(Utils.chat("&e/gang top"));
                    p.sendMessage(Utils.chat("&e/gang info"));
                    p.sendMessage(Utils.chat("&e/gang create <name>"));
                    p.sendMessage(Utils.chat("&e/gang kick <name>"));
                    p.sendMessage(Utils.chat("&e/gang disband"));
                    p.sendMessage(Utils.chat("&e/gang join <name|playername>"));
                    p.sendMessage(Utils.chat("&e/gang leave"));
                }else if(args[0].equalsIgnoreCase("leave")){
                    if(GangData.playerGang.containsKey(p.getUniqueId())){
                        Gang g = GangData.playerGang.get(p.getUniqueId());
                        if(g.getLeader() != p.getUniqueId()) {
                            ArrayList<UUID> members = g.getMembers();
                            members.remove(p.getUniqueId());
                            g.setMembers(members);
                            GangData.playerGang.remove(p.getUniqueId());
                            for (UUID member : g.getMembers()) {
                                if(plugin.getServer().getOfflinePlayer(member).isOnline()) {
                                    plugin.getServer().getPlayer(member).sendMessage(Utils.chat("&b&lGangs &7>> &c" + p.getName() + " has left the gang!"));
                                }
                            }
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &aLeft the gang: " + g.getName()));
                        }else{
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cThe leader can only disband his gang!"));
                        }
                    }else{
                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cYou are not in a gang!"));
                    }
                }else if(args[0].equalsIgnoreCase("disband")){
                    if(GangData.playerGang.containsKey(p.getUniqueId())){
                        Gang g = GangData.playerGang.get(p.getUniqueId());
                        if(g.getLeader().equals(p.getUniqueId())){
                            ArrayList<UUID> members = g.getMembers();
                            for(UUID member : members){
                                GangData.playerGang.remove(p.getUniqueId(), g);
                                if(plugin.getServer().getOfflinePlayer(member).isOnline()) {
                                    plugin.getServer().getPlayer(member).sendMessage(Utils.chat("&b&lGangs &7>> &cThe gang you were in was disbanded"));
                                }
                            }
                            members.clear();
                            g.setMembers(members);
                            GangData.gangs.remove(g);
                            plugin.getManager().gangscfg.set("gangs." + g.getName(), null);
                            try{
                                plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                            }catch (IOException e){
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &c[ERROR] Could not remove from file!"));
                            }
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &aGang disbanded!"));
                        }else{
                            p.sendMessage(g.getLeader().toString());
                            p.sendMessage(p.getUniqueId().toString());
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cOnly the leader can disband the gang!"));
                        }
                    }else{
                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cYou are not in a gang!"));
                    }
                }else if(args[0].equalsIgnoreCase("info")){
                    if(GangData.playerGang.containsKey(p.getUniqueId())){
                        Gang gang = GangData.playerGang.get(p.getUniqueId());
                        p.sendMessage(Utils.chat("&b------Gang------"));
                        p.sendMessage(Utils.chat("&eName: &b" + gang.getName()));
                        p.sendMessage(Utils.chat("&eLeader: &b" + plugin.getServer().getOfflinePlayer(gang.getLeader()).getName()));
                        if(CurrencyData.gangPoints.containsKey(gang)){
                            p.sendMessage(Utils.chat("&eGangPoints: &b" + CurrencyData.gangPoints.get(gang)));
                        }else {
                            p.sendMessage(Utils.chat("&eGangPoints: &b0"));
                        }
                        p.sendMessage(Utils.chat("&eMembers:"));
                        for(UUID member : gang.getMembers()){
                            p.sendMessage(Utils.chat("&b- " + plugin.getServer().getOfflinePlayer(member).getName()));
                        }
                    }else{
                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cYou are not in a gang!"));
                    }
                }else if(args[0].equalsIgnoreCase("top")) {
                    Gang top1 = null, top2 = null, top3 = null, top4 = null, top5 = null, top6 = null, top7 = null, top8 = null, top9 = null;
                    ArrayList<Gang> gangtop = GangData.gangs;
                    for(Gang gang : gangtop){
                        if(top1 == null){
                            top1 = gang;
                        }else{
                            if(CurrencyData.gangPoints.containsKey(top1)){
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    if(CurrencyData.gangPoints.get(gang) > CurrencyData.gangPoints.get(top1)){
                                        top1 = gang;
                                    }
                                }
                            }else{
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    top1 = gang;
                                }
                            }
                        }
                    }
                    gangtop.remove(top1);
                    for(Gang gang : gangtop){
                        if(top2 == null){
                            top2 = gang;
                        }else{
                            if(CurrencyData.gangPoints.containsKey(top2)){
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    if(CurrencyData.gangPoints.get(gang) > CurrencyData.gangPoints.get(top2)){
                                        top2 = gang;
                                    }
                                }
                            }else{
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    top2 = gang;
                                }
                            }
                        }
                    }
                    gangtop.remove(top2);
                    for(Gang gang : gangtop){
                        if(top3 == null){
                            top3 = gang;
                        }else{
                            if(CurrencyData.gangPoints.containsKey(top3)){
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    if(CurrencyData.gangPoints.get(gang) > CurrencyData.gangPoints.get(top3)){
                                        top3 = gang;
                                    }
                                }
                            }else{
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    top3 = gang;
                                }
                            }
                        }
                    }
                    gangtop.remove(top3);
                    for(Gang gang : gangtop){
                        if(top4 == null){
                            top4 = gang;
                        }else{
                            if(CurrencyData.gangPoints.containsKey(top4)){
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    if(CurrencyData.gangPoints.get(gang) > CurrencyData.gangPoints.get(top4)){
                                        top4 = gang;
                                    }
                                }
                            }else{
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    top4 = gang;
                                }
                            }
                        }
                    }
                    gangtop.remove(top4);
                    for(Gang gang : gangtop){
                        if(top5 == null){
                            top5 = gang;
                        }else{
                            if(CurrencyData.gangPoints.containsKey(top5)){
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    if(CurrencyData.gangPoints.get(gang) > CurrencyData.gangPoints.get(top5)){
                                        top5 = gang;
                                    }
                                }
                            }else{
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    top5 = gang;
                                }
                            }
                        }
                    }
                    gangtop.remove(top5);
                    for(Gang gang : gangtop){
                        if(top6 == null){
                            top6 = gang;
                        }else{
                            if(CurrencyData.gangPoints.containsKey(top6)){
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    if(CurrencyData.gangPoints.get(gang) > CurrencyData.gangPoints.get(top6)){
                                        top6 = gang;
                                    }
                                }
                            }else{
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    top6 = gang;
                                }
                            }
                        }
                    }
                    gangtop.remove(top6);
                    for(Gang gang : gangtop){
                        if(top7 == null){
                            top7 = gang;
                        }else{
                            if(CurrencyData.gangPoints.containsKey(top7)){
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    if(CurrencyData.gangPoints.get(gang) > CurrencyData.gangPoints.get(top7)){
                                        top7 = gang;
                                    }
                                }
                            }else{
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    top7 = gang;
                                }
                            }
                        }
                    }
                    gangtop.remove(top7);
                    for(Gang gang : gangtop){
                        if(top8 == null){
                            top8 = gang;
                        }else{
                            if(CurrencyData.gangPoints.containsKey(top8)){
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    if(CurrencyData.gangPoints.get(gang) > CurrencyData.gangPoints.get(top8)){
                                        top8 = gang;
                                    }
                                }
                            }else{
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    top8 = gang;
                                }
                            }
                        }
                    }
                    gangtop.remove(top8);
                    for(Gang gang : gangtop){
                        if(top9 == null){
                            top9 = gang;
                        }else{
                            if(CurrencyData.gangPoints.containsKey(top9)){
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    if(CurrencyData.gangPoints.get(gang) > CurrencyData.gangPoints.get(top9)){
                                        top9 = gang;
                                    }
                                }
                            }else{
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    top9 = gang;
                                }
                            }
                        }
                    }
                    if(top1 != null) {
                        gangtop.add(top1);
                    }
                    if(top2 != null) {
                        gangtop.add(top2);
                    }
                    if(top3 != null) {
                        gangtop.add(top3);
                    }
                    if(top4 != null) {
                        gangtop.add(top4);
                    }
                    if(top5 != null) {
                        gangtop.add(top5);
                    }
                    if(top6 != null) {
                        gangtop.add(top6);
                    }
                    if(top7 != null) {
                        gangtop.add(top7);
                    }
                    if(top8 != null) {
                        gangtop.add(top8);
                    }
                    Inventory GangTop = Bukkit.createInventory(null, 36, Utils.chat("&b&lGang top"));
                    ItemStack gang1 = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemStack gang2 = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemStack gang3 = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemStack gang4 = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemStack gang5 = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemStack gang6 = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemStack gang7 = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemStack gang8 = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemStack gang9 = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemStack closed = new ItemStack(Material.BARRIER, 1);
                    if(top1 != null) {
                        ItemMeta meta1 = gang1.getItemMeta();
                        meta1.setDisplayName(Utils.chat("&6#1 &b&lGang: " + top1.getName()));
                        ArrayList<String> lore1 = new ArrayList<String>();
                        if (CurrencyData.gangPoints.containsKey(top1)) {
                            lore1.add(Utils.chat("&6GangPoints: " + CurrencyData.gangPoints.get(top1)));
                        } else {
                            lore1.add(Utils.chat("&6GangPoints: 0"));
                        }
                        meta1.setLore(lore1);
                        gang1.setItemMeta(meta1);
                        SkullMeta skull1 = (SkullMeta) gang1.getItemMeta();
                        skull1.setOwningPlayer(plugin.getServer().getOfflinePlayer(top1.getLeader()));
                        gang1.setItemMeta(skull1);
                    }
                    if(top2 != null) {
                        ItemMeta meta2 = gang2.getItemMeta();
                        meta2.setDisplayName(Utils.chat("&6#2 &b&lGang: " + top2.getName()));
                        ArrayList<String> lore2 = new ArrayList<String>();
                        if (CurrencyData.gangPoints.containsKey(top2)) {
                            lore2.add(Utils.chat("&6GangPoints: " + CurrencyData.gangPoints.get(top2)));
                        } else {
                            lore2.add(Utils.chat("&6GangPoints: 0"));
                        }
                        meta2.setLore(lore2);
                        gang2.setItemMeta(meta2);
                        SkullMeta skull2 = (SkullMeta) gang2.getItemMeta();
                        skull2.setOwningPlayer(plugin.getServer().getOfflinePlayer(top2.getLeader()));
                        gang2.setItemMeta(skull2);
                    }
                    if(top3 != null) {
                        ItemMeta meta3 = gang3.getItemMeta();
                        meta3.setDisplayName(Utils.chat("&6#3 &b&lGang: " + top3.getName()));
                        ArrayList<String> lore3 = new ArrayList<String>();
                        if (CurrencyData.gangPoints.containsKey(top3)) {
                            lore3.add(Utils.chat("&6GangPoints: " + CurrencyData.gangPoints.get(top3)));
                        } else {
                            lore3.add(Utils.chat("&6GangPoints: 0"));
                        }
                        meta3.setLore(lore3);
                        gang3.setItemMeta(meta3);
                        SkullMeta skull3 = (SkullMeta) gang3.getItemMeta();
                        skull3.setOwningPlayer(plugin.getServer().getOfflinePlayer(top3.getLeader()));
                        gang3.setItemMeta(skull3);
                    }
                    if(top4 != null) {
                        ItemMeta meta4 = gang4.getItemMeta();
                        meta4.setDisplayName(Utils.chat("&6#4 &b&lGang: " + top4.getName()));
                        ArrayList<String> lore4 = new ArrayList<String>();
                        if (CurrencyData.gangPoints.containsKey(top4)) {
                            lore4.add(Utils.chat("&6GangPoints: " + CurrencyData.gangPoints.get(top4)));
                        } else {
                            lore4.add(Utils.chat("&6GangPoints: 0"));
                        }
                        meta4.setLore(lore4);
                        gang4.setItemMeta(meta4);
                        SkullMeta skull4 = (SkullMeta) gang4.getItemMeta();
                        skull4.setOwningPlayer(plugin.getServer().getOfflinePlayer(top4.getLeader()));
                        gang4.setItemMeta(skull4);
                    }
                    if(top5 != null) {
                        ItemMeta meta5 = gang5.getItemMeta();
                        meta5.setDisplayName(Utils.chat("&6#5 &b&lGang: " + top5.getName()));
                        ArrayList<String> lore5 = new ArrayList<String>();
                        if (CurrencyData.gangPoints.containsKey(top5)) {
                            lore5.add(Utils.chat("&6GangPoints: " + CurrencyData.gangPoints.get(top5)));
                        } else {
                            lore5.add(Utils.chat("&6GangPoints: 0"));
                        }
                        meta5.setLore(lore5);
                        gang5.setItemMeta(meta5);
                        SkullMeta skull5 = (SkullMeta) gang5.getItemMeta();
                        skull5.setOwningPlayer(plugin.getServer().getOfflinePlayer(top5.getLeader()));
                        gang5.setItemMeta(skull5);
                    }
                    if(top6 != null) {
                        ItemMeta meta6 = gang6.getItemMeta();
                        meta6.setDisplayName(Utils.chat("&6#6 &b&lGang: " + top6.getName()));
                        ArrayList<String> lore6 = new ArrayList<String>();
                        if (CurrencyData.gangPoints.containsKey(top6)) {
                            lore6.add(Utils.chat("&6GangPoints: " + CurrencyData.gangPoints.get(top6)));
                        } else {
                            lore6.add(Utils.chat("&6GangPoints: 0"));
                        }
                        meta6.setLore(lore6);
                        gang6.setItemMeta(meta6);
                        SkullMeta skull6 = (SkullMeta) gang6.getItemMeta();
                        skull6.setOwningPlayer(plugin.getServer().getOfflinePlayer(top6.getLeader()));
                        gang6.setItemMeta(skull6);
                    }
                    if(top7 != null) {
                        ItemMeta meta7 = gang7.getItemMeta();
                        meta7.setDisplayName(Utils.chat("&6#7 &b&lGang: " + top7.getName()));
                        ArrayList<String> lore7 = new ArrayList<String>();
                        if (CurrencyData.gangPoints.containsKey(top7)) {
                            lore7.add(Utils.chat("&6GangPoints: " + CurrencyData.gangPoints.get(top7)));
                        } else {
                            lore7.add(Utils.chat("&6GangPoints: 0"));
                        }
                        meta7.setLore(lore7);
                        gang7.setItemMeta(meta7);
                        SkullMeta skull7 = (SkullMeta) gang7.getItemMeta();
                        skull7.setOwningPlayer(plugin.getServer().getOfflinePlayer(top7.getLeader()));
                        gang7.setItemMeta(skull7);
                    }
                    if(top8 != null) {
                        ItemMeta meta8 = gang8.getItemMeta();
                        meta8.setDisplayName(Utils.chat("&6#8 &b&lGang: " + top8.getName()));
                        ArrayList<String> lore8 = new ArrayList<String>();
                        if (CurrencyData.gangPoints.containsKey(top8)) {
                            lore8.add(Utils.chat("&6GangPoints: " + CurrencyData.gangPoints.get(top8)));
                        } else {
                            lore8.add(Utils.chat("&6GangPoints: 0"));
                        }
                        meta8.setLore(lore8);
                        gang8.setItemMeta(meta8);
                        SkullMeta skull8 = (SkullMeta) gang8.getItemMeta();
                        skull8.setOwningPlayer(plugin.getServer().getOfflinePlayer(top8.getLeader()));
                        gang8.setItemMeta(skull8);
                    }
                    if(top9 != null) {
                        ItemMeta meta9 = gang9.getItemMeta();
                        meta9.setDisplayName(Utils.chat("&6#9 &b&lGang: " + top9.getName()));
                        ArrayList<String> lore9 = new ArrayList<String>();
                        if (CurrencyData.gangPoints.containsKey(top9)) {
                            lore9.add(Utils.chat("&6GangPoints: " + CurrencyData.gangPoints.get(top9)));
                        } else {
                            lore9.add(Utils.chat("&6GangPoints: 0"));
                        }
                        meta9.setLore(lore9);
                        gang9.setItemMeta(meta9);
                        SkullMeta skull9 = (SkullMeta) gang9.getItemMeta();
                        skull9.setOwningPlayer(plugin.getServer().getOfflinePlayer(top9.getLeader()));
                        gang9.setItemMeta(skull9);
                    }
                    ItemMeta close = closed.getItemMeta();
                    close.setDisplayName(Utils.chat("&cClose"));
                    closed.setItemMeta(close);

                    GangTop.setItem(4, gang1);
                    GangTop.setItem(12, gang2);
                    GangTop.setItem(13, gang3);
                    GangTop.setItem(14, gang4);
                    GangTop.setItem(20, gang5);
                    GangTop.setItem(21, gang6);
                    GangTop.setItem(22, gang7);
                    GangTop.setItem(23, gang8);
                    GangTop.setItem(24, gang9);
                    GangTop.setItem(31, closed);
                    p.openInventory(GangTop);
                }else{
                    p.sendMessage(Utils.chat("&b------Gangs------"));
                    p.sendMessage(Utils.chat("&e/gang help"));
                    p.sendMessage(Utils.chat("&e/gang info"));
                    p.sendMessage(Utils.chat("&e/gang create <name>"));
                    p.sendMessage(Utils.chat("&e/gang kick <name>"));
                    p.sendMessage(Utils.chat("&e/gang disband"));
                    p.sendMessage(Utils.chat("&e/gang join <name|playername>"));
                    p.sendMessage(Utils.chat("&e/gang leave"));
                }
            }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("create")){
                    if(!GangData.playerGang.containsKey(p.getUniqueId())) {
                        boolean checker = true;
                        for (Gang g : GangData.gangs) {
                            if (g.getName().equalsIgnoreCase(args[1])) {
                                checker = false;
                            }
                        }
                        if (checker) {
                            Gang gang = new Gang(args[1], p.getUniqueId());
                            GangData.playerGang.put(p.getUniqueId(), gang);
                            try {
                                ArrayList<String> UUIDList = new ArrayList<String>();
                                ArrayList<String> NameList = new ArrayList<String>();
                                UUIDList.add(p.getUniqueId().toString());
                                NameList.add(p.getName());
                                plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".leaderID", gang.getLeader().toString());
                                plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".leaderName", plugin.getServer().getOfflinePlayer(gang.getLeader()).getName());
                                plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", 0);
                                plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".membersID", UUIDList);
                                plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".membersName", NameList);
                                plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                            }catch (IOException e){

                            }
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &aGang Created!"));
                        } else {
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cA gang with that name already exists!"));
                        }
                    }else{
                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cYou are already in a gang!"));
                    }
                }else if(args[0].equalsIgnoreCase("join")){
                    if(GangData.gangInvites.containsKey(p)){
                        ArrayList<Gang> invites = GangData.gangInvites.get(p);
                        boolean checker = true;
                        for(Gang g : invites){
                            if(g.getName().equalsIgnoreCase(args[1])){
                                if(g.getMembers().size() < 4) {
                                    for (UUID member : g.getMembers()) {
                                        if (plugin.getServer().getOfflinePlayer(member).isOnline()) {
                                            plugin.getServer().getPlayer(member).sendMessage(Utils.chat("&b&lGangs &7>> &a" + p.getName() + " joinec the gang!"));
                                        }
                                    }
                                    g.addMember(p);
                                    GangData.gangInvites.remove(p);
                                    GangData.playerGang.put(p.getUniqueId(), g);
                                    try{
                                        ArrayList<String> UUIDList = new ArrayList<String>();
                                        ArrayList<String> NameList = new ArrayList<String>();
                                        for(UUID member : g.getMembers()){
                                            UUIDList.add(member.toString());
                                            NameList.add(plugin.getServer().getOfflinePlayer(member).getName());
                                        }
                                        plugin.getManager().gangscfg.set("gangs." + g.getName() + ".membersID", UUIDList);
                                        plugin.getManager().gangscfg.set("gangs." + g.getName() + ".membersName", NameList);
                                        plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                    }catch (IOException e){
                                        plugin.getServer().getConsoleSender().sendMessage(Utils.chat("&c[ERROR] Could not save to file!"));
                                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cCould not save to file contact and staffmember!"));
                                    }
                                    p.sendMessage(Utils.chat("&b&lGangs &7>> &aSuccesfully joined gang!"));
                                    checker = false;
                                }else{
                                    GangData.gangInvites.get(p).remove(g);
                                    p.sendMessage(Utils.chat("&b&lGangs &7>> &cThat gang is already full!"));
                                }
                            }
                        }
                        if(checker){
                            boolean check = true;
                            for(Gang g : invites){
                                if(plugin.getServer().getOfflinePlayer(g.getLeader()).getName().equalsIgnoreCase(args[0])){
                                    if(g.getMembers().size() < 4) {
                                        for (UUID member : g.getMembers()) {
                                            if (plugin.getServer().getOfflinePlayer(member).isOnline()) {
                                                plugin.getServer().getPlayer(member).sendMessage(Utils.chat("&b&lGangs &7>> &a" + p.getName() + " joinec the gang!"));
                                            }
                                        }
                                        g.addMember(p);
                                        GangData.gangInvites.remove(p);
                                        GangData.playerGang.put(p.getUniqueId(), g);
                                        try{
                                            ArrayList<String> UUIDList = new ArrayList<String>();
                                            ArrayList<String> NameList = new ArrayList<String>();
                                            for(UUID member : g.getMembers()){
                                                UUIDList.add(member.toString());
                                                NameList.add(plugin.getServer().getOfflinePlayer(member).getName());
                                            }
                                            plugin.getManager().gangscfg.set("gangs." + g.getName() + ".membersID", UUIDList);
                                            plugin.getManager().gangscfg.set("gangs." + g.getName() + ".membersName", NameList);
                                            plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                        }catch (IOException e){
                                            plugin.getServer().getConsoleSender().sendMessage(Utils.chat("&c[ERROR] Could not save to file!"));
                                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cCould not save to file contact and staffmember!"));
                                        }
                                        p.sendMessage(Utils.chat("&b&lGangs &7>> &aSuccesfully joined gang!"));
                                        check = false;
                                    }else{
                                        GangData.gangInvites.get(p).remove(g);
                                        p.sendMessage(Utils.chat("&b&lGangs &7>> That gang is already full!"));
                                    }
                                }
                            }
                            if(check){
                                p.sendMessage(Utils.chat("&cYou dont have an invited from a gang with that name or leadername!"));
                            }
                        }
                    }else{
                        p.sendMessage(Utils.chat("&cYou dont have a pending invite"));
                    }
                }else if(args[0].equalsIgnoreCase("invite")){
                    if(GangData.playerGang.containsKey(p.getUniqueId())){
                        Gang gang = GangData.playerGang.get(p.getUniqueId());
                        if(p == plugin.getServer().getPlayer(gang.getLeader())){
                            if(gang.getMembers().size() < 4) {
                                boolean checker = true;
                                for (OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
                                    if (player.getName().equalsIgnoreCase(args[1])) {
                                        if (player.isOnline()) {
                                            checker = false;
                                            if (GangData.playerGang.containsKey(player.getUniqueId())) {
                                                p.sendMessage(Utils.chat("&b&lGangs &7>> &cThat player is already in a gang!"));
                                            } else {
                                                if (GangData.gangInvites.containsKey(player)) {
                                                    if (GangData.gangInvites.get(player).contains(gang)) {
                                                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cThat member has already an invite!"));
                                                    } else {
                                                        ArrayList<Gang> invites = GangData.gangInvites.get(player);
                                                        invites.add(gang);
                                                        GangData.gangInvites.put((Player) player, invites);
                                                        p.sendMessage(Utils.chat("&b&lGangs &7>> &aPlayer invited!"));
                                                    }
                                                } else {
                                                    ArrayList<Gang> invites = new ArrayList<Gang>();
                                                    invites.add(gang);
                                                    GangData.gangInvites.put((Player) player, invites);
                                                    p.sendMessage(Utils.chat("&b&lGangs &7>> &aPlayer invited!"));
                                                }
                                            }
                                        } else {
                                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cThat player is not online!"));
                                        }
                                    }
                                }
                                if (checker) {
                                    p.sendMessage(Utils.chat("&b&lGangs &7>> &cPlayer not found!"));
                                }
                            }else{
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &cMax gang size is 4!"));
                            }
                        }else{
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cOnly the gangleader can invite!"));
                        }
                    }
                }else if(args[0].equalsIgnoreCase("kick")){
                    if(GangData.playerGang.containsKey(p.getUniqueId())){
                        Gang g = GangData.playerGang.get(p.getUniqueId());
                        if(plugin.getServer().getPlayer(g.getLeader()) == p){
                            boolean checker = true;
                            for(UUID member : g.getMembers()){
                                if(plugin.getServer().getOfflinePlayer(member).getName().equalsIgnoreCase(args[1])){
                                    checker = false;
                                    ArrayList<UUID> members = g.getMembers();
                                    members.remove(member);
                                    g.setMembers(members);
                                    GangData.playerGang.remove(member);
                                    try{
                                        ArrayList<String> UUIDList = new ArrayList<String>();
                                        ArrayList<String> NameList = new ArrayList<String>();
                                        for(UUID gangmember : g.getMembers()){
                                            UUIDList.add(gangmember.toString());
                                            NameList.add(plugin.getServer().getOfflinePlayer(gangmember).getName());
                                        }
                                        plugin.getManager().gangscfg.set("gangs." + g.getName() + ".membersID", UUIDList);
                                        plugin.getManager().gangscfg.set("gangs." + g.getName() + ".membersName", NameList);
                                        plugin.getManager().gangscfg.save(plugin.getManager().gangsfile);
                                    }catch (IOException e){
                                        plugin.getServer().getConsoleSender().sendMessage(Utils.chat("&c[ERROR] Could not save to file!"));
                                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cCould not save to file contact and staffmember!"));
                                    }
                                    if(plugin.getServer().getOfflinePlayer(member).isOnline()) {
                                        p.sendMessage(Utils.chat("&b&lGangs &7>> &a" + plugin.getServer().getOfflinePlayer(member).getName() + " has been kicked!"));
                                    }
                                }
                            }
                            if(checker){
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &cThere is no member with that name!"));
                            }
                        }else{
                            p.sendMessage(Utils.chat("&b&lGangs &7>> &cOnly the gangleader can kick people!"));
                        }
                    }else{
                        p.sendMessage(Utils.chat("&b&lGangs &7>> &cYou are not in a gang!"));
                    }
                }else{
                    p.sendMessage(Utils.chat("&b------Gangs------"));
                    p.sendMessage(Utils.chat("&e/gang help"));
                    p.sendMessage(Utils.chat("&e/gang info"));
                    p.sendMessage(Utils.chat("&e/gang create <name>"));
                    p.sendMessage(Utils.chat("&e/gang kick <name>"));
                    p.sendMessage(Utils.chat("&e/gang disband"));
                    p.sendMessage(Utils.chat("&e/gang join <name|playername>"));
                    p.sendMessage(Utils.chat("&e/gang leave"));
                }
            }else{
                p.sendMessage(Utils.chat("&b------Gangs------"));
                p.sendMessage(Utils.chat("&e/gang help"));
                p.sendMessage(Utils.chat("&e/gang info"));
                p.sendMessage(Utils.chat("&e/gang create <name>"));
                p.sendMessage(Utils.chat("&e/gang kick <name>"));
                p.sendMessage(Utils.chat("&e/gang disband"));
                p.sendMessage(Utils.chat("&e/gang join <name|playername>"));
                p.sendMessage(Utils.chat("&e/gang leave"));
            }
        }
        return false;
    }

}