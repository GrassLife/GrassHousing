package life.grass.grasshousing.container;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import life.grass.grasshousing.ChestLockManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Nameable;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public class Container {

    private InventoryHolder inventoryHolder;
    private Nameable nameable;
    private static final String LOCK_COMPLETE_MESSAGE = "チェストがロックされました. 所有者: ";
    private static final String UNLOCK_COMPLETE_MESSAGE = "チェストがアンロックされました.";

    public Container(BlockState blockState) {
        if (blockState instanceof ShulkerBox) {
            this.nameable = (ShulkerBox) blockState;
            this.inventoryHolder = (ShulkerBox) blockState;
        } else {
            this.nameable = (Chest) blockState;
            this.inventoryHolder = (Chest) blockState;
        }
    }

    public Container(InventoryHolder inventoryHolder) {
        if (inventoryHolder instanceof  ShulkerBox) {
            this.nameable = (ShulkerBox) inventoryHolder;
        } else {
            this.nameable = (Chest) inventoryHolder;
        }
        this.inventoryHolder = inventoryHolder;
    }

    public InventoryHolder getInventoryHolder() {
        return this.inventoryHolder;
    }

    public String getCustomName() {
        return nameable.getCustomName();
    }

    public boolean isContainerLocked() {
        return !StringUtils.isEmpty(getCustomName());
    }

    public void lockChest(Player owner) {

        if (isDoubleChest()) {

            ChestLockManager.setDoubleChestName((DoubleChest) this.inventoryHolder, chestLockJson(owner));
            owner.sendTitle("", LOCK_COMPLETE_MESSAGE + owner.getName(), 10, 70, 20);

        } else {

            this.nameable.setCustomName(chestLockJson(owner));
            owner.sendTitle("", LOCK_COMPLETE_MESSAGE + owner.getName(), 10, 70, 20);

        }

    }

    public boolean isDoubleChest() {
        if (this.inventoryHolder instanceof ShulkerBox) return false;
        return ((Chest) this.inventoryHolder).getInventory().getHolder() instanceof DoubleChest;
    }

    public String chestLockJson(Player owner) {

        JsonObject json = new JsonObject();
        json.addProperty("ownerUUID", owner.getUniqueId().toString());
        json.addProperty("ownerName", owner.getName());
        json.addProperty("securityLevel", "locked");
        return new Gson().toJson(json);

    }

    public void updateCustomName(String jsonString) {

        if (isDoubleChest()) {

            ChestLockManager.setDoubleChestName(((DoubleChest) ((Chest) this.inventoryHolder).getInventory().getHolder()), jsonString);

        } else {

            this.nameable.setCustomName(jsonString);
        }
    }


    public void unlockChest(Player owner) {

        if (isDoubleChest()) {

            ChestLockManager.setDoubleChestName((DoubleChest) this.inventoryHolder, "");
            owner.sendTitle("", UNLOCK_COMPLETE_MESSAGE, 10, 70, 20);

        } else {

            this.nameable.setCustomName("");
            owner.sendTitle("", UNLOCK_COMPLETE_MESSAGE, 10, 70, 20);
        }
    }

    public void toggleSecurityLevel(Player player) {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        JsonObject chestJson = parser.parse(this.getCustomName()).getAsJsonObject();
        JsonElement securityLevel = chestJson.get("securityLevel");

        if (securityLevel == null || securityLevel.getAsString().equals("locked")) {
            chestJson.remove("securityLevel");
            chestJson.addProperty("securityLevel", "shown");
            this.updateCustomName(gson.toJson(chestJson));
            player.sendTitle("", "セキュリティが公開に設定されました", 20, 70, 10);
        } else if (securityLevel.getAsString().equals("shown")) {
            chestJson.remove("securityLevel");
            chestJson.addProperty("securityLevel", "fullOpen");
            this.updateCustomName(gson.toJson(chestJson));
            player.sendTitle("", "セキュリティがフルオープンに設定されました", 20, 70, 10);
        } else if (securityLevel.getAsString().equals("fullOpen")) {
            chestJson.remove("securityLevel");
            chestJson.addProperty("securityLevel", "locked");
            this.updateCustomName(gson.toJson(chestJson));
            player.sendTitle("", "セキュリティが非公開に設定されました", 20, 70, 10);
        }
    }
}
