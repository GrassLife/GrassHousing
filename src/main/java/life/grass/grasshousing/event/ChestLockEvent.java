package life.grass.grasshousing.event;

import life.grass.grassblock.GrassBlock;
import life.grass.grasshousing.HousingManager;
import life.grass.grasshousing.chest.GrassChest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestLockEvent implements Listener {
    @EventHandler
    public void onRightClickChest(PlayerInteractEvent event) {
        if (event.getClickedBlock().getType() == Material.CHEST && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Location blockLocation = block.getLocation();
            Material materialInHand = event.getMaterial();
            if (materialInHand == Material.WOOD_BUTTON) {
                GrassChest chest = new GrassChest(event.getPlayer().getUniqueId(), blockLocation);
                HousingManager.registerChest(chest);
                event.getPlayer().sendMessage("Locking GrassChest... Owner: " + chest.fetchPlayer().getName());
            } else {
                GrassChest chest = HousingManager.createChest(GrassBlock.getBlockManager()
                        .getBlockInfo(
                                blockLocation.getBlockX(),
                                blockLocation.getBlockY(),
                                blockLocation.getBlockZ(),
                                blockLocation.getWorld())
                        .getJson());
                if (!chest.getOwnerUUID().toString().equals(event.getPlayer().getUniqueId().toString())) {
                    event.getPlayer().sendMessage("You Robber!!");
                    event.setCancelled(true);
                }
            }
        }
    }
}
