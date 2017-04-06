package life.grass.grasshousing;

import com.google.gson.JsonObject;
import org.bukkit.Location;

public class House {
    private String playerName;
    private Location centralLocation;

    public House(String playerName, Location location){
        this.playerName = playerName;
        this.centralLocation = location;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Location getCentralLocation() {
        return centralLocation;
    }

    public String getJsonString() {
        return"{" +
                "\"playerName\": \"" + playerName + "\"," +
                "\"worldName\": \"" + centralLocation.getWorld().toString() + "\"," +
                "\"x\": " + String.valueOf(centralLocation.getX()) + "," +
                "\"y\": " + String.valueOf(centralLocation.getY()) + "," +
                "\"z\": " + String.valueOf(centralLocation.getZ()) +
                "}";
    }
}