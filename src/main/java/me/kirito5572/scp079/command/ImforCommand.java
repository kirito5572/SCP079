package me.kirito5572.scp079.command;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.kirito5572.scp079.App;
import me.kirito5572.scp079.object.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static me.kirito5572.scp079.command.HackCommand.test;

public class ImforCommand implements ICommand {
    static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String serverID = event.getGuild().getId();

        if (IsKoreaSCPCoop.verification(event)) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.\n" +
                    " 이 명령어를 사용하기 위해서는 한코옵서버에서 서버장 또는 관리자 역할을 받아야 합니다.\n" +
                    " 한코옵 링크: discord.gg/6JxCx72").complete().delete().queueAfter(7, TimeUnit.SECONDS);
            return;
        }
        TextChannel channel = event.getChannel();

        if (args.isEmpty()) {
            event.getChannel().sendMessage("인수 부족 " + App.getPREFIX() + "명령어`" +
                    getInvoke() + "`").queue();
            return;
        }
        String ID;
        String steamId = null;
        String discordId;
        boolean isSteam = false;
        Member member = null;
        String time, rawtime;
        StringBuilder reason;
        String ip = null;
        String chattingId = null;
        String link = null;

        try {
            ID = args.get(0);
            boolean isrealSteamID = true;
            System.out.println(ID.length());
            if (ID.startsWith("76") && ID.length() == 17) {
                for (int i = 0; i < ID.length(); i++) {
                    char temp = ID.charAt(i);
                    if (!Character.isDigit(temp)) {
                        isrealSteamID = false;
                    }
                    isSteam = true;
                    steamId = ID;
                }
                if (!isrealSteamID) {
                    channel.sendMessage("스팀 ID가 올바르지 않습니다. 다시 확인해주세요.").queue();
                    return;
                }
            } else {
                if (ID.length() == 18) {
                    try {
                        boolean bypass = false;
                        List<Member> foundMember = null;
                        List<Guild> guilds = event.getJDA().getGuilds();
                        for (Guild guild : guilds) {
                            if (!bypass) {
                                foundMember = FinderUtil.findMembers(ID, guild);
                                if (!foundMember.isEmpty()) {
                                    bypass = true;
                                }
                            }
                            discordId = ID;
                        }
                        if (foundMember == null) {
                            event.getChannel().sendMessage("'" + ID + "' 로 검색되는 ID를 봇이 검색 할 수 없습니다.").queue();
                            return;
                        }
                        if (foundMember.isEmpty()) {
                            event.getChannel().sendMessage("'" + ID + "' 로 검색되는 ID를 봇이 검색 할 수 없습니다.").queue();
                            return;
                        }
                        member = foundMember.get(0);

                    } catch (Exception e) {
                        event.getChannel().sendMessage("해당 유저를 찾을수 없거나, 인수가 잘못 입력되었습니다.").queue();

                        return;
                    }
                } else {
                    event.getChannel().sendMessage("입력된 ID를 확인하여주십시오.").queue();
                    return;
                }
            }

        } catch (Exception e) {
            channel.sendMessage("Steam ID/Discord ID가 입력되지 않았거나, 인수가 잘못 입력되었습니다.").queue();

            return;
        }
        try {
            time = args.get(1);

        } catch (Exception e) {
            channel.sendMessage("시간이 입력되지 않았거나, 인수가 잘못 입력되었습니다.").queue();

            return;
        }
        int temp1;
        rawtime = time;
        if (time.contains("m")) {
            time = time.substring(0, time.indexOf("m"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf(temp1);

        } else if (time.contains("h")) {
            time = time.substring(0, time.indexOf("h"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf((temp1 * 60));


        } else if (time.contains("d")) {
            time = time.substring(0, time.indexOf("d"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf((temp1 * 1440));

        } else if (time.contains("M")) {
            time = time.substring(0, time.indexOf("M"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf((temp1 * 43830));

        } else if (time.contains("y")) {
            time = time.substring(0, time.indexOf("y"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf((temp1 * 525960));
        } else if (time.contains("영구")) {
            time = "26297460";
        } else {
            channel.sendMessage("시간 단위가 틀렸거나, 스팀 닉네임의 띄워쓰기를 삭제하여 주세요.\n" +
                    "사용법: `" + App.getPREFIX() + "명령어/도움말/help" + getInvoke() + "`").queue();

            return;
        }
        reason = new StringBuilder();
        boolean reasonFlag = true;
        boolean ipFlag = false;
        System.out.println(args.size());
        try {
            for (int i = 2; i < args.size(); i++) {
                if (args.get(i).startsWith("-ip")) {
                    reasonFlag = false;
                    if (i + 1 != args.size()) {
                        ipFlag = true;
                        ip = args.get(i + 1);
                    } else {
                        event.getChannel().sendMessage("ip가 비었습니다.").queue();
                        return;
                    }
                }
                if (args.get(i).startsWith("-chat")) {
                    reasonFlag = false;
                    if (args.get(i + 1) != null) {
                        chattingId = args.get(i + 1);
                    } else {
                        event.getChannel().sendMessage("채팅 ID가 비었습니다.").queue();
                        return;
                    }
                }
                if (args.get(i).startsWith("-link")) {
                    reasonFlag = false;
                    if (args.get(i + 1) != null) {
                        link = args.get(i + 1);
                    } else {
                        event.getChannel().sendMessage("링크가 없습니다.").queue();
                        return;
                    }
                }
                if (reasonFlag) {
                    reason.append(args.get(i)).append(" ");
                }
            }
        } catch (Exception e) {
            if (reason.toString().equals("")) {
                event.getChannel().sendMessage("사유가 입력되지 않았습니다.").queue();

                return;
            }
            e.printStackTrace();
        }
        String reasonFinal = String.join(" ", reason.toString());
        String[] returns = new String[0];
        if (isSteam) {
            returns = GetSteamID.SteamID(steamId);

            if (returns[0].equals("error")) {
                event.getChannel().sendMessage("스팀 ID 수신중 에러가 발생했습니다.").queue();

                return;
            }
            if (returns[0].equals("no")) {
                event.getChannel().sendMessage("그런 ID는 존재 하지 않습니다.").queue();

                return;
            }
        }
        if (ipFlag) {
            if (!validIP(ip)) {
                event.getChannel().sendMessage("존재하지 않는 IP주소가 입력되었습니다.").queue();
                return;
            }
        }
        if (link != null) {
            if (!linkConfirm.isLink(link)) {
                event.getChannel().sendMessage("해당 링크는 없는 링크인것 같습니다. 접속할 수 없습니다.").queue();

                return;
            }
        }
        String NickName;
        if (isSteam) {
            NickName = returns[0];
        } else {
            NickName = member.getEffectiveName();
        }

        NickName = NickName.replace(" ", "");
        NickName = NickName.replaceAll("\\p{Z}", "");

        SQLDB.SQLupload(ID, rawtime + "(" + time + "분)", reasonFinal, event.getGuild().getName(), serverID);

        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("공유된 제재 정보")
                .setColor(Color.RED)
                .addField("제재 대상자", NickName, false);
        if (isSteam) {
            builder.addField("스팀 ID", ID, false);
        } else {
            builder.addField("디스코드 ID", ID, false);
        }
        builder.addField("제재 사유", reasonFinal, false)
                .addField("제재 기간", rawtime + "(" + time + "분)", false)
                .addField("제재 담당 서버", event.getGuild().getName(), false)
                .addField("공유자", event.getMember().getAsMention(), false);
        if (isSteam) {
            if (returns[2].equals("nosteam")) {
                builder.addField("중요", "이 유저는 스팀 프로필을 등록한 적 없는 유저입니다.", false);
            }
        }
        if (ip != null) {
            builder.addField("IP", ip, false);
        }
        if (link != null) {
            builder.addField("증거 자료(외부 링크)", "[외부 링크 이동](" + link + ")", false);
        }
        if (chattingId != null) {
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
                    if (message != null) {
                        builder.addField("신고 내역", "[메세지 이동](" + message.getJumpUrl() + ")", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (error) {
                event.getChannel().sendMessage("유효하지 않은 채널 ID가 입력되었습니다.").queue();
                return;
            }
        }
        if (App.isTESTMODE()) {
            test(builder, event);
        } else {

            HackCommand.server_Send(serverID, builder, event.getJDA(), event.getChannel(), Integer.parseInt(time));
        }

    }

    @Override
    public String getHelp() {
        return "SCP 한국 서버들간 제재 정보 공유를 위한 커맨드입니다. \n" +
                "사용법: `" + App.getPREFIX() + getInvoke() + " <Steam ID> <제재 기간> <사유> `\n" +
                "제재기한 : m(min)/h(hour)/d(day)/M(month)/y(year)/영구(50y)\n" +
                "<추가 옵션>\n" +
                "-ip <ip>\n" +
                "-chat <디스코드 채팅 ID>\n" +
                "-link <URL>";
    }

    @Override
    public String getInvoke() {
        return "정보";
    }

    @Override
    public String getSmallHelp() {
        return "SCP 서버간 제재자 공유";
    }
}
