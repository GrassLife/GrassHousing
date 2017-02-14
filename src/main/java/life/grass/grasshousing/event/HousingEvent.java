package life.grass.grasshousing.event;

import life.grass.grasshousing.House;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class HousingEvent implements Listener {
    @EventHandler
    public void onHousing(BlockPlaceEvent event){
        if (event.getBlockPlaced().getType() == Material.COAL_ORE){
            House house = new House(event.getPlayer().getName(), event.getBlock().getLocation());
            System.out.println(house.getCentralLocation() + house.getPlayerName());
            event.getPlayer().sendMessage("This block is set as center of your house!! Welcome home!! Owner: " + event.getPlayer().getName());
        }
    }

}
