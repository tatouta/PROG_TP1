package models;

public class Album extends MusicItem {

    private String artist;
    private int numberOfTracks;
    private String label;

    public Album(String[] parts) {
        // might get an error for id and releaseYear (string not convertable to integer)
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[1]));
        // might get an error for numberOfTracks (string not convertable to integer)
        this.artist = parts[4];
        this.numberOfTracks = Integer.parseInt(parts[5]);
        this.label = parts[6];
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
