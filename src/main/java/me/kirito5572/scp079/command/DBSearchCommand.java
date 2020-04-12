package me.kirito5572.scp079.command;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.kirito5572.scp079.App;
import me.kirito5572.scp079.object.ObjectPool;
import me.kirito5572.scp079.object.GetHoryuSearch;
import me.kirito5572.scp079.object.GetSteamID;
import me.kirito5572.scp079.object.ICommand;
import me.kirito5572.scp079.object.IsKoreaSCPCoop;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DBSearchCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (ObjectPool.get(IsKoreaSCPCoop.class).verification(event)) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.\n" +
                    " 이 명령어를 사용하기 위해서는 한코옵서버에서 서버장 또는 관리자 역할을 받아야 합니다.\n" +
                    " 한코옵 링크: discord.gg/6JxCx72").complete().delete().queueAfter(7, TimeUnit.SECONDS);
            return;
        }

        //Validate Steam ID
        if (args.size() == 0) {
            event.getChannel().sendMessage("SteamID를 입력해주세요.").queue();
            return;
        }
        String[] SteamData = ObjectPool.get(GetSteamID.class).SteamID(args.get(0));

        if (SteamData[0].equals("no")) {
            event.getChannel().sendMessage("스팀 ID가 올바르지 않습니다.").queue();
        }
        String[] data;
        boolean flag = false;
        if (args.size() > 1) {
            if (args.get(1).startsWith("-c")) {
                if (args.size() == 2) {
                    event.getChannel().sendMessage("뒤에 Case ID를 붙혀주세요 ").queue();

                    return;
                }
                data = ObjectPool.get(GetHoryuSearch.class).Search(args.get(0), args.get(2));
                flag = true;

            } else {
                try {
                    data = ObjectPool.get(GetHoryuSearch.class).Search(args.get(0));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    event.getChannel().sendMessage("에러가 발생했습니다.").queue();

                    return;
                }
            }
        } else {
            try {
                data = ObjectPool.get(GetHoryuSearch.class).Search(args.get(0));
                if (data[0].equals("error")) {
                    event.getChannel().sendMessage("에러가 발생했습니다.").queue();
                    return;
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                event.getChannel().sendMessage("에러가 발생했습니다.").queue();

                return;
            }
        }
        if (data[0] == null) {
            event.getChannel().sendMessage("제재 기록이 없습니다.").queue();

            return;
        }
        EmbedBuilder builder = EmbedUtils.defaultEmbed();
        if (flag) {
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
            for (int i = 0; i < 10; i++) {
                if (data[i] != null) {
                    j++;
                    stringBuilder.append(data[i]).append(",");
                }
            }
            builder.setTitle("검색된 제재 정보")
                    .addField("SteamID", args.get(0), false)
                    .addField("검색된 제재 건수", String.valueOf(j), false)
                    .addField("CaseID", stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1), false);
        }

        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "DB에서 제재 정보를 검색합니다. \n" +
                "사용법: " + App.getInstance().getPREFIX() + getInvoke() + " <SteamID> ";
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
