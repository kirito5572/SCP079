package SCP079.Commands;

import SCP079.App;
import SCP079.Objects.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("퐁!").queue((message) ->
                message.editMessageFormat("결과: 봇: %sms\n API: %sms", event.getJDA().getGatewayPing(), event.getJDA().getRestPing()).queue()
        );
    }

    @Override
    public String getHelp() {
        return "핑!\n" +
                "명령어: `" + App.getPREFIX() + getInvoke() + "`";
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