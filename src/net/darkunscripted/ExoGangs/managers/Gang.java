package net.darkunscripted.ExoGangs.managers;

import net.darkunscripted.ExoGangs.data.GangData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Gang {

    private String name;
    private String tag;
    private ArrayList<UUID> Members;
    private UUID leader;

    public Gang(){
        GangData.gangs.add(this);
    }

    public Gang(String name, String tag, UUID player){
        this.name = name;
        this.tag = tag;
        this.leader = player;
        ArrayList<UUID> members = new ArrayList<UUID>();
        members.add(player);
        this.setMembers(members);
        GangData.gangs.add(this);
    }

    public Gang(String name, UUID player){
        this.name = name;
        this.tag = name.substring(0, 2);
        this.leader = player;
        ArrayList<UUID> members = new ArrayList<UUID>();
        members.add(player);
        this.setMembers(members);
        GangData.gangs.add(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setMembers(ArrayList<UUID> members) {
        Members = members;
    }

    public void addMember(Player player){
        Members.add(player.getUniqueId());
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public ArrayList<UUID> getMembers() {
        return Members;
    }

    public UUID getLeader(){
        return leader;
    }
}
