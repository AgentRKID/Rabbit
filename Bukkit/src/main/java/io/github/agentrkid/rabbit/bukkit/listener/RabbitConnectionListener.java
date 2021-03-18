package io.github.agentrkid.rabbit.bukkit.listener;

import io.github.agentrkid.rabbit.bukkit.RabbitBukkit;
import io.github.agentrkid.rabbit.bukkit.events.impl.ServerConnectedByProxyEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RabbitConnectionListener implements Listener {
    @EventHandler
    public void onServerConnect(ServerConnectedByProxyEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("rabbit.staff")) {
            RabbitBukkit.getInstance().getMetadataHandler().addMetadata(player.getUniqueId(), "rabbit-alerts", true);
        }
    }
}
