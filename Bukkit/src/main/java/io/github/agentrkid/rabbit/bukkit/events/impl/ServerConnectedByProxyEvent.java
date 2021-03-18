package io.github.agentrkid.rabbit.bukkit.events.impl;

import io.github.agentrkid.rabbit.bukkit.events.RabbitBaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;


@Getter
@AllArgsConstructor
public class ServerConnectedByProxyEvent extends RabbitBaseEvent {
    private final Player player;
}
