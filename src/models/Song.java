package models;

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
        String conversion = "";
        return conversion;
    }

    public String getInfo() {
        return "Song";
    }

}
