package me.kirito5572.scp079.command;

import me.kirito5572.scp079.object.ICommand;
import me.kirito5572.scp079.object.SQLDB;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ConnectServerCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("접속된 서버 목록");

        List<Guild> server = event.getJDA().getGuilds();

        for (Guild guild : server) {
            boolean config_complete = false;
            try {
                Statement statement = SQLDB.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM `079_config`.recieve_channel WHERE guildId=" + guild.getId());
                if(resultSet.next()) {
                    config_complete = true;
                }
                statement.close();
            } catch (Exception e) {
                SQLDB.reConnect();
                event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                e.printStackTrace();
                return;
            }
            builder.addField(guild.getName(), (config_complete) ? "설정 완료, 제재 공유 수신중" : "설정 미 완료, 제재 공유 미 수신중", false);
        }

        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "연결되어 있는 서버 목록을 출력합니다.";
    }

    @Override
    public String getInvoke() {
        return "연결서버";
    }

    @Override
    public String getSmallHelp() {
        return "연결된 서버 목록";
    }
}
