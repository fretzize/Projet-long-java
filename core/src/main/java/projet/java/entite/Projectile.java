package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import projet.java.Main;

/**
 * Classe représentant un projectile tiré par une arme à distance.
 * Gère le déplacement, la collision et le rendu du projectile.
 */
public class Projectile {
    private Vector2 position;           // Position actuelle du projectile
    private Vector2 vitesse;            // Vecteur vitesse du projectile
    private Vector2 originPosition;     // Position de départ du projectile
    private Texture texture;            // Apparence visuelle du projectile
    private int degats;                 // Dégâts infligés en cas de collision
    private float porteeMax;            // Distance maximale que le projectile peut parcourir
    private Rectangle hitbox;           // Zone de collision du projectile
    private boolean actif = true;       // Indique si le projectile est toujours actif

    /**
     * Constructeur du projectile.
     * 
     * @param x Position X initiale
     * @param y Position Y initiale
     * @param vx Composante X du vecteur vitesse
     * @param vy Composante Y du vecteur vitesse
     * @param texture Texture utilisée pour le rendu
     * @param degats Dégâts infligés en cas de collision
     * @param porteeMax Distance maximale que le projectile peut parcourir
     * @param hitbox Hitbox du projectile pour la détection de collision
     */
    public Projectile(float x, float y, float vx, float vy, Texture texture, int degats, float porteeMax, Rectangle hitbox) {
        this.position = new Vector2(x, y);
        this.originPosition = new Vector2(x, y);
        this.vitesse = new Vector2(vx, vy);
        this.texture = texture;
        this.degats = degats;
        this.porteeMax = porteeMax;
        //this.hitbox = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        this.hitbox = hitbox; // Utilisation de la hitbox fournie
    }

     /**
     * Constructeur du projectile.
     * 
     * @param x Position X initiale
     * @param y Position Y initiale
     * @param vx Composante X du vecteur vitesse
     * @param vy Composante Y du vecteur vitesse
     * @param texture Texture utilisée pour le rendu
     * @param degats Dégâts infligés en cas de collision
     * @param porteeMax Distance maximale que le projectile peut parcourir
     */
    public Projectile(float x, float y, float vx, float vy, Texture texture, int degats, float porteeMax) {
        this.position = new Vector2(x, y);
        this.originPosition = new Vector2(x, y);
        this.vitesse = new Vector2(vx, vy);
        this.texture = texture;
        this.degats = degats;
        this.porteeMax = porteeMax;
        this.hitbox = new Rectangle(x, y, 10, 10);
    }

    /**
     * Constructeur simplifié sans gestion des dégâts et de la portée.
     * Pour compatibilité avec l'ancien code.
     */
    //public Projectile(float x, float y, float vx, float vy, Texture texture) {
      //  this(x, y, vx, vy, texture, 1, 1000f); // Valeurs par défaut
    //}

    /**
     * Met à jour la position et l'état du projectile.
     * 
     * @param delta Temps écoulé depuis la dernière mise à jour
     */
    public void update(float delta) {
        // Met à jour la position en fonction de la vitesse
        position.add(vitesse.x * delta, vitesse.y * delta);
        
        // Met à jour la hitbox pour suivre la position du projectile
        hitbox.setPosition(position.x, position.y);
        
        // Vérifie si le projectile a dépassé sa portée maximale
        float distanceParcourue = position.dst(originPosition);
        if (distanceParcourue > porteeMax) {
            actif = false;  // Désactive le projectile
        }
    }

    
    /**
     * Dessine le projectile à l'écran.
     * 
     * @param batch SpriteBatch pour le rendu
     */
    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
    
    /**
     * Indique si le projectile doit être détruit.
     * 
     * @return true si le projectile n'est plus actif (collision ou hors portée)
     */
    public boolean doitEtreDetruit() {
        return !actif || isHorsPortee();
    }
    
    /**
     * Marque le projectile comme inactif après une collision.
     */
    public void toucher() {
        actif = false;
    }
    
    /**
     * Renvoie la hitbox du projectile pour la détection de collision.
     * 
     * @return Rectangle représentant la hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
    
    /**
     * Renvoie les dégâts que le projectile inflige.
     * 
     * @return Valeur des dégâts
     */
    public int getDegats() {
        return degats;
    }
    
    /**
     * Renvoie la position actuelle du projectile.
     * 
     * @return Vector2 avec les coordonnées actuelles
     */
    public Vector2 getPosition() {
        return position;
    }
    
    /**
     * Renvoie le vecteur vitesse du projectile.
     * Utile pour calculer la direction de l'impact.
     * 
     * @return Le vecteur vitesse
     */
    public Vector2 getVitesse() {
        return vitesse;
    }

    public boolean isActif() {
        return actif;
    }


    public boolean isHorsPortee() {
        return position.dst(originPosition) > porteeMax;
    }
    
    /**
     * Change les dégâts du projectile.
     * Utile pour les bonus/malus en cours de partie.
     * 
     * @param nouveauxDegats Nouveaux dégâts à appliquer
     */
    public void setDegats(int nouveauxDegats) {
        this.degats = nouveauxDegats;
    }


    // Dans la classe Projectile, modifiez la méthode draw :
    public void draw(Main game, float width, float height, float x, float y) {
        // Au lieu d'utiliser this.x et this.y pour la position, utiliser x et y fournis
        game.batch.draw(this.texture, x, y, width, height);
    }
}