package life.grass.grasshousing;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChestLockGUI implements InventoryHolder {

    private Inventory inventory;
    private Chest chest;

    public ChestLockGUI(Chest chest) {

        this.chest = chest;
        initializeInventory(chest);

    }

    private void initializeInventory(Chest chest) {

        this.inventory = Bukkit.createInventory(this, 45, "WhiteListSetting");

        JsonParser parser = new JsonParser();
        JsonObject customNameJson = parser.parse(chest.getCustomName()).getAsJsonObject();

        Player owner = Bukkit.getPlayer(UUID.fromString(customNameJson.get("ownerUUID").getAsString()));

        ChestLockManager.setBorder(3, this.inventory);
        this.inventory.setItem(0, new ItemStack(Material.STAINED_CLAY, 1, (short) 5));
        this.inventory.setItem(27, new ItemStack(Material.STAINED_CLAY, 1, (short) 1));

        JsonArray allowedPlayer;

        JsonElement allowedPlayerJsonElement = customNameJson.get("allowedPlayer");

        if (allowedPlayerJsonElement != null) {
            allowedPlayer = allowedPlayerJsonElement.getAsJsonArray();
            allowedPlayer.forEach((json) -> {

                ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta playerSkullMeta = (SkullMeta) playerHead.getItemMeta();
                playerSkullMeta.setOwner(json.getAsJsonObject().get("name").getAsString());
                playerSkullMeta.setDisplayName(json.getAsJsonObject().get("name").getAsString());
                playerHead.setItemMeta(playerSkullMeta);
                for (int i = 1 ; i <= 17 ; i++) {
                    if (this.inventory.getItem(i) == null) {
                        this.inventory.setItem(i, playerHead);
                        break;
                    }
                }
            });
        }

        List<Entity> nearbyPlayers = getNearbyPlayers(owner);
        nearbyPlayers.forEach(p -> {
            if (allowedPlayerJsonElement != null && allowedPlayerJsonElement.getAsJsonArray()
                    .contains(ChestLockManager.allowedPlayerJson(p.getName(), p.getUniqueId().toString()))) {
                return;
            }

            ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta playerSkullMeta = (SkullMeta) playerHead.getItemMeta();
            playerSkullMeta.setOwner(p.getName());
            playerSkullMeta.setDisplayName(p.getName());
            playerHead.setItemMeta(playerSkullMeta);
            for (int j = 27 ; j <= 44 ; j++) {
                if (this.inventory.getItem(j) == null) {
                    this.inventory.setItem(j, playerHead);
                    break;
                }
            }
        });

    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Entity> getNearbyPlayers(Player player) {

        return player.getNearbyEntities(30, 30, 30).stream().filter(p ->
                p instanceof Player
        ).collect(Collectors.toList());

    }

    public Chest getChest() {
        return chest;
    }
}
