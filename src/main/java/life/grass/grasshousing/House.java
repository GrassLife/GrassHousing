package life.grass.grasshousing;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class House {
    private Player player;
    private Location centralLocation;

    public House(Player player, Location location){
        this.player = player;
        this.centralLocation = location;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getCentralLocation() {
        return centralLocation;
    }
}