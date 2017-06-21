package life.grass.grasshousing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;

public class ChestLockManager {

    public static void registerChest(Player owner, Chest chest) {

        if (isDoubleChest(chest)) {

            owner.sendMessage("Locking Chest... Owner: " + owner.getName());
            setDoubleChestName((DoubleChest) chest.getInventory().getHolder(), chestLockJson(owner));
            owner.sendMessage("Chest locked successfully.");

        } else {

            owner.sendMessage("Locking Chest... Owner: " + owner.getName());
            chest.setCustomName(chestLockJson(owner));
            owner.sendMessage("Chest locked successfully.");

        }

    }

    public static void unregisterChest(Player owner, Chest chest) {

        if (isDoubleChest(chest)) {

            owner.sendMessage("Unlocking Chest...");
            setDoubleChestName((DoubleChest) chest.getInventory().getHolder(), "");
            owner.sendMessage("Chest unlocked successfully.");

        } else {

            owner.sendMessage("Unlocking Chest...");
            chest.setCustomName("");
            owner.sendMessage("Chest unlocked successfully.");

        }

    }

    public static String chestLockJson(Player owner) {
        
        JsonObject json = new JsonObject();
        json.addProperty("ownerUUID", owner.getUniqueId().toString());
        return new Gson().toJson(json);
        
    }

    private static boolean isDoubleChest(Chest chest) {
        return chest.getInventory().getHolder() instanceof DoubleChest;
    }

    private static void setDoubleChestName(DoubleChest doubleChest, String name) {

        ((Chest) doubleChest.getRightSide()).setCustomName(name);
        ((Chest) doubleChest.getLeftSide()).setCustomName(name);

    }
}
