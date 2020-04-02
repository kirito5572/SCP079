package me.kirito5572.scp079.filter;

import me.kirito5572.scp079.ObjectPool;
import me.kirito5572.scp079.listener.FilterListener;

import java.util.List;

public class WordFilter {
    public class WordFilterResult {
        public boolean isMatch;
        public boolean isWarn;
    }

    private List<String> list;
    public void init() {
        list = ObjectPool.get(FilterListener.class).getList();
    }
    public WordFilterResult valid(String message) {
        String message_Raw = message.replaceAll("[^가-힣xfea-zA-Z\\s]", "");
        message = message.replaceAll("[^가-힣xfea-zA-Z]", "");
        WordFilterResult r = new WordFilterResult();

        for(String data : list) {
            if (message.contains(data)) {
                r.isWarn = true;
                if(message_Raw.contains(data)) {
                    r.isMatch = true;
                }
            }
        }
        return r;
    }
}
