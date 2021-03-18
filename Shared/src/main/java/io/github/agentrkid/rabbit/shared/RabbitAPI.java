package io.github.agentrkid.rabbit.shared;

import io.github.agentrkid.rabbit.api.RabbitServer;

import java.util.UUID;

public interface RabbitAPI {
    /**
     * Notifies all online servers that a player
     * joined the network
     *
     * @param playerId the player which joined
     * @param to the server they joined to
     */
    void onNetworkJoin(UUID playerId, RabbitServer to);

    /**
     * Notifies all online servers that a player
     * joined using the callback result.
     *
     * @param playerId the player which joined
     * @param to the server which the callback was set to
     */
    void onNetworkLobbyCallbackJoin(UUID playerId, RabbitServer to);

    /**
     * Notifies all online servers that a player
     * has switched servers on the network
     *
     * @param playerId the player which joined
     * @param to the server they joined to
     * @param from the server they left from
     */
    void onNetworkServerSwitch(UUID playerId, RabbitServer to, RabbitServer from);

    /**
     * Notifies all online servers that a player
     * left the network
     *
     * @param playerId the player which left
     * @param from the server they left from
     */
    void onNetworkLeave(UUID playerId, RabbitServer from);

    /**
     * Notifies all online servers that a server
     * state has changed from online to offline or offline to online.
     *
     * @param server the server which state changed
     * @param newState the updated state
     * @param oldState the old state
     */
    void onServerStateChange(RabbitServer server, boolean newState, boolean oldState);

    /**
     * Notifies all online servers that a server
     * whitelist state has changed from true to false or false to true.
     *
     * @param server the server which the whitelist state changed
     * @param newState the new state
     * @param oldState
     */
    void onServerWhitelistedState(RabbitServer server, boolean newState, boolean oldState);
}
