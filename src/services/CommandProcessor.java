package services;

import models.*;
import ui.Message;

import java.io.*;
import java.util.List;

public class CommandProcessor {

    private static boolean exiting = false;

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

    // exiting methods

    private static boolean getExiting() {
        return exiting;
    }

    private static void setExiting(boolean value) {
        exiting = value;
    }

    // processing command text file method

    public static void processCommands(MusicLibrary library) {
        processFile(library, "");
    }

    public static void processFile(MusicLibrary library, String fileName) {
        if (fileName.isEmpty()) {
            fileName = DEFAULT_FILE_NAME;
        }
        String filePath = "data/" + fileName + ".txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Message.send("Sourcing " + fileName + "...");
            boolean error = true;
            int line = 1;
            String commandLine = reader.readLine();
            while(commandLine != null && !getExiting()) {
                if (!commandLine.isEmpty() && !String.valueOf(commandLine.charAt(0)).equals("#")) {
                    //Message.send(String.valueOf(line));
                    //Message.send(commandLine);
                    boolean success = activateCommandLine(library, commandLine, fileName);
                    //Message.send("==========================================================================================");
                    error = error && !success;
                }
                line += 1;
                commandLine = reader.readLine();
            }
            setExiting(false);
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
        boolean success = switch (command.toUpperCase()) {
            case "SOURCE" -> source(library, parameters, fileName);
            case "LOAD" -> load(library, parameters);
            case "SAVE" -> save(library, parameters);
            case "ADD" -> add(library, parameters, commandLine);
            case "REMOVE" -> remove(library, parameters, commandLine);
            case "SEARCH" -> search(library, parameters, commandLine);
            case "PLAY" -> play(library, parameters, commandLine);
            case "PAUSE" -> pause(library, parameters, commandLine);
            case "STOP" -> stop(library, parameters, commandLine);
            case "LIST" -> list(library, parameters, commandLine);
            case "CLEAR" -> clear(library, parameters, commandLine);
            case "EXIT" -> exit(library, parameters, commandLine);
            default -> unknown(commandLine);
        };
        return success;
    }

    // method methods

    private static boolean source(MusicLibrary library, String parameters, String fileName) {
        boolean success = false;
        if (!verifyAvoidingSourceLoop(fileName, parameters)) {
            Message.send("Currently sourcing " + fileName + "; SOURCE ignored.");
        } else {
            success = true;
            processFile(library, parameters);
        }
        return success;
    }

    private static boolean load(MusicLibrary library, String parameters) {
        if (!verifyFullParameters(parameters)) {
            Message.send("Loading from default library file");
        } else {
            Message.send("Loading from file: " + parameters);
        }
        library.load("");
        return true;
    }

    private static boolean save(MusicLibrary library, String parameters) {
        if (!verifyFullParameters(parameters)) {
            Message.send("Saving to default library file");
        } else {
            Message.send("Saving to file: " + parameters);
        }
        library.save(parameters);
        return true;
    }

    private static boolean add(MusicLibrary library, String parameters, String commandLine) {
        boolean success = false;
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
                        Message.send(item.getInfo() + " added to the library successfully.");
                        success = true;
                        library.addItem(item);
                        library.save("");
                    }
                }
            }
        }
        return success;
    }

    private static boolean remove(MusicLibrary library, String parameters, String commandLine) {
        boolean success = false;
        if (!verifyFullParameters(parameters)) {
            Message.send("Invalid REMOVE command: " + commandLine);
        } else if (!verifyId(parameters)) {
            Message.send("Invalid ID for REMOVE command: " + parameters);
        } else {
            int id = Integer.parseInt(parameters);
            MusicItem item = library.getItem(id);
            if (!verifyItem(item)) {
                Message.send("REMOVE item " + parameters + " failed; no such item.");
            } else {
                Message.send("Removed " + item.getInfo() + " successfully.");
                success = true;
                library.removeItem(id);
                library.save("");
            }
        }
        return success;
    }

    private static boolean search(MusicLibrary library, String parameters, String commandLine) {
        boolean success = false;
        MusicItem item = null;
        if (!verifyFullParameters(parameters)) {
            Message.send("Invalid SEARCH command: " + commandLine);
        } else {
            String[] specifications = parameters.split(" by ");
            if (specifications.length == 1) {
                String idString = specifications[0];
                if (verifyId(idString)) {
                    int id = Integer.parseInt(idString);
                    item = library.getItem(id);
                }
                if (!verifyItem(item)) {
                    Message.send("SEARCH item ID " + idString + " failed; no such item");
                }
            } else if (specifications.length == 2) {
                String title = specifications[0];
                String artist = specifications[1];
                item = library.getItem(title, artist);
                if (!verifyItem(item)) {
                    Message.send("SEARCH " + title + " by " + artist + " failed; no item found.");
                }
            } else {
                Message.send("Invalid SEARCH format. Use 'SEARCH <id>' or 'SEARCH <title> by <artist>'");
            }
            if (verifyItem(item)) {
                if (!verifyIsPlaying(library)) {
                    Message.send(item.getInfo() + " is ready to PLAY");
                    success = true;
                    library.searchItem(item);
                } else {
                    Message.send(item.toString());
                }
            }
        }
        return success;
    }

    private static boolean play(MusicLibrary library, String parameters, String commandLine) {
        boolean success = false;
        MusicItem item = null;
        String[] specifications = parameters.split(" by ");
        if (!verifyFullParameters(parameters)) {
            if (!verifyIsSearch(library)) {
                Message.send("No item to PLAY.");
            } else {
                item = library.getSearch();
            }
        } else if (specifications.length == 1) {
            String idString = specifications[0];
            if (!verifyId(idString)) {
                Message.send("Invalid ID for PLAY command: " + idString);
            } else {
                int id = Integer.parseInt(idString);
                item = library.getItem(id);
                if (!verifyItem(item)) {
                    Message.send("PLAY item ID " + idString + " failed; no such item.");
                }
            }
        } else if (specifications.length == 2) {
            String title = specifications[0];
            String artist = specifications[1];
            item = library.getItem(title, artist);
            if (!verifyItem(item)) {
                Message.send("PLAY item: " + title + " by " + artist + " failed; no such item.");
            }
        }
        if (verifyItem(item)) {
            if (verifyIsPlaying(library)) {
                if (library.getPlaying().compare(item)) {
                    Message.send(library.getPlaying().getInfo() + " is already playing.");
                    item = null;
                } else {
                    stop(library, "", "");
                }
            }
            if (verifyItem(item)) {
                Message.send("Playing " + item.getInfo() + ".");
                success = true;
                library.playItem(item);
            }
        }
        return success;
    }

    private static boolean pause(MusicLibrary library, String parameters, String commandLine) {
        boolean success = false;
        if (verifyFullParameters(parameters)) {
            Message.send("Invalid PAUSE command: " + commandLine);
        } else if (!verifyIsPlaying(library)) {
            Message.send("No item to PAUSE");
        } else if (verifyIsPaused(library)) {
            Message.send(library.getPlaying().getInfo() + "; is already on pause.");
        } else {
            Message.send("Pausing " + library.getPlaying().getInfo() + ".");
            success = true;
            library.pauseItem();
        }
        return success;
    }

    private static boolean stop(MusicLibrary library, String parameters, String commandLine) {
        boolean success = false;
        if (verifyFullParameters(parameters)) {
            Message.send("nvalid STOP command: " + commandLine);
        } else if (!verifyIsPlaying(library)) {
            Message.send("No item to STOP.");
        } else {
            Message.send("Stopping " + library.getPlaying().getInfo() + ".");
            success = true;
            library.stopItem();
        }
        return success;
    }

    private static boolean list(MusicLibrary library, String parameters, String commandLine) {
        boolean success = false;
        if (verifyFullParameters(parameters)) {
            Message.send("Invalid LIST command: " + commandLine);
        } else if (verifyIsEmpty(library)) {
            Message.send("The library is empty.");
        } else {
            Message.send("Library:");
            success = true;
            library.listAllItems();
        }
        return success;
    }

    private static boolean clear(MusicLibrary library, String parameters, String commanLine) {
        boolean success = false;
        if (verifyFullParameters(parameters)) {
            Message.send("Invalid CLEAR command: " + commanLine);
        } else if (verifyIsEmpty(library)) {
            Message.send("Music library is already empty");
        } else {
            Message.send("Music library has been cleared successfully.");
            success = true;
            library.clearAllItems();
            library.save("");
        }
        return success;
    }

    private static boolean exit(MusicLibrary library, String parameters, String commandLine) {
        boolean success = false;
        if (verifyFullParameters(parameters)) {
            Message.send("Invalid EXIT command: " + commandLine);
        } else {
            Message.send("Exiting program...");
            setExiting(true);
        }
        return success;
    }

    private static boolean unknown(String commandLine) {
        Message.send("Unknown command: " + commandLine);
        return false;
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

    private static boolean verifyAvoidingSourceLoop(String currentFileName, String toFileName) {
        return currentFileName == null || !currentFileName.equals(toFileName);
    }

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

    private static boolean verifyIsEmpty(MusicLibrary library) {
        return library.isEmpty();
    }

    private static boolean verifyIsSearch(MusicLibrary library) {
        return library.isSearch();
    }

    private static boolean verifyIsPlaying(MusicLibrary library) {
        return library.isPlaying();
    }

    private static boolean verifyIsPaused(MusicLibrary library) {
        return !library.isPaused();
    }

}
