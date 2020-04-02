package me.kirito5572.scp079.listener;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import me.kirito5572.scp079.ObjectPool;
import me.kirito5572.scp079.command.ConfigCommand;
import me.kirito5572.scp079.filter.WordFilter;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

        int count;

        boolean filter_start = false;
        for (count = 0; count < enable.length; count++) {
            if (enable[count].equals(event.getGuild().getId())) {
                filter_start = true;
                break;
            }
        }
        if (!filter_start) {
            return;
        }

        String message = event.getMessage().getContentRaw();
        if(ObjectPool.get(WordFilter.class).valid(message).isMatch) {

        } else {

        }

        throw new UnsupportedOperationException("Not Implemented");
    }
}
