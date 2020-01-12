package SCP079.Commands;

import SCP079.Objects.ICommand;
import SCP079.Objects.SQLDB;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class configCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            if(!event.getAuthor().getId().equals("284508374924787713")) {
                event.getChannel().sendMessage("당신은 이 명령어를 사용할 수 없습니다.").queue();
                return;
            }
        }
        EmbedBuilder builder;
        if(args.isEmpty()) {
            builder = EmbedUtils.defaultEmbed()
                    .setTitle("설정 사용법")
                    .addField("-현재", "현재 등록된 옵션값을 불러옵니다.", false)
                    .addField("-채널", "제재가 수신되는 채널 관련 옵션을 조정합니다.", false)
                    .addField("-시간", "수신 받을 최소 제재 시간 관련 옵션을 조정합니다.", false)
                    .addField("-등록", "현재 서버를 등록합니다.", false)
                    .addField("-종료", "등록한 서버의 정보를 삭제합니다.", false)
                    .addField("-봇채팅", "봇 채팅방을 등록합니다.", false);
        } else {
            switch (args.get(0)) {
                case "-채널":
                    if (args.size() > 1) {
                        try {
                            Statement statement = SQLDB.getConnection().createStatement();
                            statement.executeUpdate("UPDATE `079_config`.recieve_channel SET channelId =" + args.get(1) + " WHERE guildId=" + event.getGuild().getId());
                            statement.close();
                            event.getChannel().sendMessage("채널 ID 변경이 완료되었습니다.").queue();
                            return;
                        } catch (Exception e) {
                            SQLDB.reConnect();
                            event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        event.getChannel().sendMessage("채널 ID를 입력해주세요").queue();
                        return;
                    }
                case "-시간":
                    if (args.size() > 1) {
                        try {
                            Statement statement = SQLDB.getConnection().createStatement();
                            statement.executeUpdate("UPDATE `079_config`.receive_time SET time =" + args.get(1) + " WHERE guildId=" + event.getGuild().getId());
                            statement.close();
                            event.getChannel().sendMessage("제재 수신 최소 시간값 변경 완료되었습니다.").queue();
                            return;
                        } catch (Exception e) {
                            SQLDB.reConnect();
                            event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        event.getChannel().sendMessage("몇분인지 입력해주세요").queue();
                        return;
                    }
                case "-등록":
                    try {
                        Statement statement = SQLDB.getConnection().createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM `079_config`.recieve_channel WHERE guildId=" + event.getGuild().getId());
                        if(resultSet.next()) {
                            event.getChannel().sendMessage("이미 등록되었습니다.").queue();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    builder = EmbedUtils.defaultEmbed()
                            .setTitle("다음과 같은 정보를 수집, 처리 합니다.")
                            .setDescription("본 메세지를 확인함으로써에 대하여 사용을 시작하는것으로 확인하고, 등록을 시작합니다.\n" +
                                    "사용중지는 -종료를 통하여 해주시기 바랍니다.")
                            .addField("다음과 같은 정보를 일시적으로 수집합니다.",
                                    "1. 제재자의 이름, IP\n" +
                                            "2. 명령어 실행자의 이름, 디스코드 ID\n" +
                                            "3. 명령어 실행시 입력한 증거\n" +
                                            "4. 명령어 실행시 입력한 신고 게시글\n" +
                                            "5. 제재 공유 처리를 위하여 필요한 기타 개인정보", false)
                            .addField("다음과 같은 정보를 수집하여, Amazon Web Service의 RDS 서비스를 통하여 저장합니다.",
                                    "1. 제재자의 스팀 ID\n" +
                                            "2. 제재 기록 일시\n" +
                                            "3. 제재 기간\n" +
                                            "4. 제재 사유\n" +
                                            "5. 명령어가 실행된 서버의 이름과 ID", false)
                            .addField("등록된 정보의 조회/파기",
                                    "등록된 정보의 조회/파기에 관련된 문의는 봇의 DM을 통하여 문의주시기 바랍니다.",false);
                    try {
                        Statement statement = SQLDB.getConnection().createStatement();
                        statement.executeUpdate("INSERT INTO `079_config`.recieve_channel VALUES (" + event.getGuild().getId() + ", "+ event.getChannel().getId() + ")");
                        statement.executeUpdate("INSERT INTO `079_config`.receive_time VALUES (" + event.getGuild().getId() + ", 0)");
                        statement.executeUpdate("INSERT INTO `079_config`.bot_channel VALUES (" + event.getGuild().getId() + ", 0)");
                        statement.close();
                        event.getChannel().sendMessage("기본 등록이 완료되었습니다.").queue();
                    } catch (Exception e) {
                        SQLDB.reConnect();
                        event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "-현재":
                    String channelId = "에러 발생";
                    String time = "기본값(0분 이상)";
                    try {
                        Statement statement = SQLDB.getConnection().createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM `079_config`.recieve_channel WHERE guildId=" + event.getGuild().getId());
                        if (resultSet.next()) {
                            channelId = resultSet.getString("channelId");
                        } else {
                            channelId = "없음";
                        }
                        resultSet = statement.executeQuery("SELECT * FROM `079_config`.receive_time WHERE guildId=" + event.getGuild().getId());
                        if (resultSet.next()) {
                            time = resultSet.getString("time");
                        } else {
                            time = "없음";
                        }
                        statement.close();
                    } catch (Exception e) {
                        SQLDB.reConnect();
                        event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                        e.printStackTrace();
                        return;
                    }
                    builder = EmbedUtils.defaultEmbed()
                            .setTitle(event.getGuild().getName() + " 서버의 설정값")
                            .addField("제재 등록 수신 채널", channelId, false)
                            .addField("제재 수신 최소 시간", time, false);

                    break;
                case "-종료":
                    builder = EmbedUtils.defaultEmbed()
                            .setTitle("사용 종료에 따른 처리 사항")
                            .setDescription("기존의 등록된 제재 정보는 파기 되지 않습니다.\n" +
                                    "기존에 등록되었던 제재 정보의 파기를 원하실 경우, DM으로 파기 요청 부탁드립니다.");
                    try {
                        Statement statement = SQLDB.getConnection().createStatement();
                        statement.executeUpdate("DELETE FROM `079_config`.recieve_channel WHERE guildId=" + event.getGuild().getId());
                        statement.executeUpdate("DELETE FROM `079_config`.receive_time WHERE guildId=" + event.getGuild().getId());
                        statement.executeUpdate("DELETE FROM `079_config`.bot_channel WHERE guildId=" + event.getGuild().getId());
                        statement.close();
                        event.getChannel().sendMessage("사용 해제 작업 완료되었습니다.").queue();
                    } catch (Exception e) {
                        SQLDB.reConnect();
                        event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "-봇채팅":
                    if (args.size() > 1) {
                        try {
                            Statement statement = SQLDB.getConnection().createStatement();
                            statement.executeUpdate("UPDATE `079_config`.bot_channel SET channelId =" + args.get(1) + " WHERE guildId=" + event.getGuild().getId());
                            statement.close();
                            event.getChannel().sendMessage("봇 채팅방 업데이트가 완료되었습니다..").queue();
                            return;
                        } catch (Exception e) {
                            SQLDB.reConnect();
                            event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        event.getChannel().sendMessage("봇 채팅방 채널 ID를 제대로 입력해주세요").queue();
                        return;
                    }
                default:
                    return;
            }
        }
        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "설정을 합니다.";
    }

    @Override
    public String getInvoke() {
        return "설정";
    }

    @Override
    public String getSmallHelp() {
        return "설정하는 커맨드";
    }
}
