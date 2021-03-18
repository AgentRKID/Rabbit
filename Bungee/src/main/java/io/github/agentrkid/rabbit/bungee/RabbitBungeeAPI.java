package io.github.agentrkid.rabbit.bungee;

import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.shared.RabbitAPI;

import java.util.UUID;


public class RabbitBungeeAPI implements RabbitAPI {
    @Override
    public void onNetworkJoin(UUID playerId, RabbitServer to) { }

    @Override
    public void onNetworkLobbyCallbackJoin(UUID playerId, RabbitServer to) { }

    @Override
    public void onNetworkServerSwitch(UUID playerId, RabbitServer to, RabbitServer from) { }

    @Override
    public void onNetworkLeave(UUID playerId, RabbitServer from) { }

    @Override
    public void onServerStateChange(RabbitServer server, boolean newState, boolean oldState) { }

    @Override
    public void onServerWhitelistedState(RabbitServer server, boolean newState, boolean oldState) { }
}
