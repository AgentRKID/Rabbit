package io.github.agentrkid.rabbit.shared.jedis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;

@Getter
public abstract class JedisSub extends JedisPubSub {
    private final JedisMessageHandler handler;
    private final String[] channels;

    public JedisSub(JedisMessageHandler handler, String... channels) {
        this.handler = handler;
        this.channels = channels;

        new Thread(() -> {
            Jedis jedis = this.handler.getPool().getResource();
            jedis.subscribe(this, this.channels);
            this.handler.getPool().returnResource(jedis);
        }).start();
    }

    @Override
    public void onMessage(String channel, String message) {
        if (Arrays.asList(this.channels).contains(channel)) {
            try {
                JsonObject object = new JsonParser().parse(message).getAsJsonObject();
                String payload = object.get("payload").getAsString();
                JsonObject data = object.get("data").getAsJsonObject();
                this.onMessage(payload, data);
            } catch (JsonParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    public abstract void onMessage(String payload, JsonObject data);
}
