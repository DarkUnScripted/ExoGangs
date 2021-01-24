package net.darkunscripted.ExoGangs.data;

import net.darkunscripted.ExoGangs.managers.Gang;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CurrencyData {

    public static HashMap<Player, Integer> crystals = new HashMap<Player, Integer>();
    public static HashMap<Player, Integer> points = new HashMap<Player, Integer>();
    public static HashMap<Gang, Integer> gangPoints = new HashMap<Gang, Integer>();
    public static HashMap<Player, Integer> kills = new HashMap<Player, Integer>();

}
