package io.github.agentrkid.rabbit.bukkit.listener;

import io.github.agentrkid.rabbit.bukkit.RabbitBukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import io.github.agentrkid.rabbit.bukkit.utils.CC;

public class PlayerConnectionListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Automatically turn on alerts for someone who has the permission.
        if (player.hasPermission("rabbit.auto.alert")) {
            player.setMetadata("rabbit-alerts", new FixedMetadataValue(RabbitBukkit.getInstance(), "192351066"));
            player.sendMessage(CC.translate("&7Your server monitor alerts have been automatically turned &aon&7,"));
        }
    }
}
