package services;

import models.MusicItem;
import models.MusicItemFactory;
import ui.Message;

import java.io.*;

public class CommandProcessor {

    public static void processCommands(MusicLibrary library) {
        String filePath = "data/commands.txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Message.send( "Commands in file commands.txt loaded successfully." );
            String command = reader.readLine();
            while(command != null ) {
                if (!command.isEmpty() && !String.valueOf(command.charAt(0)).equals("#")) {
                    activateCommand(library, command);
                }
                command = reader.readLine();
            }
        } catch( IOException e ) {
            Message.send( "Error loading commands for file commands.txt: " + e.getMessage() );
        }
    }

    private static void activateCommand(MusicLibrary library, String command) {
        Message.send(command);
    }

}
