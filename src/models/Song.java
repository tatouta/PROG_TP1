package models;

import ui.Message;

public class Song extends MusicItem {

    private String artist;
    private String genre;
    private int duration;

    public Song(String[] parts) {
        // might get an error for id and releaseYear (string not convertable to integer)
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]));
        // might get an error for duration (string not convertable to integer)
        this.artist = parts[4];
        this.genre = parts[5];
        this.duration = Integer.parseInt(parts[6]);
    }

    // Getters

    public String getArtist() {
        return this.artist;
    }

    public String getGenre() {
        return this.genre;
    }

    public int getDuration() {
        return this.duration;
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
                "Genre=" + getGenre() + ", " +
                "Duration=" + getDuration() + "s"
        ) + "]";
        return string;
    }

    public String getType() {
        return "Song";
    }

    // compare method

    public boolean compare(String[] parts) {
        boolean identical = (
                super.compare(parts) &&
                getArtist().equals(parts[4]) &&
                getGenre().equals(parts[5]) &&
                String.valueOf(getDuration()).equals(parts[6])
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
                this.genre + "," +
                this.duration
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
