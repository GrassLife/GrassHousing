package life.grass.grasshousing;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestLockManager {

    private static final String LOCK_COMPLETE_MESSAGE = "チェストがロックされました. 所有者: ";
    private static final String UNLOCK_COMPLETE_MESSAGE = "チェストがアンロックされました.";

    public static void registerChest(Player owner, Chest chest) {

        if (isDoubleChest(chest)) {

            setDoubleChestName((DoubleChest) chest.getInventory().getHolder(), chestLockJson(owner));
            owner.sendTitle("", LOCK_COMPLETE_MESSAGE + owner.getName(), 10, 70, 20);

        } else {

            chest.setCustomName(chestLockJson(owner));
            owner.sendTitle("", LOCK_COMPLETE_MESSAGE + owner.getName(), 10, 70, 20);

        }

    }

    public static void unregisterChest(Player owner, Chest chest) {

        if (isDoubleChest(chest)) {

            setDoubleChestName((DoubleChest) chest.getInventory().getHolder(), "");
            owner.sendTitle("", UNLOCK_COMPLETE_MESSAGE, 10, 70, 20);

        } else {

            chest.setCustomName("");
            owner.sendTitle("", UNLOCK_COMPLETE_MESSAGE, 10, 70, 20);

        }

    }

    public static String chestLockJson(Player owner) {
        
        JsonObject json = new JsonObject();
        json.addProperty("ownerUUID", owner.getUniqueId().toString());
        json.addProperty("ownerName", owner.getName());
        return new Gson().toJson(json);
        
    }

    public static boolean isDoubleChest(Chest chest) {
        return chest.getInventory().getHolder() instanceof DoubleChest;
    }

    public static boolean isChestLocked(Chest chest) {
        return !StringUtils.isEmpty(chest.getCustomName());
    }

    public static boolean isAnotherPartLocked(Chest chest) {
        DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
        return isChestLocked(((Chest) doubleChest.getLeftSide())) || isChestLocked(((Chest) doubleChest.getRightSide()));
    }

    private static void setDoubleChestName(DoubleChest doubleChest, String name) {

        ((Chest) doubleChest.getRightSide()).setCustomName(name);
        ((Chest) doubleChest.getLeftSide()).setCustomName(name);

    }

    public static boolean isClickedChest(PlayerInteractEvent event) {
        return event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.CHEST);
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
}
