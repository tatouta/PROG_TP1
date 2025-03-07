package services;

import models.*;
import ui.Message;

import java.io.*;
import java.util.List;

public class CommandProcessor {

    private static final String DEFAULT_COMMANDS_NAME = "commands";

    // processing command text file method

    public static void processCommands(MusicLibrary library) {
        processFile(library, DEFAULT_COMMANDS_NAME);
    }

    public static void processFile(MusicLibrary library, String commandsName) {
        Message.send("Sourcing " + commandsName + "...");
        if (commandsName.isEmpty()) {
            commandsName = DEFAULT_COMMANDS_NAME;
        }
        String filePath = "data/" + commandsName + ".txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Message.send( "Commands in file commands.txt loaded successfully." );
            String command = reader.readLine();
            while(command != null ) {
                if (!command.isEmpty() && !String.valueOf(command.charAt(0)).equals("#")) {
                    activateCommand(library, commandsName, command);
                }
                command = reader.readLine();
            }
        } catch( IOException e ) {
            Message.send( "Error loading commands for file commands.txt: " + e.getMessage() );
        }
    }

    // command supplies methods

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

    // activation command method

    private static void activateCommand(MusicLibrary library, String commandsName, String command) {
        String action = getAction(command);
        String parameters = getParameters(command, action);
        switch(action.toUpperCase()) {
            case "SOURCE":
                source(library, parameters, commandsName);
                break;
            case "LOAD":
                load(library, parameters);
                break;
            case "SAVE":
                save(library, parameters);
                break;
            case "ADD":
                add(library, parameters);
                break;
            case "REMOVE":
                remove(library, parameters);
                break;
            case "SEARCH":
                search(library, parameters);
                break;
            case "PLAY":
                play(library, parameters);
                break;
            case "PAUSE":
                pause(library);
                break;
            case "STOP":
                stop(library);
                break;
            case "CLEAR":
                clear(library);
                break;
            case "LIST":
                list(library);
                break;
            default:
                Message.send("An unknown command is entered.");
        }
    }

    // method methods

    private static void source(MusicLibrary library, String parameters, String commandsName) {
        if (!commandsName.equals(parameters)) {
            processFile(library, parameters);
        } else {
            Message.send("Currently sourcing " + commandsName + "; SOURCE ignored.");
        }
    }

    private static void load(MusicLibrary library, String parameters) {
        List<MusicItem> items = MusicLibraryFileHandler.loadLibrary(parameters);
        for (int i = 0; i < items.size(); i++) {
            MusicItem item = items.get(i);
            library.addItem(item);
        }
    }

    private static void save(MusicLibrary library, String parameters) {
        library.save(parameters);
    }

    private static void add(MusicLibrary library, String parameters) {
        String[] parts = parameters.split(",");
        MusicItem item = MusicItemFactory.createFromCSV(parts);
        library.addItem(item);
        Message.send(item.getTrigger() + " added to the library successfully.");
    }

    private static void remove(MusicLibrary library, String parameters) {
        int id = Integer.parseInt(parameters);
        library.removeItem(id);
    }

    private static void search(MusicLibrary library, String parameters) {
        String[] specifications = parameters.split(" by ");
        if (specifications.length == 1) {
            int id = Integer.parseInt(specifications[0]);
            library.searchItem(id);
        } else if (specifications.length == 2) {
            String title = specifications[0];
            String artist = specifications[1];
            library.searchItem(title, artist);
        }
    }

    private static void play(MusicLibrary library, String parameters) {
        String[] specifications = parameters.split(" by ");
        if (parameters.isEmpty()) {
            library.playItem();
        } else if (specifications.length == 1) {
            int id = Integer.parseInt(specifications[0]);
            library.playItem(id);
        } else if (specifications.length == 2) {
            String title = specifications[0];
            String artist = specifications[1];
            library.playItem(title, artist);
        }
    }

    private static void pause(MusicLibrary library) {
        library.pauseItem();
    }

    private static void stop(MusicLibrary library) {
        library.stopItem();
    }

    private static void clear(MusicLibrary library) {
        library.clearAllItems();
    }

    private static void list(MusicLibrary library) {
        library.listAllItems();
    }

}
