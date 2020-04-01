package me.kirito5572.scp079.command;

import me.kirito5572.scp079.object.ICommand;
import me.kirito5572.scp079.object.SQLDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.swing.plaf.nimbus.State;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TestCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
    }

    @Override
    public String getHelp() {
        return "테스트";
    }

    @Override
    public String getInvoke() {
        return "테스트";
    }

    @Override
    public String getSmallHelp() {
        return "테스트";
    }
}
