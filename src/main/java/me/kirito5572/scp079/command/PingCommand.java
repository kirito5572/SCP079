package me.kirito5572.scp079.command;

import me.kirito5572.scp079.App;
import me.kirito5572.scp079.object.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        int ping = event.getJDA().getRestPing().complete().intValue();
        event.getChannel().sendMessage("퐁!").queue((message) ->
                message.editMessageFormat("Gateway Ping: %sms\nRest Ping: %sms", event.getJDA().getGatewayPing(), ping).queue()
        );
    }

    @Override
    public String getHelp() {
        return "핑!\n" +
                "명령어: `" + App.getInstance().getPREFIX() + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "핑";
    }

    @Override
    public String getSmallHelp() {
        return "레이턴시 확인";
    }
}