package io.github.agentrkid.rabbit.bukkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import io.github.agentrkid.rabbit.bukkit.command.DrinkRabbitCommands;
import io.github.agentrkid.rabbit.bukkit.command.param.RabbitServerProvider;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.bukkit.listener.PlayerConnectionListener;
import io.github.agentrkid.rabbit.bukkit.metadata.RabbitMetadataProvider;
import io.github.agentrkid.rabbit.bukkit.thread.RabbitPayloadThread;
import io.github.agentrkid.rabbit.shared.RabbitShared;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageHandler;
import io.github.agentrkid.rabbit.shared.thread.RabbitFetchThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class RabbitBukkit extends JavaPlugin {
    public static Gson GSON = new GsonBuilder().serializeNulls().create();

    @Getter private static RabbitBukkit instance;

    private final List<RabbitMetadataProvider> providers = new ArrayList<>();

    private RabbitServer currentServer;

    private RabbitPayloadThread payloadThread;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Configuration config = getConfig();

        new RabbitShared(new RabbitBukkitAPI(), config.getString("server.id"), true);

        currentServer = new RabbitServer(config.getString("server.id"));
        currentServer.setGroupId(config.getString("server.group-id"));
        currentServer.setWhitelisted(Bukkit.hasWhitelist());

        (payloadThread = new RabbitPayloadThread(currentServer)).start();
        (new RabbitFetchThread()).start();

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);

        RabbitShared.getInstance().getServerManager().addServer(currentServer);

        CommandService drink = Drink.get(this);

        drink.bind(RabbitServer.class).toProvider(new RabbitServerProvider());
        drink.register(new DrinkRabbitCommands(), "rabbit", "rab");
        drink.registerCommands();
    }

    @Override
    public void onDisable() {
        // We need to tell redis this server is offline
        payloadThread.setRunning(false);
        JedisMessageHandler handler = RabbitShared.getInstance().getJedisMessageHandler();

        try {
            Jedis redis = handler.getPool().getResource();

            final Map<String, String> data = new HashMap<>();
            data.put("playerCount", String.valueOf(0));
            data.put("groupId", currentServer.getGroupId());
            data.put("maxPlayerCount", String.valueOf(0));
            data.put("online", String.valueOf(false));
            data.put("whitelisted", String.valueOf(Bukkit.hasWhitelist()));


            redis.hmset("rabbit:" + this.currentServer.getId(), data);

            handler.getPool().returnResource(redis);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addProvider(RabbitMetadataProvider provider) {
        this.providers.add(provider);
    }
}
