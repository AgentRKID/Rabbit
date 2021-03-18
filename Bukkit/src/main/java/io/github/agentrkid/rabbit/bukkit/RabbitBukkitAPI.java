package io.github.agentrkid.rabbit.bukkit;

import io.github.agentrkid.rabbit.bukkit.events.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.github.agentrkid.rabbit.bukkit.utils.CC;
import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.shared.RabbitAPI;

import java.util.UUID;

public class RabbitBukkitAPI implements RabbitAPI {
    @Override
    public void onNetworkJoin(UUID playerId, RabbitServer to) {
        Bukkit.getPluginManager().callEvent(new NetworkJoinEvent(playerId, to));
    }

    @Override
    public void onNetworkServerSwitch(UUID playerId, RabbitServer to, RabbitServer from) {
        Bukkit.getPluginManager().callEvent(new NetworkSwitchServerEvent(playerId, to, from));
    }

    @Override
    public void onNetworkLeave(UUID playerId, RabbitServer from) {
        Bukkit.getPluginManager().callEvent(new NetworkLeaveEvent(playerId, from));
    }

    @Override
    public void onServerStateChange(RabbitServer server, boolean newState, boolean oldState) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasMetadata("rabbit-alerts") && onlinePlayer.hasPermission("rabbit.alerts")) {
                onlinePlayer.sendMessage(CC.translate("&7[&fRabbit Alert&7] &7" + server.getId() + " is now "
                        + (server.isOnline() ? "&aonline" : "&coffline") + "&7."));
            }
        }
        Bukkit.getPluginManager().callEvent(new ServerStateChangeEvent(server, newState, oldState));
    }

    @Override
    public void onServerWhitelistedState(RabbitServer server, boolean newState, boolean oldState) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasMetadata("rabbit-alerts") && onlinePlayer.hasPermission("rabbit.alerts")) {
                onlinePlayer.sendMessage(CC.translate("&7[&fRabbit Alert&7] &7" + server.getId() + " is now "
                        + (server.isWhitelisted() ? "&aWhitelisted" : "&cUnWhitelisted") + "&7."));
            }
        }
        Bukkit.getPluginManager().callEvent(new ServerWhitelistStateChangeEvent(server, newState, oldState));
    }
}
