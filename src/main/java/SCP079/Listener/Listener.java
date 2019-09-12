package SCP079.Listener;

import SCP079.App;
import SCP079.Commands.HackCommand;
import SCP079.Constants;
import SCP079.Objects.CommandManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;

public class Listener extends ListenerAdapter {
    private final CommandManager manager;
    private final Logger logger = LoggerFactory.getLogger(Listener.class);

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
            logger.info(String.format("[PRIV]<%#s>: %s", author, content));
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
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
        String ID1 = IDreader.toString();
        String ID2 = IDreader1.toString();
        if (event.getMessage().getContentRaw().equalsIgnoreCase(App.getPREFIX() + "종료") &&
                (
                        (event.getAuthor().getIdLong() == Long.decode(ID1)) ||
                                (event.getAuthor().getIdLong() == Long.decode(ID2))
                )) {
            System.out.println(ID1 + ID2);
            shutdown(event.getJDA(), event);
            return;

        } else if(event.getMessage().getContentRaw().equalsIgnoreCase(App.getPREFIX() + "재시작") &&
                (
                        (event.getAuthor().getIdLong() == Long.decode(ID1)) ||
                                (event.getAuthor().getIdLong() == Long.decode(ID2))
                )) {
            restart(event.getJDA(), event);
            return;
        }
        if(event.getAuthor().isBot()) {
            boolean flag = true;
            if(event.getMember().getUser().getId().equals("607585394237636629")) {
                if(event.getGuild().getId().equals("600010501266866186")) {
                    if(event.getChannel().getId().equals("600012818879741963")) {
                        flag = false;
                    }
                }
            }
            if(flag) {
                return;
            }
        }
        if(event.getMessage().isWebhookMessage()) {

            return;
        }

        if (event.getMessage().getContentRaw().startsWith(Constants.PREFIX)) {
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
    private void restart(JDA jda, GuildMessageReceivedEvent event) {
        new Thread(() -> {
            String reason = event.getMessage().getContentRaw().substring((App.getPREFIX() + "재시작").length());
            event.getMessage().delete().queue();
            event.getChannel().sendMessage("종료 하는중....").queue();
            if (event.getAuthor().getId().equals("284508374924787713")) {
                EmbedBuilder builder = EmbedUtils.defaultEmbed()
                        .setTitle("봇 재시작")
                        .addField("재시작 목적", reason, false)
                        .addField("점검 시간","최대 5분간", false)
                        .setDescription("이용에 불편을 드려 죄송합니다.");
                HackCommand.server_Send(event.getGuild().getId(), builder, event);
            } else {
                event.getJDA().getGuildById("600010501266866186").getTextChannelById("600010501266866188").sendMessage(event.getJDA().getSelfUser().getAsMention() + " 업데이트틀 위해 1분간 사용이 불가능합니다.").queue();
            }
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                jda.shutdown();
                System.exit(-1);
            }).start();
        }).start();
    }

}

