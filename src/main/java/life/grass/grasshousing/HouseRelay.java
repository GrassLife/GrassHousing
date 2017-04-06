package life.grass.grasshousing;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class HouseRelay {
    private String playerName;
    private String worldName;
    private double x;
    private double y;
    private double z;

    public HouseRelay( String playerName, String worldName, double x, double y, double z ) {
        this.playerName = playerName;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public House makeHouse() {
        Location location = new Location(Bukkit.getWorld(this.worldName), this.x, this.y, this.z);
        return new House(this.playerName, location);
    }
}
