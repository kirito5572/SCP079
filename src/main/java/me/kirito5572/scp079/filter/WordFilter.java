package me.kirito5572.scp079.filter;

import me.kirito5572.scp079.object.ObjectPool;
import me.kirito5572.scp079.listener.FilterListener;

import java.util.List;

public class WordFilter {
    public class WordFilterResult {
        public boolean isMatch = false;
        public boolean isWarn = false;
        public String filter_Match = "";
        public String filter_Warn = "";
        public int matchSize = 0;
    }

    public List<String> list;
    public void init() {
        ObjectPool.get(FilterListener.class).reload();
        list = ObjectPool.get(FilterListener.class).getList();
    }
    public WordFilterResult valid(String message) {
        String message_Raw = message.replaceAll("[^가-힣xfea-zA-Z\\s]", "");
        message = message.replaceAll("[^가-힣xfea-zA-Z]", "");
        WordFilterResult r = new WordFilterResult();
        for(String data : list) {
            if (message.contains(data)) {
                if(message_Raw.contains(data)) {
                    r.isMatch = true;
                    r.filter_Match = r.filter_Match + data + ",";
                    r.matchSize++;
                } else {
                    r.isWarn = true;
                    r.filter_Warn = r.filter_Warn + data + ",";
                }
            }
        }
        return r;
    }
}
