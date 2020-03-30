package me.kirito5572.scp079.object;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IsKoreaSCPCoop {
    public static boolean verification(GuildMessageReceivedEvent event) {
        boolean isExist = false;
        Guild hanCoOp = Objects.requireNonNull(event.getJDA().getGuildById("607303213602963643"));
        List<Member> memberList = hanCoOp.getMembers();
        List<Member> adminList = new ArrayList<>();
        for (Member member : memberList) {
            if (member.getRoles().contains(hanCoOp.getRoleById("607314941929586691"))) {
                adminList.add(member);
            }
            if (member.getRoles().contains(hanCoOp.getRoleById("607314967946854431"))) {
                adminList.add(member);
            }
        }
        for (Member member : adminList) {
            if (member.getId().equals(event.getAuthor().getId())) {
                isExist = true;
            }
        }
        if (event.getAuthor().getId().equals("688434014066835484")) {
            isExist = true;
        }
        return !isExist;
    }
}
