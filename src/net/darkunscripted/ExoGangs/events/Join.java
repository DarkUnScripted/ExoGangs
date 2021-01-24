package net.darkunscripted.ExoGangs.events;

import net.darkunscripted.ExoGangs.Main;
import net.darkunscripted.ExoGangs.data.CurrencyData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(plugin.getManager().playerscfg.contains("players." + p.getUniqueId() + ".crystals")){
            CurrencyData.crystals.put(p, plugin.getManager().playerscfg.getInt("players." + p.getUniqueId() + ".crystals"));
        }
    }

}
