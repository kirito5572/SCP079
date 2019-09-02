package SCP079.Commands;

import SCP079.App;
import SCP079.Objects.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class imforCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String serverID = event.getGuild().getId();
        String greenServer = "600010501266866186";
        String greenServerChat = "617908612064346122";
        String simaServer = "582091661266386944";
        String dokdoServer = "581835684986486785";
        String snoServer = "570659322007126029";
        String gariaServer = "585437712639590423";
        String gariaServerChat = "617973738582966292";
        String carDogeServer = "609985979167670272";
        String carDogeServerChat = "617938587102478337";
        String tlServer = "551022729441312779";
        String tlServerChat = "617924927944785931";
        String koreanServer = "607542356866236416";
        String koreanServerChat = "607548370843860994";
        if(!(event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getMember().hasPermission(Permission.MANAGE_ROLES) ||
                event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || event.getMember().hasPermission(Permission.MANAGE_CHANNEL) ||
                event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS))) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.").queue();

            return;
        }

        if(args.isEmpty()) {
            event.getChannel().sendMessage("인수 부족 '" + App.getPREFIX() + "명령어" +
                    getInvoke() + "'").queue();
            return;
        }
        String SteamID;
        StringBuilder reason;
        try {
            SteamID = args.get(0);

        } catch (Exception e) {
            event.getChannel().sendMessage("Steam ID가 입력되지 않았거나, 인수가 잘못 입력되었습니다.").queue();

            return;
        }
        reason = new StringBuilder();
        try {
            for(int i = 1; i <= args.size(); i++) {
                reason.append(args.get(i));
            }
        } catch (Exception e) {
            if(reason.toString().equals("")) {
                event.getChannel().sendMessage("사유가 입력되지 않았습니다.").queue();

                return;
            }
        }
        String reasonFinal = String.join(" ", reason.toString());
        final String[] temp = new String[1];
        WebUtils.ins.scrapeWebPage("https://steamid.io/lookup/" + SteamID).async((document1 -> {
            String a1 = document1.getElementsByTag("body").first().toString();
            String a2 = a1;
            try {
                int b2 = a2.indexOf("data-clipboard-text=\"");
                int b1 = a1.indexOf(" <dt class=\"key\">\n" +
                        "       name");
                a1 = a1.substring(b1 + 75);
                a2 = a2.substring(b2 + 21);
                b2 = a2.indexOf("data-clipboard-text=\"");
                a2 = a2.substring(b2 + 21);
                b2 = a2.indexOf("data-clipboard-text=\"");
                a2 = a2.substring(b2 + 21);
                int c1 = a1.indexOf("</dd>");
                int c2 = a2.indexOf(" src=");
                a1 = a1.substring(0, c1 - 7);
                a2 = a2.substring(0, c2 - 1);
                System.out.println(a1);
                System.out.println(a2);
                temp[0] = a1;
            } catch (Exception e) {
                e.printStackTrace();
                event.getChannel().sendMessage("봇이 스팀 프로필을 불러오는데 실패하였습니다.").queue();

                return;
            }
            String NickName;
            String ID;
            ID = a2;

            NickName = temp[0];

            NickName = NickName.replace(" ", "");
            NickName= NickName.replaceAll("\\p{Z}","");

            EmbedBuilder builder = EmbedUtils.defaultEmbed()
                    .setTitle("공유된 제재 정보")
                    .setColor(Color.RED)
                    .addField("제재 대상자", NickName, false)
                    .addField("스팀 ID", ID, false)
                    .addField("제재 사유", reasonFinal, false)
                    .addField("제재 담당 서버", event.getGuild().getName(), false)
                    .addField("공유자", event.getMember().getAsMention(), false);

            HackCommand.server_Send(serverID, builder, event);

        }));
    }

    @Override
    public String getHelp() {
        return "SCP 한국 서버들간 제재 정보 공유를 위한 커맨드입니다. \n" +
                "사용법: `" + App.getPREFIX() + getInvoke() + " <Steam ID> <사유> `";
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
