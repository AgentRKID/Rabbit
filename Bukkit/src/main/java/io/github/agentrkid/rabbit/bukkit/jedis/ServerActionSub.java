package io.github.agentrkid.rabbit.bukkit.jedis;

import com.google.gson.JsonObject;
import io.github.agentrkid.rabbit.shared.RabbitShared;
import io.github.agentrkid.rabbit.shared.jedis.JedisSub;
import org.bukkit.Bukkit;

public class ServerActionSub extends JedisSub {
    public ServerActionSub(String serverId) {
        super(RabbitShared.getInstance().getJedisMessageHandler(), "Rabbit-data-" + serverId);
    }

    @Override
    public void onMessage(String payload, JsonObject data) {
        RabbitServerAction serverAction = RabbitServerAction.valueOf(payload);

        // TODO Add more actions maybe?
        if (serverAction == RabbitServerAction.WHITELIST) {
            Bukkit.setWhitelist(data.get("state").getAsBoolean());
        }
    }
}
