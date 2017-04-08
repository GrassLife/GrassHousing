package life.grass.grasshousing.event;

import life.grass.grassblock.GrassBlock;
import life.grass.grasshousing.House;
import life.grass.grasshousing.HousingManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class HousingEvent implements Listener {
    @EventHandler
    public void onHousing(BlockPlaceEvent event){
        if (event.getBlockPlaced().getType() == Material.COAL_ORE){
            House house = new House(event.getPlayer().getUniqueId(), event.getBlock().getLocation());
            Block block = event.getBlock();
            event.getPlayer().sendMessage("This block is set as center of your house?? Welcome home!! Owner: " + event.getPlayer().getName());
            GrassBlock.getBlockManager().registerBlockInfo(block).setJson(house.getJsonString());
        }
    }

    @EventHandler
    public void onDestroyingHouse(BlockBreakEvent event) {

        Location blockLocation = event.getBlock().getLocation();

        if (event.getBlock().getType() == Material.COAL_ORE) { //ブロックの情報の存在を真偽値でとってきたい
            House house = HousingManager.createHouse(GrassBlock.getBlockManager()
                    .getBlockInfo(
                    blockLocation.getBlockX(),
                    blockLocation.getBlockY(),
                    blockLocation.getBlockZ(),
                    blockLocation.getWorld())
                            .getJson());

            event.getPlayer().sendMessage("This House has been destroyed !! PITY!! Owner: " + house.fetchPlayer().getName());
            GrassBlock.getBlockManager().unregisterBlockInfo(event.getBlock());

        }
    }

}
