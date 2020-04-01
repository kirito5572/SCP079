package me.kirito5572.scp079.filter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WordFilterTest {
    @Before
    public void setUp() {
        WordFilter.init();
    }

    @Test
    public void test() {
        String[] charList = {"씨", "발"};
        StringBuilder b = new StringBuilder();
        b.append(charList[0]);
        for(int i = 0; i<3;i++) {
            b.append("t");
        }
        b.append(charList[1]);

        assertTrue(WordFilter.valid("안녕"));
        assertFalse(WordFilter.valid("안녕 " + b.toString()));
        assertFalse(WordFilter.valid("안녕" + b.toString()));

    }
}