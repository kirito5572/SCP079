package SCP079.Commands;

import SCP079.App;
import SCP079.Listener.Listener;
import SCP079.Listener.SQLDB;
import SCP079.Objects.ICommand;
import SCP079.Objects.getSteamID;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class TestCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!event.getAuthor().getId().equals(Listener.getID1())) {
            event.getChannel().sendMessage("이 명령어는 개발자의 테스트 커맨드입니다.").queue();

            return;
        }
        String serverID = event.getGuild().getId();

        if(!(event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getMember().hasPermission(Permission.MANAGE_ROLES) ||
                event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || event.getMember().hasPermission(Permission.MANAGE_CHANNEL) ||
                event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS))) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.").queue();

            return;
        }
        TextChannel channel = event.getChannel();

        if(args.isEmpty()) {
            event.getChannel().sendMessage("인수 부족 '" + App.getPREFIX() + "명령어" +
                    getInvoke() + "'").queue();
            return;
        }
        String SteamID;
        String time, rawtime;
        StringBuilder reason;

        try {
            SteamID = args.get(0);

        } catch (Exception e) {
            channel.sendMessage("Steam ID가 입력되지 않았거나, 인수가 잘못 입력되었습니다.").queue();

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
            time = time.substring(0,time.indexOf("m"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf(temp1);

        } else if(time.contains("h")) {
            time = time.substring(0,time.indexOf("h"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf((temp1 * 60));


        } else if(time.contains("d")) {
            time = time.substring(0,time.indexOf("d"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf((temp1 * 1440));

        } else if(time.contains("M")) {
            time = time.substring(0,time.indexOf("M"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf((temp1 * 43830));

        } else if(time.contains("y")) {
            time = time.substring(0,time.indexOf("y"));
            try {
                temp1 = Integer.parseInt(time);
            } catch (Exception e) {
                channel.sendMessage("시간의 숫자가 잘못 입력되었습니다.").queue();

                return;
            }
            time = String.valueOf((temp1 * 525960));
        } else if(time.contains("영구")) {
            time = "26297460";
        } else {
            channel.sendMessage("시간 단위가 틀렸거나, 스팀 닉네임의 띄워쓰기를 삭제하여 주세요.\n" +
                    "사용법: `" + App.getPREFIX() + "명령어/도움말/help" +getInvoke() + "`").queue();

            return;
        }
        reason = new StringBuilder();
        try {
            for(int i = 2; i <= args.size(); i++) {
                reason.append(args.get(i)).append(" ");
            }
        } catch (Exception e) {
            if(reason.toString().equals("")) {
                event.getChannel().sendMessage("사유가 입력되지 않았습니다.").queue();

                return;
            }
        }
        String reasonFinal = String.join(" ", reason.toString());

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

        NickName = NickName.replace(" ", "");
        NickName= NickName.replaceAll("\\p{Z}","");

        SQLDB.SQLupload(ID, rawtime + "(" + time + "분)", reasonFinal, event.getGuild().getName(), serverID);

        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("공유된 제재 정보")
                .setColor(Color.RED)
                .addField("제재 대상자", NickName, false)
                .addField("스팀 ID", ID, false)
                .addField("제재 사유", reasonFinal, false)
                .addField("제재 기간", rawtime + "(" + time + "분)", false)
                .addField("제재 담당 서버", event.getGuild().getName(), false)
                .addField("공유자", event.getMember().getAsMention(), false);
        if(returns[2].equals("nosteam")) {
            builder.addField("중요", "이 유저는 스팀 프로필을 등록한 적 없는 유저입니다.", false);
        }
    }

    @Override
    public String getHelp() {
        return "테스트";
    }

    @Override
    public String getInvoke() {
        return "테스트";
    }

    @Override
    public String getSmallHelp() {
        return "테스트";
    }
}
