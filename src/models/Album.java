package models;

import ui.Message;

public class Album extends MusicItem {

    private String artist;
    private String label;
    private int numberOfTracks;

    public Album(String[] parts) {
        // might get an error for id and releaseYear (string not convertable to integer)
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]));
        // might get an error for numberOfTracks (string not convertable to integer)
        this.artist = parts[4];
        this.label = parts[5];
        this.numberOfTracks = Integer.parseInt(parts[6]);
    }

    // Getters

    public String getArtist() {
        return this.artist;
    }

    public String getLabel() {
        return this.label;
    }

    public int getNumberOfTracks() {
        return this.numberOfTracks;
    }

    // Play, pause and stop methods

    public void play() {
        setIsPlaying(true);
    }

    public void pause() {
        setIsPlaying(false);
    }

    public void stop() {
        setIsPlaying(false);
    }

    // Message methods

    public String toString() {
        String string = super.toString();
        string = getType() + " [" + (
                string.substring(1, string.length() - 1) + ", " +
                "Artist=" + getArtist() + ", " +
                "Tracks=" + getNumberOfTracks() + ", " +
                "Label=" + getLabel()
        ) + "]";
        return string;
    }

    public String getType() {
        return "Album";
    }

    // compare method

    public boolean compare(String[] parts) {
        boolean identical = (
                super.compare(parts) &&
                getArtist().equals(parts[4]) &&
                getLabel().equals(parts[5]) &&
                String.valueOf(getNumberOfTracks()).equals(parts[6])
        );
        return identical;
    }

    public boolean compare(MusicItem item) {
        String csv = item.toCSV();
        String[] parts = csv.split(",");
        return compare(parts);
    }

    // abstract methods

    public String toCSV() {
        String csv = (
                getType().toLowerCase() + "," +
                getId() + "," +
                getTitle() + "," +
                getReleaseYear() + "," +
                this.artist + "," +
                this.label + "," +
                this.numberOfTracks
        );
        return csv;
    }

    public String getInfo() {
        return (
                getType() + " " +
                getTitle() + " of " +
                getReleaseYear() + " with " +
                getNumberOfTracks() + " tracks by " +
                this.artist
        );
    }

}
