package life.grass.grasshousing;

import com.google.gson.Gson;
import life.grass.grassblock.GrassBlock;
import life.grass.grasshousing.chest.GrassChest;
import life.grass.grasshousing.chest.ChestRelay;
import life.grass.grasshousing.house.House;
import life.grass.grasshousing.house.HouseRelay;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class HousingManager {

    public static House createHouse(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, HouseRelay.class).makeHouse();
    }

    public static GrassChest createChest(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, ChestRelay.class).makeChest();
    }

    public static void registerChest(GrassChest chest) {
        Block block = chest.getLocation().getBlock();
        GrassBlock.getBlockManager().registerBlockInfo(block).setJson(chest.getJsonString());

        if (block.getLocation().add(1, 0, 0).getBlock().getType() == Material.CHEST) {
            GrassChest nearbyChest = new GrassChest(chest.getOwnerUUID(), block.getLocation().add(1, 0, 0));
            GrassBlock.getBlockManager().registerBlockInfo(nearbyChest.getLocation().getBlock()).setJson(nearbyChest.getJsonString());
        } else if (block.getLocation().add(-1, 0, 0).getBlock().getType() == Material.CHEST) {
            GrassChest nearbyChest = new GrassChest(chest.getOwnerUUID(), block.getLocation().add(-1, 0, 0));
            GrassBlock.getBlockManager().registerBlockInfo(nearbyChest.getLocation().getBlock()).setJson(nearbyChest.getJsonString());
        } else if (block.getLocation().add(0, 0, 1).getBlock().getType() == Material.CHEST) {
            GrassChest nearbyChest = new GrassChest(chest.getOwnerUUID(), block.getLocation().add(0, 0, 1));
            GrassBlock.getBlockManager().registerBlockInfo(nearbyChest.getLocation().getBlock()).setJson(nearbyChest.getJsonString());
        } else if (block.getLocation().add(0, 0, -1).getBlock().getType() == Material.CHEST) {
            GrassChest nearbyChest = new GrassChest(chest.getOwnerUUID(), block.getLocation().add(0, 0, -1));
            GrassBlock.getBlockManager().registerBlockInfo(nearbyChest.getLocation().getBlock()).setJson(nearbyChest.getJsonString());
        }
    }
}
