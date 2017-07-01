package life.grass.grasshousing.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;

public class WindowPacketRenamer {
    private static WindowPacketRenamer instance;
    private static Gson gson;

    static {
        instance = new WindowPacketRenamer();
        gson = new Gson();
    }

    private WindowPacketRenamer() {
    }

    public static WindowPacketRenamer getInstance() {
        return instance;
    }

    public void addListener(ProtocolManager manager, JavaPlugin plugin) {
        manager.addPacketListener(new PacketAdapter(plugin,
                ListenerPriority.LOW,
                PacketType.Play.Server.OPEN_WINDOW) {

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketType type = event.getPacketType();

                if (type != PacketType.Play.Server.OPEN_WINDOW) return;

                try {
                    WrappedChatComponent titleChatComponent = event.getPacket().getChatComponents().read(0);
                    if (titleChatComponent == null) return;

                    JsonObject titleJsonObject = gson.fromJson(titleChatComponent.getJson(), JsonObject.class);
                    if (titleJsonObject == null || !titleJsonObject.has("text")) return;

                    JsonObject textJsonObject = gson.fromJson(titleJsonObject.get("text").getAsString().replace("'", ""), JsonObject.class);
                    if (textJsonObject == null) return;

                    String ownerName = textJsonObject.has("ownerName") ? textJsonObject.get("ownerName").getAsString() : "UNKNOWN";

                    titleJsonObject.addProperty("text", ownerName + "'s chest");
                    titleChatComponent.setJson(titleJsonObject.toString());

                    event.getPacket().getChatComponents().write(0, titleChatComponent);
                } catch (Exception ex) {
                    return;
                }
            }
        });
    }
}
