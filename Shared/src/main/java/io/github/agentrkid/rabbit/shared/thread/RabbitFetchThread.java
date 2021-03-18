package io.github.agentrkid.rabbit.shared.thread;

import com.google.gson.JsonParser;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageHandler;
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

                    boolean oldState = server.isOnline();
                    boolean newState = Boolean.parseBoolean(redis.hget(serverKey, "online"));

                    server.setPlayerCount(Integer.parseInt(redis.hget(serverKey, "playerCount")));
                    server.setMaxPlayerCount(Integer.parseInt(redis.hget(serverKey, "maxPlayerCount")));
                    server.setMetaData(new JsonParser().parse(redis.hget(serverKey, "metaData")).getAsJsonObject());
                    server.setGroupId(redis.hget(serverKey, "groupId"));
                    server.setOnline(newState);

                    if (newState != oldState) {
                        shared.getApi().onServerStateChange(server, newState, oldState);
                    }
                }
                handler.getPool().returnResource(redis);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(1250L);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
