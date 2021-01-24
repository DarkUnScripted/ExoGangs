package net.darkunscripted.ExoGangs.events;

import net.darkunscripted.ExoGangs.data.CurrencyData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Death {

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player killer = e.getEntity().getKiller();
        if(CurrencyData.kills.containsKey(killer)) {
            CurrencyData.kills.put(killer, CurrencyData.kills.get(killer) + 1);
        }else{
            CurrencyData.kills.put(killer, 1);
        }
    }
}
