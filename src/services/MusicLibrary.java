package services;

import models.MusicItem;
import ui.Message;

import java.util.ArrayList;

public class MusicLibrary {

    private ArrayList <MusicItem> items;
    private MusicItem playing;
    private MusicItem search;

    public MusicLibrary() {
        this.items = new ArrayList<>();
    }

    public MusicLibrary(ArrayList <MusicItem> items) {
        this.items = items;
    }

    // item methods

    public void addItem( MusicItem item ) {
        this.items.add(item);
    }

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

    public MusicItem getItem(int id) {
        MusicItem item = null;
        int index = getIndex(id);
        if (index > -1) {
            item = this.items.get(index);
        }
        return item;
    }

    public MusicItem getItem(String title, String artist) {
        MusicItem item = null;
        int index = getIndex(title, artist);
        if (index > -1) {
            item = this.items.get(index);
        }
        return item;
    }

    public void removeItem( int id ) {
        int index = getIndex(id);
        if (index > -1) {
            this.items.remove(index);
        }
    }

    public void searchItem(int id) {
        MusicItem item = getItem(id);
        if (item != null) {
            this.search = item;
        }
    }

    public void searchItem(String title, String artist) {
        MusicItem item = getItem(title, artist);
        if (item != null) {
            this.search = item;
        }
    }

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

    public void playItem(MusicItem item) {
        this.playing = item;
        this.playing.play();
    }

    public void playItem() {
        playItem(this.search);
    }
    
    public void playItem(int id) {
        MusicItem item = getItem(id);
        if (item != null) {
            playItem(item);
        }
    }

    public void playItem(String title, String artist) {
        MusicItem item = getItem(title, artist);
        if (item != null) {
            playItem(item);
        }
    }

    public void pauseItem() {
        this.playing.pause();
    }

    public void stopItem() {
        this.playing.stop();
        this.playing = null;
    }

    public void clearAllItems() {
        this.items.clear();
    }

    public void save(String filePath) {
        MusicLibraryFileHandler.saveLibrary(this.items, filePath);
    }

}
