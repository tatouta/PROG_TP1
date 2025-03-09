package models;

import ui.Message;

public class Podcast extends MusicItem {

    private String host;
    private String topic;
    private int episodeNumber;

    public Podcast(String[] parts) {
        // might get an error for id and releaseYear (string not convertable to integer)
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]));
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
        string = getType() + " [" + (
                string.substring(1, string.length() - 1) + ", " +
                "Host=" + getHost() + ", " +
                "Episode=" + getEpisodeNumber() + ", " +
                "Topic=" + getTopic()
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
                this.host + "," +
                this.topic  + "," +
                this.episodeNumber
        );
        return csv;
    }

    public String getInfo() {
        return (
                getType() + " " +
                getTitle() + " episode " +
                getEpisodeNumber() + " of " +
                getReleaseYear() + " on " +
                getTopic() + " by " +
                this.host
        );
    }

}
