package models;

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

    // Play, pause and stop methods

    public void play() {
      // empty...
    }

    public void pause() {
        // empty...
    }

    public void stop() {
        // empty...
    }

    // toString method

    public String toString() {
        return this.title;
    }

    // Abstrait methods

    public abstract String toCSV();
    public abstract String getInfo();

}
