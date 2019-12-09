package SCP079.Objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class getHoryuSearch {
    private static String[] get_Infor(String SteamID) {
        String temp = get(SteamID);
        String[] returnData = new String[] {
                null, null, null, null, null,
                null, null
        };
        JsonParser parser = new JsonParser();
        JsonElement element = null;
        try {
            element = parser.parse(temp);
        } catch (Exception e) {
            temp = get(SteamID);
            if (temp.equals("error")) {
                return new String[] {"error"};
            }
            element = parser.parse(temp);
        }
        try {
            for(int i = 0; i < 7; i++) {
                if(element.getAsJsonArray().size() > i) {
                    returnData[i] = element.getAsJsonArray().get(i).getAsJsonObject().get("id").getAsString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnData;
    }
    private static String[] get_Infor(int caseID, String steamID) {
        String[] returnData = new String[7];
        String temp = get(steamID);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(temp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        for(int i = 0; i < element.getAsJsonArray().size(); i++) {
            if(element.getAsJsonArray().get(i).getAsJsonObject().get("id").getAsInt() == caseID) {
                JsonObject data = element.getAsJsonArray().get(i).getAsJsonObject();
                Date date = new Date(data.get("time").getAsLong());
                returnData[0] = data.get("id").getAsString();      //CaseID
                returnData[1] = data.get("steamId").getAsString();      //SteamID
                returnData[2] = simpleDateFormat.format(date);      //DB 기록 시간
                returnData[3] = UnixToTime.UnixToTimeChange(date.getTime(), data.get("pardonTime").getAsLong());      //재재 기간
                returnData[4] = data.get("reason").getAsString();     //이유
                returnData[5] = "호류 SCP 서버";
                returnData[6] = "563045452774244361";
            }
        }
        return returnData;
    }
    private static String get(String steamID) {
        try {
            HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpGet getRequest = new HttpGet("https://scpsl.kr/api/block/steam/" + steamID); //GET 메소드 URL 생성

            HttpResponse response = client.execute(getRequest);

            //Response 출력
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                return handler.handleResponse(response);
            } else {
                return "error";
            }

        } catch (Exception e){
            return "error";
        }
    }
    public static String[] Search(String SteamID) throws SQLException, ClassNotFoundException {
        String[] HoryuData = get_Infor(SteamID);
        String[] DBData = SQLDB.SQLdownload(SteamID);
        String[] GreenData = SQLDB.GreenDBDownload(SteamID);
        String[] Data = new String[HoryuData.length + DBData.length + GreenData.length];
        int j = 0, k = 0, g = 0;
        for(int i = 0; i < (HoryuData.length + DBData.length + GreenData.length); i++) {
            if(j < HoryuData.length) {
                if(HoryuData[j] != null) {
                    Data[i] = "h" + HoryuData[j];
                    j++;
                }
            }
            if(k < DBData.length) {
                if(DBData[k] != null) {
                    Data[i] = DBData[k];
                    k++;
                }
            }
            if(g < GreenData.length) {
                if(GreenData[g] != null) {
                    Data[i] = "g" + GreenData[g];
                    g++;
                }
            }
        }
        return Data;
    }
    public static String[] Search(String steamID, String caseID) {
        String[] data = new String[7];
        if(caseID.startsWith("h")) { //식별자: 호류
            data = get_Infor(Integer.parseInt(caseID.replaceFirst("h", "")), steamID);
        } else if(caseID.startsWith("g")) {
            try {
                data = SQLDB.GreenDBDownload(Integer.parseInt(caseID.replaceFirst("g", "")), steamID);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else { //식별자: 자체 DB
            try {
                data = SQLDB.SQLdownload(Integer.parseInt(caseID));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
