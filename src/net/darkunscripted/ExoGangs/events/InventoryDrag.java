package net.darkunscripted.ExoGangs.events;

import net.darkunscripted.ExoGangs.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDrag implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e){
        if(e.getInventory().getName().equals(Utils.chat("&b&lCrystal Shop")) || e.getInventory().getName().equals(Utils.chat("&b&lGang top"))){
            e.setCancelled(true);
        }
    }
}
