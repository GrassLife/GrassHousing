package life.grass.grasshousing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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
}
