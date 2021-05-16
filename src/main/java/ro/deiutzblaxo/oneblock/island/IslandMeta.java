package ro.deiutzblaxo.oneblock.island;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import ro.deiutzblaxo.oneblock.island.permissions.ISLANDSETTINGS;
import ro.deiutzblaxo.oneblock.island.permissions.PERMISSIONS;
import ro.deiutzblaxo.oneblock.player.RANK;
import ro.deiutzblaxo.oneblock.utils.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class IslandMeta implements Serializable {
    private HashMap<UUID, RANK> members = new HashMap<>();
    private ArrayList<Location> block = new ArrayList<>() {{
        add(new Location(0, 81, 0));
    }};
    private Location spawn = new Location(0, 82, 0);
    private int count = 0;
    private String radiusType = "member";
    private int radiusTire = 1;
    private boolean locked = false;
    private ArrayList<UUID> banned = new ArrayList<>();
    private HashMap<PERMISSIONS, RANK> permissions = new HashMap<>();
    private HashMap<ISLANDSETTINGS, Boolean> settings = new HashMap<>();
    private String name;
    private long time = 0;
    private byte maxMembers = 5;

    public IslandMeta(String ownerName) {
        name = ownerName + "'s island!";//TODO MESSAGE
    }

    public byte getMaxMembers() {
        if (maxMembers < 5)
            return 5;
        else return maxMembers;
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static IslandMeta deserialize(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, IslandMeta.class);

    }

}
