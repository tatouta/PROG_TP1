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

    private static final int DURATION_MIN = 1;
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
            case "ADD" -> add(library, parameters, commandLine);
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
        String errorMessage = "Invalid ADD command: " + commandLine;
        if (parameters.isEmpty()) {
            Message.send(errorMessage);
        } else {
            errorMessage = "Wrong number of elements: " + commandLine;
            String[] parts = parameters.split(",");
            if (parts.length != NUMBER_OF_ELEMENT_TO_ADD) {
                Message.send(errorMessage);
            } else {
                errorMessage = "ADD " + parameters + " failed; item already in library";
                if (library.compareItems(parts)) {
                    Message.send(errorMessage);
                } else {
                    errorMessage = "Wrong music item: " + commandLine;
                    String type = parts[0];
                    boolean isSong = type.equalsIgnoreCase("song");
                    boolean isAlbum = type.equalsIgnoreCase("album");
                    boolean isPodcast = type.equalsIgnoreCase("podcast");
                    if (!isSong && !isAlbum && !isPodcast) {
                        Message.send(errorMessage);
                    } else {
                        errorMessage = "Invalid music ID: " + commandLine;
                        try {
                            int id = Integer.parseInt(parts[1]);
                            if (id < ID_MIN) {
                                Message.send(errorMessage);
                            } else {
                                errorMessage = "ADD " + parameters + " failed; ID already used.";
                                if (library.getItem(id) != null) {
                                    Message.send(errorMessage);
                                } else {
                                    errorMessage = "Invalid release year: " + commandLine;
                                    try {
                                        int releaseYear = Integer.parseInt(parts[3]);
                                        if (releaseYear < RELEASE_YEAR_MIN || releaseYear > RELEASE_YEAR_MAX) {
                                            Message.send(errorMessage);
                                        } else {
                                            boolean success = false;
                                            if (isSong) {
                                                errorMessage = "Invalid duration: " + commandLine;
                                                try {
                                                    int duration = Integer.parseInt(parts[6]);
                                                    if (duration < DURATION_MIN || duration > DURATION_MAX) {
                                                        Message.send(errorMessage);
                                                    } else {
                                                        success = true;
                                                    }
                                                } catch (NumberFormatException _) {
                                                    Message.send(errorMessage);
                                                }
                                            } else if (isAlbum) {
                                                errorMessage = "Invalid number of tracks: " + commandLine;
                                                try {
                                                    int numberOfTracks = Integer.parseInt(parts[6]);
                                                    if (numberOfTracks < NUMBER_OF_TRACKS_MIN || numberOfTracks > NUMBER_OF_TRACKS_MAX) {
                                                        Message.send(errorMessage);
                                                    } else {
                                                        success = true;
                                                    }
                                                } catch (NumberFormatException _) {
                                                    Message.send(errorMessage);
                                                }
                                            } else {
                                                errorMessage = "Invalid episode number: " + commandLine;
                                                try {
                                                    int episodeNumber = Integer.parseInt(parts[6]);
                                                    if (episodeNumber < EPISODE_NUMBER_MIN || episodeNumber > EPISODE_NUMBER_MAX) {
                                                        Message.send(errorMessage);
                                                    } else {
                                                        success = true;
                                                    }
                                                } catch (NumberFormatException _) {
                                                    Message.send(errorMessage);
                                                }
                                            }
                                            if (success) {
                                                errorMessage = "ADD item " + parameters + " failed; no such item";
                                                MusicItem item = MusicItemFactory.createFromCSV(parts);
                                                if (item == null) {
                                                    Message.send(errorMessage);
                                                } else {
                                                    library.addItem(item);
                                                    Message.send(item.getTrigger() + " added to the library successfully.");
                                                    error = false;
                                                }
                                            }
                                        }
                                    } catch (NumberFormatException _) {
                                        Message.send(errorMessage);
                                    }
                                }
                            }
                        } catch (NumberFormatException _) {
                            Message.send(errorMessage);
                        }
                    }
                }

            }
        }
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
