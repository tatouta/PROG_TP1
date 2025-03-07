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
        String action = getAction(command);
        String parameters = getParameters(command, action);
    }

    private static String getAction(String command) {
        String action = "";
        for (int i = 0; i < command.length(); i++) {
            String character = String.valueOf(command.charAt(i));
            if (!character.equals(" ")) {
                action += character;
            } else {
                break;
            }
        }
        return action;
    }

    private static String getParameters(String command, String action) {
        String parameters = "";
        if (command.length() > action.length()) {
            parameters = command.substring(action.length() + 1);
        }
        return parameters;
    }

}
