package net.darkunscripted.ExoGangs.managers;

import net.darkunscripted.ExoGangs.Main;
import net.darkunscripted.ExoGangs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataManager {

    private Main plugin = Main.getPlugin(Main.class);

    //Files & File Configs
    public FileConfiguration playerscfg;
    public File playersfile;
    public FileConfiguration gangscfg;
    public File gangsfile;

    //--------------------

    public void setup(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        playersfile = new File(plugin.getDataFolder(), "players.yml");

        if(!playersfile.exists()){
            try{
                playersfile.createNewFile();
            }catch (IOException e){
                Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&cCould not create players.yml file!"));
            }
        }

        playerscfg = YamlConfiguration.loadConfiguration(playersfile);
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&aplayers.yml file has been created!!"));
    }

    public FileConfiguration getPlayers(){
        return playerscfg;
    }

    public void savePlayers(){
        try{
            playerscfg.save(playersfile);
            Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&aplayers.yml has been saved"));
        }catch (IOException e){
            Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&cCould not save the players.yml file"));
        }
    }

    public void reloadPlayers(){
        playerscfg = YamlConfiguration.loadConfiguration(playersfile);
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&aplayers.yml has been reloaded"));
    }

    public void setupGangs(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        gangsfile = new File(plugin.getDataFolder(), "gangs.yml");

        if(!gangsfile.exists()){
            try{
                gangsfile.createNewFile();
            }catch (IOException e){
                Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&cCould not create gangs.yml file!"));
            }
        }

        gangscfg = YamlConfiguration.loadConfiguration(gangsfile);
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&agangs.yml file has been created!!"));
    }

    public FileConfiguration getGangs(){
        return gangscfg;
    }

    public void saveGangs(){
        try{
            gangscfg.save(gangsfile);
            Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&agangs.yml has been saved"));
        }catch (IOException e){
            Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&cCould not save the gangs.yml file"));
        }
    }

    public void reloadGangs(){
        gangscfg = YamlConfiguration.loadConfiguration(gangsfile);
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&agangs.yml has been reloaded"));
    }

}
