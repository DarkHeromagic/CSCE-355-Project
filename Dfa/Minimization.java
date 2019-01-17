package com.dfa;

import java.io.IOException;

public class Minimization {
    public static void main(String[] args) {
        String filename  = args[1];
        try {
            DFATable minimizedTable = InputUtils.initDFATable(filename).minimize();
            System.out.println(minimizedTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
