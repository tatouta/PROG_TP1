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

    // Play, pause and stop methods

    public void play() {
        setIsPlaying(true);
        Message.send("Playing " + getTrigger() + ".");
    }

    public void pause() {
        setIsPlaying(false);
        Message.send("Pausing " + getTrigger() + ".");
    }

    public void stop() {
        setIsPlaying(false);
        Message.send("Stopping " + getTrigger() + ".");
    }

    private String getTrigger() {
        return (
                getInfo() + " of " +
                getReleaseYear() + " " +
                getTitle() + "by " +
                this.artist
        );
    }

    // toString method

    public String toString() {
        String string = super.toString();
        string = getInfo() + " [" + (
                string + ", " +
                "Artist=" + this.artist + ", " +
                "Genre=" + this.genre + ", " +
                "Duration=" + this.duration + "s"
        ) + "]";
        return string;
    }

    // abstract methods

    public String toCSV() {
        String csv = (
                getInfo().toLowerCase() + "," +
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
        return "Song";
    }

}
