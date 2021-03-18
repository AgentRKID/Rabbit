package io.github.agentrkid.rabbit.bukkit.metadata.player;

import io.github.agentrkid.rabbit.bukkit.RabbitBukkit;
import io.github.agentrkid.rabbit.bukkit.events.impl.NetworkJoinEvent;
import io.github.agentrkid.rabbit.bukkit.events.impl.NetworkLeaveEvent;
import io.github.agentrkid.rabbit.bukkit.events.impl.NetworkSwitchServerEvent;
import io.github.agentrkid.rabbit.bukkit.events.impl.ServerConnectedByProxyEvent;
import io.github.agentrkid.rabbit.bukkit.jedis.ServerActionSub;
import io.github.agentrkid.rabbit.bukkit.jedis.object.RabbitServerAction;
import io.github.agentrkid.rabbit.shared.RabbitShared;
import io.github.agentrkid.rabbit.shared.jedis.ChainableMap;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class MetadataHandler implements Listener {
    private final List<UUID> awaitingConnection = new ArrayList<>();

    private final Map<UUID, Map<String, Object>> metadataCache = new HashMap<>();

    public MetadataHandler() {
        Bukkit.getPluginManager().registerEvents(this, RabbitBukkit.getInstance());
        Bukkit.getScheduler().runTaskTimerAsynchronously(RabbitBukkit.getInstance(), this::update, 0, 15);
    }

    public void addMetadata(UUID playerId, String key, Object value) {
        metadataCache.computeIfAbsent(playerId, map -> new HashMap<>()).put(key, value);
    }

    public void removeMetadata(UUID playerId, String key) {
        if (metadataCache.containsKey(playerId)) {
            Player player = Bukkit.getPlayer(playerId);
            // No need to remove the data if they're off, it will be removed on update.
            if (player != null && player.isOnline()) {
                player.removeMetadata(key, RabbitBukkit.getInstance());
                metadataCache.get(playerId).remove(key);
            }
        }
    }

    public boolean hasMetadata(UUID playerId, String key) {
        if (metadataCache.containsKey(playerId)) {
            return metadataCache.get(playerId).containsKey(key);
        }
        return false;
    }

    private void update() {
        for (Map.Entry<UUID, Map<String, Object>> entry : metadataCache.entrySet()) {
            UUID playerId = entry.getKey();
            Map<String, Object> metadataEntryMap = entry.getValue();

            if (!awaitingConnection.contains(playerId)) {
                Player player = Bukkit.getPlayer(playerId);

                if (player != null && player.isOnline()) {
                    for (Map.Entry<String, Object> metadataEntry : metadataEntryMap.entrySet()) {
                        String metadataName = metadataEntry.getKey();
                        Object metadata = metadataEntry.getKey();

                        if (!player.hasMetadata(metadataName)) {
                            player.setMetadata(metadataName, new FixedMetadataValue(RabbitBukkit.getInstance(), metadata));
                        }
                    }
                } else {
                    metadataCache.remove(playerId);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (awaitingConnection.contains(player.getUniqueId())) {
            Bukkit.getPluginManager().callEvent(new ServerConnectedByProxyEvent(player));
            awaitingConnection.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onNetworkJoin(NetworkJoinEvent event) {
        if (event.isToCurrent()) {
            awaitingConnection.add(event.getPlayerId());
        }
    }


    @EventHandler
    public void onServerSwitch(NetworkSwitchServerEvent event) {
        UUID playerId = event.getPlayerId();

        if (event.getFrom() == RabbitBukkit.getInstance().getCurrentServer()) {
            Map<String, Object> savedMetadata = this.metadataCache.get(playerId);

            if (savedMetadata != null) {
                JedisMessageUtil.sendMessage(RabbitServerAction.METADATA_TRANSFER,
                        ChainableMap.create().append("player", playerId.toString())
                                .append("map", RabbitBukkit.GSON.toJsonTree(savedMetadata)),
                        RabbitShared.getInstance().getJedisMessageHandler(), ServerActionSub.SERVER_ACTION_PREFIX + event.getTo().getId());
            }
        }
    }

    @EventHandler
    public void onNetworkLeave(NetworkLeaveEvent event) {
        UUID playerId = event.getPlayerId();

        metadataCache.remove(playerId);
        awaitingConnection.remove(playerId);
    }
}
