package SCP079.Objects;

import me.duncte123.botcommons.web.WebUtils;

public class getSteamID {
    public static String[] SteamID(String ID) {
        String[] returns = new String[2];
        final boolean[] flag = {true};
        WebUtils.ins.scrapeWebPage("https://steamcommunity.com/profiles/" + ID + "/?xml=1").async((document -> {
            String a1 = document.getElementsByTag("profile").first().toString();
            String a2 = a1;
            try {
                a1 = a1.substring(a1.indexOf("<steamid64>") + 14 , a1.indexOf("</steamid64>") - 2);
                a2 = a2.substring(a2.indexOf("<steamid>"));
                a2 = a2.substring(a2.indexOf("![CDATA[") + 8, a2.indexOf("]]>"));
                returns[0] = a2;
                returns[1] = a1;
                System.out.println(a2);
                System.out.println(a1);

            } catch (Exception e) {
                e.printStackTrace();

                returns[0] = "error";
            }
            flag[0] = false;
        }));
        while(flag[0]) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return returns;

    }
}
