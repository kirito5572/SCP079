package me.kirito5572.scp079.command;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.kirito5572.scp079.object.ICommand;
import me.kirito5572.scp079.object.IsKoreaSCPCoop;
import me.kirito5572.scp079.object.SQLDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConfigCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (IsKoreaSCPCoop.verification(event)) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.\n" +
                    " 이 명령어를 사용하기 위해서는 한코옵서버에서 서버장 또는 관리자 역할을 받아야 합니다.\n" +
                    " 한코옵 링크: discord.gg/6JxCx72").complete().delete().queueAfter(7, TimeUnit.SECONDS);
            return;
        }
        EmbedBuilder builder;
        if (args.isEmpty()) {
            builder = EmbedUtils.defaultEmbed()
                    .setTitle("설정 사용법")
                    .addField("현재", "현재 등록된 옵션값을 불러옵니다.", false)
                    .addField("채널", "제재가 수신되는 채널 관련 옵션을 조정합니다.", false)
                    .addField("시간", "수신 받을 최소 제재 시간 관련 옵션을 조정합니다.", false)
                    .addField("등록", "현재 서버를 등록합니다.", false)
                    .addField("종료", "등록한 서버의 정보를 삭제합니다.", false)
                    .addField("봇채팅", "봇 채팅방을 등록합니다.", false)
                    .addField("필터링", "필터링 시스템 관련 기능을 설정합니다.", false);
        } else {
            switch (args.get(0)) {
                case "채널":
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
                case "시간":
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
                case "등록":
                    try {
                        Statement statement = SQLDB.getConnection().createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM `079_config`.recieve_channel WHERE guildId=" + event.getGuild().getId());
                        if (resultSet.next()) {
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
                                    "등록된 정보의 조회/파기에 관련된 문의는 봇의 DM을 통하여 문의주시기 바랍니다.", false);
                    try {
                        Statement statement = SQLDB.getConnection().createStatement();
                        statement.executeUpdate("INSERT INTO `079_config`.recieve_channel VALUES (" + event.getGuild().getId() + ", " + event.getChannel().getId() + ");");
                        statement.executeUpdate("INSERT INTO `079_config`.receive_time VALUES (" + event.getGuild().getId() + ", 0);");
                        statement.executeUpdate("INSERT INTO `079_config`.bot_channel VALUES (" + event.getGuild().getId() + ", 0);");
                        statement.executeUpdate("INSERT INTO `079_config`.filter_enable VALUES (" + event.getGuild().getId() + ", 0, 0, 0, 0);");
                        statement.executeUpdate("INSERT INTO `079_config`.filter_warning VALUES (" + event.getGuild().getId() + ", 0, 0);");
                        statement.close();
                        event.getChannel().sendMessage("기본 등록이 완료되었습니다.").queue();
                    } catch (Exception e) {
                        SQLDB.reConnect();
                        event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "현재":
                    String channelId = "에러 발생";
                    String time = "기본값(0분 이상)";
                    try {
                        Statement statement = SQLDB.getConnection().createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM `079_config`.recieve_channel WHERE guildId=" + event.getGuild().getId() + ";");
                        if (resultSet.next()) {
                            channelId = resultSet.getString("channelId");
                        } else {
                            channelId = "없음";
                        }
                        resultSet = statement.executeQuery("SELECT * FROM `079_config`.receive_time WHERE guildId=" + event.getGuild().getId() + ";");
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
                case "종료":
                    builder = EmbedUtils.defaultEmbed()
                            .setTitle("사용 종료에 따른 처리 사항")
                            .setDescription("기존의 등록된 제재 정보는 파기 되지 않습니다.\n" +
                                    "기존에 등록되었던 제재 정보의 파기를 원하실 경우, DM으로 파기 요청 부탁드립니다.");
                    try {
                        Statement statement = SQLDB.getConnection().createStatement();
                        statement.executeUpdate("DELETE FROM `079_config`.recieve_channel WHERE guildId=" + event.getGuild().getId() + ";");
                        statement.executeUpdate("DELETE FROM `079_config`.receive_time WHERE guildId=" + event.getGuild().getId() + ";");
                        statement.executeUpdate("DELETE FROM `079_config`.bot_channel WHERE guildId=" + event.getGuild().getId() + ";");
                        statement.executeUpdate("DELETE FROM `079_config`.filter_enable WHERE guildId=" + event.getGuild().getId() + ";");
                        statement.close();
                        event.getChannel().sendMessage("사용 해제 작업 완료되었습니다.").queue();
                    } catch (Exception e) {
                        SQLDB.reConnect();
                        event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "봇채팅":
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
                case "필터링":
                    if (args.size() > 1) {
                        switch (args.get(1)) {
                            case "차단활성화":
                                int enable = 0;
                                if (args.size() < 3) {
                                    event.getChannel().sendMessage("인수가 부족합니다.").queue();
                                    return;
                                }
                                if (args.get(2).equals("활성화")) {
                                    enable = 1;
                                } else if (args.get(2).equals("비활성화")) {
                                } else {
                                    event.getChannel().sendMessage("활성화/비활성화 조건이여야 합니다.\n" +
                                            "입력된 조건:" + args.get(2)).queue();
                                }
                                try {
                                    Statement statement = SQLDB.getConnection().createStatement();
                                    statement.executeUpdate("UPDATE `079_config`.filter_enable SET enable=" + enable + " WHERE guildId=" + event.getGuild().getId() + ";");
                                } catch (Exception e) {
                                    SQLDB.reConnect();
                                    event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                                    e.printStackTrace();
                                    return;
                                }
                                break;
                            case "차단채널알림": {
                                String executeString;
                                if (args.size() < 3) {
                                    event.getChannel().sendMessage("인수가 부족합니다.").queue();
                                    return;
                                }
                                if (args.get(2).equals("0")) {
                                    executeString = "UPDATE `079_config`.filter_enable SET channelId=" + 0 + " WHERE guildId=" + event.getGuild().getId() + ";";
                                } else if (args.get(2).equals("현재채널")) {
                                    executeString = "UPDATE `079_config`.filter_enable SET channelId=" + event.getChannel().getId() + " WHERE guildId=" + event.getGuild().getId() + ";";
                                } else {
                                    List<TextChannel> textChannelList = FinderUtil.findTextChannels(args.get(2), event.getJDA());
                                    if (textChannelList.isEmpty()) {
                                        event.getChannel().sendMessage("해당 채널을 검색 할 수 없습니다.").queue();
                                        return;
                                    } else {
                                        executeString = "UPDATE `079_config`.filter_enable SET channelId=" + textChannelList.get(0).getId() + " WHERE guildId=" + event.getGuild().getId() + ";";
                                    }
                                }
                                try {
                                    Statement statement = SQLDB.getConnection().createStatement();
                                    statement.executeUpdate(executeString);
                                    statement.close();
                                } catch (Exception e) {
                                    SQLDB.reConnect();
                                    event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                                    e.printStackTrace();
                                    return;
                                }
                                break;
                            }
                            case "뮤트": {
                                int count;
                                if (args.size() < 3) {
                                    event.getChannel().sendMessage("인수가 부족합니다.").queue();
                                    return;
                                }
                                try {
                                    count = Integer.parseInt(args.get(2));
                                } catch (Exception e) {
                                    event.getChannel().sendMessage("횟수에 숫자가 아닌게 입력되었습니다.").queue();
                                    return;
                                }
                                String executeString;
                                if (count == 0) {
                                    executeString = "UPDATE `079_config`.filter_enable SET mute=0, muterole=0 WHERE guildId=" + event.getGuild().getId() + ";";
                                } else {
                                    if (args.size() < 4) {
                                        event.getChannel().sendMessage("인수가 부족합니다.").queue();
                                        return;
                                    }
                                    List<Role> roles = FinderUtil.findRoles(args.get(3), event.getGuild());
                                    if (roles.isEmpty()) {
                                        event.getChannel().sendMessage("서버에서 역할을 찾을수 없습니다.").queue();
                                        return;
                                    }
                                    executeString = "UPDATE `079_config`.filter_enable SET mute=" + count + ", muterole= " + roles.get(0).getId() + " WHERE guildId=" + event.getGuild().getId() + ";";
                                }
                                try {
                                    Statement statement = SQLDB.getConnection().createStatement();
                                    statement.executeUpdate(executeString);
                                    statement.close();
                                } catch (Exception e) {
                                    SQLDB.reConnect();
                                    event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                                    e.printStackTrace();
                                    return;
                                }
                                break;
                            }
                            case "킥": {
                                int count;
                                if (args.size() < 3) {
                                    event.getChannel().sendMessage("인수가 부족합니다.").queue();
                                    return;
                                }
                                try {
                                    count = Integer.parseInt(args.get(2));
                                } catch (Exception e) {
                                    event.getChannel().sendMessage("횟수에 숫자가 아닌게 입력되었습니다.").queue();
                                    return;
                                }
                                String executeString;
                                if (count == 0) {
                                    executeString = "UPDATE `079_config`.filter_enable SET kick=0 WHERE guildId=" + event.getGuild().getId() + ";";
                                } else {
                                    executeString = "UPDATE `079_config`.filter_enable SET kick=" + count + " WHERE guildId=" + event.getGuild().getId() + ";";
                                }
                                try {
                                    Statement statement = SQLDB.getConnection().createStatement();
                                    statement.executeUpdate(executeString);
                                    statement.close();
                                } catch (Exception e) {
                                    SQLDB.reConnect();
                                    event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                                    e.printStackTrace();
                                    return;
                                }
                                break;
                            }
                            case "밴": {
                                int count;
                                if (args.size() < 3) {
                                    event.getChannel().sendMessage("인수가 부족합니다.").queue();
                                    return;
                                }
                                try {
                                    count = Integer.parseInt(args.get(2));
                                } catch (Exception e) {
                                    event.getChannel().sendMessage("횟수에 숫자가 아닌게 입력되었습니다.").queue();
                                    return;
                                }
                                String executeString;
                                if (count == 0) {
                                    executeString = "UPDATE `079_config`.filter_enable SET ban=0 WHERE guildId=" + event.getGuild().getId() + ";";
                                } else {
                                    executeString = "UPDATE `079_config`.filter_enable SET ban=" + count + " WHERE guildId=" + event.getGuild().getId() + ";";
                                }
                                try {
                                    Statement statement = SQLDB.getConnection().createStatement();
                                    statement.executeUpdate(executeString);
                                    statement.close();
                                } catch (Exception e) {
                                    SQLDB.reConnect();
                                    event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                                    e.printStackTrace();
                                    return;
                                }
                                break;
                            }
                            default:
                                event.getChannel().sendMessage("그런 설정 항목은 존재 하지 않습니다.\n" +
                                        "`$$설정 필터링` 을 참고해주세요").queue();
                                break;
                        }
                        return;
                    } else {
                        builder = EmbedUtils.defaultEmbed()
                                .setTitle("필터링 설정 부가 항목")
                                .setDescription("차단채널알림, 오토뮤트, 오토킥, 오토밴은 횟수가 0으로 설정된 경우 자동으로 비활성화 됩니다.")
                                .addField("차단활성화", "필터링 차단을 활성화/비활성화 할지 결정합니다 \n" +
                                        "$$설정 필터링 차단활성화 <활성화/비활성화>", false)
                                .addField("차단채널알림", "필터링 차단시 채팅으로 알려줍니다\n" +
                                        "$$설정 필터링 차단채널알림 <0/채널ID/#멘션/채널명/현재채널>", false)
                                .addField("뮤트", "한 문장에 지정된 횟수 이상의 욕설이 감지된 경우 자동으로 뮤트합니다.\n" +
                                        "$$설정 필터링 오토뮤트 <횟수> <역할ID>", false)
                                .addField("킥", "한 문장에 지정된 횟수 이상의 욕설이 감지된 경우 자동으로 서버에서 킥합니다.\n" +
                                        "$$설정 필터링 오토킥 <횟수>", false)
                                .addField("밴", "한 문장에 지정된 횟수 이상의 욕설이 감지된 경우 자동으로 서버에서 밴합니다.\n" +
                                        "$$설정 필터링 오토밴 <횟수>", false);
                    }
                    break;
                default:
                    return;
            }
        }
        event.getChannel().sendMessage(builder.build()).queue();
    }

    public void config_Reload() {
        try {
            Statement statement = SQLDB.getConnection().createStatement();
        } catch (Exception e) {
            SQLDB.reConnect();
            config_Reload();
        }
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
