package me.kirito5572.scp079.command;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.kirito5572.scp079.App;
import me.kirito5572.scp079.object.GetSteamID;
import me.kirito5572.scp079.object.ICommand;
import me.kirito5572.scp079.object.IsKoreaSCPCoop;
import me.kirito5572.scp079.object.ObjectPool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TransCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (ObjectPool.get(IsKoreaSCPCoop.class).verification(event)) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.\n" +
                    " 이 명령어를 사용하기 위해서는 한코옵서버에서 서버장 또는 관리자 역할을 받아야 합니다.\n" +
                    " 한코옵 링크: discord.gg/6JxCx72").complete().delete().queueAfter(7, TimeUnit.SECONDS);
            return;
        }
        TextChannel channel = event.getChannel();

        if (args.isEmpty()) {
            event.getChannel().sendMessage("인수 부족 " + App.getInstance().getPREFIX() + "명령어`" +
                    getInvoke() + "`").queue();
            return;
        }
        boolean isSteam= false;
        String ID;
        String nickname = null;
        String time;
        String reason;
        try {
            ID = args.get(0);
            boolean isrealSteamID = true;
            if (ID.startsWith("76") && ID.length() == 17) {
                for (int i = 0; i < ID.length(); i++) {
                    char temp = ID.charAt(i);
                    if (!Character.isDigit(temp)) {
                        isrealSteamID = false;
                    }
                    isSteam = true;
                }
                if (!isrealSteamID) {
                    channel.sendMessage("스팀 ID가 올바르지 않습니다. 다시 확인해주세요.").queue();
                    return;
                }
            } else {
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
                    }
                    if (foundMember == null) {
                        event.getChannel().sendMessage("'" + ID + "' 로 검색되는 ID를 봇이 검색 할 수 없습니다.").queue();
                        return;
                    }
                    if (foundMember.isEmpty()) {
                        event.getChannel().sendMessage("'" + ID + "' 로 검색되는 ID를 봇이 검색 할 수 없습니다.").queue();
                        return;
                    }
                    nickname = foundMember.get(0).getUser().getName();
                    ID = foundMember.get(0).getId();

                } catch (Exception e) {
                    event.getChannel().sendMessage("해당 유저를 찾을수 없거나, 인수가 잘못 입력되었습니다.").queue();

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
        String[] returns = new String[0];
        if (isSteam) {
            returns = ObjectPool.get(GetSteamID.class).SteamID(ID);

            if (returns[0].equals("error")) {
                event.getChannel().sendMessage("스팀 ID 수신중 에러가 발생했습니다.").queue();

                return;
            }
            if (returns[0].equals("no")) {
                event.getChannel().sendMessage("그런 ID는 존재 하지 않습니다.").queue();

                return;
            }
        }
        int temp1;
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
                    "사용법: `" + App.getInstance().getPREFIX() + "명령어/도움말/help" + getInvoke() + "`").queue();

            return;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 2; i < args.size(); i++) {
                stringBuilder.append(args.get(i)).append(" ");
            }
            reason = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            reason = "";
        }
        String startTime = String.valueOf((new Date().getTime() - 32400L) * 10000L + 621355968000000000L);
        String endTime = String.valueOf((new Date().getTime() + Long.parseLong(time) * 60000L) * 10000L + 621355968000000000L);
        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("변환 결과");
        try {
            if (isSteam)
                builder.setDescription(returns[0] + ";" + ID + "@steam;" + endTime + ";" + reason + ";"
                        + event.getAuthor().getName() + ";" + startTime + ";");
            else
                builder.setDescription(nickname + ";" + ID + "@discord;" + endTime + ";" + reason + ";"
                        + event.getAuthor().getName() + ";" + startTime + ";");
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("에러 발생으로 변환 불가, <@284508374924787713>를 불러주세요").queue();
            return;
        }
        event.getChannel().sendMessage(builder.build()).queue();


    }

    @Override
    public String getHelp() {
        return "사용법: `" + App.getInstance().getPREFIX() + getInvoke() + " <SteamID/DiscordId/@멘션/디스코드유저명> <기간> (사유)`" +
                "**<필수항목>** **(필요없는 항목)**";
    }

    @Override
    public String getInvoke() {
        return "변환";
    }

    @Override
    public String getSmallHelp() {
        return "SL 밴 양식에 맞도록 변환해줍니다.";
    }
}
