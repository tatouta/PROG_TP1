package models;

public class MusicItem {

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
        System.out.println("play!");
    }

    public void pause() {
        System.out.println("pause!");
    }

    public void stop() {
        System.out.println("stop!");
    }

    // toString method

    public String toString() {
        String string = (
                "Id: " + this.id + "\n" +
                "Title: " + this.title + "\n" +
                "Release Year: " + this.releaseYear
        );
        return string;
    }

    // Abstrait methods

    public String toCSV() {return "";}
    public String getInfo() {return "";}

}
