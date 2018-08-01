package pl.wojtektrzos.filmkrecimy.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class EnterLog {

    public static void log(String place, String robie, String data) {
        File log = new File("log.txt");
        FileWriter pisak;
        if(!log.exists()){
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        {
            try {
                pisak = new FileWriter(log, true);
                pisak.append("-----======="+LocalDateTime.now().toString()+"======---------\n");
                pisak.append("place: "+ place +"\n");
                pisak.append("message: "+ robie +"\n");
                pisak.append("data: "+data+"\n");
                pisak.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
