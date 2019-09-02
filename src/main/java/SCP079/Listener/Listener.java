package SCP079.Listener;

import SCP079.App;
import SCP079.Constants;
import SCP079.Objects.CommandManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileReader;

public class Listener extends ListenerAdapter {
    private final CommandManager manager;
    private final Logger logger = LoggerFactory.getLogger(Listener.class);
    private static String ID1;
    private static String ID2;
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
        ID1 = IDreader.toString();
        ID2 = IDreader1.toString();
        if (event.getMessage().getContentRaw().equalsIgnoreCase(App.getPREFIX() + "종료") &&
                (
                        (event.getAuthor().getIdLong() == Long.decode(IDreader.toString())) ||
                        (event.getAuthor().getIdLong() == Long.decode(IDreader1.toString()))
                )) {
            shutdown(event.getJDA(), event);
            return;
        }
        if(event.getAuthor().isBot()) {
            return;

        }
        if(event.getMessage().isWebhookMessage()) {

            return;
        }
        if(event.getGuild().getId().equals("600010501266866186")) {
            if(!event.getChannel().getId().equals("600012818879741963")) {
                if(!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
                    if (event.getMessage().getContentRaw().startsWith(Constants.PREFIX)) {
                        event.getChannel().sendMessage(event.getMember().getAsMention() + " , 명령어는 봇 명령어 채널에서 사용해주세요").queue();

                        return;
                    }
                }
            }
        }
        if(event.getGuild().getId().equals("617222347425972234")) {
            if(!event.getChannel().getId().equals("617230917315854356")) {
                if(!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
                    if (event.getMessage().getContentRaw().startsWith(Constants.PREFIX)) {
                        event.getChannel().sendMessage(event.getMember().getAsMention() + " , 명령어는 봇 명령어 채널에서 사용해주세요").queue();

                        return;
                    }
                }
            }
        }

        if (event.getMessage().getContentRaw().startsWith(Constants.PREFIX)) {
            manager.handleCommand(event);
        }
    }
    private void shutdown(JDA jda, GuildMessageReceivedEvent event) {
        Guild greenServer;
        TextChannel channel;
        String[] status = new String[5];
        try {
            greenServer = event.getJDA().getGuildById("600010501266866186");
            channel = greenServer.getTextChannelById("600015521433518090");
            status[0] = greenServer.getMemberById("580691748276142100").getOnlineStatus().toString();
            status[1] = greenServer.getMemberById("586590053539643408").getOnlineStatus().toString();
            status[2] = greenServer.getMemberById("600658772876197888").getOnlineStatus().toString();
            status[3] = greenServer.getMemberById("600660530230722560").getOnlineStatus().toString();
            status[4] = greenServer.getMemberById("600676751118696448").getOnlineStatus().toString();

        } catch (Exception e) {

            return;
        }
        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("서버 오픈 상태")
                .setColor(Color.RED)
                .addField("Green Color 상태", "OFF", false)
                .addField("1서버", status[0], false)
                .addField("2서버", status[1], false)
                .addField("3서버", status[2], false)
                .addField("4서버", status[3], false)
                .addField("5서버", status[4], false)
                .setFooter("1분마다 서버 상태가 자동 새로고침됩니다.","https://steamuserimages-a.akamaihd.net/ugc/982233321887038211/EB88C5E32425929921EF653FF5B784715B7D0639/");
        channel.editMessageById("616234404452499476", builder.build() + "\n" +
                "서버의 상태를 확인하는 봇이 종료되어 확인이 불가능합니다.").queue();
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

    public static String getID2() {
        return ID2;
    }
}

