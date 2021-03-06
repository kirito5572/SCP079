package me.kirito5572.scp079.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.kirito5572.scp079.App;
import me.kirito5572.scp079.object.ObjectPool;
import me.kirito5572.scp079.command.HackCommand;
import me.kirito5572.scp079.object.SQLDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class GetHoryuBanList extends ListenerAdapter {
    private int caseNum;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        final String[] time = new String[1];
        TimerTask job = new TimerTask() {
            @Override
            public void run() {
                String temp = get();
                if (temp.contains("response is error")) {
                    App.getInstance().getLogTextChannel().sendMessage("호류서버 제재자 목록을 불러오는 중 에러가 발생했습니다.\n" +
                            temp).queue();
                    return;
                }
                String[] data = temp.split("},");
                String lastmessage;
                lastmessage = filereader();
                String message = data[data.length - 1];
                if (lastmessage.equals(message)) {
                    return;
                } else {
                    filesave(message);
                }
                String[] mainData = splitString(message);
                if (mainData[0].equals("error")) {
                    return;
                }
                if (mainData[0].equals("no result return")) {
                    return;
                }
                if (caseNum < Integer.parseInt(mainData[6])) {
                    caseNum = Integer.parseInt(mainData[6]);
                } else {
                    return;
                }
                if (mainData[2].contains("AUTOMATRON REASON")) {
                    return;
                }
                if (!mainData[5].equals("없음")) {
                    long temp1 = Long.parseLong(mainData[5], 10);
                    long temp2 = Long.parseLong(mainData[4], 10);
                    long timeTemp = temp1 - temp2;
                    timeTemp = timeTemp / 1000L;
                    if (mainData[5].equals("0")) {
                        mainData[5] = "영구";
                        time[0] = "99999999";
                    } else {
                        if (timeTemp < 10) {
                            mainData[5] = "영구";
                            time[0] = "99999999";
                        } else if (timeTemp < 60) {
                            mainData[5] = timeTemp + "초";
                            time[0] = "0";
                        }
                        if (timeTemp > 59) {
                            timeTemp = timeTemp / 60;
                            time[0] = String.valueOf(timeTemp);
                            mainData[5] = timeTemp + "분";
                            if (timeTemp > 59) {
                                timeTemp = timeTemp / 60;
                                mainData[5] = timeTemp + "시";
                                if (timeTemp > 23) {
                                    timeTemp = timeTemp / 24;
                                    mainData[5] = timeTemp + "일";
                                    if (timeTemp > 29) {
                                        timeTemp = timeTemp / 30;
                                        mainData[5] = timeTemp + "월";
                                        if (timeTemp > 11) {
                                            timeTemp = timeTemp / 12;
                                            mainData[5] = timeTemp + "년";
                                            if (timeTemp > 50) {
                                                mainData[5] = "50년 이상";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                InputStreamReader reader;
                if(App.getInstance().getOsInfo() == App.windows) {
                    try {
                        reader = new InputStreamReader(new FileInputStream("C:\\GitHub\\SCP079\\build\\libs\\horyu_rule.json"), StandardCharsets.UTF_8);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        try {
                            reader = new InputStreamReader(new FileInputStream("C:\\Users\\Administrator\\Desktop\\horyu_rule.json"), StandardCharsets.UTF_8);
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                            try {
                                reader = new InputStreamReader(new FileInputStream("horyu_rule.json"), StandardCharsets.UTF_8);
                            } catch (FileNotFoundException e2) {
                                e2.printStackTrace();
                                return;
                            }
                        }
                    }
                } else if(App.getInstance().getOsInfo() == App.linux) {
                    try {
                        reader = new InputStreamReader(new FileInputStream("root/horyu_rule.json"), StandardCharsets.UTF_8);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
                JsonElement element;
                try {
                    JsonParser parser = new JsonParser();
                    element = parser.parse(reader);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                try {
                    mainData[2] = element.getAsJsonObject().get(mainData[2]).getAsString();
                } catch (Exception ignored) {
                }
                String[] id_data = mainData[1].split("@");
                EmbedBuilder builder;
                String startTime = String.valueOf((new Date().getTime() - 32400L) * 10000L + 621355968000000000L);
                String endTime = String.valueOf((new Date().getTime() + Long.parseLong(time[0]) * 60000L) * 10000L + 621355968000000000L);
                if (id_data[1].equals("steam")) {
                    builder = EmbedUtils.defaultEmbed()
                            .setTitle("제재 정보 공유(호류서버)")
                            .setDescription(mainData[0] + ";" + id_data[0] + "@steam;" + endTime + ";" + mainData[2] + ";"
                                    + "호류서버 제재 공유;" + startTime + ";")
                            .setColor(Color.RED)
                            .setFooter("API from scpsl.kr, API made by 호류#7777", "https://cdn.discordapp.com/attachments/563060742551633931/607216118859431966/HoryuServer_Logo_Final.gif")
                            .addField("case", mainData[6], false)
                            .addField("제재 대상자", mainData[0], false)
                            .addField("스팀 ID", id_data[0], false)
                            .addField("제재 사유", mainData[2], false)
                            .addField("제재 기간", mainData[5] + "(" + time[0] + "분)", false)
                            .addField("제재 담당 서버", "호류 SCP 서버", false);
                } else if (id_data[1].equals("discord")) {
                    builder = EmbedUtils.defaultEmbed()
                            .setTitle("제재 정보 공유(호류서버)")
                            .setColor(Color.RED)
                            .setDescription(mainData[0] + ";" + id_data[0] + "@discord;" + endTime + ";" + mainData[2] + ";"
                                    + "호류서버 제재 공유;" + startTime + ";")
                            .setFooter("API from scpsl.kr, API made by 호류#7777", "https://cdn.discordapp.com/attachments/563060742551633931/607216118859431966/HoryuServer_Logo_Final.gif")
                            .addField("case", mainData[6], false)
                            .addField("제재 대상자", "<@" + id_data[0] + ">", false)
                            .addField("디스코드 ID", id_data[0], false)
                            .addField("제재 사유", mainData[2], false)
                            .addField("제재 기간", mainData[5] + "(" + time[0] + "분)", false)
                            .addField("제재 담당 서버", "호류 SCP 서버", false);
                } else {
                    return;
                }
                //testsend(builder, event);
                if(id_data[1].equals("steam")) {
                    ObjectPool.get(HackCommand.class).auto_ban(id_data[0], mainData[5], mainData[2] + "(제재 공유 시스템)", event.getJDA());
                }
                ObjectPool.get(HackCommand.class).server_Send("563045452774244361", builder, event.getJDA(), App.getInstance().getLogTextChannel(), Integer.parseInt(time[0]));
            }
        };
        Timer jobScheduler = new Timer();
        jobScheduler.scheduleAtFixedRate(job, 1000, 10000);
    }

    private String get() {
        try {
            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpGet getRequest = new HttpGet("https://scpsl.kr/api/block/user"); //GET 메소드 URL 생성

            HttpResponse response = client.execute(getRequest);

            //Response 출력
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                return handler.handleResponse(response);
            } else {
                return "response is error : " + response.getStatusLine().getStatusCode();
            }

        } catch (Exception e) {
            return e.toString();
        }
    }

    private void filesave(String message) {
        try {
            PreparedStatement preparedStatement = ObjectPool.get(SQLDB.class).getConnection().prepareStatement("UPDATE `079_config`.last_caseID SET horyu_last_temp=?");
            preparedStatement.setString(1, message);
            preparedStatement.execute();

        } catch (SQLException e) {
            ObjectPool.get(SQLDB.class).reConnect();
            e.printStackTrace();
        }

    }

    private String filereader() {
        try {
            ResultSet resultSet = ObjectPool.get(SQLDB.class).getConnection().createStatement().executeQuery("SELECT * FROM `079_config`.last_caseID;");
            if(resultSet.next()) {
                return resultSet.getString("horyu_last_temp");
            } else {
                return "no result return";
            }
        } catch (SQLException e) {
            ObjectPool.get(SQLDB.class).reConnect();
            e.printStackTrace();
            return "error";
        }
    }

    private String[] splitString(String message) {
        String[] returnData = new String[7];
        message = message.substring(0, message.length() - 1);
        //{"id":223,"name":"keum2912","userId":76561198965054054@steam,"time":1569663223000,"pardonTime":1570527223000,"reason":"문트롤"}]
        JsonParser parser = new JsonParser();
        try {
            if (message.contains("response is error")) {
                System.out.println("error: " + message);
                return new String[]{"error"};
            }
            JsonElement element = parser.parse(message);
            returnData[0] = element.getAsJsonObject().get("name").getAsString();        //CaseID
            returnData[1] = element.getAsJsonObject().get("userId").getAsString();     //UserID
            returnData[2] = element.getAsJsonObject().get("reason").getAsString();      //reason
            returnData[4] = element.getAsJsonObject().get("time").getAsString();        //time
            returnData[5] = element.getAsJsonObject().get("pardonTime").getAsString();  //pardonTime;
            returnData[6] = element.getAsJsonObject().get("id").getAsString();          //id
            if (returnData[5].equals("null")) {
                returnData[5] = "없음";
            } else if (returnData[5].equals("0")) {
                returnData[5] = returnData[4];
            } else {
                returnData[5] = element.getAsJsonObject().get("pardonTime").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"error"};
        }

        return returnData;
    }

    private void send(EmbedBuilder builder, ReadyEvent event, boolean youngminSend) {
        try {
            String greenServer = "600010501266866186";
            String greenServerChat = "617908612064346122";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(greenServer)).getTextChannelById(greenServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String carDogeServer = "609985979167670272";
            String carDogeServerChat = "644446489543835648";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(carDogeServer)).getTextChannelById(carDogeServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String gariaServer = "585437712639590423";
            String gariaServerChat = "617973738582966292";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(gariaServer)).getTextChannelById(gariaServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String simaServer = "582091661266386944";
            String simaServerChat = "595597485238648833";
            String simaServerChat2 = "598126633588883457";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(simaServer)).getTextChannelById(simaServerChat)).sendMessage(builder.build()).queue();
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(simaServer)).getTextChannelById(simaServerChat2)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String dokdoServer = "581835684986486785";
            String dokdoServerChat = "618411407742074880";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(dokdoServer)).getTextChannelById(dokdoServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String snoServer = "570659322007126029";
            String snoServerChat = "620125504246382592";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(snoServer)).getTextChannelById(snoServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String SNJServer = "531777289684254731";
            String SNJServerChat = "623105335514759168";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(SNJServer)).getTextChannelById(SNJServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String ClassDServer = "614348538222215188";
            String ClassDServerChat = "629135900059631647";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(ClassDServer)).getTextChannelById(ClassDServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {

        }
        try {
            String ArtServer = "614348538222215188";
            String ArtServerChat = "629167608180113458";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(ArtServer)).getTextChannelById(ArtServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String ArtServer = "619746711992270869";
            String ArtServerChat = "629167608180113458";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(ArtServer)).getTextChannelById(ArtServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        if (youngminSend) {
            try {
                String YoungminServer = "623316315620245544";
                String YoungminServerChat = "623322259570032640";
                Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(YoungminServer)).getTextChannelById(YoungminServerChat)).sendMessage(builder.build()).queue();
            } catch (Exception ignored) {
            }
        }
        try {
            String VAServer = "614793325081526282";
            String VAServerChat = "630336982140190730";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(VAServer)).getTextChannelById(VAServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String HSSServer = "553932158436376586";
            String HSSServerChat = "641953563299282944";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(HSSServer)).getTextChannelById(HSSServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
        try {
            String SCP079Server = "616601689327140908";
            String SCP079ServerChat = "632817086745411594";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(SCP079Server)).getTextChannelById(SCP079ServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
    }

    private void testsend(EmbedBuilder builder, ReadyEvent event) {
        try {
            String greenServer = "600010501266866186";
            String greenServerChat = "617908612064346122";
            Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(greenServer)).getTextChannelById(greenServerChat)).sendMessage(builder.build()).queue();
        } catch (Exception ignored) {
        }
    }
}
