package io.github.agentrkid.rabbit.bukkit.command.param;

import com.jonahseguin.drink.argument.CommandArg;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;
import io.github.agentrkid.rabbit.api.RabbitServer;
import io.github.agentrkid.rabbit.shared.RabbitShared;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RabbitServerProvider extends DrinkProvider<RabbitServer> {
    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Nullable
    @Override
    public RabbitServer provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String rabbitServerName = arg.get();
        RabbitServer server = RabbitShared.getInstance().getServerManager().getServerById(rabbitServerName);

        if (server != null) {
            return server;
        }
        throw new CommandExitMessage("No server with the name '" + rabbitServerName + "' found.");
    }

    @Override
    public String argumentDescription() {
        return "Rabbit server";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return new ArrayList<>(RabbitShared.getInstance().getServerManager().getServers().stream().map(RabbitServer::getId).collect(Collectors.toList()));
    }
}
