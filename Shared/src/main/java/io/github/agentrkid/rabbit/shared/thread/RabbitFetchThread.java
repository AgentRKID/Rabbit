package io.github.agentrkid.rabbit.shared.thread;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageHandler;
import io.github.agentrkid.rabbit.shared.util.ParseUtil;
import redis.clients.jedis.Jedis;
import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.shared.RabbitShared;

import java.util.Set;

public class RabbitFetchThread extends Thread {
    @Override
    public void run() {
        while(true) {
            try {
                RabbitShared shared = RabbitShared.getInstance();
                JedisMessageHandler handler = shared.getJedisMessageHandler();
                Jedis redis = handler.getPool().getResource();

                final Set<String> servers = redis.keys("rabbit:*");

                for (String serverName : servers) {
                    String serverKey = serverName;
                    serverName = serverName.replaceAll("rabbit:", "");

                    RabbitServer server = shared.getServerManager().getServerById(serverName);

                    // Servers are deletable from memory & redis,
                    // so we check if its been loaded again
                    if (server == null) {
                        shared.getServerManager().addServer(new RabbitServer(serverName));
                        continue;
                    }

                    int playerCount = ParseUtil.parseInteger(redis.hget(serverKey, "playerCount"));
                    if (playerCount != -1) {
                        server.setPlayerCount(playerCount);
                    }

                    int maxPlayerCount = ParseUtil.parseInteger(redis.hget(serverKey, "maxPlayerCount"));
                    if (maxPlayerCount != -1) {
                        server.setMaxPlayerCount(maxPlayerCount);
                    }

                    JsonElement element = new JsonParser().parse(redis.hget(serverKey, "metaData"));
                    if (!(element instanceof JsonNull) && element instanceof JsonObject) {
                        server.setMetaData(element.getAsJsonObject());
                    }

                    String groupId = redis.hget(serverKey, "groupId");

                    if (groupId != null) {
                        server.setGroupId(groupId);
                    }

                    boolean whitelistedBefore = server.isWhitelisted();
                    boolean whitelistedNow = ParseUtil.parseBoolean(redis.hget(serverKey, "whitelisted"));

                    server.setWhitelisted(whitelistedNow);

                    if (whitelistedNow != whitelistedBefore) {
                        shared.getEventListener().onServerWhitelistedState(server, whitelistedNow, whitelistedBefore);
                    }


                    boolean oldState = server.isOnline();
                    boolean newState = ParseUtil.parseBoolean(redis.hget(serverKey, "online"));

                    server.setOnline(newState);
                    if (newState != oldState) {
                        shared.getEventListener().onServerStateChange(server, newState, oldState);
                    }
                }
                handler.getPool().returnResource(redis);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(100L);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
