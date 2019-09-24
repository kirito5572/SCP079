package SCP079.Commands;

import SCP079.App;
import SCP079.Listener.SQLDB;
import SCP079.Objects.ICommand;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBSearchCommand implements ICommand {
    private static String[][] data;
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!(event.getMember().hasPermission(Permission.MANAGE_CHANNEL) || event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS) ||
                event.getMember().hasPermission(Permission.MANAGE_ROLES) || event.getMember().hasPermission(Permission.MANAGE_SERVER) ||
                event.getMember().hasPermission(Permission.MESSAGE_MANAGE))) {

            event.getChannel().sendMessage("당신은 이 명령어를 사용할 수 없습니다.").queue();
            return;
        }
        try {
            data = SQLDB.SQLdownload(args.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
            event.getChannel().sendMessage("해당 스팀 ID 검색 결과가 없습니다.").queue();
        }
        int i = 0;
        try {
            if (args.get(1).equals("1")) {
                try {
                    i = Integer.parseInt(args.get(1)) - 1;
                } catch (Exception e) {
                    event.getChannel().sendMessage("숫자를 입력해주세요").queue();
                }
            }
            if(data[i][0] == null) {
                event.getChannel().sendMessage(App.getPREFIX() + getInvoke() + args.get(0) + "를 입력해주세요").queue();
            }
        } catch (Exception ignored) {

        }
        boolean flag = false;
        if(i < 4) {
            if (data[i + 1][0] != null) {
                flag = true;
            }
        }
        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("검색된 제재 정보")
                .addField("caseID", data[i][0], false)
                .addField("스팀 ID", data[i][1], false)
                .addField("DB 기록 시간", data[i][2], false)
                .addField("제재 기간", data[i][3], false)
                .addField("이유", data[i][4], false)
                .addField("등록 서버", data[i][5], false)
                .addField("서버 ID", data[i][6], false);
        if(flag) {
            builder.appendDescription(App.getPREFIX() + " " + getInvoke() + " " + args.get(0) + " " + i + 1);
        }

        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "DB에서 제재 정보를 검색합니다. \n" +
                "사용법: " + App.getPREFIX() + getInvoke() + " <SteamID> ";
    }

    @Override
    public String getInvoke() {
        return "검색";
    }

    @Override
    public String getSmallHelp() {
        return "제재 정보 검색";
    }
}