package life.grass.grasshousing;

import com.google.gson.Gson;
import life.grass.grasshousing.house.House;
import life.grass.grasshousing.house.HouseRelay;

public class HousingManager {

    public static House createHouse(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, HouseRelay.class).makeHouse();
    }
}
