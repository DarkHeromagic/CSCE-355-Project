
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TextSearch {
    public static void main(String[] args) {
        String filename = args[1];
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            lines.forEach((line) -> {
                DFATable dfa = DFATable.createDFATable(line);
                System.out.println(dfa.toString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
