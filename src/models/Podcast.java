package models;

import ui.Message;

public class Podcast extends MusicItem {

    private String host;
    private String topic;
    private int episodeNumber;

    public Podcast(String[] parts) {
        // might get an error for id and releaseYear (string not convertable to integer)
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[1]));
        this.host = parts[4];
        this.topic = parts[5];
        this.episodeNumber = Integer.parseInt(parts[6]);
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

    // Message methods

    public String toString() {
        String string = super.toString();
        string = getInfo() + " ["  + (
                string + ", " +
                "Host=" + this.host + ", " +
                "Episode Number=" + this.episodeNumber + ", " +
                "Topic=" + this.topic
        ) + "]";
        return string;
    }

    public String getTrigger() {
        return (
                getInfo() + " of " +
                        getReleaseYear() + " " +
                        getTitle() + "by " +
                        this.host
        );
    }

    // abstract methods

    public String toCSV() {
        String csv = (
                getInfo().toLowerCase() + "," +
                getId() + "," +
                getTitle() + "," +
                getReleaseYear() + "," +
                this.host + "," +
                this.episodeNumber + "," +
                this.topic
        );
        return csv;
    }

    public String getInfo() {
        return "Podcast";
    }

}
