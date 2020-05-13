package me.kirito5572.scp079.command;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.kirito5572.scp079.App;
import me.kirito5572.scp079.object.ICommand;
import me.kirito5572.scp079.object.IsKoreaSCPCoop;
import me.kirito5572.scp079.object.ObjectPool;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SteamUserInfoCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (ObjectPool.get(IsKoreaSCPCoop.class).verification(event)) {
            event.getChannel().sendMessage("당신은 이 명령어를 쓸 권한이 없습니다.\n" +
                    " 이 명령어를 사용하기 위해서는 한코옵서버에서 서버장 또는 관리자 역할을 받아야 합니다.\n" +
                    " 한코옵 링크: discord.gg/6JxCx72").complete().delete().queueAfter(7, TimeUnit.SECONDS);
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessage("인수 부족 " + App.getInstance().getPREFIX() + "명령어`" +
                    getInvoke() + "`").queue();
            return;
        }

        String steamId;
        String steamId64;
        String steamID3;
        String customURL;
        String profileState;
        String profileCreated;
        String nickname;
        String location;
        String status;
        String profileLink;

        try {
            Document doc = Jsoup.connect("https://steamid.io/lookup/" + args.get(0)).get();
            Element mainElement = doc.getElementsByTag("body").first();
            mainElement = mainElement.getElementsByClass("container").first().getElementsByClass("row").get(3);
            mainElement = mainElement.getElementsByClass("col-md-6").first().getElementById("content");
            mainElement = mainElement.getElementsByClass("panel-body").first();

            steamId = mainElement.getElementsByClass("value short").get(0).getElementsByAttribute("rel").get(0).text();
            steamID3 = mainElement.getElementsByClass("value short").get(1).getElementsByAttribute("rel").get(0).text();
            steamId64 = mainElement.getElementsByClass("value short").get(2).getElementsByAttribute("href").get(0).text();
            customURL = mainElement.getElementsByClass("value short").get(3).getElementsByAttribute("href").get(0).text();
            profileState = mainElement.getElementsByClass("value").get(4).getElementsByClass("profile-state public").get(0).text();
            profileCreated = mainElement.getElementsByClass("value short").get(4).text();
            nickname = mainElement.getElementsByClass("value").get(6).text();
            location = mainElement.getElementsByClass("value").get(7).getElementsByAttribute("href").get(0).text();
            status = mainElement.getElementsByClass("value").get(8).getElementsByClass("online").get(0).text();
            profileLink = mainElement.getElementsByClass("value").get(9).getElementsByAttribute("href").text();

            EmbedBuilder builder = EmbedUtils.defaultEmbed()
                    .setTitle(args.get(0) + "로 조회한 기록")
                    .addField("닉네임", nickname, false)
                    .addField("스팀 ID 64", steamId64, false)
                    .addField("스팀 프로필 생성일", profileCreated, false)
                    .addField("커스텀 URL", customURL, false)
                    .addField("프로필 링크", "[링크](" + profileLink + ")" , false)
                    .addField("현재 프로필 상태", profileState, false)
                    .addField("현재 스팀 상태", status, false)
                    .addField("지역", location, false)
                    .addField("스팀 ID1", steamId, false)
                    .addField("스팀 ID3", steamID3, false)
                    .setFooter("조회: steamid.io");

            event.getChannel().sendMessage(builder.build()).queue();



        } catch (IOException e) {
            e.printStackTrace();
            event.getChannel().sendMessage("에러가 발생했습니다. 조회가 불가능 합니다.").queue();
        }

    }

    @Override
    public String getHelp() {
        return "steamid.io로 부터 유저 정보를 조회합니다.\n" +
                "사용법: " + App.getInstance().getPREFIX() + getInvoke() + " <스팀ID/customURL/닉네임>";
    }

    @Override
    public String getInvoke() {
        return "스팀유저정보";
    }

    @Override
    public String getSmallHelp() {
        return "스팀에서 유저 정보를 불러옵니다.";
    }
}
