package me.kirito5572.scp079;/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import me.kirito5572.scp079.listener.ActivityChangeListener;
import me.kirito5572.scp079.listener.GetHoryuBanList;
import me.kirito5572.scp079.listener.Listener;
import me.kirito5572.scp079.object.CommandManager;
import me.kirito5572.scp079.object.ObjectPool;
import me.kirito5572.scp079.object.SQLDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class App {
    private static App instance;
    public static App getInstance() {
        return instance;
    }

    private int osInfo;

    public int getOsInfo() {
        return osInfo;
    }

    public static final int windows = 0;
    public static final int linux = 1;
    public static final int unix = 2;

    private final String Time;
    private final String PREFIX;
    private TextChannel logTextChannel;
    private final Random random = new Random();


    public App() {
        instance = this;

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.out.println(System.getProperty("os.name") + " 에서 부팅 시작....");
            osInfo = App.windows;
        } else if (os.contains("mac")) {
            System.out.println(System.getProperty("os.name") + " 은 지원하지 않는 운영체제 입니다.");
            System.exit(-3);
        }  else if (os.contains("linux")) {
            System.out.println(System.getProperty("os.name") + " 에서 부팅 시작....");
            osInfo = App.linux;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            System.out.println(System.getProperty("os.name") + " 에서 부팅 시작....");
            osInfo = App.unix;
        } else if (os.contains("sunos")) {
            System.out.println(System.getProperty("os.name") + " 은 지원하지 않는 운영체제 입니다.");
            System.exit(-3);
        } else {
            System.out.println(System.getProperty("os.name") + " 은 지원하지 않는 운영체제 입니다.");
            System.exit(-3);
        }


        Date date = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd aa hh:mm:ss z");
        Time = format1.format(date);
        ObjectPool.get(SQLDB.class);
        CommandManager commandManager = new CommandManager();
        StringBuilder TOKENreader = new StringBuilder();
        System.out.println(osInfo);
        if(osInfo == windows) {
            try {
                File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\TOKEN.txt");
                FileReader fileReader = new FileReader(file);
                int singalCh;
                while ((singalCh = fileReader.read()) != -1) {
                    TOKENreader.append((char) singalCh);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(osInfo == linux) {
            try {
                File file = new File("\\root\\TOKEN.txt");
                FileReader fileReader = new FileReader(file);
                int singalCh;
                while ((singalCh = fileReader.read()) != -1) {
                    TOKENreader.append((char) singalCh);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("아직 해당 운영체제는 지원하지 않습니다.");
            System.exit(-4);
        }

        String TOKEN = TOKENreader.toString();
        PREFIX = "$$";


        Logger logger = LoggerFactory.getLogger(App.class);

        WebUtils.setUserAgent("Chrome 75.0.3770.100 kirito's discord bot/kirito5572#5572");

        Listener listener = new Listener(commandManager);
        GetHoryuBanList getHoryuBanList = new GetHoryuBanList();
        ActivityChangeListener activityChangeListener = new ActivityChangeListener();

        JDA jda;
        try {
            logger.info("부팅");
            jda = JDABuilder.createDefault(TOKEN)
                    .setAutoReconnect(true)
                    .addEventListeners(listener, getHoryuBanList, activityChangeListener)
                    .setEnabledIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .build().awaitReady();
            logger.info("부팅완료");
            logTextChannel = Objects.requireNonNull(jda.getGuildById("665581943382999048")).getTextChannelById("665581943382999051");
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            return;
        }
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(getRandomColor())
                        .setFooter("Made By kirito5572#5572")//, Objects.requireNonNull(jda.getUserById("284508374924787713")).getAvatarUrl())
        );



    }

    public String getPREFIX() {
        return PREFIX;
    }

    public static void main(String[] args) {
        new App();
    }

    public String getTime() {
        return Time;
    }

    public TextChannel getLogTextChannel() {
        return logTextChannel;
    }

    private Color getRandomColor() {
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }
}
