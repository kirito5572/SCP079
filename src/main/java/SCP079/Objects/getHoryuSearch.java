package SCP079.Objects;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

public class getHoryuSearch {
    private String get_Infor(String steamID) {
        String temp = get(steamID);
        String[] data = temp.split("},");

        return temp;
    }
    private String get(String steamID) {
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
}
