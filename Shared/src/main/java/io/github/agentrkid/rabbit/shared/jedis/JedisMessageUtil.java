package io.github.agentrkid.rabbit.shared.jedis;

import com.google.gson.JsonObject;
import io.github.agentrkid.rabbit.shared.RabbitShared;


import java.util.Map;

public class JedisMessageUtil {
    public static void sendMessage(Enum<?> update, ChainableMap data, JedisMessageHandler handler, String channel) {
        new Thread(() -> {
            JsonObject object = new JsonObject();
            for (Map.Entry<String, Object> entry : data.getMap().entrySet()) {
                object.add(entry.getKey(), RabbitShared.GSON.toJsonTree(entry.getValue()));
            }
            handler.writeMessage(update, object, channel);
        }).start();
    }
}
