package models;

import ui.Message;

public class Podcast extends MusicItem {

    private String host;
    private String topic;
    private int episodeNumber;

    // Constructor
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

    // Start playing the podcast.
    public void play() {
        setIsPlaying(true);
    }
    // Pause the playback of the podcast.
    public void pause() {
        setIsPlaying(false);
    }
    // Stop the playback of the podcast.
    public void stop() {
        setIsPlaying(false);
    }

    // To display the information of the podcast as a string.
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
                (getArtist() == null || getArtist().equals(parts[4])) &&
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

    // This method converts the podcast into a CSV format.
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

    // This method provides a detailed description of the podcast
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