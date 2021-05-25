package com.fatguy.behealthy.Diagnose;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    private int rowC = 0;
    private String[] symtoms = new String[150];
    private String[] disease = new String[50];
    private byte[][] data = new byte[150][50];
    
    public String[] getSymtoms() {
        return symtoms;
    }

    public void setSymtoms(String[] symtoms) {
        this.symtoms = symtoms;
    }

    public String[] getDisease() {
        return disease;
    }

    public void setDisease(String[] disease) {
        this.disease = disease;
    }

    public byte[][] getData() {
        return data;
    }

    public void setData(byte[][] data) {
        this.data = data;
    }
    

    public Reader() {
    }
    

    public Reader csvFile(String input) throws FileNotFoundException, IOException {
        String row;
        Reader r = new Reader();
        BufferedReader csvReader = new BufferedReader(new FileReader(input));
        while ((row = csvReader.readLine()) != null) {
            String[] parser = row.split(",");
            if (rowC == 0) {
                for (int i = 0; i < parser.length - 1; i++) {
                    symtoms[i] = parser[i];
                }
            } 
            if (rowC > 0){
                for (int i = 0; i < parser.length - 1; i++) {
                    data[i][rowC] = Byte.valueOf(parser[i]);
                }
            }    
            disease[rowC] = parser[parser.length - 1];
            rowC++;
        }
        r.setData(data);
        r.setDisease(disease);
        r.setSymtoms(symtoms);
        csvReader.close();
        return r;
    }
}