package me.kirito5572.scp079.filter;

import me.kirito5572.scp079.ObjectPool;
import me.kirito5572.scp079.listener.FilterListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WordFilterTest {

    private FilterListener listener;

    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        listener = ObjectPool.get(FilterListener.class);
        listener.reload();
        ObjectPool.get(WordFilter.class).init();
    }

    @Test
    public void test() {
        String[] charList = {"씨", "발"};
        StringBuilder b = new StringBuilder();
        b.append(charList[0]);
        for(int i = 0; i<3;i++) {
            b.append("@");
        }
        b.append(charList[1]);

        assertFalse(ObjectPool.get(WordFilter.class).valid("안녕").isMatch);
        assertTrue(ObjectPool.get(WordFilter.class).valid("안녕 " + b.toString()).isMatch);
        assertTrue(ObjectPool.get(WordFilter.class).valid("안녕" + b.toString()).isMatch);

    }
}