package SCP079.Commands;

import SCP079.App;
import SCP079.Objects.ICommand;
import SCP079.Objects.getSteamID;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class HackCommand implements ICommand {
    private String serverID;
    private static String starhaServer = "576823770329907201";

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
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
        StringBuilder temp = new StringBuilder();
        try {
            for(int i = 2; i < args.size(); i++) {
                temp.append(args.get(i)).append(" ");
            }
            level = temp.toString();
        } catch (Exception e) {
            event.getChannel().sendMessage("의심 등급이 입력되지 않았거나, 인수가 잘못 입력되었습니다.").queue();

            return;
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

        String NickName = returns[0];
        String ID = returns[1];


        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("공유된 제재 정보")
                .setColor(Color.RED)
                .addField("제재 대상자", NickName + "(" + ipAddress + ")", false)
                .addField("스팀 ID", ID, false)
                .addField("제재 사유", "핵 사용자", false)
                .addField("의심 등급", level, false)
                .addField("제재 담당 서버", event.getGuild().getName(), false)
                .addField("공유자", event.getMember().getAsMention(), false);
        if(returns[2].equals("nosteam")) {
            builder.addField("중요", "이 유저는 스팀 프로필을 등록한 적 없는 유저입니다.", false);
        }

        HackCommand.simaAutoSend(serverID, NickName, ID, "26297460", "핵 사용", event);
        server_Send(serverID, builder, event, false);

    }

    @Override
    public String getHelp() {
        return "SCP 한국 서버들간 핵 정보 공유를 위한 커맨드입니다. \n" +
                "사용법: `" + App.getPREFIX() + getInvoke() + " <Steam ID> <ip주소> <의심 정도>`";
    }

    @Override
    public String getInvoke() {
        return "핵";
    }

    @Override
    public String getSmallHelp() {
        return "SCP 서버간 핵 유저 공유";
    }

    public static void server_Send(String serverID, EmbedBuilder builder, GuildMessageReceivedEvent event, boolean simaSend) {
        try {
            String greenServer = "600010501266866186";
            if(!serverID.equals(greenServer)) {
                String greenServerChat = "617908612064346122";
                event.getJDA().getGuildById(greenServer).getTextChannelById(greenServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            
            event.getChannel().sendMessage("그린서버 전송 실패").queue();
        }
        /*try {
            String tlServer = "551022729441312779";
            if(!serverID.equals(tlServer)) {
                String tlServerChat = "617924927944785931";
                event.getJDA().getGuildById(tlServer).getTextChannelById(tlServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("TL서버 전송 실패").queue();
        }*/
        try {
            String carDogeServer = "609985979167670272";
            if(!serverID.equals(carDogeServer)) {
                String carDogeServerChat = "617938587102478337";
                event.getJDA().getGuildById(carDogeServer).getTextChannelById(carDogeServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("Doge서버 전송 실패").queue();
        }
        try {
            String koreanServer = "607542356866236416";
            if(!serverID.equals(koreanServer)) {
                String koreanServerChat = "607548370843860994";
                event.getJDA().getGuildById(koreanServer).getTextChannelById(koreanServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("한국서버 전송 실패").queue();
        }
        try {
            String gariaServer = "585437712639590423";
            if(!serverID.equals(gariaServer)) {
                String gariaServerChat = "617973738582966292";
                event.getJDA().getGuildById(gariaServer).getTextChannelById(gariaServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("가리아서버 전송 실패").queue();
        }
        try {
            String simaServer = "582091661266386944";
            if(!serverID.equals(simaServer)) {
                String simaServerChat = "595597485238648833";
                String simaServerChat2 = "598126633588883457";
                event.getJDA().getGuildById(simaServer).getTextChannelById(simaServerChat).sendMessage(builder.build()).queue();
                event.getJDA().getGuildById(simaServer).getTextChannelById(simaServerChat2).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("시마서버 전송 실패").queue();
        }
        try {
            String dokdoServer = "581835684986486785";
            if(!serverID.equals(dokdoServer)) {
                String dokdoServerChat = "618411407742074880";
                event.getJDA().getGuildById(dokdoServer).getTextChannelById(dokdoServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("독도서버 전송 실패").queue();
        }
        try {
            String snoServer = "570659322007126029";
            if(!serverID.equals(snoServer)) {
                String snoServerChat = "620125504246382592";
                event.getJDA().getGuildById(snoServer).getTextChannelById(snoServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("스노서버 전송 실패").queue();
        }
        try {
            String horyuServer = "563045452774244361";
            if(!serverID.equals(horyuServer)) {
                String horyuServerChat = "622691793661853706";
                event.getJDA().getGuildById(horyuServer).getTextChannelById(horyuServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("호류서버 전송 실패").queue();
        }
        try {
            String SNJServer = "531777289684254731";
            if(!serverID.equals(SNJServer)) {
                String SNJServerChat = "623105335514759168";
                event.getJDA().getGuildById(SNJServer).getTextChannelById(SNJServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("SNJ서버 전송 실패").queue();
        }

        event.getChannel().sendMessage("전송이 완료되었습니다.").queue();
    }
    public static void simaAutoSend(String serverID, String Nickname, String ID, String time, String reason, GuildMessageReceivedEvent event) {
        try {
            String simaServer = "582091661266386944";
            if(!serverID.equals(simaServer)) {
                String simaServerChat = "595597485238648833";
                String simaServerChat2 = "598126633588883457";
                String simaAutoBanChat = "595597485238648833";
                event.getJDA().getGuildById(simaServer).getTextChannelById(simaAutoBanChat).sendMessage("+oban " + Nickname + " " + ID + " " + time).queue();
                event.getJDA().getGuildById(simaServer).getTextChannelById(simaServerChat).sendMessage(Nickname + "(" + ID + ") 가 " + time + "분의 제재가 수신되어 자동 처리 되었습니다.\n" +
                        "사유: " + reason).queue();
                event.getJDA().getGuildById(simaServer).getTextChannelById(simaServerChat2).sendMessage(Nickname + "(" + ID + ") 가 " + time + "분의 제재가 수신되어 자동 처리 되었습니다.\n" +
                        "사유: " + reason).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("시마서버 전송 실패").queue();
        }
    }
}
