package me.kirito5572.scp079.listener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.kirito5572.scp079.command.ConfigCommand;
import me.kirito5572.scp079.filter.WordFilter;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FilterListener extends ListenerAdapter implements Reloadable {
    private List<String> list = new ArrayList<>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        reload();
    }

    public void reload() {
        StringBuilder TOKENreader = new StringBuilder();
        try {
            File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\filter_list.txt");
            FileReader fileReader = new FileReader(file);
            int singalCh;
            while ((singalCh = fileReader.read()) != -1) {
                TOKENreader.append((char) singalCh);
            }

            JsonParser parser = new JsonParser();
            JsonArray element = parser.parse(TOKENreader.toString()).getAsJsonArray();

            for (JsonElement jsonObject : element) {
                list.add(jsonObject.getAsString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] enable = ConfigCommand.getFilter_enable();

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
        if(WordFilter.valid(message)) {

        } else {

        }
    }
}
