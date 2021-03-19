package io.github.agentrkid.rabbit.bukkit.utils;

import io.github.agentrkid.rabbit.bukkit.RabbitBukkit;
import io.github.agentrkid.rabbit.bukkit.jedis.ServerActionSub;
import io.github.agentrkid.rabbit.bukkit.jedis.object.RabbitServerAction;
import io.github.agentrkid.rabbit.shared.RabbitShared;
import io.github.agentrkid.rabbit.shared.jedis.ChainableMap;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageUtil;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class MetadataUtil {
    /**
     * Adds metadata to a players session
     *
     * @param playerId the player session
     * @param key the key/metadata to add
     * @param value the value of the metadata
     * @param crossServerCall should make a redis request
     * @param serverId the server id of the player
     */
    public void addMetadata(UUID playerId, String key, Object value, boolean crossServerCall, String serverId) {
        if (crossServerCall) {
            JedisMessageUtil.sendMessage(RabbitServerAction.METADATA_SEND,
                    ChainableMap.create().append("player", playerId.toString()).append("key", key).append("value", value),
                    RabbitShared.getInstance().getJedisMessageHandler(), ServerActionSub.SERVER_ACTION_PREFIX + serverId);
        } else {
            RabbitBukkit.getInstance().getMetadataHandler().addMetadata(playerId, key, value);
        }
    }

    /**
     * Removes metadata from a players session
     *
     * @param playerId the player session
     * @param key the key/metadata to remove
     * @param crossServerCall should make a redis request
     * @param serverId the server id of the player
     */
    public void removeMetadata(UUID playerId, String key, boolean crossServerCall, String serverId) {
        if (crossServerCall) {
            JedisMessageUtil.sendMessage(RabbitServerAction.METADATA_REMOVE, ChainableMap.create().append("player", playerId.toString()).append("key", key),
                    RabbitShared.getInstance().getJedisMessageHandler(),ServerActionSub.SERVER_ACTION_PREFIX + serverId);
        } else {
            RabbitBukkit.getInstance().getMetadataHandler().removeMetadata(playerId, key);
        }
    }
}
