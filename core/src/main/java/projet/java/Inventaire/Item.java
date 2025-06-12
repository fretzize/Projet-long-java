package projet.java.Inventaire;
import com.badlogic.gdx.graphics.Texture;
public class Item {
    
    private String nom;
    private String textureString;
    private ItemType type;
    private int nombre; // ça va être ici les degats de l'arme et le nombre de vie que recupere le joueur
    private float range; // pour les armes la distance d'action
    private float vitesse;
    private Texture icone;
    private float angle; // pourra etre utilisé pour la largeur de la hitbox de l'arme
    // il faut rajouter le recul et le nombre de mana, la texture des prjectiles
    private float forceKnockback;
    private String texture_proj;
    private int mana;

    public enum ItemType {
        ARME, PISTOLET, POTION, POTIONVITESSE
    }

    public Item(String nom, String textureString, ItemType type, int nombre, float vitesse, float range, float angle, int mana, String texture_proj, float forceKnockback) {
        this.nom = nom;
        this.textureString = textureString;
        this.type = type;
        this.nombre = nombre;
        this.vitesse = vitesse;
        this.range = range;
        this.angle = angle;
        this.mana = mana;
        this.texture_proj = texture_proj;
        this.forceKnockback = forceKnockback;
        this.icone = new Texture(textureString);
    }

    public String getNom() { return nom; }
    public Texture getIcone() { return icone; }
    public String getTextureString() {return textureString;}
    public ItemType getType() { return type; }
    public int getNombre() {return nombre; }
    public float getRange() {return range; }
    public float getAngle() {return angle;}
    public float getVitesse() {return vitesse;}
    public int getMana() {return mana; }
    public String getTexture_proj() {return texture_proj;}
    public float getForceKnockback() {return forceKnockback;}

     public void dispose() {
        if (icone != null) {
            icone.dispose();
        }
    }

    

}