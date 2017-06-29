package life.grass.grasshousing;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChestLockGUI implements InventoryHolder {

    private Inventory inventory;

    public ChestLockGUI(Chest chest) {
        Gson gson = new Gson();
        Map<String, String> chestPlayerDataMap = gson.fromJson(chest.getCustomName(), new TypeToken<Map<String, String>>() {
        }.getType());


        this.inventory = Bukkit.createInventory(this, 54, "WhiteListSetting");

        for (int i = 0; i <= 5; i++) {
            this.inventory.setItem((i * 9) + 4, new ItemStack(Material.THIN_GLASS));
        }
        this.inventory.setItem(0, new ItemStack(Material.STAINED_CLAY, 1, (short) 5));
        this.inventory.setItem(5, new ItemStack(Material.STAINED_CLAY, 1, (short) 1));

        System.out.println(chestPlayerDataMap.toString());
        Map<String, String> allowedPlayer = (Map<String, String>) chestPlayerDataMap.get("allowedPlayer");

        allowedPlayer.forEach((name, uuid) -> {
            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta playerSkullMeta = (SkullMeta) playerHead.getItemMeta();
            playerSkullMeta.setOwner(name);
            playerSkullMeta.setDisplayName(name);
            playerHead.setItemMeta(playerSkullMeta);
            this.inventory.setItem(2, playerHead);
        });

        List<Player> nearbyPlayers = getNearbyPlayers(chest.getLocation(), 10);
        nearbyPlayers.forEach(p -> {
            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta playerSkullMeta = (SkullMeta) playerHead.getItemMeta();
            playerSkullMeta.setOwner(p.getName());
            playerSkullMeta.setDisplayName(p.getName());
            playerHead.setItemMeta(playerSkullMeta);
            this.inventory.setItem(6, playerHead);
        });

    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Player> getNearbyPlayers(Location loc, int distance) {
        double squaredDistance = Math.pow(distance, 2);
        List<Player> list = new ArrayList<Player>();
        for(Player p: Bukkit.getOnlinePlayers())
            if(calculateSquaredDistanceBetween(loc, p.getLocation()) < squaredDistance) list.add(p);
        return list;
    }

    public static double calculateSquaredDistanceBetween(Location loc1, Location loc2) {
        double distance;

        return distance = Math.pow(loc1.getX() - loc2.getX(), 2)
                + Math.pow(loc1.getY() - loc2.getY(), 2)
                + Math.pow(loc1.getZ() - loc2.getZ(), 2);

    }

}
