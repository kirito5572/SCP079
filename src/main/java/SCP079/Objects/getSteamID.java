package SCP079.Objects;

import me.duncte123.botcommons.web.WebUtils;

public class getSteamID {
    public static String[] SteamID(String ID) {
        String[] returns = new String[3];
        final boolean[] flag = {true, true};
        final boolean[] steamno = {false};
        final String[] NickName = new String[1];
        final String[] finalID = new String[1];
        try {
            WebUtils.ins.scrapeWebPage("https://steamcommunity.com/profiles/" + ID + "/?xml=1").async((document -> {
                String a1 = "";
                boolean pass = false;
                try {
                    a1 = document.getElementsByTag("profile").first().toString();
                } catch (Exception e) {
                    pass = true;
                    flag[0] = false;
                    NickName[0] = "";
                    finalID[0] = "";
                    returns[2] = "nosteam";
                }
                if (!pass) {
                    String a2 = a1;
                    try {
                        a1 = a1.substring(a1.indexOf("<steamid64>") + 14, a1.indexOf("</steamid64>") - 2);
                        a2 = a2.substring(a2.indexOf("<steamid>"));
                        a2 = a2.substring(a2.indexOf("![CDATA[") + 8, a2.indexOf("]]>"));
                        for (; a2.contains(" "); ) {
                            a2 = a2.replaceFirst(" ", "");
                        }
                        NickName[0] = a2;
                        finalID[0] = a1;

                    } catch (Exception e) {
                        e.printStackTrace();

                        returns[0] = "error";
                    }
                    flag[0] = false;
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
            returns[0] = "";
            flag[0] = false;
        }
        while(flag[0]) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        returns[0] = NickName[0];
        returns[1] = finalID[0];
        returns[2] = "steam";

        if(returns[0].equals("")) {
            flag[1] = false;
            WebUtils.ins.scrapeWebPage("https://steamid.io/lookup/" + ID).async((document1 -> {
                String a11 = document1.getElementsByTag("body").first().toString();
                String a21 = a11;
                try {
                    int b21 = a21.indexOf("data-clipboard-text=\"");
                    int b11 = a11.indexOf(" <dt class=\"key\">\n" +
                            "       name");
                    a11 = a11.substring(b11 + 75);
                    a21 = a21.substring(b21 + 21);
                    b21 = a21.indexOf("data-clipboard-text=\"");
                    a21 = a21.substring(b21 + 21);
                    b21 = a21.indexOf("data-clipboard-text=\"");
                    a21 = a21.substring(b21 + 21);
                    int c11 = a11.indexOf("</dd>");
                    int c21 = a21.indexOf(" src=");
                    a11 = a11.substring(0, c11 - 7);
                    a21 = a21.substring(0, c21 - 1);
                    for(; a11.contains(" ");) {
                        a11 = a11.replaceFirst(" ", "");
                    }
                    if(a11.equals("")) {
                        steamno[0] = true;
                    }
                    returns[0] = a11;
                    returns[1] = a21;
                    returns[2] = "nosteam";
                    flag[1] = true;

                } catch (Exception e) {
                    e.printStackTrace();
                    returns[0] = "no";
                    flag[1] = true;
                }
            }));
        } else {
            flag[1] = true;
        }
        while(!flag[1]) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(steamno[0]) {
            returns[0] = "no";
        }
        System.out.println(returns[0]);
        System.out.println(returns[1]);
        System.out.println(returns[2]);
        return returns;

    }
}
