package life.grass.grasshousing;

import life.grass.grasshousing.event.ChestLockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassHousing extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ChestLockEvent(), this);
    }

    @Override
    public void onDisable() {
        //nothing for now
    }
}
