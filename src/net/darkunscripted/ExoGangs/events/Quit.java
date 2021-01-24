package net.darkunscripted.ExoGangs.events;

import net.darkunscripted.ExoGangs.Main;
import net.darkunscripted.ExoGangs.data.CurrencyData;
import net.darkunscripted.ExoGangs.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class Quit implements Listener {

    Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        plugin.getManager().playerscfg.set("players." + p.getUniqueId() + ".name", p.getName());
        if(CurrencyData.crystals.containsKey(p)) {
            try {
                plugin.getManager().playerscfg.set("players." + p.getUniqueId() + ".crystals", CurrencyData.crystals.get(p));
                plugin.getManager().playerscfg.save(plugin.getManager().playersfile);
            }catch (IOException exception){
                plugin.getServer().getConsoleSender().sendMessage(Utils.chat("&c[ERROR] Could not save to file!"));
            }
        }else{
            try {
                plugin.getManager().playerscfg.set("players." + p.getUniqueId() + ".crystals", 0);
                plugin.getManager().playerscfg.save(plugin.getManager().playersfile);
            }catch (IOException exception){
                plugin.getServer().getConsoleSender().sendMessage(Utils.chat("&c[ERROR] Could not save to file!"));
            }
        }
        if(CurrencyData.points.containsKey(p)){
            try{
                plugin.getManager().playerscfg.set("players." + p.getUniqueId() + ".points", CurrencyData.points.get(p));
                plugin.getManager().playerscfg.save(plugin.getManager().playersfile);
            }catch (IOException exception){
                plugin.getServer().getConsoleSender().sendMessage(Utils.chat("&c[ERROR] Could not save to file!"));
            }
        }else{
            try{
                plugin.getManager().playerscfg.set("players." + p.getUniqueId() + ".points", 0);
                plugin.getManager().playerscfg.save(plugin.getManager().playersfile);
            }catch (IOException exception){
                plugin.getServer().getConsoleSender().sendMessage(Utils.chat("&c[ERROR] Could not save to file!"));
            }
        }
    }

}
