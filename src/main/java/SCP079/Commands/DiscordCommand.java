package SCP079.Commands;

import SCP079.App;
import SCP079.Objects.ICommand;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

import static SCP079.Commands.HackCommand.server_Send;

public class DiscordCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String serverID;
        StringBuilder reason = new StringBuilder();
        String ID;
        String NickName;
        String Mantion;

        if(!(event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getMember().hasPermission(Permission.MANAGE_ROLES) ||
                event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || event.getMember().hasPermission(Permission.MANAGE_CHANNEL) ||
                event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS))) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.").queue();

            return;
        }

        serverID = event.getGuild().getId();

        if(args.isEmpty()) {
            event.getChannel().sendMessage("인수 부족 '" + App.getPREFIX() + "명령어" +
                    getInvoke() + "'").queue();
            return;
        }
        try {
            boolean bypass = false;
            List<Member> foundMember = null;
            List<Guild> guilds = event.getJDA().getGuilds();
            for (Guild guild : guilds) {
                if(!bypass) {
                    foundMember = FinderUtil.findMembers(args.get(0), guild);
                    if (!foundMember.isEmpty()) {
                        bypass = true;
                    }
                }
            }
            if(foundMember == null) {
                event.getChannel().sendMessage("'" + args.get(0) + "' 라는 유저는 없습니다.").queue();
                return;
            }
            if(foundMember.isEmpty()) {
                event.getChannel().sendMessage("'" + args.get(0) + "' 라는 유저는 없습니다.").queue();
                return;
            }

            User user = foundMember.get(0).getUser();
            ID = user.getId();
            NickName = user.getName();
            Member member = foundMember.get(0);
            Mantion = member.getAsMention();

        } catch (Exception e) {
            event.getChannel().sendMessage("해당 유저를 찾을수 없거나, 인수가 잘못 입력되었습니다.").queue();

            return;
        }
        try {
            for(int i = 1; i <= args.size(); i++) {
                reason.append(args.get(i)).append(" ");
            }
        } catch (Exception e) {
            if(reason.toString().equals("")) {
                event.getChannel().sendMessage("사유가 입력되지 않았습니다.").queue();

                return;
            }
        }
        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("공유된 디스코드 제재 정보")
                .setColor(Color.RED)
                .addField("제재 대상자", NickName + "(" + Mantion + ")", false)
                .addField("디스코드 ID", ID, false)
                .addField("제재 사유", reason.toString(), false)
                .addField("제재 담당 서버", event.getGuild().getName(), false)
                .addField("공유자", event.getMember().getAsMention(), false);

        server_Send(serverID, builder, event, true);
    }

    @Override
    public String getHelp() {
        return "SCP 한국 서버들간 디스코드 제재 정보 공유를 위한 커맨드입니다. \n" +
                "사용법: `" + App.getPREFIX() + getInvoke() + " <Discord ID/@멘션/유저명> <사유>`";
    }

    @Override
    public String getInvoke() {
        return "디스코드";
    }

    @Override
    public String getSmallHelp() {
        return "디스코드 제재 정보 공유";
    }
}
