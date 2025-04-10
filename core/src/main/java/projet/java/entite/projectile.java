package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
     */
    public Projectile(float x, float y, float vx, float vy, Texture texture, int degats, float porteeMax) {
        this.position = new Vector2(x, y);  // Position initiale
        this.originPosition = new Vector2(x, y);  // Sauvegarde la position initiale
        this.vitesse = new Vector2(vx, vy);  // Vecteur vitesse
        this.texture = texture;
        this.degats = degats;
        this.porteeMax = porteeMax;
        this.hitbox = new Rectangle(x, y, texture.getWidth(), texture.getHeight());  // Hitbox aux dimensions de la texture
    }
    
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
        float distanceParcourue = position.dst(originPosition);  // Distance entre position actuelle et initiale
        if (distanceParcourue > porteeMax) {
            actif = false;  // Désactive le projectile s'il dépasse sa portée
        }
    }
    
    /**
     * Dessine le projectile à l'écran.
     * 
     * @param batch SpriteBatch pour le rendu
     */
    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);  // Dessine la texture à la position actuelle
    }
    
    /**
     * Indique si le projectile doit être détruit.
     * 
     * @return true si le projectile n'est plus actif (collision ou hors portée)
     */
    public boolean doitEtreDetruit() {
        return !actif;
    }
    
    /**
     * Marque le projectile comme inactif après une collision.
     */
    public void toucher() {
        actif = false;  // Désactive le projectile quand il touche quelque chose
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
}