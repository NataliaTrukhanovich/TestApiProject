package Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
    public static String getJsonData(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName + ".json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
