
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

public class InputUtils {

    public static DFATable initDFATable(String filename) throws IOException{
        DFATable table = new DFATable();
        List<String> lines = Files.readAllLines(Paths.get(filename));
        for(int i = 0;  i < lines.size(); i++) {
            String line = lines.get(i);
            if (i == 0) {
                int number = Integer.valueOf(line.split(":")[1].trim());
                IntStream.range(0, number).forEach((x) -> table.addState(new State(x)));
            } else if (i == 1) {
                String[] acceptings = line.split(":")[1].split(" ");
                for (String acceptState : acceptings) {
                    if (acceptState.trim().isEmpty())
                        continue;
                    table.getState(Integer.valueOf(acceptState)).setAcceptable();
                }
            } else if (i == 2) {
                String  alp = line.split(":")[1].trim();
                for (int j = 0; j < alp.length(); j++) {
                    table.addCharacter(alp.charAt(j));
                }
            } else  {
                String[] row = line.split(" ");
                List<Character> alphabet = table.getAlphabet();
                for (int index = 0; index < alphabet.size() ; index++) {
                    table.addTransition(alphabet.get(index),
                            table.getState(i-3),
                            table.getState(Integer.valueOf(row[index]))
                    );
                }
            }
        }
        table.reset();
        return table;

    }
}
