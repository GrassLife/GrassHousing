package life.grass.grasshousing;

import life.grass.grasshousing.event.ChestLockEvent;
import life.grass.grasshousing.event.HousingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassHousing extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new HousingEvent(), this);
        getServer().getPluginManager().registerEvents(new ChestLockEvent(), this);
    }

    @Override
    public void onDisable() {
        //nothing for now
    }
}
