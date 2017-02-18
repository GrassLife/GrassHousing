package life.grass.grasshousing.event;

import life.grass.grasshousing.House;
import life.grass.grasshousing.HousingManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;

public class HousingEvent implements Listener {
    @EventHandler
    public void onHousing(BlockPlaceEvent event){
        if (event.getBlockPlaced().getType() == Material.COAL_ORE){
            House house = new House(event.getPlayer(), event.getBlock().getLocation());
            HousingManager.addHouse(house);
            /*
            デバッグ用
             */
            event.getPlayer().sendMessage("This block is set as center of your house!! Welcome home!! Owner: " + event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onDestroyingHouse(BlockBreakEvent event) {

        HashMap<Location, House> houseMap = HousingManager.getHouseMap();
        Location blockLocation = event.getBlock().getLocation();

        if (houseMap.containsKey(blockLocation)) {
            /*
            デバッグ用
             */
            event.getPlayer().sendMessage("This House has been destroyed !! PITY!! Owner: " + houseMap.get(blockLocation).getPlayer().getName());
            /*
            ここまで
             */
            HousingManager.removeHouse(blockLocation);

        }
    }

}
