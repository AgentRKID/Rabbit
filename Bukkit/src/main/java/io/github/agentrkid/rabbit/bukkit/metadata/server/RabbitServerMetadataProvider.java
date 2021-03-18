package io.github.agentrkid.rabbit.bukkit.metadata.server;

import com.google.gson.JsonObject;

// We provide a metadata object since there can/will be multiple providers.
public abstract class RabbitServerMetadataProvider {
    /**
     * Gets the metadata from the provider and puts it into the object
     *
     * @param object the object to put the metadata into
     * @return the object which has the metadata
     */
    public abstract JsonObject getMetadata(JsonObject object);
}
