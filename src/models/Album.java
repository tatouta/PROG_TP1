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
                this.artist
        );
    }

    // toString method

    public String toString() {
        String string = super.toString();
        string = getInfo() + " ["  + (
                string + ", " +
                "Artist=" + this.artist + ", " +
                "Number Of Tracks=" + this.numberOfTracks + ", " +
                "Label=" + this.label
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
                this.numberOfTracks + "," +
                this.label
        );
        return csv;
    }

    public String getInfo() {
        return "Album";
    }

}
