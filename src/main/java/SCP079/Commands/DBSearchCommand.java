package SCP079.Commands;

import SCP079.App;
import SCP079.Objects.ICommand;
import SCP079.Objects.SQLDB;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static SCP079.Objects.getSteamID.SteamID;

public class DBSearchCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!(Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_CHANNEL) || event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS) ||
                event.getMember().hasPermission(Permission.MANAGE_ROLES) || event.getMember().hasPermission(Permission.MANAGE_SERVER) ||
                event.getMember().hasPermission(Permission.MESSAGE_MANAGE))) {

            event.getChannel().sendMessage("당신은 이 명령어를 사용할 수 없습니다.").queue();
            return;
        }
        //Validate Steam ID
        String[] SteamData = SteamID(args.get(0));
        if(SteamData[0].equals("no")) {
            event.getChannel().sendMessage("스팀 ID가 올바르지 않습니다.").queue();
        }
        String[] data;
        boolean flag = false;
        if(args.get(2) != null) {
            if(args.get(2).startsWith("-c")) {
                try {
                    data = SQLDB.SQLdownload(Integer.parseInt(args.get(3)));
                    flag = true;
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    event.getChannel().sendMessage("에러가 발생했습니다.").queue();

                    return;
                }
            } else {
                try {
                    data = SQLDB.SQLdownload(args.get(0));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    event.getChannel().sendMessage("에러가 발생했습니다.").queue();

                    return;
                }
            }
        } else {
            try {
                data = SQLDB.SQLdownload(args.get(0));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                event.getChannel().sendMessage("에러가 발생했습니다.").queue();

                return;
            }
        }
        if(data[0] == null) {
            event.getChannel().sendMessage("제재 기록이 없습니다.").queue();

            return;
        }
        EmbedBuilder builder = EmbedUtils.defaultEmbed();
        if(flag) {
            builder.setTitle("검색된 제재 정보")
                    .addField("caseID", data[0], false)
                    .addField("스팀 ID", data[1], false)
                    .addField("DB 기록 시간", data[2], false)
                    .addField("제재 기간", data[3], false)
                    .addField("이유", data[4], false)
                    .addField("등록 서버", data[5], false)
                    .addField("서버 ID", data[6], false);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            int j = 0;
            for(int i = 0; i < 10; i++) {
                if(data[i] != null) {
                    j++;
                    stringBuilder.append(data[i]).append(",");
                }
            }
            builder.setTitle("검색된 제재 정보")
                    .addField("SteamID", args.get(0), false)
                    .addField("검색된 제재 건수", String.valueOf(j), false)
                    .addField("CaseID", stringBuilder.toString().substring(0, stringBuilder.toString().length() - 2), false);
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
