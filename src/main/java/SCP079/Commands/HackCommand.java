package SCP079.Commands;

import SCP079.App;
import SCP079.Objects.ICommand;
import SCP079.Objects.SQLDB;
import SCP079.Objects.getSteamID;
import SCP079.Objects.linkConfirm;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import static SCP079.Commands.imforCommand.validIP;

public class HackCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!(Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || event.getMember().hasPermission(Permission.MANAGE_ROLES) ||
                event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || event.getMember().hasPermission(Permission.MANAGE_CHANNEL) ||
                event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS) || event.getMember().hasPermission(Permission.MANAGE_SERVER))) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.").queue();

            return;
        }

        String serverID = event.getGuild().getId();

        if(args.isEmpty()) {
            event.getChannel().sendMessage("인수 부족 '" + App.getPREFIX() + "명령어" +
                    getInvoke() + "'").queue();
            return;
        }
        String SteamID;
        try {
            SteamID = args.get(0);

        } catch (Exception e) {
            event.getChannel().sendMessage("Steam ID가 입력되지 않았거나, 인수가 잘못 입력되었습니다.").queue();

            return;
        }
        String ip = null, chattingId = null, link = null, rank = null;

        try {
            for(int i = 1; i < args.size(); i++) {
                if(args.get(i).startsWith("-ip")) {
                    if(i + 1 != args.size()) {
                        ip = args.get(i + 1);
                    } else {
                        event.getChannel().sendMessage("ip가 비었습니다.").queue();
                        return;
                    }
                }
                if(args.get(i).startsWith("-chat")) {
                    if(args.get(i + 1) != null) {
                        chattingId = args.get(i + 1);
                    } else {
                        event.getChannel().sendMessage("채팅 ID가 비었습니다.").queue();
                        return;
                    }
                }
                if(args.get(i).startsWith("-link")) {
                    if(args.get(i + 1) != null) {
                        link = args.get(i + 1);
                    } else {
                        event.getChannel().sendMessage("링크가 없습니다.").queue();
                        return;
                    }
                }
                if(args.get(i).startsWith("-rank")) {
                    if(args.get(i + 1) != null) {
                        rank = args.get(i + 1);
                    } else {
                        event.getChannel().sendMessage("의심등급이 입력되지 않았습니다.").queue();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String[] returns = getSteamID.SteamID(SteamID);

        if(returns[0].equals("error")) {
            event.getChannel().sendMessage("스팀 ID 수신중 에러가 발생했습니다.").queue();

            return;
        }
        if (returns[0].equals("no")) {
            event.getChannel().sendMessage("그런 ID는 존재 하지 않습니다.").queue();

            return;
        }
        if(ip != null) {
            if (!validIP(ip)) {
                event.getChannel().sendMessage("존재하지 않는 IP주소가 입력되었습니다.").queue();
                return;
            }
        }
        if(link != null) {
            if (!linkConfirm.isLink(link)) {
                event.getChannel().sendMessage("해당 링크는 없는 링크인것 같습니다. 접속할 수 없습니다.").queue();

                return;
            }
        }

        String NickName = returns[0];
        String ID = returns[1];

        SQLDB.SQLupload(ID, "영구", "핵 사용자", event.getGuild().getName(), serverID);

        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("공유된 제재 정보")
                .setColor(Color.RED);
        if(ip != null) {
            builder.addField("제재 대상자", NickName + "(" + ip + ")", false);
        } else {
            builder.addField("제재 대상자", NickName, false);
        }
        builder.addField("스팀 ID", ID, false);
        if(rank != null) {
            builder.addField("의심 등급", rank, false);
        }
        if(link != null) {
            builder.addField("증거 링크", "[링크 이동](" + link + ")", false);
        }
        if(chattingId != null) {
            List<TextChannel> textChannels = event.getGuild().getTextChannels();
            boolean error = true;
            for (TextChannel textChannel : textChannels) {
                Message message;
                try {
                    message = textChannel.retrieveMessageById(chattingId).complete();
                    error = false;
                } catch (IllegalArgumentException e) {
                    event.getChannel().sendMessage("채팅 ID 양식에 유효 하지 않은 입력이 감지 되었습니다.").queue();
                    return;
                } catch (ErrorResponseException e1) {
                    continue;
                }
                try {
                    if(message != null) {
                        builder.addField("신고 내역", "[메세지 이동](" + message.getJumpUrl() + ")",false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(error) {
                event.getChannel().sendMessage("유효하지 않은 채널 ID가 입력되었습니다.").queue();
                return;
            }
        }
        builder.addField("제재 사유", "핵 사용자", false)
                .addField("제재 담당 서버", event.getGuild().getName(), false)
                .addField("공유자", event.getMember().getAsMention(), false);
        if(returns[2].equals("nosteam")) {
            builder.addField("중요", "이 유저는 스팀 프로필을 등록한 적 없는 유저입니다.", false);
        }
        if(App.isTESTMODE()) {
            test(builder, event);
        } else {
            server_Send(serverID, builder, event.getJDA(), event.getChannel(),99999999);
        }
    }

    @Override
    public String getHelp() {
        return "SCP 한국 서버들간 핵 정보 공유를 위한 커맨드입니다. \n" +
                "사용법: `" + App.getPREFIX() + getInvoke() + " <Steam ID> `\n" +
                "<추가 옵션>\n" +
                "-ip <ip>\n" +
                "-chat <디스코드 채팅 ID>\n" +
                "-link <URL>" +
                "-rank <의심등급>";
    }

    @Override
    public String getInvoke() {
        return "핵";
    }

    @Override
    public String getSmallHelp() {
        return "SCP 서버간 핵 유저 공유";
    }


    public static void server_Send(String serverID, EmbedBuilder builder, JDA jda, TextChannel sendChannel, int servertime) {
        int guildCount = jda.getGuilds().size();
        String[] guildId = new String[guildCount];
        String[] channelId = new String[guildCount];
        String[] time = new String[guildCount];
        int i = 0;
        try {
            String queryString = "SELECT * FROM `079_config`.recieve_channel";

            Statement statement = SQLDB.getConnection().createStatement();
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
            for(i = 0; i < guildId.length; i++) {
                if(guildId[i] == null) {
                    continue;
                }
                if(serverID.equals(guildId[i])) {
                    continue;
                }
                if(servertime < Integer.parseInt(time[i])) {
                    continue;
                }
                Guild guild;
                TextChannel channel;
                try {
                    guild = jda.getGuildById(guildId[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendChannel.sendMessage("에러\n" +
                            "서버 ID명: " + guildId[i] + " 사유: 그런 서버가 존재하지 않음").queue();
                    continue;
                }
                try {
                    assert guild != null;
                    channel = guild.getTextChannelById(channelId[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendChannel.sendMessage("에러\n" +
                            "채널 ID명: " + guildId[i] + " 사유: 서버에 채널이 존재하지 않음\n" +
                            "발생한 서버:" + guild.getName()).queue();
                    continue;
                }
                try {
                    assert channel != null;
                    channel.sendMessage(builder.build()).queue();
                } catch (Exception e) {
                    e.printStackTrace();
                    sendChannel.sendMessage("에러\n" +
                            "채널명: " + channel.getName() + " 사유: 채널은 존재하나 메세지를 보낼수 없음\n" +
                            "발생한 서버:" + guild.getName()).queue();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder stringBuilder = new StringBuilder();
            for(StackTraceElement stackTraceElement : e.getStackTrace()) {
                stringBuilder.append(stackTraceElement.toString()).append("\n");
            }
            EmbedBuilder embedBuilder = EmbedUtils.defaultEmbed()
                    .setTitle("에러 발생")
                    .setDescription("제재 정보를 전송하던중 에러가 발생했습니다.")
                    .addField("발생한 예외", e.getMessage(), false)
                    .addField("StackTrace", stringBuilder.toString(), false);
            sendChannel.sendMessage(embedBuilder.build()).complete();
            Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById("607303213602963643")).getTextChannelById("651387107662626846")).sendMessage(embedBuilder.build()).complete();
            return;
        }
        sendChannel.sendMessage("전송이 완료되었습니다.").queue();
    }

    /*


    public static void simaAutoSend(String serverID, String Nickname, String ID, String time, String reason, JDA jda) {
        try {
            String simaServer = "582091661266386944";
            String simaServerChat = "595597485238648833";
            String simaServerChat2 = "598126633588883457";
            String simaAutoBanChat = "595597485238648833";
            Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(simaServer)).getTextChannelById(simaAutoBanChat)).sendMessage("+oban " + Nickname + " " + ID + " " + time).queue();
            Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(simaServer)).getTextChannelById(simaServerChat)).sendMessage(Nickname + "(" + ID + ") 가 " + time + "분의 제재가 수신되어 자동 처리 되었습니다.\n" +
                        "사유: " + reason + "\n" +
                    "수신된 서버: " + Objects.requireNonNull(jda.getGuildById(serverID)).getName()).queue();
            Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(simaServer)).getTextChannelById(simaServerChat2)).sendMessage(Nickname + "(" + ID + ") 가 " + time + "분의 제재가 수신되어 자동 처리 되었습니다.\n" +
                    "사유: " + reason + "\n" +
                    "수신된 서버: " + Objects.requireNonNull(jda.getGuildById(serverID)).getName()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */
    public static void test(EmbedBuilder builder, GuildMessageReceivedEvent event) {
        Objects.requireNonNull(event.getJDA().getGuilds().get(0).getTextChannelById("593991995433680924")).sendMessage(builder.build()).queue();
    }
}
