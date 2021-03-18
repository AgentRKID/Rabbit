package io.github.agentrkid.rabbit.shared.jedis.sub;

import com.google.gson.JsonObject;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageHandler;
import io.github.agentrkid.rabbit.shared.jedis.JedisSub;
import io.github.agentrkid.rabbit.shared.RabbitShared;
import io.github.agentrkid.rabbit.shared.jedis.sub.object.ConnectionType;
import io.github.agentrkid.rabbit.shared.jedis.sub.object.ServerConnectionType;

import java.util.UUID;

public class GlobalConnectionSub extends JedisSub {
    private final static RabbitShared shared = RabbitShared.getInstance();

    public GlobalConnectionSub(JedisMessageHandler handler) {
        super(handler, "rabbit-global");
    }

    @Override
    public void onMessage(String payload, JsonObject data) {
        ConnectionType connectionType = ConnectionType.valueOf(payload);

        UUID playerId;

        try {
            playerId = UUID.fromString(data.get("player").getAsString());
        } catch (Exception ignored) {
            System.out.println("[Rabbit-data] Invalid UUID was provided!");
            return;
        }

        if (connectionType == ConnectionType.JOIN) {
            ServerConnectionType type = ServerConnectionType.valueOf(data.get("connectType").getAsString());

            if (type == ServerConnectionType.NETWORK) {
                shared.getApi().onNetworkJoin(playerId, shared.getServerManager().getServerById(data.get("to").getAsString()));
            } else {
                shared.getApi().onNetworkServerSwitch(playerId, shared.getServerManager().getServerById(data.get("to").getAsString()),
                        shared.getServerManager().getServerById(data.get("from").getAsString()));
            }
        } else {
            shared.getApi().onNetworkLeave(playerId, shared.getServerManager().getServerById(data.get("from").getAsString()));
        }
    }
}
