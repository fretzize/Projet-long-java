package projet.java.Inventaire;
import com.badlogic.gdx.graphics.Texture;
public class Item {
    
    private String nom;
    private Texture icone;
    private ItemType type;
    private int nombre; // ça va être ici les degats de l'arme et le nombre de vie que recupere le joueur
    private int range; // pour les armes la distance d'action

    public enum ItemType {
        ARME, POTION, POTIONVITESSE
    }

    public Item(String nom, Texture icone, ItemType type, int nombre, int range) {
        this.nom = nom;
        this.icone = icone;
        this.type = type;
        this.nombre = nombre;
        this.range = range;
    }

    public String getNom() { return nom; }
    public Texture getIcone() { return icone; }
    public ItemType getType() { return type; }
    public int getNombre() {return nombre; }
    public int getRange() {return range; }

     public void dispose() {
        if (icone != null) {
            icone.dispose();
        }
    }

    

}