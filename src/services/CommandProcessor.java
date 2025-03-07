package services;

import models.MusicItem;
import models.MusicItemFactory;
import ui.Message;

import java.io.*;

public class CommandProcessor {

    public static void processCommands(MusicLibrary library) {
        String filePath = "data/commands.txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String command = reader.readLine();
            while(command != null ) {
                activateCommand(library, command);
                command = reader.readLine();
            }
            Message.send( "Commands in file commands.txt loaded successfully." );
        } catch( IOException e ) {
            Message.send( "Error loading commands for file commands.txt: " + e.getMessage() );
        }
    }

    private static void activateCommand(MusicLibrary library, String command) {

    }

}
