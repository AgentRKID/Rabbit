package io.github.agentrkid.rabbit.bukkit.thread;

import com.google.gson.JsonObject;
import io.github.agentrkid.rabbit.bukkit.RabbitBukkit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.bukkit.metadata.RabbitMetadataProvider;
import io.github.agentrkid.rabbit.shared.RabbitShared;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageHandler;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RabbitPayloadThread extends Thread {
    private static final RabbitShared shared = RabbitShared.getInstance();

    private static final JedisMessageHandler handler = shared.getJedisMessageHandler();

    private final RabbitServer currentServer;

    @Setter private boolean running;

    public RabbitPayloadThread(RabbitServer currentServer) {
        this.running = true;
        this.currentServer = currentServer;
    }

    @Override
    public void run() {
        while(this.running) {
            try {
                Jedis redis = handler.getPool().getResource();

                final Map<String, String> data = new HashMap<>();
                data.put("playerCount", String.valueOf(Bukkit.getOnlinePlayers().size()));
                data.put("groupId", currentServer.getGroupId());
                data.put("maxPlayerCount", String.valueOf(Bukkit.getMaxPlayers()));
                data.put("metaData", RabbitBukkit.GSON.toJson(loadMetadata()));
                data.put("whitelisted", String.valueOf(Bukkit.hasWhitelist()));
                data.put("online", String.valueOf(true));

                redis.hmset("rabbit:" + this.currentServer.getId(), data);

                handler.getPool().returnResource(redis);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(250L);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Runs through all metadata providers and gets
     * all available metadata
     *
     * @return the final metadata
     */
    private JsonObject loadMetadata() {
        JsonObject object = new JsonObject();
        for (RabbitMetadataProvider provider : RabbitBukkit.getInstance().getProviders()) {
            object = provider.getMetadata(object);
        }
        return object;
    }
}
