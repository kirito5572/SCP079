package me.kirito5572.scp079.listener;

import me.kirito5572.scp079.App;
import me.kirito5572.scp079.command.VersionCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.*;

public class ActivityChangeListener extends ListenerAdapter {
    private static JDA jda;
    private int i = 0;

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        jda = event.getJDA();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                List<Guild> guilds = jda.getGuilds();
                int memberSize = 0;
                Set<Long> memberIds = new HashSet<>();
                for (Guild guild : guilds) {
                    memberSize += guild.getMembers().size();
                    for (Member member : guild.getMembers()) {
                        memberIds.add(member.getIdLong());
                    }
                }
                switch (i) {
                    case 0:
                        Objects.requireNonNull(jda.getPresence()).setActivity(Activity.playing("도움말:" + App.getPREFIX() + "명령어"));
                        break;
                    case 1:
                        Objects.requireNonNull(jda.getPresence()).setActivity(Activity.playing(guilds.size() + "개의 서버, " + memberSize + " 명의 유저(중복 제외:" + memberIds.size() + " 명"));
                        break;
                    case 2:
                        Objects.requireNonNull(jda.getPresence()).setActivity(Activity.playing(VersionCommand.getVersion()));
                        break;
                    case 3:
                        Objects.requireNonNull(jda.getPresence()).setActivity(Activity.playing("버그/개선 사항은 제작자 DM으로"));
                        break;
                    case 4:
                        Objects.requireNonNull(jda.getPresence()).setActivity(Activity.playing("kirito5572#5572 제작"));
                }
                i++;
                if (i > 4) {
                    i = 0;
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
    }
}
