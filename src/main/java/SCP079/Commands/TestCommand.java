package SCP079.Commands;

import SCP079.App;
import SCP079.Listener.Listener;
import SCP079.Objects.SQLDB;
import SCP079.Objects.ICommand;
import SCP079.Objects.getSteamID;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TestCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String[][] data;
        try {
            data = SQLDB.SQLdownload(args.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
            event.getChannel().sendMessage("에러가 발생했습니다.").queue();

            return;
        }
        System.out.println(data[0][0]);
        if(data[0][0] == null) {
            System.out.println("null 값");
        }
        if(data[0][0].equals("")) {
            System.out.println("\"\"값");
        }
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
