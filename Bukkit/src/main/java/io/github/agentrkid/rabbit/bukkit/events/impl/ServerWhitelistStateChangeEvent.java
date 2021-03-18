package io.github.agentrkid.rabbit.bukkit.events.impl;

import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.bukkit.events.RabbitBaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServerWhitelistStateChangeEvent extends RabbitBaseEvent {
    private final RabbitServer server;
    private final boolean newState;
    private final boolean oldState;
}
