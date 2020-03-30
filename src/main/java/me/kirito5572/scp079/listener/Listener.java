package me.kirito5572.scp079.listener;

import me.kirito5572.scp079.App;
import me.kirito5572.scp079.object.CommandManager;
import me.kirito5572.scp079.object.SQLDB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter {
    private final CommandManager manager;
    private final Logger logger = LoggerFactory.getLogger(Listener.class);
    private static String ID1;

    public Listener(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onReady(ReadyEvent event) {
        logger.info(String.format("로그인 성공: %#s", event.getJDA().getSelfUser()));
        System.out.println(String.format("로그인 성공: %#s", event.getJDA().getSelfUser()));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User author = event.getAuthor();
        Message message = event.getMessage();
        String content = message.getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();

            logger.info(String.format("\n" +
                    "보낸사람: %#s\n" +
                    "친 내용: %s\n" +
                    "[서버: %s]  " +
                    "[채팅방: %s]", author, content, guild.getName(), textChannel.getName()));
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            if (event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                return;
            }
            if (content.equals("a")) {
                event.getAuthor().openPrivateChannel().complete().sendMessage("이미지 등은 전송할 수 없습니다.").queue();
            } else {
                logger.warn(String.format("[PRIV]<%#s>: %s", author, content));
                StringBuilder stringBuilder = new StringBuilder();
                if (!message.getEmbeds().isEmpty()) {
                    List<MessageEmbed> messageEmbedList = message.getEmbeds();
                    for (MessageEmbed messageEmbed : messageEmbedList) {
                        Objects.requireNonNull(event.getJDA().getUserById("284508374924787713")).openPrivateChannel().complete().sendMessage(messageEmbed).queue();
                    }
                }
                if (!message.getAttachments().isEmpty()) {
                    List<Message.Attachment> attachmentList = message.getAttachments();
                    for (Message.Attachment attachment : attachmentList) {
                        stringBuilder.append("전송된 파일: ").append(attachment.getUrl()).append("\n");
                    }
                }
                if (!message.getEmotes().isEmpty()) {
                    List<Emote> emoteList = message.getEmotes();
                    for (Emote emote : emoteList) {
                        stringBuilder.append("이모지명: ").append(emote.getName()).append(" 이모지 링크: ").append(emote.getImageUrl()).append("\n");
                    }
                }
                if (stringBuilder.toString().length() > 0) {
                    Objects.requireNonNull(event.getJDA().getUserById("284508374924787713")).openPrivateChannel().complete().sendMessage(author.getAsTag() + "(" + author.getId() + ") 로 부터 온 메세지: " + content + "\n" +
                            "기타 첨부물:\n" + stringBuilder.toString()).queue();
                } else {
                    Objects.requireNonNull(event.getJDA().getUserById("284508374924787713")).openPrivateChannel().complete().sendMessage(author.getAsTag() + "(" + author.getId() + ")로 부터 온 메세지: " + content).queue();
                }
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        StringBuilder IDreader = new StringBuilder();
        StringBuilder IDreader1 = new StringBuilder();
        try {
            File file = new File("C:\\DiscordServerBotSecrets\\rito-bot\\OWNER_ID.txt");
            File file1 = new File("C:\\DiscordServerBotSecrets\\rito-bot\\OWNER_ID1.txt");
            FileReader fileReader = new FileReader(file);
            FileReader fileReader1 = new FileReader(file1);
            int singalCh, singalCh1;
            while((singalCh = fileReader.read()) != -1) {
                IDreader.append((char) singalCh);
            }
            while((singalCh1 = fileReader1.read()) != -1) {
                IDreader1.append((char) singalCh1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ID1 = IDreader.toString();
        String ID2 = IDreader1.toString();
        if (event.getMessage().getContentRaw().equalsIgnoreCase(App.getPREFIX() + "종료") &&
                (
                        (event.getAuthor().getIdLong() == Long.decode(ID1)) ||
                                (event.getAuthor().getIdLong() == Long.decode(ID2))
                )) {
            System.out.println(ID1 + ID2);
            shutdown(event.getJDA(), event);
            return;

        }
        if(event.getAuthor().isBot()) {
            boolean flag = true;
            try {
                if (Objects.requireNonNull(event.getMember()).getUser().getId().equals("688434014066835484")) {
                    if (event.getGuild().getId().equals("674941008265478157")) {
                        if (event.getChannel().getId().equals("688410340769136664")) {
                            flag = false;
                        }
                    }
                }
            } catch (Exception ignored) {
            }
            if(flag) {
                return;
            }
        }
        String[] guildId = new String[event.getJDA().getGuilds().size()];
        String[] channelId = new String[event.getJDA().getGuilds().size()];
        int i = 0;
        try {
            String queryString = "SELECT * FROM `079_config`.bot_channel";

            Statement statement = SQLDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                guildId[i] = resultSet.getString("guildId");
                channelId[i] = resultSet.getString("channelId");
                i++;
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(event.getMessage().isWebhookMessage()) {
            return;
        }
        if (event.getMessage().getContentRaw().startsWith(App.getPREFIX())) {
            for (i = 0; i < guildId.length; i++) {
                if (guildId[i] == null) {
                    continue;
                }
                if (event.getGuild().getId().equals(guildId[i])) {
                    if (channelId[i].equals("0")) {
                        continue;
                    }
                    if (!event.getChannel().getId().equals(channelId[i])) {
                        if (!(Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || event.getMember().hasPermission(Permission.MANAGE_ROLES) ||
                                event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || event.getMember().hasPermission(Permission.MANAGE_CHANNEL) ||
                                event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS) || event.getMember().hasPermission(Permission.MANAGE_SERVER))) {
                            event.getChannel().sendMessage(event.getMember().getAsMention() + ", 여긴 지정된 봇 채팅방이 아닙니다.").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                            return;
                        }
                    }
                }
            }
            manager.handleCommand(event);
        }
    }
    private void shutdown(JDA jda, GuildMessageReceivedEvent event) {
        new Thread(() -> {
            event.getChannel().sendMessage("봇이 종료됩니다.").queue();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jda.shutdown();
            System.exit(0);
        }).start();
    }

    public static String getID1() {
        return ID1;
    }
}

