package models;

import ui.Message;

public abstract class MusicItem {

    private int id;
    private String title;
    private int releaseYear;
    private boolean isPlaying;

    // Constructor
    public MusicItem(int id, String title, int releaseYear) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.isPlaying = false;
    }

    // Getters

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    public String getArtist() {
        return null;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    protected void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    // Play, pause and stop methods

    // To start playing a music item
    public void play() {
        this.isPlaying = true;
        Message.send("Playing Item" + getId() + " of " + this.releaseYear + " " + getTitle() + ".");
    }
    // To pause the playback a music item.
    public void pause() {
        this.isPlaying = false;
        Message.send("Pausing Item" + getId() + " of " + this.releaseYear + " " + getTitle() + ".");
    }
    // To stop the playback of a music item.
    public void stop() {
        this.isPlaying = false;
        Message.send("Stopping Item" + getId() + " of " + this.releaseYear + " " + getTitle() + ".");
    }

    // To display the information of a music item as a string.
    public String toString() {
        return "[" +  (
                "ID=" + getId() + ", " +
                "Title=" + getTitle() + ", " +
                "Release Year=" + this.releaseYear
        ) + "]";
    }

    // this method gets the type of music item.
    public String getType() {
        return "Item";
    }

    // compare method

    public boolean compare(String[] parts) {
        boolean identical = (
                getTitle().equals(parts[2]) &&
                String.valueOf(getReleaseYear()).equals(parts[3])
        );
        return identical;
    }

    public boolean compare(MusicItem item) {
        String csv = item.toCSV();
        String[] parts = csv.split(",");
        return compare(parts);
    }

    // Abstract methods

    public abstract String toCSV();
    public abstract String getInfo();

}