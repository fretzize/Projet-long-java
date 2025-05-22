package projet.java.Inventaire;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import projet.java.Inventaire.Potion;
import projet.java.Inventaire.Item;
import java.util.Random;


public class DatabaseItem {
    private List<Item> database = new ArrayList<>();
    
    private Texture potion_vie = new Texture("potion_de_vie.png");
    private Texture potion_vitesse = new Texture("potion_de_vitesse.png");
    private Texture arme1 = new Texture("epee1.png");
    private Texture arme2 = new Texture("epee2.png");
    private Texture arme3 = new Texture("epee3.png");
    
    

    Item Arme1 = new Item("arme1", arme1, Item.ItemType.ARME, 3);
    Item Arme2 = new Item("arme2", arme2, Item.ItemType.ARME, 2);
    Item Arme3 = new Item("arme3", arme3, Item.ItemType.ARME, 3);
    Item Potion1 = new Item("potion_vie", potion_vie, Item.ItemType.POTION, 3);
    Item Potion2 = new Item("potion_vitesse", potion_vitesse, Item.ItemType.POTIONVITESSE, 30);

    public DatabaseItem() {
        database.add(Arme1);
        database.add(Arme2);
        database.add(Arme3);
        database.add(Potion1);
        database.add(Potion2);
    }

    public List<Item> getData() {
        return this.database;
    }

    public Item getItemAlea() {
        int taille = database.size();
        Random rand = new Random();
        int randomNumber = rand.nextInt(taille-1);
        return database.get(randomNumber);
    }
}
