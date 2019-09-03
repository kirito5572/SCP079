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

public class UnbanCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String serverID;
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
        String SteamID;
        String ipAddress;
        String level;
        try {
            SteamID = args.get(0);

        } catch (Exception e) {
            event.getChannel().sendMessage("Steam ID가 입력되지 않았거나, 인수가 잘못 입력되었습니다.").queue();

            return;
        }
        try {
            ipAddress = args.get(1);

        } catch (Exception e) {
            event.getChannel().sendMessage("ip 주소가 입력되지 않았거나, 인수가 잘못 입력되었습니다.").queue();

            return;
        }
        StringBuilder levelBuilder = new StringBuilder();
        try {
            for(int i = 2; i <= args.size(); i++) {
                levelBuilder.append(args.get(i));
            }
        } catch (Exception e) {
            if(levelBuilder.toString().equals("")) {
                event.getChannel().sendMessage("제재 감소 기간이 입력되지 않았습니다.").queue();

                return;
            }
        }
        level = levelBuilder.toString();

        final String[] temp = new String[1];
        String finalLevel = level;
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
                    .setTitle("공유된 제재 해제 정보")
                    .setColor(Color.GREEN)
                    .addField("제재 대상자", NickName + "(" + ipAddress + ")", false)
                    .addField("스팀 ID", ID, false)
                    .addField("제재 해제 사유", "이의 제기가 받아드려 제재 해제/기간 감소", false)
                    .addField("제재 감소 기간", finalLevel, false)
                    .addField("제재 담당 서버", event.getGuild().getName(), false)
                    .addField("공유자", event.getMember().getAsMention(), false);

            HackCommand.server_Send(serverID, builder, event);

        }));
    }

    @Override
    public String getHelp() {
        return "SCP 한국 서버들간 제재 해제 정보 공유를 위한 커맨드입니다. \n" +
                "사용법: `" + App.getPREFIX() + getInvoke() + " <Steam ID> <ip주소> <의심 정도>`";
    }

    @Override
    public String getInvoke() {
        return "이의";
    }

    @Override
    public String getSmallHelp() {
        return "SCP 서버간 제재 해제 정보 공유";
    }
}
