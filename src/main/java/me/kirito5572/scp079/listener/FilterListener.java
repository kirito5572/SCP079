package me.kirito5572.scp079.listener;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.kirito5572.scp079.object.ObjectPool;
import me.kirito5572.scp079.command.ConfigCommand;
import me.kirito5572.scp079.filter.WordFilter;
import me.kirito5572.scp079.object.Reloadable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterListener extends ListenerAdapter implements Reloadable {
    private List<String> list = new ArrayList<>();

    private final Type typeToken = new TypeToken<ArrayList<String>>(){}.getType();

    public List<String> getList() {
        return list;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        reload();
    }

    public void reload() {
        try {
            File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\filter_list.json");
            FileReader fileReader = new FileReader(file);
            list = new Gson().fromJson(new JsonReader(fileReader), typeToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] enable = ObjectPool.get(ConfigCommand.class).getFilter_enable();
        String[] warn = ObjectPool.get(ConfigCommand.class).getFilter_warn_enable();

        int value;

        boolean filter_enable = false;
        boolean filter_warn = false;
        for (value = 0; value < enable.length; value++) {
            if (enable[value].equals(event.getGuild().getId())) {
                filter_enable = true;
                break;
            }
        }
        if (!filter_enable) {
            for (value = 0; value < warn.length; value++) {
                if (warn[value].equals(event.getGuild().getId())) {
                    filter_warn = true;
                    break;
                }
            }
            if(!filter_warn) {
                return;
            }
        }

        String message = event.getMessage().getContentRaw();
        WordFilter.WordFilterResult r = ObjectPool.get(WordFilter.class).valid(message);
        if(r.isMatch) {
            event.getMessage().delete().complete();
            Objects.requireNonNull(event.getMember()).getUser().openPrivateChannel().complete().sendMessage("욕설 등 금지된 단어 사용을 하지 마세요.").queue();
            EmbedBuilder builder = EmbedUtils.defaultEmbed()
                    .setTitle("금지어 사용")
                    .addField("사용 유저", event.getMember().getAsMention() + "(" + event.getMember().getId() + ")", false)
                    .addField("감지된 금지어 목록", r.filter_Match, false);
            if (r.matchSize > Integer.parseInt(ObjectPool.get(ConfigCommand.class).getFilter_ban()[value])) {
                event.getMember().ban(1, "금지어 대량 사용").complete();
                builder.addField("페널티 부과", "밴", false);
                Objects.requireNonNull(event.getGuild().getTextChannelById(ObjectPool.get(ConfigCommand.class).getFilter_channelId()[value])).sendMessage(builder.build()).queue();
            }
            else if (r.matchSize > Integer.parseInt(ObjectPool.get(ConfigCommand.class).getFilter_kick()[value])) {
                event.getMember().kick("금지어 대량 사용").complete();
                builder.addField("페널티 부과", "킥", false);
                Objects.requireNonNull(event.getGuild().getTextChannelById(ObjectPool.get(ConfigCommand.class).getFilter_channelId()[value])).sendMessage(builder.build()).queue();
            }
            else if (r.matchSize > Integer.parseInt(ObjectPool.get(ConfigCommand.class).getFilter_mute()[value])) {
                event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()),
                        Objects.requireNonNull(event.getGuild().getRoleById(ObjectPool.get(ConfigCommand.class).getFilter_muterole()[value]))).complete();
                builder.addField("페널티 부과", "뮤트", false);
                Objects.requireNonNull(event.getGuild().getTextChannelById(ObjectPool.get(ConfigCommand.class).getFilter_channelId()[value])).sendMessage(builder.build()).queue();
            }
            Objects.requireNonNull(event.getGuild().getTextChannelById(ObjectPool.get(ConfigCommand.class).getFilter_channelId()[value])).sendMessage(builder.build()).queue();
        } else if (r.isWarn) {
            EmbedBuilder builder = EmbedUtils.defaultEmbed()
                    .setTitle("금지어 경고")
                    .addField("사용 유저", Objects.requireNonNull(event.getMember()).getAsMention() + "(" + event.getMember().getId() + ")", false)
                    .addField("감지된 금지어 목록", r.filter_Warn, false);
            Objects.requireNonNull(event.getGuild().getTextChannelById(ObjectPool.get(ConfigCommand.class).getFilter_warn_channelId()[value])).sendMessage(builder.build()).queue();
        }

        throw new UnsupportedOperationException("Not Implemented");
    }
}
