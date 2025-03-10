package models;

import ui.Message;

public class Song extends MusicItem {

    private String artist;
    private String genre;
    private int duration;

    // Constructor
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

    // Start playing the song.
    public void play() { setIsPlaying(true); }
    // Pause the playback of the song.
    public void pause() { setIsPlaying(false); }
    // Stop the playback of the song.
    public void stop() { setIsPlaying(false); }

    // To Display the information of the song as a string.
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

    // this method gets the type of music item.
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

    // This method converts the song into a CSV format.
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

    // This method provides a detailed description of the song
    public String getInfo() {
        return (
                getType() + " of " +
                getReleaseYear() + " " +
                getTitle() + " by " +
                this.artist
        );
    }

}