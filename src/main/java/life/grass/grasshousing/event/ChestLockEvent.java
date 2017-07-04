package life.grass.grasshousing.event;

import com.google.gson.*;
import life.grass.grasshousing.ChestLockGUI;
import life.grass.grasshousing.ChestLockManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ChestLockEvent implements Listener {
    @EventHandler
    public void onRightClickChest(PlayerInteractEvent event) {

        if (ChestLockManager.isClickedChest(event)) {

            Player player = event.getPlayer();
            Nameable nameable;
            InventoryHolder inventoryHolder;

            if (event.getClickedBlock().getState() instanceof ShulkerBox) {

                nameable = (ShulkerBox) event.getClickedBlock().getState();
                inventoryHolder = (ShulkerBox) event.getClickedBlock().getState();

            } else {
                nameable = (Chest) event.getClickedBlock().getState();
                inventoryHolder = (Chest) event.getClickedBlock().getState();
            }
            Material materialInHand = event.getMaterial();

            if (!ChestLockManager.isChestLocked(nameable)) {

                if (materialInHand.equals(Material.WOOD_BUTTON)) {

                    ChestLockManager.registerChest(player, inventoryHolder);
                    event.setCancelled(true);

                }

            } else {

                JsonParser parser = new JsonParser();
                Gson gson = new Gson();
                JsonObject chestJson = parser.parse(nameable.getCustomName()).getAsJsonObject();
                String playerUUID = chestJson.get("ownerUUID").getAsString();
                JsonElement securityLevel = chestJson.get("securityLevel");

                if (playerUUID.equals(player.getUniqueId().toString())) {

                    if (materialInHand.equals(Material.WOOD_BUTTON)) {

                        ChestLockManager.unregisterChest(player, inventoryHolder);
                        event.setCancelled(true);

                    } else if (materialInHand.equals(Material.STONE_BUTTON)) {

                        event.setCancelled(true);

                        ChestLockGUI gui = new ChestLockGUI(inventoryHolder);
                        player.openInventory(gui.getInventory());

                    } else if (materialInHand.equals(Material.LEVER)) {

                        event.setCancelled(true);

                       if (securityLevel == null || securityLevel.getAsString().equals("locked")) {
                            chestJson.remove("securityLevel");
                            chestJson.addProperty("securityLevel", "shown");
                            ChestLockManager.updateCustomName(inventoryHolder, gson.toJson(chestJson));
                            player.sendTitle("", "セキュリティが公開に設定されました", 20, 70, 10);
                        } else if (securityLevel.getAsString().equals("shown")) {
                            chestJson.remove("securityLevel");
                            chestJson.addProperty("securityLevel", "fullOpen");
                            ChestLockManager.updateCustomName(inventoryHolder, gson.toJson(chestJson));
                            player.sendTitle("", "セキュリティがフルオープンに設定されました", 20, 70, 10);
                        } else if (securityLevel.getAsString().equals("fullOpen")) {
                            chestJson.remove("securityLevel");
                            chestJson.addProperty("securityLevel", "locked");
                            ChestLockManager.updateCustomName(inventoryHolder, gson.toJson(chestJson));
                            player.sendTitle("", "セキュリティが非公開に設定されました", 20, 70, 10);

                        }

                    }

                } else if (securityLevel != null && (securityLevel.getAsString().equals("fullOpen") || securityLevel.getAsString().equals("shown"))) {

                    return;

                } else if (chestJson.get("allowedPlayer") != null) {

                    JsonArray allowedPlayerArray = chestJson.get("allowedPlayer").getAsJsonArray();
                    boolean isAllowed = false;

                    for (int i = 0; i < allowedPlayerArray.size(); i++) {
                        if (player.getUniqueId().toString().equals(allowedPlayerArray.get(i).getAsJsonObject().get("UUID").getAsString())) {
                            isAllowed = true;
                            break;
                        }
                    }

                    if (!isAllowed && ((securityLevel != null && securityLevel.getAsString().equals("locked")) || securityLevel == null)) {

                        event.setCancelled(true);
                        if (chestJson.get("ownerName") == null) {

                            player.sendTitle("", "この泥棒!! これは君のチェストじゃない!!", 10, 70, 20);
                        } else {

                            player.sendTitle("", "この泥棒!! これは" + chestJson.get("ownerName").getAsString() + "のチェストだ!!", 10, 70, 20);
                        }
                    }

                } else {

                    event.setCancelled(true);

                    if (chestJson.get("ownerName") != null) {
                        player.sendTitle("", "この泥棒!! これは" + chestJson.get("ownerName").getAsString() + "のチェストだ!!", 10, 70, 20);
                    } else {
                        // 不具合解消以前に設置されたチェストに関して、ownerNameが記録されていないものに関する例外処理
                        player.sendTitle("", "この泥棒!! これは君のチェストじゃない!!", 10, 70, 20);

                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreakChest(BlockBreakEvent event) {
        if ((event.getBlock().getType().equals(Material.CHEST) || event.getBlock().getType().equals(Material.TRAPPED_CHEST) || event.getBlock().getType().toString().contains("SHULKER_BOX")) &&
                !StringUtils.isEmpty((event.getBlock().getType().toString().contains("SHULKER_BOX") ? (ShulkerBox) event.getBlock().getState() :
                        (Chest) event.getBlock().getState()).getCustomName())) {

            event.getPlayer().sendMessage("このチェストはロックされています. あなたが所有者であれば、壊す前にロックを解除してください.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExtendChest(BlockPlaceEvent event) {

        if ((event.getBlockPlaced().getType().equals(Material.CHEST) || event.getBlockPlaced().getType().equals(Material.TRAPPED_CHEST)) && ChestLockManager.isDoubleChest(((Chest) event.getBlockPlaced().getState()))) {

            Chest chest = (Chest) event.getBlockPlaced().getState();
            Player player = event.getPlayer();

            if (ChestLockManager.isAnotherPartLocked(chest)) {

                JsonParser parser = new JsonParser();

                DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
                String key = ChestLockManager.isChestLocked((Chest) doubleChest.getLeftSide()) ?
                        ((Chest) doubleChest.getLeftSide()).getCustomName() :
                        ((Chest) doubleChest.getRightSide()).getCustomName();

                if (player.getUniqueId().toString().equals(parser.parse(key).getAsJsonObject().get("ownerUUID").getAsString())) {

                    ChestLockManager.setDoubleChestName(doubleChest, key);

                } else {

                    player.sendMessage("他者の所有するチェストを拡張することはできません。");
                    event.setCancelled(true);

                }
            }
        }
    }

    @EventHandler
    public void onClickChestInventory(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Chest || event.getInventory().getHolder() instanceof ShulkerBox) {
            JsonParser parser = new JsonParser();
            String chestJsonString = event.getInventory().getHolder() instanceof ShulkerBox ?
                    ((ShulkerBox) event.getInventory().getHolder()).getCustomName() :
                    ((Chest) event.getInventory().getHolder()).getCustomName();
            JsonObject chestJson;
            if (chestJsonString == null) {
                return;
            } else {
                chestJson = parser.parse(chestJsonString).getAsJsonObject();
            }
            String ownerUUID = chestJson.get("ownerUUID").getAsString();
            String securityLevel = "locked";
            if (chestJson.get("securityLevel") != null) securityLevel = chestJson.get("securityLevel").getAsString();
            JsonElement allowedPlayers = chestJson.get("allowedPlayer");
            if (securityLevel.equals("shown")
                    && (allowedPlayers == null || !ChestLockManager.isAllowed(event.getWhoClicked().getName(), allowedPlayers.getAsJsonArray()))
                    && !ownerUUID.equals(event.getWhoClicked().getUniqueId().toString())) {
                event.setCancelled(true);
            }
            
        }else if (event.getInventory().getHolder() instanceof DoubleChest) {
            JsonParser parser = new JsonParser();
            String chestJsonString = ((Chest) ((DoubleChest) event.getInventory().getHolder()).getLeftSide()).getCustomName();
            JsonObject chestJson;
            if (chestJsonString == null) {
                return;
            } else {
                chestJson = parser.parse(chestJsonString).getAsJsonObject();
            }
            String ownerUUID = chestJson.get("ownerUUID").getAsString();
            JsonElement allowedPlayers = chestJson.get("allowedPlayer");
            String securityLevel = "locked";
            if (chestJson.get("securityLevel") != null) securityLevel = chestJson.get("securityLevel").getAsString();
            if (securityLevel.equals("shown")
                    && (allowedPlayers == null || !ChestLockManager.isAllowed(event.getWhoClicked().getName(), allowedPlayers.getAsJsonArray()))
                    && !ownerUUID.equals(event.getWhoClicked().getUniqueId().toString())) {

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClickWhitelistGUI(InventoryClickEvent event) {

        if (event.getInventory().getHolder() instanceof ChestLockGUI) {
            event.setCancelled(true);
            Inventory inventory = event.getInventory();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null) {
                if (ChestLockManager.isAllowedPart(event.getSlot())) {
                    inventory.setItem(event.getSlot(), new ItemStack(Material.AIR));

                    for (int i = 28 ; i <= 44 ; i++) {
                        if (inventory.getItem(i) == null) {
                            inventory.setItem(i, clickedItem);
                            break;
                        }
                    }
                } else if (ChestLockManager.isNotAllowedPart(event.getSlot())) {

                    inventory.setItem(event.getSlot(), new ItemStack(Material.AIR));

                    for (int j = 1 ; j <= 17 ; j++) {
                        if (inventory.getItem(j) == null) {
                            inventory.setItem(j, clickedItem);
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCloseWhitelistGUI(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        JsonParser parser = new JsonParser();

        if (inventory.getHolder() instanceof ChestLockGUI) {

            InventoryHolder inventoryHolder = ((ChestLockGUI) inventory.getHolder())
                    .getInventoryHolder();
            Nameable nameable = inventoryHolder instanceof ShulkerBox ? (ShulkerBox) inventoryHolder : (Chest) inventoryHolder;

            JsonObject chestJsonObject = parser.parse(nameable.getCustomName()).getAsJsonObject();

            JsonArray allowedJsonArray = new JsonArray();

            if (chestJsonObject.has("allowedPlayer"))
                allowedJsonArray = chestJsonObject.get("allowedPlayer").getAsJsonArray();

            for (int i = 1; i <= 17; i++) {
                if (inventory.getItem(i) != null) {

                    ItemStack item = inventory.getItem(i);
                    SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

                    if (!ChestLockManager.isAllowed(skullMeta.getOwner(), allowedJsonArray)) {

                        Player allowedPlayer = Bukkit.getPlayer(skullMeta.getOwner());
                        JsonObject allowedJson = ChestLockManager.allowedPlayerJson(allowedPlayer.getName(), allowedPlayer.getUniqueId().toString());
                        allowedJsonArray.add(allowedJson);
                    }
                }
            }

            for (int j = 28; j <= 44; j++) {

                if (inventory.getItem(j) != null) {
                    ItemStack item = inventory.getItem(j);
                    SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                    String skullName = skullMeta.getOwner();

                    if (ChestLockManager.isAllowed(skullMeta.getOwner(), allowedJsonArray)) {

                        for (int k = 0; k < allowedJsonArray.size(); k++) {
                            JsonObject json = allowedJsonArray.get(k).getAsJsonObject();
                            if (skullName.equals(json.getAsJsonObject().get("name").getAsString())) {
                                allowedJsonArray.remove(k);
                            }
                        }
                    }
                }
            }

            Gson gson = new Gson();

            chestJsonObject.add("allowedPlayer", allowedJsonArray);

            ChestLockManager.updateCustomName(inventoryHolder, gson.toJson(chestJsonObject));
        }
    }
}
