package io.github.agentrkid.rabbit.api;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RabbitServer {
    private final String id;

    private String groupId;

    private int playerCount;

    private int maxPlayerCount;

    private boolean online;

    private boolean whitelisted;

    private JsonObject metaData;

    public RabbitServer(String id) {
        this.id = id;
        this.groupId = "Not set";
        this.playerCount = 0;
        this.maxPlayerCount = 250;
        this.metaData = new JsonObject();
    }
}
