package io.github.agentrkid.rabbit.bungee;

import io.github.agentrkid.rabbit.shared.thread.RabbitFetchThread;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import io.github.agentrkid.rabbit.bungee.listener.PlayerConnectionListener;
import io.github.agentrkid.rabbit.shared.RabbitShared;

@Getter
public class RabbitBungee extends Plugin {
    @Getter private static RabbitBungee instance;

    @Override
    public void onEnable() {
        instance = this;

        new RabbitShared(new RabbitBungeeAPI(), "Bungee", false);

        (new RabbitFetchThread()).start();

        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerConnectionListener());

        getLogger().info("Rabbit initialized");
    }

    @Override
    public void onDisable() {
        getLogger().info("Rabbit Disabling.");
    }
}
