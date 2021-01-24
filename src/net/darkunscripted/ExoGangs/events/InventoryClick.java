package net.darkunscripted.ExoGangs.events;

import net.darkunscripted.ExoGangs.Main;
import net.darkunscripted.ExoGangs.data.CurrencyData;
import net.darkunscripted.ExoGangs.data.GangData;
import net.darkunscripted.ExoGangs.managers.Gang;
import net.darkunscripted.ExoGangs.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Currency;

public class InventoryClick implements Listener {

    Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getClickedInventory().getName().equals(Utils.chat("&b&lCrystal Shop"))){
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            if(e.getSlot() == 4){
                if(e.getClick().isLeftClick() || e.getClick().isRightClick()){
                    if(CurrencyData.crystals.containsKey(p)){
                        if(CurrencyData.crystals.get(p) >= plugin.getConfig().getInt("settings.crystal-for-gangpoints")){
                            if(GangData.playerGang.containsKey(p.getUniqueId())){
                                CurrencyData.crystals.put(p, CurrencyData.crystals.get(p) - plugin.getConfig().getInt("settings.crystal-for-gangpoints"));
                                Gang gang = GangData.playerGang.get(p.getUniqueId());
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    CurrencyData.gangPoints.put(gang, CurrencyData.gangPoints.get(gang) + 1);
                                    plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                }else{
                                    CurrencyData.gangPoints.put(gang, 1);
                                    plugin.getManager().gangscfg.set("gangs." + gang.getName() + ".gangpoints", CurrencyData.gangPoints.get(gang));
                                }
                            }else{
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &cYou are not in a gang!"));
                            }
                        }
                    }else{
                        p.closeInventory();
                        p.sendMessage(Utils.chat("&b&lCrystals &7>> &cYou dont have any crystals!"));
                    }
                }else if(e.getClick().isShiftClick()){
                    if(CurrencyData.crystals.containsKey(p)){
                        if(CurrencyData.crystals.get(p) >= plugin.getConfig().getInt("settings.crystal-for-gangpoints") * 10){
                            if(GangData.playerGang.containsKey(p.getUniqueId())){
                                CurrencyData.crystals.put(p, CurrencyData.crystals.get(p) - plugin.getConfig().getInt("settings.crystal-for-gangpoints"));
                                Gang gang = GangData.playerGang.get(p.getUniqueId());
                                if(CurrencyData.gangPoints.containsKey(gang)){
                                    CurrencyData.gangPoints.put(gang, CurrencyData.gangPoints.get(gang) + 10);
                                }else{
                                    CurrencyData.gangPoints.put(gang, 10);
                                }
                            }else{
                                p.closeInventory();
                                p.sendMessage(Utils.chat("&b&lGangs &7>> &cYou are not in a gang!"));
                            }
                        }else{
                            p.closeInventory();
                            p.sendMessage(Utils.chat("&b&lCrystals &7>> &cYou dont have enough crystals!"));
                        }
                    }else{
                        p.closeInventory();
                        p.sendMessage(Utils.chat("&b&lCrystals &7>> &cYou dont have any crystals!"));
                    }
                }
            }
        }else if(e.getClickedInventory().getName().equals(Utils.chat("&b&lGang top"))){
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            if(e.getSlot() == 31){
                p.closeInventory();
            }
        }
    }
}
