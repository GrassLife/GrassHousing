package life.grass.grasshousing.event;

import com.google.gson.Gson;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChestLockEvent implements Listener {
    @EventHandler
    public void onRightClickChest(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.CHEST)) {

            Player player = event.getPlayer();
            Chest chest = (Chest) event.getClickedBlock().getState();
            Material materialInHand = event.getMaterial();

            if (!ChestLockManager.isChestLocked(chest)) {

                if (materialInHand.equals(Material.WOOD_BUTTON)) {

                    ChestLockManager.registerChest(player, chest);
                    event.setCancelled(true);

                }

            } else {

                if (chest.getCustomName().equals(ChestLockManager.chestLockJson(player))) {

                    if (materialInHand.equals(Material.WOOD_BUTTON)) {

                        ChestLockManager.unregisterChest(player, chest);
                        event.setCancelled(true);

                    }

                } else {

                    Gson gson = new Gson();
                    HashMap<String, String> str = gson.fromJson(chest.getCustomName(), HashMap.class);

                    player.sendTitle("", "この泥棒!! これは" + Bukkit.getPlayer(UUID.fromString(str.get("ownerUUID"))).getName() + "のチェストだ!!", 10, 70, 20);
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
