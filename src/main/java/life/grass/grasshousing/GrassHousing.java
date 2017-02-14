package life.grass.grasshousing;

import life.grass.grasshousing.event.HousingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassHousing extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(new HousingEvent(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
