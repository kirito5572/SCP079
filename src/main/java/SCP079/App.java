package SCP079;/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import SCP079.Listener.Listener;
import SCP079.Listener.activityChangeListener;
import SCP079.Listener.getHoryuBanList;
import SCP079.Objects.CommandManager;
import SCP079.Objects.SQLDB;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class App {
    private String TOKEN;
    private static Date date;
    private static String Time;
    private static String PREFIX;
    private final Random random = new Random();

    private App() {
        date = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy/MM/dd aa hh:mm:ss z");
        Time = format1.format(date);
        SQLDB sqldb = new SQLDB();
        CommandManager commandManager = new CommandManager();
        StringBuilder TOKENreader = new StringBuilder();
        try {
            File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\TOKEN.txt");
            FileReader fileReader = new FileReader(file);
            int singalCh;
            while((singalCh = fileReader.read()) != -1) {
                TOKENreader.append((char) singalCh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TOKEN = TOKENreader.toString();
        PREFIX = Constants.PREFIX;

        Logger logger = LoggerFactory.getLogger(App.class);

        WebUtils.setUserAgent("Chrome 75.0.3770.100 kirito's discord bot/kirito5572#5572");

        Listener listener = new Listener(commandManager);
        getHoryuBanList getHoryuBanList = new getHoryuBanList();
        activityChangeListener activityChangeListener = new activityChangeListener();


        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(getRandomColor())
                        .setFooter("Made By kirito5572#5572",null)
        );

        try {
            logger.info("부팅");
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(TOKEN)
                    .setAutoReconnect(true)
                    .addEventListeners(listener, getHoryuBanList, activityChangeListener)
                    .build().awaitReady();
            logger.info("부팅완료");
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    private Color getRandomColor() {
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public static void main(String[] args) {
        new App();
    }

    public static String getTime() {
        return Time;
    }

    public static Date getDate() {
        return date;
    }
}
