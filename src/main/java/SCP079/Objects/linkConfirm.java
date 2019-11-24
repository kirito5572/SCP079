package SCP079.Objects;

import java.net.HttpURLConnection;
import java.net.URL;

public class linkConfirm {
    private static String link;

    public static boolean isLink(String rawMessage) {
        if (rawMessage.startsWith("http://")) {
            rawMessage = rawMessage.replaceFirst("http://", "");
        }
        if (rawMessage.startsWith("https://")) {
            rawMessage = rawMessage.replaceFirst("https://", "");
        }
        try {
            System.out.println("http://" + rawMessage);
            URL url = new URL("http://" + rawMessage);
            link = rawMessage;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            return (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
                    || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getLink() {
        return link;
    }
}
