package io.github.agentrkid.rabbit.bukkit.utils;

import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.bukkit.RabbitBukkit;
import io.github.agentrkid.rabbit.shared.RabbitShared;

import java.util.List;
import java.util.stream.Collectors;

public class RabbitAPI {
    /**
     * Gets a server by the provided id
     *
     * @param id the server id
     * @return the server found
     */
    public static RabbitServer getServer(String id) {
        return RabbitShared.getInstance().getServerManager().getServerById(id);
    }

    /**
     * Gets all groups
     *
     * @return All groups
     */
    public static List<String> getGroups() {
        return RabbitShared.getInstance().getServerManager().getGroups();
    }

    /**
     * Provides all servers in a group
     *
     * @param group the group
     * @return All servers in a group
     */
    public static List<RabbitServer> getServers(String group) {
        return RabbitShared.getInstance().getServerManager().getServersByGroup(group);
    }

    /**
     * Gets all available servers
     *
     * @return All available servers
     */
    public static List<RabbitServer> getServers() {
        return RabbitShared.getInstance().getServerManager().getServers();
    }

    /**
     * Gets all online servers
     *
     * @return All the online servers
     */
    public static List<RabbitServer> getOnlineServers() {
        return getServers().stream().filter(RabbitServer::isOnline).collect(Collectors.toList());
    }

    /**
     * Gets all offline servers
     *
     * @return All the offline servers
     */
    public static List<RabbitServer> getOfflineServers() {
        return getServers().stream().filter(server -> !server.isOnline()).collect(Collectors.toList());
    }

    /**
     * Gets the current server
     *
     * @return the current server instance
     */
    public static RabbitServer getCurrentServer() {
        return RabbitBukkit.getInstance().getCurrentServer();
    }

    /**
     * Gets the current servers group
     *
     * @return the current servers group
     */
    public static String getCurrentServerGroup() {
        return getCurrentServer().getGroupId();
    }
}
