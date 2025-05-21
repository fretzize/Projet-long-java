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
     * Version originale maintenue pour compatibilité
     */
    @Override
    public void attaquer_arme(Vector2 position, Vector2 direction) {
        // Appeler la version améliorée avec une hitbox par défaut
        attaquer_arme(position, direction, new Vector2(10, 10));
    }

    /**
     * Version améliorée qui prend en compte la taille de la hitbox du personnage
     */
    @Override
    public void attaquer_arme(Vector2 position, Vector2 direction, Vector2 hitboxSize) {
        // Commence l'animation d'attaque
        enAnimation = true;
        tempsAnimation = 0;
        
        // Calcule les dimensions de la zone d'attaque
        float zoneWidth = portee;
        float zoneHeight = portee / 2;
        
        // Ajuster les dimensions selon la direction pour une meilleure jouabilité
        if (Math.abs(direction.y) > Math.abs(direction.x)) {
            // Attaque verticale (haut/bas)
            float temp = zoneWidth;
            zoneWidth = zoneHeight;
            zoneHeight = temp;
        }
        
        // Position du centre de la hitbox du personnage
        Vector2 hitboxCenter = new Vector2(
            position.x + hitboxSize.x / 2,
            position.y + hitboxSize.y / 2
        );
        
        // Normaliser la direction pour avoir un vecteur unitaire
        Vector2 normalizedDir = new Vector2(direction).nor();
        
        // Calculer où placer la hitbox d'attaque pour qu'elle soit adjacente à la hitbox du joueur
        float offsetX = (hitboxSize.x / 2) * Math.abs(normalizedDir.x);
        float offsetY = (hitboxSize.y / 2) * Math.abs(normalizedDir.y);
        
        // Position du point d'attaque (bord de la hitbox du joueur dans la direction de l'attaque)
        Vector2 attackPoint = new Vector2(hitboxCenter);
        attackPoint.x += normalizedDir.x * offsetX;
        attackPoint.y += normalizedDir.y * offsetY;
        
        // Positionner la zone d'attaque à partir de ce point
        Vector2 centerZone = new Vector2(attackPoint).add(
            normalizedDir.x * zoneWidth / 2,
            normalizedDir.y * zoneHeight / 2
        );
        
        // Définir la zone d'attaque comme un rectangle
        zoneAttaque.set(
            centerZone.x - zoneWidth / 2,
            centerZone.y - zoneHeight / 2,
            zoneWidth,
            zoneHeight
        );
        
        // Détecter les collisions avec les sbires
        detecterCollisions(position, normalizedDir);
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
            if (!sbire.enVie()) continue; // Ignorer les sbires morts
            
            // Vérification avec des logs pour déboguer
            boolean overlaps = zoneAttaque.overlaps(sbire.getHitbox());
            if (overlaps) {
                System.out.println("Hitbox overlap détecté avec sbire: " + sbire);
                
                // Vérification supplémentaire: l'sbire est-il dans l'arc d'attaque?
                Vector2 directionVersSbire = new Vector2(
                    sbire.getPositionX() + sbire.getHitbox().width/2, 
                    sbire.getPositionY() + sbire.getHitbox().height/2
                ).sub(position).nor();
                
                float angleVersSbire = directionVersSbire.angleDeg();
                float angleDifference = Math.abs(angle - angleVersSbire);
                angleDifference = angleDifference > 180 ? 360 - angleDifference : angleDifference;
                
                System.out.println("Angle d'attaque: " + angle + ", Angle vers sbire: " + angleVersSbire + 
                                  ", Différence: " + angleDifference + ", Max permis: " + (angleAttaque / 2));
                
                // Si l'sbire est dans l'arc d'attaque (défini par angleAttaque)
                if (angleDifference <= angleAttaque / 2) {
                    System.out.println("Sbire touché! Application des dégâts: " + degats);
                    
                    // Appliquer les dégâts à l'sbire
                    sbire.prendreDegats(degats);
                    
                    // Appliquer le knockback si la force est > 0
                    if (forceKnockback > 0) {
                        // Direction du knockback = direction de l'attaque
                        sbire.appliquerKnockback(normalizedDirection, forceKnockback);
                    }
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
     * Renvoie la zone d'attaque actuelle pour l'affichage ou la détection de collision.
     * 
     * @return Rectangle représentant la zone d'attaque
     */
    public Rectangle getZoneAttaque() {
        return this.zoneAttaque;
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