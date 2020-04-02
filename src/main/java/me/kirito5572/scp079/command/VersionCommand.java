package me.kirito5572.scp079.command;

import me.kirito5572.scp079.App;
import me.kirito5572.scp079.object.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class VersionCommand implements ICommand {
    private String version = "빌드 버젼 V 1.2.1";

    public String getVersion() {
        return version;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(version + " (" + App.getInstance().getTime() + ")").queue();
    }

    @Override
    public String getHelp() {
        return "빌드 버젼을 알려줍니다. \n" +
                "사용법: `" + App.getInstance().getPREFIX() + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "봇버젼";
    }

    @Override
    public String getSmallHelp() {
        return "봇의 빌드 버젼을 알려줍니다";
    }
}
