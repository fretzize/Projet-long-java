package projet.java.Inventaire;
import java.util.ArrayList;
import java.util.List;

public class Inventaire {

    private List<Item> liste_item;

    public Inventaire() {
        this.liste_item = new ArrayList<>();
    }

    public void addItem(Item item) {
        liste_item.add(item);
    }

    public void removeItem(Item item) {
        liste_item.remove(item);
    }

    public List<Item> getItems() {
        return liste_item;
    }
}