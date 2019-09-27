package SCP079.Objects;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.sql.SQLException;

public class getHoryuSearch {
    private static String[][] get_Infor(String steamID) {
        String temp = get(steamID);
        String[] rawdata = temp.split("},");
        String[][] returnData = new String[rawdata.length][7];
        for(int i = 0; i < rawdata.length; i++) {
            String[] data = rawdata[i].split(":");
            returnData[i][0] = data[0].substring(0, data[0].indexOf(","));      //CaseID
            returnData[i][1] = data[2].substring(0, data[2].indexOf(","));      //SteamID
            returnData[i][2] = data[3].substring(0, data[3].indexOf(","));      //DB 기록 시간
            returnData[i][3] = data[4].substring(0, data[4].indexOf(","));      //재재 기간
            returnData[i][4] = data[5].substring(1, data[5].indexOf("\""));     //이유
            returnData[i][5] = "호류 SCP 서버";
            returnData[i][6] = "563045452774244361";
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
                return "response is error : " + response.getStatusLine().getStatusCode();
            }


        } catch (Exception e){
            return e.toString();
        }
    }
    public static String[][] Search(String SteamID) throws SQLException {
        String[][] HoryuData = get_Infor(SteamID);
        String[][] DBData = SQLDB.SQLdownload(SteamID);
        String[][] Data = new String[HoryuData.length + DBData.length][7];
        int j = 0, k = 0;
        for(int i = 0; i < (HoryuData.length + DBData.length); i++) {
            if(j < HoryuData.length) {
                Data[i] = HoryuData[j];
                j++;
            }
            if(j < DBData.length) {
                Data[i] = DBData[k];
                k++;
            }
        }
        return Data;
    }
}
