package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Classe représentant une arme à distance (arc, pistolet, etc.)
 * Ces armes tirent des projectiles qui voyagent sur une certaine distance.
 */
public class ArmeDistance extends ArmeBase {
    private Texture projectileTexture;      // Texture des projectiles
    private Array<Projectile> projectiles;  // Liste des projectiles actifs
    private float vitesseProjectile;        // Vitesse des projectiles
    private int nbProjectilesParTir;        // Nombre de projectiles tirés à chaque utilisation
    private float angleDispersion;          // Angle de dispersion entre projectiles (shotgun, etc.)
    
    /**
     * Constructeur d'une arme à distance.
     * 
     * @param nom Nom de l'arme
     * @param degats Dégâts infligés par chaque projectile
     * @param manaRequis Coût en mana pour utiliser l'arme
     * @param portee Distance maximale des projectiles
     * @param vitesseAttaque Délai entre deux tirs
     * @param texturePath Chemin vers la texture de l'arme
     * @param projectileTexturePath Chemin vers la texture des projectiles
     * @param vitesseProjectile Vitesse de déplacement des projectiles
     * @param nbProjectiles Nombre de projectiles tirés à chaque utilisation
     * @param angleDispersion Angle de dispersion entre les projectiles (en degrés)
     */
    public ArmeDistance(String nom, int degats, int manaRequis, float portee, float vitesseAttaque,
                       String texturePath, String projectileTexturePath, float vitesseProjectile, 
                       int nbProjectiles, float angleDispersion) {
        super(nom, degats, manaRequis, portee, vitesseAttaque, texturePath);
        this.projectileTexture = new Texture(projectileTexturePath);  // Charge la texture du projectile
        this.projectiles = new Array<>();  // Initialise la liste de projectiles
        this.vitesseProjectile = vitesseProjectile;
        this.nbProjectilesParTir = nbProjectiles;
        this.angleDispersion = angleDispersion;
    }
    
    /**
     * Effectue une attaque avec l'arme à distance.
     * Crée et lance un ou plusieurs projectiles.
     * 
     * @param position Position du joueur
     * @param direction Direction du tir
     */
    @Override
    public void attaquer_arme(Vector2 position, Vector2 direction) {
        if (!peutAttaquer) return;  // Vérifie si l'arme peut attaquer (pas en cooldown)
        
        // Normalise la direction pour avoir un vecteur unitaire
        Vector2 dir = new Vector2(direction).nor();
        
        // Crée et lance les projectiles
        if (nbProjectilesParTir == 1) {
            // Un seul projectile dans la direction donnée
            creerProjectile(position, dir);
        } else {
            // Plusieurs projectiles avec dispersion (comme pour un shotgun)
            float angleTotal = angleDispersion;
            float angleStep = angleTotal / (nbProjectilesParTir - 1);  // Angle entre chaque projectile
            float startAngle = -angleTotal / 2;  // Angle de départ (centré)
            
            for (int i = 0; i < nbProjectilesParTir; i++) {
                float currentAngle = startAngle + (angleStep * i);  // Angle courant
                Vector2 rotatedDir = new Vector2(dir).rotate(currentAngle);  // Direction rotée
                creerProjectile(position, rotatedDir);  // Crée un projectile dans cette direction
            }
        }
        
        // Met l'arme en cooldown
        peutAttaquer = false;
        tempsDepuisDerniereAttaque = 0;
    }
    
    /**
     * Crée un nouveau projectile à partir de la position du joueur dans la direction spécifiée.
     * 
     * @param position Position initiale du projectile
     * @param direction Direction dans laquelle le projectile va se déplacer
     */
    private void creerProjectile(Vector2 position, Vector2 direction) {
        // Crée un nouveau projectile avec la position, direction et caractéristiques
        Projectile projectile = new Projectile(
            position.x, position.y,  // Position initiale
            direction.x * vitesseProjectile, direction.y * vitesseProjectile,  // Vitesse (direction * magnitude)
            projectileTexture, degats, portee  // Texture, dégâts et portée
        );
        projectiles.add(projectile);  // Ajoute le projectile à la liste des projectiles actifs
    }
    
    /**
     * Met à jour l'état de l'arme à distance et de ses projectiles.
     * 
     * @param delta Temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void update(float delta) {
        super.update(delta);  // Appel de la méthode de la classe parente
        
        // Mettre à jour tous les projectiles actifs
        for (int i = projectiles.size - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.update(delta);  // Met à jour la position du projectile
            
            // Supprime les projectiles qui ont dépassé leur portée ou touché une cible
            if (projectile.doitEtreDetruit()) {
                projectiles.removeIndex(i);
            }
        }
    }
    
    /**
     * Dessine l'arme et ses projectiles.
     * 
     * @param batch SpriteBatch pour le rendu
     */
    @Override
    public void render(SpriteBatch batch) {
        // Dessine tous les projectiles actifs
        for (Projectile projectile : projectiles) {
            projectile.draw(batch);
        }
    }
    
    /**
     * Libère les ressources utilisées par l'arme et ses projectiles.
     */
    @Override
    public void dispose() {
        super.dispose();  // Appel de la méthode de la classe parente
        projectileTexture.dispose();  // Libère la mémoire de la texture des projectiles
    }
    
    /**
     * Renvoie la liste des projectiles actifs pour la détection de collision.
     * 
     * @return Liste de projectiles actifs
     */
    public Array<Projectile> getProjectiles() {
        return projectiles;
    }
}