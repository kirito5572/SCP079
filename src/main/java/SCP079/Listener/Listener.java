package SCP079.Listener;

import SCP079.App;
import SCP079.Commands.HackCommand;
import SCP079.Objects.CommandManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
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
import java.util.Objects;

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
            logger.info(String.format("[PRIV]<%#s>: %s", author, content));
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
            try {
                if (Objects.requireNonNull(event.getMember()).getUser().getId().equals("592987181186940931")) {
                    if (event.getGuild().getId().equals("600010501266866186")) {
                        if (event.getChannel().getId().equals("600012818879741963")) {
                            flag = false;
                        }
                    }
                }
            } catch (Exception ignored) {
            }
            /*
            순서: 호류 / 그린 / 독도 / 스노 / HSS / 도지 / 시마 / 낙지 / 디클 / 브아 / 아트
             */
            if(flag) {
                return;
            }
        }
        if(event.getMessage().isWebhookMessage()) {

            return;
        }
        String[] serverList = new String[] {
                "563045452774244361", "600010501266866186", "581835684986486785", "570659322007126029", "553932158436376586", "609985979167670272", "582091661266386944", "531777289684254731", "614348538222215188", "614793325081526282", "619746711992270869", "616601689327140908"
        };
        String[] channelList = new String[] {
                "563205968608100352", "600012818879741963", "581836360051195915", "575423098149535744", "558651343330607125", "644203968628785165", "602391121573707807", "531788408603803658", "614743528928575527", "616927037185196042", "619747385806946332", "623823012428251155"
        };
        if (event.getMessage().getContentRaw().startsWith(App.getPREFIX())) {
            try {
                for(int i = 0; i < serverList.length; i++) {
                    if(event.getGuild().getId().equals(serverList[i])) {
                        if(!event.getChannel().getId().equals(channelList[i])) {
                            System.out.println("여긴 봇 채팅방이 아닙니다!");
                            if(!(Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || event.getMember().hasPermission(Permission.MANAGE_ROLES) ||
                                    event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || event.getMember().hasPermission(Permission.MANAGE_CHANNEL) ||
                                    event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS) || event.getMember().hasPermission(Permission.MANAGE_SERVER))) {
                                System.out.println("당신은 권한이 없습니다!");
                                if(!event.getGuild().getId().equals("508913681279483913")) {
                                    return;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                event.getChannel().sendMessage(e.getMessage()).queue();
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
                HackCommand.server_Send(event.getGuild().getId(), builder, event, true);
            } else {
                Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById("600010501266866186")).getTextChannelById("600010501266866188")).sendMessage(event.getJDA().getSelfUser().getAsMention() + " 업데이트틀 위해 1분간 사용이 불가능합니다.").queue();
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

    public static String getID1() {
        return ID1;
    }
}

