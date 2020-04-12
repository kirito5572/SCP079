package me.kirito5572.scp079.command;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.kirito5572.scp079.object.ObjectPool;
import me.kirito5572.scp079.object.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class HackCommand {
    public void server_Send(String serverID, EmbedBuilder builder, JDA jda, TextChannel sendChannel, int servertime) {
        int guildCount = jda.getGuilds().size();
        String[] guildId = new String[guildCount];
        String[] channelId = new String[guildCount];
        String[] time = new String[guildCount];
        int i = 0;
        try {
            String queryString = "SELECT * FROM `079_config`.recieve_channel";

            Statement statement = ObjectPool.get(SQLDB.class).getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                guildId[i] = resultSet.getString("guildId");
                channelId[i] = resultSet.getString("channelId");
                i++;
            }
            queryString = "SELECT * FROM `079_config`.receive_time";
            resultSet = statement.executeQuery(queryString);
            i = 0;
            while (resultSet.next()) {
                time[i] = resultSet.getString("time");
                i++;
            }
            statement.close();
            for (i = 0; i < guildId.length; i++) {
                if (guildId[i] == null) {
                    continue;
                }
                if (serverID.equals(guildId[i])) {
                    continue;
                }
                if (servertime < Integer.parseInt(time[i])) {
                    continue;
                }
                Guild guild;
                TextChannel channel;
                try {
                    guild = jda.getGuildById(guildId[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendChannel.sendMessage("에러\n" +
                            "사유: JDA ERROR").queue();
                    continue;
                }
                try {
                    assert guild != null;
                    channel = guild.getTextChannelById(channelId[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendChannel.sendMessage("에러\n" +
                            "채널 ID명: " + guildId[i] + " 사유: 서버가 존재하지 않음\n" +
                            "발생한 서버:" + guild.getName()).queue();
                    continue;
                }
                try {
                    assert channel != null;
                    channel.sendMessage(builder.build()).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                    sendChannel.sendMessage("에러\n" +
                            "채널 ID명: " + guildId[i] + " 사유: 서버에 채널이 존재하지 않음\n" +
                            "발생한 서버:" + guild.getName()).queue();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder stringBuilder = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                stringBuilder.append(stackTraceElement.toString()).append("\n");
            }
            try {
                EmbedBuilder embedBuilder = EmbedUtils.defaultEmbed()
                        .setTitle("에러 발생")
                        .setDescription("제재 정보를 전송하던중 에러가 발생했습니다.")
                        .addField("발생한 예외", e.getMessage(), false)
                        .addField("StackTrace", stringBuilder.toString(), false);
                sendChannel.sendMessage(embedBuilder.build()).complete();
                Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById("607303213602963643")).getTextChannelById("651387107662626846")).sendMessage(embedBuilder.build()).complete();
                return;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        sendChannel.sendMessage("전송이 완료되었습니다.").queue();
    }

    public void test(EmbedBuilder builder, GuildMessageReceivedEvent event) {
        Objects.requireNonNull(event.getJDA().getGuilds().get(0).getTextChannelById("593991995433680924")).sendMessage(builder.build()).queue();
    }
}
