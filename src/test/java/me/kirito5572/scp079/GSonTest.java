package me.kirito5572.scp079;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class GSonTest {
    @Test
    public void test() throws FileNotFoundException {
        File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\filter_list.json");
        FileReader fileReader = new FileReader(file);

        ArrayList<String> a = new Gson().fromJson(new JsonReader(fileReader), new TypeToken<ArrayList<String>>(){}.getType());
        System.out.println(a);
    }
}
