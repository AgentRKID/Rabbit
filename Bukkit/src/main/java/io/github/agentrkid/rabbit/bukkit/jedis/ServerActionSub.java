package io.github.agentrkid.rabbit.bukkit.jedis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.agentrkid.rabbit.bukkit.RabbitBukkit;
import io.github.agentrkid.rabbit.bukkit.jedis.object.RabbitServerAction;
import io.github.agentrkid.rabbit.shared.RabbitShared;
import io.github.agentrkid.rabbit.shared.jedis.JedisSub;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

public class ServerActionSub extends JedisSub {
    public static final String SERVER_ACTION_PREFIX = "Rabbit-data-";

    public ServerActionSub(String serverId) {
        super(RabbitShared.getInstance().getJedisMessageHandler(), SERVER_ACTION_PREFIX + serverId);
    }

    @Override
    public void onMessage(String payload, JsonObject data) {
        RabbitServerAction serverAction = RabbitServerAction.valueOf(payload);

        // TODO Add more actions maybe?
        if (serverAction == RabbitServerAction.WHITELIST) {
            Bukkit.setWhitelist(data.get("state").getAsBoolean());
        } else {
            System.out.println(data.get("map"));
            for (Map.Entry<String, JsonElement> entry : data.get("map").getAsJsonObject().entrySet()) {
                RabbitBukkit.getInstance().getMetadataHandler().addMetadata(UUID.fromString(data.get("player").getAsString()), entry.getKey(), entry.getValue());
            }
        }
    }
}
