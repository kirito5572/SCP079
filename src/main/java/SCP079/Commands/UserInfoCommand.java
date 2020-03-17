package SCP079.Commands;

import SCP079.App;
import SCP079.Objects.ICommand;
import SCP079.Objects.IsKoreaSCPCoop;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserInfoCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(IsKoreaSCPCoop.verification(event)) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.\n" +
                    " 이 명령어를 사용하기 위해서는 한코옵서버에서 서버장 또는 관리자 역할을 받아야 합니다.\n" +
                    " 한코옵 링크: discord.gg/6JxCx72").complete().delete().queueAfter(7, TimeUnit.SECONDS);
            return;
        }
        User user;
        Member member;
        Guild guildinfo = null;
        if(args.isEmpty()) {
            user = event.getMember().getUser();
            member = event.getMember();
        } else {
            String joined = String.join(" ", args);
            try {
                boolean bypass = false;
                List<Member> foundMember = null;
                List<Guild> guilds = event.getJDA().getGuilds();
                for (Guild guild : guilds) {
                    if(!bypass) {
                        foundMember = FinderUtil.findMembers(joined, guild);
                        if (!foundMember.isEmpty()) {
                            bypass = true;
                            guildinfo = guild;
                        }
                    }
                }
                if(foundMember == null) {
                    event.getChannel().sendMessage("'" + joined + "' 라는 유저는 없습니다.").queue();
                    return;
                }
                if(foundMember.isEmpty()) {
                    event.getChannel().sendMessage("'" + joined + "' 라는 유저는 없습니다.").queue();
                    return;
                }

                user = foundMember.get(0).getUser();
                member = foundMember.get(0);

            } catch (Exception e) {
                event.getChannel().sendMessage("해당 유저를 찾을수 없거나, 인수가 잘못 입력되었습니다.").queue();

                return;
            }
        }
        StringBuilder serverRole = new StringBuilder();
        List<Role> role = member.getRoles();
        for (Role value : role) {
            serverRole.append(value.getAsMention()).append("\n");
        }

        assert guildinfo != null;
        MessageEmbed embed = EmbedUtils.defaultEmbed()
                .setColor(member.getColor())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .addField("유저이름#번호", String.format("%#s", user), false)
                .addField("서버 표시 이름", member.getEffectiveName(), false)
                .addField("유저 ID + 언급 멘션", String.format("%s (%s)", user.getId(), member.getAsMention()), false)
                .addField("디스코드 가입 일자", member.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())), false)
                .addField("서버 초대 일자", member.getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())), false)
                .addField("서버 부여 역할", serverRole.toString(), false)
                .addField("온라인 상태", member.getOnlineStatus().name().toLowerCase().replaceAll("_", " "), false)
                .addField("봇 여부", user.isBot() ? "예" : "아니요", false)
                .addField("검색 된 서버", guildinfo.getName(), false)
                .build();

        event.getChannel().sendMessage(embed).queue();

    }

    @Override
    public String getHelp() {
        return "유저정보 알기! \n" +
                "사용법: `" + App.getPREFIX() + getInvoke() + " [유저 이름/@유저/유저 id] `";
    }

    @Override
    public String getInvoke() {
        return "유저정보";
    }

    @Override
    public String getSmallHelp() {
        return "디스코드 유저정보를 검색.";
    }
}
