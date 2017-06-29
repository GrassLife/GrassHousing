package life.grass.grasshousing.event;

import com.google.gson.Gson;
import life.grass.grasshousing.ChestLockGUI;
import life.grass.grasshousing.ChestLockManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChestLockEvent implements Listener {
    @EventHandler
    public void onRightClickChest(PlayerInteractEvent event) {

        if (ChestLockManager.isClickedChest(event)) {

            Player player = event.getPlayer();
            Chest chest = (Chest) event.getClickedBlock().getState();
            Material materialInHand = event.getMaterial();

            if (!ChestLockManager.isChestLocked(chest)) {

                if (materialInHand.equals(Material.WOOD_BUTTON)) {

                    ChestLockManager.registerChest(player, chest);
                    event.setCancelled(true);

                }

            } else {


                Gson gson = new Gson();
                HashMap<String, String> str = gson.fromJson(chest.getCustomName(), HashMap.class);
                String playerUUID = str.get("ownerUUID");

                if (playerUUID.equals(player.getUniqueId().toString())) {

                    if (materialInHand.equals(Material.WOOD_BUTTON)) {

                        ChestLockManager.unregisterChest(player, chest);
                        event.setCancelled(true);

                    } else if (materialInHand.equals(materialInHand.STONE_BUTTON)) {
                        event.setCancelled(true);

                        ChestLockGUI gui = new ChestLockGUI(chest);
                        player.openInventory(gui.getInventory());

                    }

                } else {


                    if (!StringUtils.isEmpty(str.get("ownerName"))) {
                        player.sendTitle("", "この泥棒!! これは" + str.get("ownerName") + "のチェストだ!!", 10, 70, 20);
                    } else {
                        // 不具合解消以前に設置されたチェストに関して、ownerNameが記録されていないものに関する例外処理
                        player.sendTitle("", "この泥棒!! これは君のチェストじゃない!!", 10, 70, 20);

                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBreakChest(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.CHEST) &&
                !StringUtils.isEmpty(((Chest) event.getBlock().getState()).getCustomName())) {

            event.getPlayer().sendMessage("このチェストはロックされています. あなたが所有者であれば、壊す前にロックを解除してください.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExtendChest(BlockPlaceEvent event) {

        if (event.getBlockPlaced().getType().equals(Material.CHEST) && ChestLockManager.isDoubleChest(((Chest) event.getBlockPlaced().getState()))) {

            Chest chest = (Chest) event.getBlockPlaced().getState();
            Player player = event.getPlayer();

            if (ChestLockManager.isAnotherPartLocked(chest)) {

                DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
                String key = ChestLockManager.isChestLocked((Chest) doubleChest.getLeftSide()) ?
                        ((Chest) doubleChest.getLeftSide()).getCustomName() :
                        ((Chest) doubleChest.getRightSide()).getCustomName();

                if (key.equals(ChestLockManager.chestLockJson(event.getPlayer()))) {

                    ChestLockManager.registerChest(player, chest);

                } else {

                    player.sendMessage("他者の所有するチェストを拡張することはできません。");
                    event.setCancelled(true);

                }
            }
        }
    }
}
