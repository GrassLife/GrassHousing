package life.grass.grasshousing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ChestLockManager {

    public static boolean isDoubleChest(InventoryHolder inventoryHolder) {
        if (inventoryHolder instanceof ShulkerBox) return false;
        return ((Chest) inventoryHolder).getInventory().getHolder() instanceof DoubleChest;
    }

    public static boolean isChestLocked(Nameable nameable) {
        return !StringUtils.isEmpty(nameable.getCustomName());
    }

    public static boolean isAnotherPartLocked(Chest chest) {
        DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
        return isChestLocked(((Chest) doubleChest.getLeftSide())) || isChestLocked(((Chest) doubleChest.getRightSide()));
    }



    public static boolean isClickedChest(PlayerInteractEvent event) {
        return event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && (event.getClickedBlock().getType().equals(Material.CHEST)
                || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)
                || event.getClickedBlock().getType().toString().contains("SHULKER_BOX"));
    }

    public static JsonObject allowedPlayerJson(String name, String uuid) {
        JsonObject allowedJson = new JsonObject();
        allowedJson.addProperty("name", name);
        allowedJson.addProperty("UUID", uuid);

        return allowedJson;
    }

    public static boolean isAllowedPart(int i) {
        return i > 0 && i <= 17;
    }

    public static boolean isNotAllowedPart(int i) {
        return i > 27 && i <= 44;
    }

    public static boolean isAllowed(String name, JsonArray allowedArray) {

        boolean isAllowed = false;

        for(int i = 0 ; i < allowedArray.size() ; i++) {

            JsonObject jsonObject = allowedArray.get(i).getAsJsonObject();

            if(name.equals(jsonObject.get("name").getAsString())) {
                isAllowed = true;
                break;
            }
        }
        return isAllowed;
    }
    public static void setBorder(int row, Inventory inventory) {
        for (int i = row * 6; i < (row * 6) + 9; i++) {
            inventory.setItem(i, new ItemStack(Material.THIN_GLASS, 1, (short) 15));
        }
    }

    public static void setDoubleChestName(DoubleChest doubleChest, String name) {

        ((Chest) doubleChest.getRightSide()).setCustomName(name);
        ((Chest) doubleChest.getLeftSide()).setCustomName(name);

    }
}
