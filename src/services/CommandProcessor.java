package services;

import models.*;
import ui.Message;

import java.io.*;
import java.util.List;

public class CommandProcessor {

    private static final String DEFAULT_FILE_NAME = "commands";

    private static final int NUMBER_OF_ELEMENT_TO_ADD = 7;

    private static final int ID_MIN = 1;

    private static final int RELEASE_YEAR_MIN = 1850;
    private static final int RELEASE_YEAR_MAX = 2025;

    private static final int DURATION_MIN = 0;
    private static final int DURATION_MAX = 36000;

    private static final int NUMBER_OF_TRACKS_MIN = 1;
    private static final int NUMBER_OF_TRACKS_MAX = 100;

    private static final int EPISODE_NUMBER_MIN = 1;
    private static final int EPISODE_NUMBER_MAX = 500;


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
            boolean error = true;
            String commandLine = reader.readLine();
            while(commandLine != null ) {
                if (!commandLine.isEmpty() && !String.valueOf(commandLine.charAt(0)).equals("#")) {
                    boolean state = activateCommandLine(library, commandLine, fileName);
                    error = error && state;
                }
                commandLine = reader.readLine();
            }
            if (error) {
                Message.send("Error reading command file: " + fileName);
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
            case "ADD" -> add(library, parameters, commandLine);
            case "REMOVE" -> remove(library, parameters, commandLine);
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
        for (MusicItem item : items) {
            library.addItem(item);
        }
        return error;
    }

    private static boolean save(MusicLibrary library, String parameters) {
        boolean error = false;
        library.save(parameters);
        return error;
    }

    private static boolean add(MusicLibrary library, String parameters, String commandLine) {
        boolean error = true;
        if (!verifyFullParameters(parameters)) {
            Message.send("Invalid ADD command: " + commandLine);
        } else {
            String[] parts = parameters.split(",");
            if (!verifyNumberOfElementsToAdd(parts)) {
                Message.send("Wrong number of elements: " + commandLine);
            } else {
                String type = parts[0];
                String idString = parts[1];
                String releaseYearString = parts[3];
                String numberString = parts[6];
                if (!verifyItemType(type)) {
                    Message.send("Wrong music item: " + commandLine);
                } else if (!verifyUnicityOfItem(library, parts)) {
                    Message.send("ADD " + parameters + " failed; item already in library");
                } else if (!verifyId(idString)) {
                    Message.send("Invalid music ID: " + commandLine);
                } else if (!verifyUnicityOfId(library, idString)) {
                    Message.send("ADD " + parameters + " failed; ID already used.");
                } else if (!verifyReleaseYear(releaseYearString)) {
                    Message.send("Invalid release year: " + commandLine);
                } else if (isSong(type) && !verifyDuration(numberString)) {
                    Message.send("Invalid duration: " + commandLine);
                } else if (isAlbum(type) && !verifyNumberOfTracks(numberString)) {
                    Message.send("Invalid number of tracks: " + commandLine);
                } else if (isPodcast(type) && !verifyEpisodeNumber(numberString)) {
                    Message.send("Invalid episode number: " + commandLine);
                } else {
                    MusicItem item = MusicItemFactory.createFromCSV(parts);
                    if (!verifyItem(item)) {
                        Message.send("ADD item " + parameters + " failed; no such item");
                    } else {
                        library.addItem(item);
                        Message.send(item.getInfo() + " added to the library successfully.");
                        error = false;
                    }
                }
            }
        }
        return error;
    }

    private static boolean remove(MusicLibrary library, String parameters, String commandLine) {
        boolean error = true;
        if (!verifyFullParameters(parameters)) {
            Message.send("Invalid REMOVE command: " + commandLine);
        } else if (!verifyId(parameters)) {
            Message.send("Invalid ID for REMOVE command: " + parameters);
        } else {
            int id = Integer.parseInt(parameters);
            MusicItem item = library.getItem(id);
            if (!verifyItem(item)) {
                Message.send("No item found with ID: " + id);
            } else {
                library.removeItem(id);
                Message.send("Removed "+item.getInfo()+" successfully");
                error = false;
            }
        }
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

    // type methods

    private static boolean isSong(String type) {
        return type.equalsIgnoreCase("song");
    }

    private static boolean isAlbum(String type) {
        return type.equalsIgnoreCase("album");
    }

    private static boolean isPodcast(String type) {
        return type.equalsIgnoreCase("podcast");
    }

    // verify methods

    private static boolean verifyFullParameters(String parameters) {
        return !parameters.isEmpty();
    }

    private static boolean verifyNumberOfElementsToAdd(String[] parts) {
        return parts.length == NUMBER_OF_ELEMENT_TO_ADD;
    }

    private static boolean verifyUnicityOfItem(MusicLibrary library, String[] parts) {
        return !library.compareItems(parts);
    }

    private static boolean verifyItemType(String type) {
        return isSong(type) || isAlbum(type) || isPodcast(type);
    }

    private static boolean verifyId(String idString) {
        boolean success;
        try {
            int id = Integer.parseInt(idString);
            success = id >= ID_MIN;
        } catch (NumberFormatException _) {
            success = false;
        }
        return success;
    }

    private static boolean verifyUnicityOfId(MusicLibrary library, String idString) {
        boolean success;
        try {
            int id = Integer.parseInt(idString);
            success = library.getItem(id) == null;
        } catch (NumberFormatException _) {
            success = false;
        }
        return success;
    }

    private static boolean verifyReleaseYear(String releaseYearString) {
        boolean success;
        try {
            int releaseYear = Integer.parseInt(releaseYearString);
            success = releaseYear >= RELEASE_YEAR_MIN && releaseYear <= RELEASE_YEAR_MAX;
        } catch (NumberFormatException _) {
            success = false;
        }
        return success;
    }

    private static boolean verifyDuration(String durationString) {
        boolean success;
        try {
            int duration = Integer.parseInt(durationString);
            success = duration >= DURATION_MIN && duration <= DURATION_MAX;
        } catch (NumberFormatException _) {
            success = false;
        }
        return success;
    }

    private static boolean verifyNumberOfTracks(String numberOfTracksString) {
        boolean success;
        try {
            int numberOfTracks = Integer.parseInt(numberOfTracksString);
            success = numberOfTracks >= NUMBER_OF_TRACKS_MIN && numberOfTracks <= NUMBER_OF_TRACKS_MAX;
        } catch (NumberFormatException _) {
            success = false;
        }
        return success;
    }

    private static boolean verifyEpisodeNumber(String episodeNumberString) {
        boolean success;
        try {
            int episodeNumber = Integer.parseInt(episodeNumberString);
            success = episodeNumber >= EPISODE_NUMBER_MIN && episodeNumber <= EPISODE_NUMBER_MAX;
        } catch (NumberFormatException _) {
            success = false;
        }
        return success;
    }

    private static boolean verifyItem(MusicItem item) {
        return item != null;
    }

}
