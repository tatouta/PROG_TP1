package services;

import models.MusicItem;
import ui.Message;

import java.util.ArrayList;
import java.util.List;

public class MusicLibrary {

    private ArrayList <MusicItem> items;

    private MusicItem search;

    private MusicItem playing;
    private boolean paused;

    // constructor
    public MusicLibrary() {
        this.items = new ArrayList<>();
        this.paused = false;
        load("");
    }

    // Getters

    public MusicItem getSearch() {
        return this.search;
    }

    public MusicItem getPlaying() {
        return this.playing;
    }

    // get the index in the list of items searching by id.
    public int getIndex(int id) {
        int index = -1;
        for (int i = 0; i < this.items.size(); i++) {
            MusicItem item = this.items.get(i);
            if (item.getId() == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    // get the index in the list of items searching by title and artist.
    public int getIndex(String title, String artist) {
        int index = -1;
        for (int i = 0; i < this.items.size(); i++) {
            MusicItem item = this.items.get(i);
            if (item.getArtist() != null && item.getTitle().equals(title) && item.getArtist().equals(artist)) {
                index = i;
                break;
            }
        }
        return index;
    }

    // get the item in the list of items searching by id.
    public MusicItem getItem(int id) {
        MusicItem item = null;
        int index = getIndex(id);
        if (index > -1) {
            item = this.items.get(index);
        }
        return item;
    }

    // get the item in the list of items searching by title and artist.
    public MusicItem getItem(String title, String artist) {
        MusicItem item = null;
        int index = getIndex(title, artist);
        if (index > -1) {
            item = this.items.get(index);
        }
        return item;
    }

    // States

    public boolean isSearch() {
        return this.search != null;
    }

    public boolean isPlaying() {
        return this.playing != null;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    // item methods

    // To add a music item to the library.
    public void addItem( MusicItem item ) {
        this.items.add(item);
    }

    // To remove the music item with the specified ID from the library.
    public void removeItem( int id ) {
        int index = getIndex(id);
        if (index > -1) {
            this.items.remove(index);
        }
    }

    // To list all the music items present in the library.
    public void listAllItems() {
        String display = "";
        int size = this.items.size();
        int last = size - 1;
        for (int i = 0; i < size; i++) {
            MusicItem item = this.items.get(i);
            display += item.toString();
            if (i < last) {
                display += "\n";
            }
        }
        Message.send(display);
    }

    // compare an item between another item and if they are the same
    public boolean compareItems(String[] parts) {
        boolean identical = false;
        for (int i = 0; i < this.items.size(); i++) {
            MusicItem item = this.items.get(i);
            if (item.compare(parts)) {
                identical = true;
                break;
            }
        }
        return identical;
    }

    // This method finds a music item
    public void searchItem(MusicItem item) {
        this.search = item;
    }

    // To start/play the music item
    public void playItem(MusicItem item) {
        this.playing = item;
        this.playing.play();
        this.paused = false;
    }

    // play the searched item
    public void playItem() {
        playItem(this.search);
    }

    // To start/play the music item with the specified ID.
    public void playItem(int id) {
        MusicItem item = getItem(id);
        if (item != null) {
            playItem(item);
        }
    }
    // To start/play the music item with the specified title and artist.
    public void playItem(String title, String artist) {
        MusicItem item = getItem(title, artist);
        if (item != null) {
            playItem(item);
        }
    }

    // To pause the music item that is currently playing.
    public void pauseItem() {
        this.playing.pause();
        this.paused = true;
    }

    // To stop the music item that is currently playing.
    public void stopItem() {
        this.playing.stop();
        this.playing = null;
        this.paused = false;
    }

    // To clear the list of all music items present in the library.
    public void clearAllItems() {
        this.items.clear();
    }

    // This method loads music items from a file.
    public void load(String filePath) {
        List<MusicItem> items = MusicLibraryFileHandler.loadLibrary(filePath);
        for (MusicItem item : items) {
            addItem(item);
        }
    }

    // This method saves the library to a specified file.
    public void save(String filePath) {
        MusicLibraryFileHandler.saveLibrary(this.items, filePath);
    }

}