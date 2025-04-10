package projet.java.entite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Classe représentant une arme de mêlée (épée, hache, etc.)
 * Ces armes attaquent dans une zone devant le joueur.
 */
public class ArmeMelee extends ArmeBase {
    private float angleAttaque;      // Angle d'arc de l'attaque en degrés
    private float dureeAnimation;    // Durée de l'animation d'attaque en secondes
    private float tempsAnimation;    // Compteur pour l'animation
    private boolean enAnimation;     // Indique si l'arme est en train d'effectuer une animation d'attaque
    private Rectangle zoneAttaque;   // Zone de collision de l'attaque
    
    /**
     * Constructeur d'une arme de mêlée.
     * 
     * @param nom Nom de l'arme
     * @param degats Dégâts infligés par l'arme
     * @param manaRequis Coût en mana pour utiliser l'arme
     * @param portee Distance d'attaque
     * @param vitesseAttaque Délai entre deux attaques
     * @param texturePath Chemin vers la texture
     * @param angleAttaque Angle d'arc de l'attaque en degrés
     */
    public ArmeMelee(String nom, int degats, int manaRequis, float portee, float vitesseAttaque, 
                     String texturePath, float angleAttaque) {
        super(nom, degats, manaRequis, portee, vitesseAttaque, texturePath);
        this.angleAttaque = angleAttaque;
        this.dureeAnimation = 0.3f;  // 300ms par défaut pour l'animation d'attaque
        this.zoneAttaque = new Rectangle();  // Zone de collision initialement vide
    }
    
    /**
     * Effectue une attaque avec l'arme de mêlée.
     * Crée une zone d'attaque devant le joueur et vérifie les collisions.
     * 
     * @param position Position du joueur
     * @param direction Direction de l'attaque
     */
    @Override
    public void attaquer_arme(Vector2 position, Vector2 direction) {
        if (!peutAttaquer) return;  // Vérifie si l'arme peut attaquer (pas en cooldown)
        
        // Commence l'animation d'attaque
        enAnimation = true;
        tempsAnimation = 0;
        peutAttaquer = false;  // L'arme entre en cooldown
        
        // Calcule les dimensions de la zone d'attaque
        float zoneWidth = portee;
        float zoneHeight = portee / 2;
        
        // Calcule la position de la zone d'attaque devant le joueur
        Vector2 normalizedDirection = new Vector2(direction).nor();  // Normalise la direction
        Vector2 centerZone = new Vector2(position).add(
            normalizedDirection.x * portee / 2,
            normalizedDirection.y * portee / 2
        );
        
        // Définit la zone d'attaque comme un rectangle
        zoneAttaque.set(
            centerZone.x - zoneWidth / 2,  // Position X
            centerZone.y - zoneHeight / 2,  // Position Y
            zoneWidth,  // Largeur
            zoneHeight  // Hauteur
        );
        
        // Détecte les collisions avec les ennemis
        detecterCollisions();
    }
    
    /**
     * Détecte les collisions entre la zone d'attaque et les ennemis.
     * À adapter selon votre système de gestion des entités.
     */
    private void detecterCollisions() {
        // Exemple de détection avec une liste d'ennemis (à adapter)
        // for (Ennemi ennemi : listeEnnemis) {
        //     if (zoneAttaque.overlaps(ennemi.getHitbox())) {
        //         ennemi.prendreDegat(degats);  // Inflige des dégâts à l'ennemi
        //     }
        // }
    }
    
    /**
     * Met à jour l'état de l'arme de mêlée.
     * Gère l'animation et le cooldown.
     * 
     * @param delta Temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void update(float delta) {
        super.update(delta);  // Appel de la méthode de la classe parente
        
        // Gestion de l'animation
        if (enAnimation) {
            tempsAnimation += delta;  // Incrémente le compteur d'animation
            if (tempsAnimation >= dureeAnimation) {  // Si l'animation est terminée
                enAnimation = false;  // Arrête l'animation
            }
        }
    }
    
    /**
     * Dessine l'arme et son animation d'attaque.
     * 
     * @param batch SpriteBatch pour le rendu
     */
    @Override
    public void render(SpriteBatch batch) {
        if (enAnimation) {
            // Code pour dessiner l'animation d'attaque
            // Exemple: batch.draw(texture, position.x, position.y, originX, originY, width, height, scaleX, scaleY, rotation, ...);
        }
        
        // Pour debug: dessiner la zone d'attaque
        // ShapeRenderer shapeRenderer = new ShapeRenderer();
        // shapeRenderer.begin(ShapeType.Line);
        // shapeRenderer.rect(zoneAttaque.x, zoneAttaque.y, zoneAttaque.width, zoneAttaque.height);
        // shapeRenderer.end();
    }
}