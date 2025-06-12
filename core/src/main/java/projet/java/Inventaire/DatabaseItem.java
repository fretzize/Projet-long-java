package projet.java.Inventaire;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import projet.java.Inventaire.Potion;
import projet.java.entite.ArmeMelee;
import projet.java.entite.Arme;
import projet.java.entite.ArmeDistance;
import projet.java.Inventaire.Item;
import java.util.Random;



public class DatabaseItem {
    private List<Item> database = new ArrayList<>();

    // private Map<Item, Arme> database = new HashMap<>();
    
    private String potion_vie = "potion_de_vie.png";
    private String potion_vitesse = "potion_de_vitesse.png";
    private String arme1 = "epee1.png";
    private String arme2 = "epee2.png";
    private String arme3 = "epee3.png";
    private String hache = "HERCULEpng/HERCULEpng/hache.png";
    private String canon = "canon.png";
    
    

    Item Arme1 = new Item("arme1", arme1, Item.ItemType.ARME, 20, 20, 150f, 0, 0, null, 20);
    Item Arme2 = new Item("arme2", arme2, Item.ItemType.ARME, 100, 10, 300f, 0, 0, null, 20);
    Item Arme3 = new Item("arme3", arme3, Item.ItemType.ARME, 100, 10, 300f, 0, 0, null, 30);
    Item Hache = new Item("hache", hache, Item.ItemType.ARME, 100, 15, 300f, 0, 0, null, 50);
    Item Canon = new Item("canon", canon, Item.ItemType.PISTOLET, 1, 0, 300f, 0, 1, "FB001.png", 10);
    Item Potion1 = new Item("potion_vie", potion_vie, Item.ItemType.POTION, 3,0, 0, 0, 0, null, 0);
    Item Potion2 = new Item("potion_vitesse", potion_vitesse, Item.ItemType.POTIONVITESSE, 60, 0, 0, 0, 0, null, 0);
    // ArmeMelee armeMelee1 = new ArmeMelee("Épée", 20, 0, 35f, 0.4f, "menubackground.png", 90f, 150f);
    // ArmeMelee armeMelee2= new ArmeMelee("Hache", 40, 0, 65f, 1f, "menubackground.png", 90f, 150f);
    // ArmeDistance armeDistance1 = new ArmeDistance("Canon", 1, 1, 70f, 0.1f, "menubackground.png", "FB001.png" , 50, 20, 45);


    public DatabaseItem() {
        database.add(Arme1);
        database.add(Arme2);
        database.add(Arme3);
        database.add(Hache);
        database.add(Canon);
        database.add(Potion1);
        database.add(Potion2);
    }

    public List<Item> getData() {
        return this.database;
    }

    public Item getItemAlea() {
        int taille = database.size();
        Random rand = new Random();
        int index = rand.nextInt(taille);
        return database.get(index);
    }
}
