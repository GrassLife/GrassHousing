package life.grass.grasshousing.chest;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class ChestRelay {
    private String ownerUUID;
    private String worldName;
    private double x;
    private double y;
    private double z;

    public ChestRelay( String ownerUUID, String worldName, double x, double y, double z ) {
        this.ownerUUID = ownerUUID;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GrassChest makeChest() {
        Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
        return new GrassChest(UUID.fromString(ownerUUID), location);
    }
}
