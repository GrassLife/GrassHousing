package life.grass.grasshousing.event;

import com.google.gson.Gson;
import life.grass.grassblock.GrassBlock;
import life.grass.grasshousing.House;
import life.grass.grasshousing.HousingManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class HousingEvent implements Listener {
    @EventHandler
    public void onHousing(BlockPlaceEvent event){
        if (event.getBlockPlaced().getType() == Material.COAL_ORE){
            House house = new House(event.getPlayer().getUniqueId(), event.getBlock().getLocation());
            /*
            デバッグ用
             */
            Block block = event.getBlock();
            event.getPlayer().sendMessage("This block is set as center of your house?? Welcome home!! Owner: " + event.getPlayer().getName());
            GrassBlock.getBlockManager().registerBlockInfo(block).setJson(house.getJsonString());
        }
    }

    @EventHandler
    public void onDestroyingHouse(BlockBreakEvent event) {

        Location blockLocation = event.getBlock().getLocation();

        if (event.getBlock().getType() == Material.COAL_ORE) {
            House house = HousingManager.createHouse(GrassBlock.getBlockManager()
                    .getBlockInfo(
                    blockLocation.getBlockX(),
                    blockLocation.getBlockY(),
                    blockLocation.getBlockZ(),
                    blockLocation.getWorld())
                            .getJson());
            /*
            デバッグ用
             */
            event.getPlayer().sendMessage("This House has been destroyed !! PITY!! Owner: " + house.fetchPlayerName());
            /*
            ここまで
             */
            GrassBlock.getBlockManager().unregisterBlockInfo(event.getBlock());

        }
    }

}
