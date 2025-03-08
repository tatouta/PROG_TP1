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

    // Getters

    public String getHost() {
        return this.host;
    }

    public String getTopic() {
        return this.topic;
    }

    public int getEpisodeNumber() {
        return this.episodeNumber;
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
                "Host=" + this.host + ", " +
                "Episode Number=" + this.episodeNumber + ", " +
                "Topic=" + this.topic
        ) + "]";
        return string;
    }

    public String getType() {
        return "Podcast";
    }

    // compare method

    public boolean compare(String[] parts) {
        boolean identical = (
                super.compare(parts) &&
                getArtist().equals(parts[4]) &&
                getTopic().equalsIgnoreCase(parts[5]) &&
                String.valueOf(getEpisodeNumber()).equals(parts[6])
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
                this.host + "," +
                this.episodeNumber + "," +
                this.topic
        );
        return csv;
    }

    public String getInfo() {
        return (
                getType() + " of " +
                getReleaseYear() + " " +
                getTitle() + " by " +
                this.host
        );
    }

}
