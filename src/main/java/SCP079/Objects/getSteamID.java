package SCP079.Objects;

import me.duncte123.botcommons.web.WebUtils;

public class getSteamID {
    public static String[] SteamID(String ID) {
        String[] returns = new String[2];
        WebUtils.ins.scrapeWebPage("https://steamcommunity.com/profiles/" + ID + "/?xml=1").async((document -> {
            String a1 = document.getElementsByTag("profile").first().toString();
            String a2 = a1;
            try {
                a1 = a1.substring(a1.indexOf("<steamID64>") + 11, a1.indexOf("</steamID64>"));
                a2 = a2.substring(a2.indexOf("<steamID>"));
                a2 = a2.substring(a2.indexOf("![CDATA[ ") + 8, a2.indexOf("]]>") - 1);
                returns[0] = a2;
                returns[1] = a1;

            } catch (Exception e) {
                e.printStackTrace();

                returns[0] = "error";
            }
        }));
        return returns;
    }
}
