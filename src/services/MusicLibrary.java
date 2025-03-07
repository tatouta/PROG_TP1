package services;

import models.MusicItem;
import ui.Message;

import java.util.ArrayList;

public class MusicLibrary {

    private ArrayList <MusicItem> items;
    private int search;

    public MusicLibrary() {
        this.items = new ArrayList<>();
        this.search = 0;
    }

    public MusicLibrary(ArrayList <MusicItem> items) {
        this.items = items;
        this.search = 0;
    }

    // item methods

    public void addItem( MusicItem item ) {
        this.items.add(item);
    }

    public void removeItem( int id ) {
        int index = findIndex(id);
        if (index > -1) {
            this.items.remove(index);
        }
    }

    public MusicItem findItem(int id) {
        MusicItem item = null;
        int index = findIndex(id);
        if (index > -1) {
            item = this.items.get(index);
        }
        return item;
    }

    public int findIndex(int id) {
        int index = -1;
        for (int i = 0; i < this.items.size(); i++) {
            MusicItem item = this.items.get(i);
            if (item.getId() == id) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            Message.send("The item with the specified ID does not exist.");
        }
        return index;
    }

    public int findIndex(String title, String artist) {
        int index = -1;
        for (int i = 0; i < this.items.size(); i++) {
            MusicItem item = this.items.get(i);
            if (!item.getInfo().equals("Podcast") && item.getTitle() == title) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            Message.send("The item with the specified ID does not exist.");
        }
        return index;
    }

    public void searchItem(int id) {
        int index = findIndex(id);
        if (index > -1) {
            this.search = findIndex(id);
        }
    }

    public void searchItem(String title, String artist) {

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

    public void playItem( int id ) {
        MusicItem item = this.items.get(this.search);
        item.play();
    }

    public void pauseItem() {
        MusicItem item = this.items.get(this.search);
        item.pause();
    }

    public void stopItem() {
        MusicItem item = this.items.get(this.search);
        item.stop();
    }

    public void clearAllItems() {
        this.items.clear();
        Message.send("Music library has been cleared successfully.");
    }

    public void save(String filePath) {
        MusicLibraryFileHandler.saveLibrary(this.items, filePath);
    }

}
