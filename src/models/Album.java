package models;

import ui.Message;

public class Album extends MusicItem {

    private String artist;
    private String label;
    private int numberOfTracks;

    public Album(String[] parts) {
        // might get an error for id and releaseYear (string not convertable to integer)
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[1]));
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
        string = getInfo() + " ["  + (
                string + ", " +
                "Artist=" + this.artist + ", " +
                "Number Of Tracks=" + this.numberOfTracks + ", " +
                "Label=" + this.label
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

    // abstract methods

    public String toCSV() {
        String csv = (
                getInfo().toLowerCase() + "," +
                getId() + "," +
                getTitle() + "," +
                getReleaseYear() + "," +
                this.artist + "," +
                this.numberOfTracks + "," +
                this.label
        );
        return csv;
    }

    public String getInfo() {
        return (
                getType() + " of " +
                getReleaseYear() + " " +
                getTitle() + " by " +
                this.artist
        );
    }

}
