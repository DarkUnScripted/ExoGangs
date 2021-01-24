package net.darkunscripted.ExoGangs.data;

import net.darkunscripted.ExoGangs.managers.Gang;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GangData {

    public static ArrayList<Gang> gangs = new ArrayList<Gang>();
    public static HashMap<Player, ArrayList<Gang>> gangInvites = new HashMap<Player, ArrayList<Gang>>();
    public static HashMap<UUID, Gang> playerGang = new HashMap<UUID, Gang>();

}
