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

    // pour ajouter un élément de musique à la librairie.
    public void addItem( MusicItem item ) {
        this.items.add(item);
    }

    // pour retirer l'élément de musique avec l'id de la librairie.
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
        return index;
    }

    public void searchItem(int id) {
        MusicItem item = findItem(id);
        if (item != null) {
            this.search = findIndex(id);
        }
    }

    // pour lister les éléments de musique présents dans la librairie.
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

    // pour lancer/jouer l'élément musical id.
    public void playItem( int id ) {
        MusicItem item = this.items.get(this.search);
        item.play();
    }

    // pour mettre sur pause l'élément musical qui joue.
    public void pauseItem() {
        MusicItem item = this.items.get(this.search);
        item.pause();
    }

    // pour arrêter l'élément musical qui joue.
    public void stopItem() {
        MusicItem item = this.items.get(this.search);
        item.stop();
    }

    // pour vider la liste des éléments musicaux présents dans la librairie
    public void clearAllItems() {
        this.items.clear();
        Message.send("Music library has been cleared successfully.");
    }

    public void save(String filePath) {
        MusicLibraryFileHandler.saveLibrary(this.items, filePath);
    }

}
