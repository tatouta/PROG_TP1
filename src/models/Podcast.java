package models;

public class Podcast extends MusicItem {

    private String host;
    private int episodeNumber;
    private String topic;

    public Podcast(String[] parts) {
        // might get an error for id and releaseYear (string not convertable to integer)
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[1]));
        this.host = parts[4];
        this.episodeNumber = Integer.parseInt(parts[5]);
        this.topic = parts[6];
    }

    // Play, pause and stop methods

    public void play() {
        setIsPlaying(true);
        System.out.println("Playing " + getTrigger() + ".");
    }

    public void pause() {
        setIsPlaying(false);
        System.out.println("Pausing " + getTrigger() + ".");
    }

    public void stop() {
        setIsPlaying(false);
        System.out.println("Stopping " + getTrigger() + ".");
    }

    private String getTrigger() {
        return (
                getInfo() + " of " +
                getReleaseYear() + " " +
                getTitle() + "by " +
                this.host
        );
    }

    // toString method

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
