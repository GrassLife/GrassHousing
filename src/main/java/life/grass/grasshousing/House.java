package life.grass.grasshousing;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class House {
    private UUID playerUUID;
    private Location centralLocation;

    public House(UUID playerUUID, Location location){
        this.playerUUID = playerUUID;
        this.centralLocation = location;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Location getCentralLocation() {
        return centralLocation;
    }

    public String getJsonString() {
        return"{" +
                "\"playerUUID\": \"" + playerUUID.toString() + "\"," +
                "\"worldName\": \"" + centralLocation.getWorld().toString() + "\"," +
                "\"x\": " + String.valueOf(centralLocation.getX()) + "," +
                "\"y\": " + String.valueOf(centralLocation.getY()) + "," +
                "\"z\": " + String.valueOf(centralLocation.getZ()) +
                "}";
    }

    public String fetchPlayerName() {
        return Bukkit.getPlayer(playerUUID).getName();
    }
}