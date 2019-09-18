package SCP079.Listener;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class getHoryuBanList extends ListenerAdapter {
    private static int caseNum = 179;
    @Override
    public void onReady(ReadyEvent event) {
        TimerTask job = new TimerTask() {
            @Override
            public void run() {
                String temp = get();
                String[] data = temp.split("},");
                String lastmessage;
                try {
                    lastmessage = filereader();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                String message = data[data.length - 1];
                if(lastmessage.equals(message)) {

                    return;
                } else {
                    try {
                        filesave(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                String[] maindata = splitString(message);
                if(caseNum < Integer.parseInt(maindata[4])) {
                    caseNum = Integer.parseInt(maindata[4]);
                } else {
                    return;
                }
                if(!maindata[5].equals("없음")) {
                    try {
                        Date nowdate = new Date();
                        Date lawData = new SimpleDateFormat("yyyy-MM-ddhh:mm:ss").parse(maindata[5].replaceFirst("T",""));
                        Date date = new Date(lawData.getTime() - nowdate.getTime());
                        long lawTime = (date.getTime() / 1000);
                        if(lawTime < 59) {
                            lawTime = lawTime / 60;
                            maindata[5] = lawTime + "분";
                            if(lawTime < 59) {
                                lawTime = lawTime / 60;
                                maindata[5] = lawTime+ "시";
                                if(lawTime < 23) {
                                    lawTime = lawTime / 24;
                                    maindata[5] = lawTime+ "일";
                                    if(lawTime < 29) {
                                        lawTime = lawTime / 30;
                                        maindata[5] = lawTime+ "월";
                                        if(lawTime < 11) {
                                            lawTime = lawTime / 12;
                                            maindata[5] = lawTime+ "년";
                                        }
                                    }
                                }
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                EmbedBuilder builder = EmbedUtils.defaultEmbed()
                        .setTitle("제재 정보 공유(호류서버)")
                        .setColor(Color.RED)
                        .setFooter("From scpsl.kr", "https://cdn.discordapp.com/attachments/563060742551633931/607216118859431966/HoryuServer_Logo_Final.gif")
                        .addField("case", maindata[4], false)
                        .addField("제재 대상자", maindata[0], false)
                        .addField("스팀 ID", maindata[1], false)
                        .addField("제재 사유", maindata[2], false)
                        .addField("제재 종료 시간", maindata[5], false)
                        .addField("제재 담당 서버", "호류 SCP 서버", false);
                send(builder, event);
            }
        };
        Timer jobScheduler = new Timer();
        jobScheduler.scheduleAtFixedRate(job, 1000, 10000);
    }
    private String get() {
        try {
            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpGet getRequest = new HttpGet("https://scpsl.kr/api/block/steam"); //GET 메소드 URL 생성

            HttpResponse response = client.execute(getRequest);

            //Response 출력
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                return handler.handleResponse(response);
            } else {
                return "response is error : " + response.getStatusLine().getStatusCode();
            }

        } catch (Exception e){
            return e.toString();
        }
    }
    private void filesave(String message) throws IOException{

        File file = new File("C:\\디스코드봇 파일들\\SCP079\\마지막제재.txt");
        FileWriter writer;


        writer = new FileWriter(file, false);
        writer.write(message);
        writer.flush();

        writer.close();

    }
    private String filereader() throws IOException {
        File file = new File("C:\\디스코드봇 파일들\\SCP079\\마지막제재.txt");
        FileReader reader;

        reader = new FileReader(file);
        StringBuilder message = new StringBuilder();
        int chars;
        while((chars = reader.read())!=-1){
            message.append((char)chars);
        }
        return message.toString();
    }
    private String[] splitString(String message) {
        String[] returnData = new String[6];
        returnData[0] = message.substring(message.indexOf("\"name\"") + 8, message.indexOf(",\"steamId\"") - 1);
        returnData[1] = message.substring(message.indexOf(",\"steamId\"") + 11, message.indexOf(",\"time\""));
        returnData[2] = message.substring(message.indexOf(",\"reason\"") + 11, message.indexOf("\",\"punishFrom\""));
        returnData[3] = message.substring(message.indexOf("\",\"admin\"") + 11, message.indexOf("\"}"));
        returnData[4] = message.substring(message.indexOf("\"id\"") + 5, message.indexOf(",\"name\""));
        returnData[5] = message.substring(message.indexOf("\"pardonTime\"") + 13, message.indexOf(",\"reason\""));
        if(returnData[5].equals("null")) {
            returnData[5] = "없음";
        } else {
            returnData[5] = message.substring(message.indexOf("\"pardonTime\"") + 14, message.indexOf(",\"reason\"") - 1);
        }

        return returnData;
    }
    private void send(EmbedBuilder builder, ReadyEvent event) {
        try {
            String greenServer = "600010501266866186";
            String greenServerChat = "617908612064346122";
            event.getJDA().getGuildById(greenServer).getTextChannelById(greenServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        /*try {
            String tlServer = "551022729441312779";
            if(!serverID.equals(tlServer)) {
                String tlServerChat = "617924927944785931";
                event.getJDA().getGuildById(tlServer).getTextChannelById(tlServerChat).sendMessage(builder.build()).queue();
            }
        } catch (Exception ignored) {
        }*/
        try {
            String carDogeServer = "609985979167670272";
            String carDogeServerChat = "617938587102478337";
            event.getJDA().getGuildById(carDogeServer).getTextChannelById(carDogeServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String koreanServer = "607542356866236416";
            String koreanServerChat = "607548370843860994";
            event.getJDA().getGuildById(koreanServer).getTextChannelById(koreanServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String gariaServer = "585437712639590423";
            String gariaServerChat = "617973738582966292";
            event.getJDA().getGuildById(gariaServer).getTextChannelById(gariaServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String simaServer = "582091661266386944";
            String simaServerChat = "595597485238648833";
            String simaServerChat2 = "598126633588883457";
            event.getJDA().getGuildById(simaServer).getTextChannelById(simaServerChat).sendMessage(builder.build()).queue();
            event.getJDA().getGuildById(simaServer).getTextChannelById(simaServerChat2).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String dokdoServer = "581835684986486785";
            String dokdoServerChat = "618411407742074880";
            event.getJDA().getGuildById(dokdoServer).getTextChannelById(dokdoServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String snoServer = "570659322007126029";
            String snoServerChat = "620125504246382592";
            event.getJDA().getGuildById(snoServer).getTextChannelById(snoServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String SNJServer = "531777289684254731";
            String SNJServerChat = "623105335514759168";
            event.getJDA().getGuildById(SNJServer).getTextChannelById(SNJServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
    }
}
