package io.github.agentrkid.rabbit.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageHandler;
import io.github.agentrkid.rabbit.shared.jedis.sub.GlobalConnectionSub;
import lombok.Getter;
import io.github.agentrkid.rabbit.shared.manager.RabbitServerManager;

@Getter
public class RabbitShared {
    public static Gson GSON = new GsonBuilder().serializeNulls().create();

    @Getter private static RabbitShared instance;

    private final RabbitAPI api;

    private final String connectionName;

    private final JedisMessageHandler jedisMessageHandler;

    private final RabbitServerManager serverManager;

    public RabbitShared(RabbitAPI api, String connectionName, boolean receiveConnections) {
        instance = this;

        this.api = api;
        this.connectionName = connectionName;

        jedisMessageHandler = new JedisMessageHandler();
        serverManager = new RabbitServerManager();

        if (receiveConnections) {
            new GlobalConnectionSub(jedisMessageHandler);
        }
    }
}
