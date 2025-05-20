package projet.java.Inventaire;
import com.badlogic.gdx.graphics.Texture;
public class Item {
    
    private String nom;
    private Texture icone;
    private ItemType type;

    public enum ItemType {
        ARME, POTION
    }

    public Item(String nom, Texture icone, ItemType type) {
        this.nom = nom;
        this.icone = icone;
        this.type = type;
    }

    public String getNom() { return nom; }
    public Texture getIcone() { return icone; }
    public ItemType getType() { return type; }

     public void dispose() {
        if (icone != null) {
            icone.dispose();
        }
    }

    

}