package life.grass.grasshousing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
        JsonObject json = new JsonObject();
        json.addProperty("playerUUID", playerUUID.toString() );
        json.addProperty("worldName", centralLocation.getWorld().toString() );
        json.addProperty("x", centralLocation.getX() );
        json.addProperty("y", centralLocation.getY() );
        json.addProperty("z", centralLocation.getZ() );

        return new Gson().toJson(json);
    }

    public Player fetchPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }
}