package me.kirito5572.scp079.object;

import me.kirito5572.scp079.App;
import me.kirito5572.scp079.command.*;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandManager() {

        addCommand(new HelpCommand(this));
        addCommand(ObjectPool.get(PingCommand.class));
        addCommand(ObjectPool.get(VersionCommand.class));
        addCommand(ObjectPool.get(ImforCommand.class));
        addCommand(ObjectPool.get(UnbanCommand.class));
        addCommand(ObjectPool.get(ConnectServerCommand.class));
        addCommand(ObjectPool.get(TestCommand.class));
        addCommand(ObjectPool.get(DBSearchCommand.class));
        addCommand(ObjectPool.get(UserInfoCommand.class));
        addCommand(ObjectPool.get(ConfigCommand.class));
        addCommand(ObjectPool.get(TransCommand.class));
        addCommand(ObjectPool.get(SteamUserInfoCommand.class));
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(String name) {
        return commands.get(name);
    }

    public void handleCommand(GuildMessageReceivedEvent event) {
        final TextChannel channel = event.getChannel();
        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(App.getInstance().getPREFIX()), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            try {
                channel.sendTyping().queue();
            } catch (ErrorResponseException e) {
                e.printStackTrace();
                System.out.println(event.getGuild());
            }
            commands.get(invoke).handle(args, event);
        }
    }
}
