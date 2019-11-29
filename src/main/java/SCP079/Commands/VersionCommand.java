package SCP079.Commands;

import SCP079.App;
import SCP079.Objects.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class VersionCommand implements ICommand {
    private static String version = "빌드 버젼 V 1.1.0 beta 8 hotfix";

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(version + " (" + App.getTime() + ")").queue();
    }

    @Override
    public String getHelp() {
        return "빌드 버젼을 알려줍니다. \n" +
                "사용법: `" + App.getPREFIX() + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "봇버젼";
    }

    @Override
    public String getSmallHelp() {
        return "봇의 빌드 버젼을 알려줍니다";
    }

    public static String getVersion() {
        return version;
    }
}
