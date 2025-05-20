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
    private float forceKnockback;    // Force de recul appliquée aux sbires touchés
    
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
     * @param forceKnockback Force de recul appliquée aux sbires (0 pour pas de recul)
     */
    public ArmeMelee(String nom, int degats, int manaRequis, float portee, float vitesseAttaque, 
                     String texturePath, float angleAttaque, float forceKnockback) {
        super(nom, degats, manaRequis, portee, vitesseAttaque, texturePath);
        this.angleAttaque = angleAttaque;
        this.dureeAnimation = 0.3f;  // 300ms par défaut pour l'animation d'attaque
        this.zoneAttaque = new Rectangle();  // Zone de collision initialement vide
        this.forceKnockback = forceKnockback;  // Force du recul
    }
    
    /**
     * Constructeur alternatif sans knockback (par défaut à 0)
     */
    public ArmeMelee(String nom, int degats, int manaRequis, float portee, float vitesseAttaque, 
                     String texturePath, float angleAttaque) {
        this(nom, degats, manaRequis, portee, vitesseAttaque, texturePath, angleAttaque, 0);
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
        // IMPORTANT: Ne pas vérifier peutAttaquer ici, car c'est déjà géré dans AttackManager
        // if (!peutAttaquer) return;
        
        // Commence l'animation d'attaque
        enAnimation = true;
        tempsAnimation = 0;
        
        // NE PAS modifier peutAttaquer ici
        // peutAttaquer = false;
        
        // Calcule les dimensions de la zone d'attaque (reste inchangé)
        float zoneWidth = portee;
        float zoneHeight = portee / 2;
        
        // Calcule la position de la zone d'attaque devant le joueur (reste inchangé)
        Vector2 normalizedDirection = new Vector2(direction).nor();
        Vector2 centerZone = new Vector2(position).add(
            normalizedDirection.x * portee / 2,
            normalizedDirection.y * portee / 2
        );
        
        // Définit la zone d'attaque comme un rectangle (reste inchangé)
        zoneAttaque.set(
            centerZone.x - zoneWidth / 2,
            centerZone.y - zoneHeight / 2,
            zoneWidth,
            zoneHeight
        );
        
        // Détecte les collisions avec les sbires (reste inchangé)
        detecterCollisions(position, normalizedDirection);
    }
    
    /**
     * Détecte les collisions entre la zone d'attaque et les sbires.
     * Applique dégâts et knockback aux sbires touchés.
     */
    private void detecterCollisions(Vector2 position, Vector2 normalizedDirection) {
        // Obtenir la liste des sbires
        Array<Sbire> sbires = niveau.getSbires();
        
        // Calculer l'angle de l'attaque basé sur la direction
        float angle = new Vector2(normalizedDirection).angleDeg();
        
        for (Sbire sbire : sbires) {
            // Vérification simple avec le rectangle
            if (zoneAttaque.overlaps(sbire.getHitbox())) {
                // Vérification supplémentaire: l'sbire est-il dans l'arc d'attaque?
                Vector2 directionVersSbire = new Vector2(sbire.getPositionX(),sbire.getPositionY()).sub(position).nor();
                float angleVersSbire = directionVersSbire.angleDeg();
                float angleDifference = Math.abs(angle - angleVersSbire);
                angleDifference = angleDifference > 180 ? 360 - angleDifference : angleDifference;
                
                // Si l'sbire est dans l'arc d'attaque (défini par angleAttaque)
                if (angleDifference <= angleAttaque / 2) {
                    // Appliquer les dégâts à l'sbire
                    sbire.prendreDegats(degats);
                    
                    // Appliquer le knockback si la force est > 0
                    if (forceKnockback > 0) {
                        // Direction du knockback = direction de l'attaque
                        // Alternative: directionVersSbire pour pousser dans la direction exacte de l'sbire
                        sbire.appliquerKnockback(normalizedDirection, forceKnockback);
                    }
                    
                    // Effet visuel: projection de sang ou particules
                    spawnParticules(new Vector2(sbire.getPositionX(),sbire.getPositionY()), 10); // 10 particules
                }
            }
        }
    }
    
    // Méthode pour créer un effet de particules
    private void spawnParticules(Vector2 position, int count) {
        // Code pour générer des particules d'impact
        // À implémenter avec le système de particules de LibGDX
    }
    
    /**
     * Modifie la force du knockback de l'arme.
     * Utile pour les améliorations d'armes en cours de jeu.
     * 
     * @param force Nouvelle force de knockback
     */
    public void setForceKnockback(float force) {
        this.forceKnockback = force;
    }
    
    /**
     * @return Force actuelle du knockback
     */
    public float getForceKnockback() {
        return forceKnockback;
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