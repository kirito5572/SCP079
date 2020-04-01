package me.kirito5572.scp079.filter;

import me.kirito5572.scp079.listener.FilterListener;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFilter {
    private static List<String> list;
    public static void init() {
        list = new FilterListener().getList();
    }
    public static boolean valid(String message) {
        message = message.replaceAll("[^가-힣xfe0-9a-zA-Z\\s]", "");
        boolean isMatch = false;
        for(String data : list)
        if(message.contains(data)) {

        }
        return isMatch;
    }
}
