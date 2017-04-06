package life.grass.grasshousing;

import com.google.gson.Gson;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;

public class HousingManager {

    public static House createHouse(String jsonString) {
        Gson gson = new Gson();
        House house = gson.fromJson(jsonString, HouseRelay.class).makeHouse();
        return house;
    }
}
