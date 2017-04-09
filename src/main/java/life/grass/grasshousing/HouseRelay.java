package life.grass.grasshousing;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class HouseRelay {
    private String playerUUID;
    private String worldName;
    private double x;
    private double y;
    private double z;

    public HouseRelay( String playerUUID, String worldName, double x, double y, double z ) {
        this.playerUUID = playerUUID;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public House makeHouse() {
        Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
        return new House(UUID.fromString(playerUUID), location);
    }
}
