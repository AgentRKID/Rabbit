package io.github.agentrkid.rabbit.bukkit.events.impl;

import io.github.agentrkid.rabbit.bukkit.events.RabbitBaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import io.github.agentrkid.rabbit.api.RabbitServer;

@Getter
@AllArgsConstructor
public class ServerStateChangeEvent extends RabbitBaseEvent {
    private final RabbitServer server;
    private final boolean newState;
    private final boolean oldState;
}
