package me.kirito5572.scp079.object;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class IsKoreaSCPCoop {
    public boolean verification(GuildMessageReceivedEvent event) {
        return veri_main(event.getJDA(), event.getAuthor());
    }

    private boolean veri_main(JDA jda, User author) {
        boolean isExist = false;
        Guild hanCoOp = Objects.requireNonNull(jda.getGuildById("607303213602963643"));
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
            if (member.getId().equals(author.getId())) {
                isExist = true;
            }
        }
        if (author.getId().equals("688434014066835484")) {
            isExist = true;
        }
        return !isExist;
    }

    public boolean verification(MessageReceivedEvent event) {
        return veri_main(event.getJDA(), event.getAuthor());
    }
}
