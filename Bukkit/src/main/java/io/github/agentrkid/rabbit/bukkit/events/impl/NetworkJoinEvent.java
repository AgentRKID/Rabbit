package io.github.agentrkid.rabbit.bukkit.events.impl;

import io.github.agentrkid.rabbit.bukkit.RabbitBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.bukkit.events.RabbitBaseEvent;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class NetworkJoinEvent extends RabbitBaseEvent {
    private final UUID playerId;
    private final RabbitServer to;

    public boolean isToCurrent() {
        return to == RabbitBukkit.getInstance().getCurrentServer();
    }
}
