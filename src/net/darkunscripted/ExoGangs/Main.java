package net.darkunscripted.ExoGangs;

import net.darkunscripted.ExoGangs.commands.CrystalCommand;
import net.darkunscripted.ExoGangs.commands.GangCommand;
import net.darkunscripted.ExoGangs.commands.GangPointsCommand;
import net.darkunscripted.ExoGangs.commands.PointsCommand;
import net.darkunscripted.ExoGangs.data.CurrencyData;
import net.darkunscripted.ExoGangs.data.GangData;
import net.darkunscripted.ExoGangs.events.InventoryClick;
import net.darkunscripted.ExoGangs.events.InventoryDrag;
import net.darkunscripted.ExoGangs.events.Join;
import net.darkunscripted.ExoGangs.events.Quit;
import net.darkunscripted.ExoGangs.managers.DataManager;
import net.darkunscripted.ExoGangs.managers.Gang;
import net.darkunscripted.ExoGangs.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Savepoint;
import java.util.*;

public class Main extends JavaPlugin {

    private DataManager cfgm;

    @Override
    public void onEnable() {
        loadConfigManager();
        loadConfig();
        registerCommands();
        registerEvents();
        registerManagers();
        LoadGangs();
    }

    @Override
    public void onDisable() {
        SaveGangs();
    }

    public void registerCommands(){
        getCommand("gang").setExecutor(new GangCommand());
        getCommand("crystal").setExecutor(new CrystalCommand());
        getCommand("points").setExecutor(new PointsCommand());
        getCommand("gangpoints").setExecutor(new GangPointsCommand());
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        getServer().getPluginManager().registerEvents(new InventoryDrag(), this);
        getServer().getPluginManager().registerEvents(new Join(), this);
        getServer().getPluginManager().registerEvents(new Quit(), this);
    }

    public void registerManagers(){

    }

    public void loadConfigManager(){
        cfgm = new DataManager();
        cfgm.setup();
        cfgm.savePlayers();
        cfgm.reloadPlayers();
        cfgm.setupGangs();
        cfgm.saveGangs();
        cfgm.reloadGangs();
    }

    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public DataManager getManager(){
        return cfgm;
    }

    public void LoadGangs(){
        if(cfgm.gangscfg.isConfigurationSection("gangs")) {
            Set gangs = cfgm.gangscfg.getConfigurationSection("gangs").getKeys(false);
            List<String> ganglist = new ArrayList<>(gangs);
            this.getServer().getConsoleSender().sendMessage(String.valueOf(ganglist));
            for (String gangname : ganglist) {
                String leaderUUID = cfgm.gangscfg.getString("gangs." + gangname + ".leaderID");
                UUID luuid = UUID.fromString(leaderUUID);
                OfflinePlayer leader = this.getServer().getOfflinePlayer(luuid);
                Gang gang = new Gang(gangname, luuid);
                GangData.playerGang.put(leader.getUniqueId(), gang);
                List<String> memberList = cfgm.gangscfg.getStringList("gangs." + gangname + ".membersID");
                for (String MemberUUID : memberList) {
                    UUID uuid = UUID.fromString(MemberUUID);
                    OfflinePlayer member = this.getServer().getOfflinePlayer(uuid);
                    if(member != leader) {
                        GangData.playerGang.put(member.getUniqueId(), gang);
                        ArrayList<UUID> members = gang.getMembers();
                        members.add(member.getUniqueId());
                        gang.setMembers(members);
                    }
                }
                int GangPoints = cfgm.gangscfg.getInt("gangs." + gangname + "gangpoints");
                CurrencyData.gangPoints.put(gang, GangPoints);
            }
        }
    }

    public void SaveGangs(){
        try {
            for (Gang gang : GangData.gangs) {
                ArrayList<String> UUIDList = new ArrayList<String>();
                ArrayList<String> NameList = new ArrayList<String>();
                for(UUID p : gang.getMembers()){
                    UUIDList.add(p.toString());
                    NameList.add(this.getServer().getOfflinePlayer(p).getName());
                }
                cfgm.gangscfg.set("gangs." + gang.getName() + ".leaderID", gang.getLeader().toString());
                cfgm.gangscfg.set("gangs." + gang.getName() + ".leaderName", this.getServer().getOfflinePlayer(gang.getLeader()).getName());
                int gangpoints = 0;
                if (CurrencyData.gangPoints.containsKey(gang)) {
                    gangpoints = CurrencyData.gangPoints.get(gang);
                }
                cfgm.gangscfg.set("gangs." + gang.getName() + ".gangpoints", gangpoints);
                cfgm.gangscfg.set("gangs." + gang.getName() + ".membersID", UUIDList);
                cfgm.gangscfg.set("gangs." + gang.getName() + ".membersName", NameList);
            }
            cfgm.gangscfg.save(cfgm.gangsfile);
        }catch (IOException e){
            this.getServer().getConsoleSender().sendMessage(Utils.chat("&c[ERROR] Could not save gangs to file!"));
        }
    }

}
