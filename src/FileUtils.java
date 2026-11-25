import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class FileUtils {
public static byte[] readFile(String filePath) throws IOException {
    return Files.readAllBytes(Paths.get(filePath));
}
public static void writeFile(String filePath , byte[] data)throws IOException{
  Files.write(Paths.get(filePath), data);
}
}