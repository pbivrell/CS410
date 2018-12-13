package cs410.io;

import java.util.function.Consumer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LineReader{
    public static void read(String filename, Consumer<String> consumer) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String line = "";
        while((line = br.readLine()) != null){
            consumer.accept(line);
        }
    }

}
