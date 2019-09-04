package SCP079.Commands;

import SCP079.Objects.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ConnectServerCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("연결된 서버 목록");

        List<Guild> server = event.getJDA().getGuilds();

        for (Guild guild : server) {
            builder.addField(guild.getName(), "", false);
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
