package io.github.agentrkid.rabbit.bukkit.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import io.github.agentrkid.rabbit.bukkit.RabbitBukkit;
import io.github.agentrkid.rabbit.bukkit.jedis.object.RabbitServerAction;
import io.github.agentrkid.rabbit.bukkit.jedis.ServerActionSub;
import io.github.agentrkid.rabbit.bukkit.metadata.player.MetadataHandler;
import io.github.agentrkid.rabbit.bukkit.utils.MetadataUtil;
import io.github.agentrkid.rabbit.shared.jedis.ChainableMap;
import io.github.agentrkid.rabbit.shared.jedis.JedisMessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.bukkit.utils.CC;
import io.github.agentrkid.rabbit.shared.RabbitShared;

public class DrinkRabbitCommands {
    private static final MetadataHandler metadataHandler = RabbitBukkit.getInstance().getMetadataHandler();

    private static final String LINE = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat('-', 53);

    @Command(name = "", desc = "Rabbit help", usage = "")
    @Require("rabbit.staff")
    public void displayHelp(@Sender CommandSender sender) {

    }

    // Requires no permission because, we allow users to see some information.
    @Command(name = "env", aliases = {"dump"}, desc = "Display the cached dump of the searched server", usage = "<server>")
    public void displayDump(@Sender CommandSender sender, RabbitServer server) {
        sender.sendMessage(LINE);
        sender.sendMessage(CC.translate("&f&l&nRabbit Data Dump"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate(" &7* &fName: &7" + server.getId()));
        sender.sendMessage(CC.translate(" &7* &fGroup: &7" + server.getGroupId()));

        if (sender.hasPermission("rabbit.staff")) {
            sender.sendMessage(CC.translate(" &7* &fOnline: " + (server.isOnline() ? "&aOnline" : "&cOffline") + "&7."));
            if (server.isOnline()) {
                sender.sendMessage(CC.translate(" &7* &fWhitelisted: " + (server.isWhitelisted() ? "&aOn" : "&cOff") + "&7."));
                sender.sendMessage(CC.translate(" &7* &fPlayer Count: &7" + server.getPlayerCount()));
                sender.sendMessage(CC.translate(" &7* &fMax Player Count: &7" + server.getMaxPlayerCount()));
                if (sender.hasPermission("rabbit.developer")) {
                    sender.sendMessage(CC.translate(" &7* &fMetadata: &7" + server.getMetaData()));
                }
            }
        }
        sender.sendMessage(LINE);
    }

    @Command(name = "groups", desc = "Displays all cached groups")
    @Require("rabbit.staff")
    public void displayGroupList(@Sender CommandSender sender) {
        sender.sendMessage(CC.translate("&7&oListing groups."));
        for (String group : RabbitShared.getInstance().getServerManager().getGroups()) {
            sender.sendMessage(CC.translate("&f" + group + " &7(&7&o" + RabbitShared.getInstance().getServerManager().getServersByGroup(group).size() + "&7)"));
        }
    }

    @Command(name = "servers", desc = "Displays all cached servers")
    @Require("rabbit.staff")
    public void displayServersList(@Sender CommandSender sender) {
        sender.sendMessage(CC.translate("&7&oListing servers."));
        for (RabbitServer server : RabbitShared.getInstance().getServerManager().getServers()) {
            sender.sendMessage(CC.translate("&f" + server.getId() + " &7(&7&o" + server.getGroupId() + "&7) &f-> " + (server.isOnline() ? "&aOnline" : "&cOffline") + "&7."));
        }
    }

    @Command(name = "whitelist", desc = "Whitelist a server through redis")
    @Require("rabbit.whitelist")
    public void changeWhitelistState(@Sender CommandSender sender, RabbitServer server) {
        if (!server.isOnline()) {
            sender.sendMessage(CC.translate("&c" + server.getId() + " is currently offline."));
            return;
        }

        // We don't need a callback message from this,
        // the callback should be the alert (if they have their alerts on)
        sender.sendMessage(CC.translate("&7&oChanging the whitelist state on " + server.getId()));
        JedisMessageUtil.sendMessage(RabbitServerAction.WHITELIST, ChainableMap.create().append("state", !server.isWhitelisted()),
                RabbitShared.getInstance().getJedisMessageHandler(), ServerActionSub.SERVER_ACTION_PREFIX + server.getId());
    }

    @Command(name = "alerts", desc = "Change your alert status")
    @Require("rabbit.staff")
    public void changeAlertsState(@Sender Player sender) {
        if (sender.hasMetadata("rabbit-alerts")) {
            MetadataUtil.removeMetadata(sender.getUniqueId(), "rabbit-alerts", false, null);
        } else {
            MetadataUtil.addMetadata(sender.getUniqueId(), "rabbit-alerts", true, false, null);
        }
        sender.sendMessage(CC.translate("&7You've toggled your server monitor alerts "
                + (metadataHandler.hasMetadata(sender.getUniqueId(), "rabbit-alerts") ? "&aon" : "&coff") + "&7."));
    }
}
