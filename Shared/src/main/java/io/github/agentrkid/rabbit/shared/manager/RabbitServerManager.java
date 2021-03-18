package io.github.agentrkid.rabbit.shared.manager;

import lombok.Getter;
import io.github.agentrkid.rabbit.api.RabbitServer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RabbitServerManager {
    private final List<RabbitServer> servers = new ArrayList<>();

    /**
     * Get a server by the id
     *
     * @param serverName the server id/name
     * @return the server data object
     */
    public RabbitServer getServerById(String serverName) {
        return this.servers.stream().filter(server -> server.getId().equalsIgnoreCase(serverName)).findAny().orElse(null);
    }

    /**
     * Gets all servers inside a group
     *
     * @param groupId the group to search in
     * @return a list of all server data objects that are in the group
     */
    public List<RabbitServer> getServersByGroup(String groupId) {
        return this.servers.stream().filter(server -> server.getGroupId().equalsIgnoreCase(groupId)).collect(Collectors.toList());
    }

    /**
     * Gets all unique groups
     *
     * @return a list with unique groups
     */
    public List<String> getGroups() {
        List<String> avaiableGroups = new ArrayList<>();
        for (RabbitServer server : servers) {
            if (!(avaiableGroups.contains(server.getGroupId()))) {
                avaiableGroups.add(server.getGroupId());
            }
        }
        return avaiableGroups;
    }

    /**
     * Add a server to the cache
     *
     * @param server the server to be added
     */
    public void addServer(RabbitServer server) {
        this.servers.add(server);
    }

    /**
     * Remove a server from the cache
     *
     * @param server the server to be removed
     */
    public void removeServer(RabbitServer server) {
        this.servers.remove(server);
    }
}
