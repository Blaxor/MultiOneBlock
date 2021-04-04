package ro.deiutzblaxo.oneblock.island;

import lombok.Getter;
import lombok.Setter;
import ro.deiutzblaxo.oneblock.player.RANK;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class IslandMeta implements Serializable {
    private HashMap<UUID, RANK> members = new HashMap<>();
    private double XSpawn = 0;
    private double YSpawn = 82;
    private double ZSpawn = 0;
    private double XBlock = 0;
    private double YBlock = 81;
    private double ZBlock = 0;
    private int count = 0;
    private int radius = 25;//TODO implementation per rank
    private boolean locked = false;
    
    protected static IslandMeta deserialize(InputStream stream) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(stream);
        try {
            return (IslandMeta) ois.readObject();
        } finally {
            ois.close();
        }
    }

    protected byte[] serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        oos.close();
        return baos.toByteArray();
    }

    @Override
    public String toString() {
        return "IslandMeta{" +
                "members=" + members +
                ", XSpawn=" + XSpawn +
                ", YSpawn=" + YSpawn +
                ", ZSpawn=" + ZSpawn +
                ", XBlock=" + XBlock +
                ", YBlock=" + YBlock +
                ", ZBlock=" + ZBlock +
                ", count=" + count +
                ", radius=" + radius +
                ", locked=" + locked +
                '}';
    }
}
