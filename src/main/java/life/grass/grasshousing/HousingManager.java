package life.grass.grasshousing;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class HousingManager {

    private static HashMap<Location, House> houseMap = new HashMap<>();

    public static void addHouse(House house) {
        houseMap.put(house.getCentralLocation(), house);
    }

    public static void removeHouse(Location location) {
        houseMap.remove(location);
    }

    public static HashMap<Location, House> getHouseMap() {
        return houseMap;
    }
}
