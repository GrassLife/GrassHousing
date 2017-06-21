package life.grass.grasshousing.event;

import life.grass.grasshousing.ChestLockManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestLockEvent implements Listener {
    @EventHandler
    public void onRightClickChest(PlayerInteractEvent event) {

        if (event.getClickedBlock().getType().equals(Material.CHEST) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            Player player = event.getPlayer();
            Chest chest = (Chest) event.getClickedBlock().getState();
            Material materialInHand = event.getMaterial();

            if (StringUtils.isEmpty(chest.getCustomName())) {

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

                    player.sendMessage("You Robber!! This is " + player.getName() + "'s Chest!!");
                    event.setCancelled(true);

                }
            }
        }
    }

    @EventHandler
    public void onBreakChest(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.CHEST) &&
                !StringUtils.isEmpty(((Chest) event.getBlock().getState()).getCustomName())) {

            event.getPlayer().sendMessage("This chest is locked. If you're the owner, unlock this chest before breaking.");
            event.setCancelled(true);
        }
    }
}
