package ui;

import java.io.FileNotFoundException;

public class Main {
    //EFFECTS: Entry point for console application
    public static void main(String[] args) {
        try {
            new UI();
        } catch (FileNotFoundException e) {
            System.out.println("A critical error has occured, the program will terminate");
        }

    }
}