package services;

import models.MusicItem;
import java.util.ArrayList;

public class MusicLibrary {

    private ArrayList <MusicItem> list;
    private int search;

    public MusicLibrary() {
        list = new ArrayList<>();
        this.search = 0;
    }

    // pour ajouter un élément de musique à la librairie.
    public void addItem( MusicItem item ) {
        this.list.add(item);
    }

    // pour retirer l'élément de musique avec l'id de la librairie.
    public void removeItem( int id ) {
        int index = findIndex(id);
        if (index > -1) {
            this.list.remove(index);
        }
    }

    public MusicItem findItem(int id) {
        MusicItem item = null;
        int index = findIndex(id);
        if (index > -1) {
            item = this.list.get(index);
        }
        return item;
    }

    public int findIndex(int id) {
        int index = -1;
        for (int i = 0; i < this.list.size(); i++) {
            MusicItem item = this.list.get(i);
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
        int size = this.list.size();
        int last = size - 1;
        for (int i = 0; i < size; i++) {
            MusicItem item = this.list.get(i);
            display += item.toString();
            if (i < last) {
                display += "\n";
            }
        }
        System.out.println(display);
    }

    // pour lancer/jouer l'élément musical id.
    public void playItem( int id ) {
        MusicItem item = this.list.get(this.search);
        item.play();
    }

    // pour mettre sur pause l'élément musical qui joue.
    public void pauseItem() {
        MusicItem item = this.list.get(this.search);
        item.pause();
    }

    // pour arrêter l'élément musical qui joue.
    public void stopItem() {
        MusicItem item = this.list.get(this.search);
        item.stop();
    }

    // pour vider la liste des éléments musicaux présents dans la librairie
    public void clearAllItems() {
        this.list.clear();
        System.out.println("Music library has been cleared successfully.");
    }

    public void save(String filePath) {
        MusicLibraryFileHandler.saveLibrary(this.list, filePath);
    }

}
