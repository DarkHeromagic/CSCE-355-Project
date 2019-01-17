package com.dfa;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SimulateDFA{
    public static void main(String[] args) {
        String dfaFilename =  args[1];
        String inputFilename = args[2];
        try {
            DFATable dfa = InputUtils.initDFATable(dfaFilename);
            List<String> lines = Files.readAllLines(Paths.get(inputFilename));
            lines.forEach((String line) -> {
                System.out.println(dfa.start(line));
                dfa.reset();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
