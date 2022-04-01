package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import model.EventLog;

import java.io.FileNotFoundException;

import static ui.GUI.createAndShowGUI;
import static ui.GUI.printEventLog;

public class Main extends Application {
    ////EFFECTS: Entry point for console application
    //public static void main(String[] args) {
    //    try {
    //        new UI();
    //    } catch (FileNotFoundException e) {
    //        System.out.println("A critical error has occurred, the program will terminate");
    //    }
    //}

    //PROGRAM ENTRY POINT
    //EFFECTS: entry point for the code
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> printEventLog(EventLog.getInstance())));

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}