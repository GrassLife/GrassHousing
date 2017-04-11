package life.grass.grasshousing.chest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GrassChest {
    private UUID ownerUUID;
    private Location location;

    public GrassChest(UUID ownerUUID, Location location) {
        this.ownerUUID = ownerUUID;
        this.location = location;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public Location getLocation() {
        return location;
    }

    public String getJsonString() {
        JsonObject json = new JsonObject();
        json.addProperty("ownerUUID", ownerUUID.toString());
        json.addProperty("worldName", location.getWorld().toString() );
        json.addProperty("x", location.getX() );
        json.addProperty("y", location.getY() );
        json.addProperty("z", location.getZ() );
        return new Gson().toJson(json);
    }

    public Player fetchPlayer() {
        return Bukkit.getPlayer(ownerUUID);
    }
}
