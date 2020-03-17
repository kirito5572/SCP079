package SCP079.Commands;

import SCP079.App;
import SCP079.Listener.Listener;
import SCP079.Objects.SQLDB;
import SCP079.Objects.ICommand;
import SCP079.Objects.getSteamID;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
