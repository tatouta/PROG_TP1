package services;

import models.*;
import ui.Message;

import java.io.*;
import java.util.List;

public class CommandProcessor {

    private static final String DEFAULT_FILE_NAME = "commands";

    // processing command text file method

    public static void processCommands(MusicLibrary library) {
        processFile(library, DEFAULT_FILE_NAME);
    }

    public static void processFile(MusicLibrary library, String fileName) {
        if (fileName.isEmpty()) {
            fileName = DEFAULT_FILE_NAME;
        }
        String filePath = "data/" + fileName + ".txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Message.send("Sourcing " + fileName + "...");
            String commandLine = reader.readLine();
            while(commandLine != null ) {
                if (!commandLine.isEmpty() && !String.valueOf(commandLine.charAt(0)).equals("#")) {
                    boolean error = activateCommandLine(library, commandLine, fileName);
                    if (error) {
                        Message.send("Error reading command file: " + fileName);
                        break;
                    }
                }
                commandLine = reader.readLine();
            }
        } catch( IOException e ) {
            Message.send( "Sourcing " + fileName + " failed; file not found");
        }
    }

    // command supplies methods

    private static String getCommand(String commandLine) {
        String action = "";
        for (int i = 0; i < commandLine.length(); i++) {
            String character = String.valueOf(commandLine.charAt(i));
            if (!character.equals(" ")) {
                action += character;
            } else {
                break;
            }
        }
        return action;
    }

    private static String getParameters(String commandLine, String command) {
        String parameters = "";
        if (commandLine.length() > command.length()) {
            parameters = commandLine.substring(command.length() + 1);
        }
        return parameters;
    }

    // activation command method

    private static boolean activateCommandLine(MusicLibrary library, String commandLine, String fileName) {
        String command = getCommand(commandLine);
        String parameters = getParameters(commandLine, command);
        boolean error = switch (command.toUpperCase()) {
            case "SOURCE" -> source(library, parameters, fileName);
            case "LOAD" -> load(library, parameters);
            case "SAVE" -> save(library, parameters);
            case "ADD" -> add(library, parameters);
            case "REMOVE" -> remove(library, parameters);
            case "SEARCH" -> search(library, parameters);
            case "PLAY" -> play(library, parameters);
            case "PAUSE" -> pause(library);
            case "STOP" -> stop(library);
            case "CLEAR" -> clear(library);
            case "LIST" -> list(library);
            default -> true;
        };
        return error;
    }

    // method methods

    private static boolean source(MusicLibrary library, String parameters, String fileName) {
        boolean error = false;
        if (!fileName.equals(parameters)) {
            processFile(library, parameters);
        } else {
            Message.send("Currently sourcing " + fileName + "; SOURCE ignored.");
        }
        return error;
    }

    private static boolean load(MusicLibrary library, String parameters) {
        boolean error = false;
        List<MusicItem> items = MusicLibraryFileHandler.loadLibrary(parameters);
        for (int i = 0; i < items.size(); i++) {
            MusicItem item = items.get(i);
            library.addItem(item);
        }
        return error;
    }

    private static boolean save(MusicLibrary library, String parameters) {
        boolean error = false;
        library.save(parameters);
        return error;
    }

    private static boolean add(MusicLibrary library, String parameters) {
        boolean error = false;
        String[] parts = parameters.split(",");
        MusicItem item = MusicItemFactory.createFromCSV(parts);
        library.addItem(item);
        Message.send(item.getTrigger() + " added to the library successfully.");
        return error;
    }

    private static boolean remove(MusicLibrary library, String parameters) {
        boolean error = false;
        int id = Integer.parseInt(parameters);
        library.removeItem(id);
        return error;
    }

    private static boolean search(MusicLibrary library, String parameters) {
        boolean error = false;
        String[] specifications = parameters.split(" by ");
        if (specifications.length == 1) {
            int id = Integer.parseInt(specifications[0]);
            library.searchItem(id);
        } else if (specifications.length == 2) {
            String title = specifications[0];
            String artist = specifications[1];
            library.searchItem(title, artist);
        }
        return error;
    }

    private static boolean play(MusicLibrary library, String parameters) {
        boolean error = false;
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
        return error;
    }

    private static boolean pause(MusicLibrary library) {
        boolean error = false;
        library.pauseItem();
        return error;
    }

    private static boolean stop(MusicLibrary library) {
        boolean error = false;
        library.stopItem();
        return error;
    }

    private static boolean clear(MusicLibrary library) {
        boolean error = false;
        library.clearAllItems();
        return error;
    }

    private static boolean list(MusicLibrary library) {
        boolean error = false;
        library.listAllItems();
        return error;
    }

}
