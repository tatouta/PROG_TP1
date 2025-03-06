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

    public String toCSV() {
        String conversion = "";
        return conversion;
    }

    public String getInfo() {
        String info = "";
        return info;
    }

}
