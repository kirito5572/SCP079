package SCP079.Listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import java.util.Timer;
import java.util.TimerTask;

public class getHoryuBanList extends ListenerAdapter {
    private static int caseNum = 217;
    @Override
    public void onReady(ReadyEvent event) {
        final String[] time = new String[1];
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
                if(caseNum < Integer.parseInt(maindata[6])) {
                    caseNum = Integer.parseInt(maindata[6]);
                } else {
                    return;
                }
                if(!maindata[5].equals("없음")) {
                    long temp1 = Long.parseLong(maindata[5], 10);
                    long temp2 = Long.parseLong(maindata[4], 10);
                    long timeTemp = temp1 - temp2;
                    timeTemp = timeTemp / 1000L;
                    if(timeTemp == 0) {
                        maindata[5] = "영구";
                        time[0] = "99999999";
                    } else if(timeTemp < 60L) {
                        maindata[5] = timeTemp + "초";
                    } if(timeTemp > 59L) {
                        timeTemp = timeTemp / 60L;
                        time[0] = String.valueOf(timeTemp);
                        maindata[5] = timeTemp + "분";
                    } if(timeTemp > 59L) {
                        timeTemp = timeTemp / 60L;
                        maindata[5] = timeTemp + "시";
                    } if(timeTemp > 23L) {
                        timeTemp = timeTemp / 24L;
                        maindata[5] = timeTemp + "일";
                    } if(timeTemp > 29L) {
                        timeTemp = timeTemp / 30L;
                        maindata[5] = timeTemp + "월";
                    } if(timeTemp > 11L) {
                        timeTemp = timeTemp / 12L;
                        maindata[5] = timeTemp + "년";
                    } if(timeTemp > 50L) {
                        maindata[5] = "50년 이상";
                    }
                }

                EmbedBuilder builder = EmbedUtils.defaultEmbed()
                        .setTitle("제재 정보 공유(호류서버)")
                        .setColor(Color.RED)
                        .setFooter("API from scpsl.kr, API made by 호류#1234", "https://cdn.discordapp.com/attachments/563060742551633931/607216118859431966/HoryuServer_Logo_Final.gif")
                        .addField("case", maindata[6], false)
                        .addField("제재 대상자", maindata[0], false)
                        .addField("스팀 ID", maindata[1], false)
                        .addField("제재 사유", maindata[2], false)
                        .addField("제재 기간", maindata[5] + "(" + time[0] + "분)", false)
                        .addField("제재 담당 서버", "호류 SCP 서버", false);
                //testsend(builder, event);
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
        String[] returnData = new String[7];
        message = message.substring(0, message.length() - 1);
        //{"id":223,"name":"keum2912","steamId":76561198965054054,"time":1569663223000,"pardonTime":1570527223000,"reason":"문트롤"}]
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(message);
        returnData[0] = element.getAsJsonObject().get("name").getAsString();        //CaseID
        returnData[1] = element.getAsJsonObject().get("steamId").getAsString();     //SteamID
        returnData[2] = element.getAsJsonObject().get("reason").getAsString();      //reason
        returnData[4] = element.getAsJsonObject().get("time").getAsString();        //time
        returnData[5] = element.getAsJsonObject().get("pardonTime").getAsString();  //pardonTime;
        returnData[6] = element.getAsJsonObject().get("id").getAsString();          //id
        if(returnData[5].equals("null")) {
            returnData[5] = "없음";
        } else if(returnData[5].equals("0")) {
            returnData[5] = returnData[4];
        } else {
            returnData[5] = element.getAsJsonObject().get("pardonTime").getAsString();
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
        try {
            String ClassDServer = "614348538222215188";
            String ClassDServerChat = "629135900059631647";
            event.getJDA().getGuildById(ClassDServer).getTextChannelById(ClassDServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {

        }
        try {
            String ArtServer = "614348538222215188";
            String ArtServerChat = "629135900059631647";
            event.getJDA().getGuildById(ArtServer).getTextChannelById(ArtServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        /*
        try {
            String SCP079Server = "614348538222215188";
            String SCP079ServerChat = "628922311927398400";
            event.getJDA().getGuildById(SCP079Server).getTextChannelById(SCP079ServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        */

    }
    private void testsend(EmbedBuilder builder, ReadyEvent event) {
        try {
            String greenServer = "600010501266866186";
            String greenServerChat = "617908612064346122";
            event.getJDA().getGuildById(greenServer).getTextChannelById(greenServerChat).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
    }
}
