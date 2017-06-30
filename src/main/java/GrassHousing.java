import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import life.grass.grasshousing.event.ChestLockEvent;
import life.grass.grasshousing.protocol.WindowPacketRenamer;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrassHousing extends JavaPlugin {
    private static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ChestLockEvent(), this);

        protocolManager = ProtocolLibrary.getProtocolManager();
        WindowPacketRenamer.getInstance().addListener(protocolManager, this);
    }

    @Override
    public void onDisable() {
        //nothing for now
    }
}
