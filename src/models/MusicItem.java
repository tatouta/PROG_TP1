package models;

import ui.Message;

public abstract class MusicItem {

    private int id;
    private String title;
    private int releaseYear;
    private boolean isPlaying;

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

    public void play() {
        this.isPlaying = true;
        Message.send("Playing Item" + getId() + " of " + this.releaseYear + " " + getTitle() + ".");
    }

    public void pause() {
        this.isPlaying = false;
        Message.send("Pausing Item" + getId() + " of " + this.releaseYear + " " + getTitle() + ".");
    }

    public void stop() {
        this.isPlaying = false;
        Message.send("Stopping Item" + getId() + " of " + this.releaseYear + " " + getTitle() + ".");
    }

    // toString method

    public String toString() {
        String string = (
                "Id=" + getId() + ", " +
                "Title=" + getTitle() + ", " +
                "Release Year=" + this.releaseYear
        );
        return string;
    }

    // Abstrait methods

    public abstract String toCSV();
    public abstract String getInfo();

}
