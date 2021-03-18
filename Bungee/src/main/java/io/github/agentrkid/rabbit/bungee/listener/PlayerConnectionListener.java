package io.github.agentrkid.rabbit.bungee.listener;

import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import io.github.agentrkid.rabbit.shared.RabbitShared;
import io.github.agentrkid.rabbit.shared.jedis.ChainableMap;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageUtil;
import io.github.agentrkid.rabbit.shared.jedis.sub.object.ConnectionType;
import io.github.agentrkid.rabbit.shared.jedis.sub.object.ServerConnectionType;

public class PlayerConnectionListener implements Listener {
    @EventHandler(priority = -64)
    public void onPlayerJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerConnectEvent.Reason reason = event.getReason();

        ChainableMap chain = ChainableMap.create();

        if (reason != ServerConnectEvent.Reason.JOIN_PROXY && reason != ServerConnectEvent.Reason.LOBBY_FALLBACK && player != null) {
            chain.append("connectType", ServerConnectionType.SWITCH);
            chain.append("from", player.getServer().getInfo().getName());
            chain.append("to", event.getTarget().getName());
        } else if (reason == ServerConnectEvent.Reason.LOBBY_FALLBACK) {
            chain.append("connectType", ServerConnectionType.FALLBACK);
            chain.append("to", event.getTarget().getName());
        } else {
            chain.append("connectType", ServerConnectionType.NETWORK);
            chain.append("to", event.getTarget().getName());
        }

        chain.append("player", player.getUniqueId().toString());

        JedisMessageUtil.sendMessage(ConnectionType.JOIN, chain, RabbitShared.getInstance().getJedisMessageHandler(), "rabbit-global");
    }

    @EventHandler(priority = -64)
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerInfo leftFrom = player.getServer().getInfo();

        JedisMessageUtil.sendMessage(ConnectionType.LEAVE, ChainableMap.create().append("from", leftFrom.getName())
                        .append("player", player.getUniqueId().toString()),
                RabbitShared.getInstance().getJedisMessageHandler(),
                "rabbit-global");
    }
}
