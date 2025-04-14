package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
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
    private Niveau niveau;                  // Référence au niveau actuel (pour accéder aux ennemis)
    private Sound sonTir;                   // Son joué lors du tir
    private Sound sonImpact;                // Son joué lors d'un impact

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
     * @param niveau Référence au niveau actuel
     */
    public ArmeDistance(String nom, int degats, int manaRequis, float portee, float vitesseAttaque,
                       String texturePath, String projectileTexturePath, float vitesseProjectile, 
                       int nbProjectiles, float angleDispersion, Niveau niveau) {
        super(nom, degats, manaRequis, portee, vitesseAttaque, texturePath);
        this.projectileTexture = new Texture(projectileTexturePath);
        this.projectiles = new Array<>();
        this.vitesseProjectile = vitesseProjectile;
        this.nbProjectilesParTir = nbProjectiles;
        this.angleDispersion = angleDispersion;
        this.niveau = niveau;
        
        // Chargement des sons (à adapter selon votre système de gestion des assets)
        // this.sonTir = Gdx.audio.newSound(Gdx.files.internal("sons/tir_" + nom + ".wav"));
        // this.sonImpact = Gdx.audio.newSound(Gdx.files.internal("sons/impact_projectile.wav"));
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
     * Gère aussi la détection des collisions avec les ennemis.
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
            
            // Vérifier les collisions avec les ennemis
            verifierCollisionProjectile(projectile);
            
            // Supprimer les projectiles qui ont dépassé leur portée ou touché une cible
            if (projectile.doitEtreDetruit()) {
                projectiles.removeIndex(i);
            }
        }
    }
    
    /**
     * Vérifie si un projectile entre en collision avec un ennemi et applique les dégâts si nécessaire.
     * 
     * @param projectile Le projectile à vérifier
     */
    private void verifierCollisionProjectile(Projectile projectile) {
        if (niveau == null) return; // Sécurité
        
        Array<Ennemi> ennemis = niveau.getEnnemis();
        
        for (Ennemi ennemi : ennemis) {
            // Vérifier si l'ennemi est vivant et si le projectile touche son hitbox
            if (ennemi.enVie() && projectile.getHitbox().overlaps(ennemi.getHitbox())) {
                // Appliquer les dégâts à l'ennemi
                ennemi.prendreDegat(projectile.getDegats());
                
                // Effet de recul (knockback) - optionnel
                Vector2 directionKnockback = new Vector2(projectile.getVitesse()).nor();
                float forceKnockback = 50f; // À ajuster selon votre gameplay
                ennemi.appliquerKnockback(directionKnockback, forceKnockback);
                
                // Effet visuel au point d'impact
                creerEffetImpact(projectile.getPosition());
                
                // Jouer le son d'impact
                if (sonImpact != null) {
                    sonImpact.play(0.5f);
                }
                
                // Marquer le projectile comme touché (pour le supprimer)
                projectile.toucher();
                
                // Sort de la boucle car un projectile ne touche qu'un seul ennemi
                // (à moins que vous vouliez des projectiles perforants)
                break;
            }
        }
    }
    
    /**
     * Crée un effet visuel au point d'impact du projectile.
     * 
     * @param position Position de l'impact
     */
    private void creerEffetImpact(Vector2 position) {
        // Code pour créer un effet visuel comme des particules ou une animation flash
        // Exemple avec le système de particules de LibGDX:
        /*
        ParticleEffect impact = new ParticleEffect();
        impact.load(Gdx.files.internal("effets/impact.p"), Gdx.files.internal("effets"));
        impact.setPosition(position.x, position.y);
        impact.start();
        niveau.ajouterEffet(impact);
        */
    }
    
    /**
     * Méthode appelée lorsque le joueur change d'arme active.
     * Permet de nettoyer les ressources et les projectiles en cours.
     */
    public void desactiver() {
        // Supprimer tous les projectiles en cours
        projectiles.clear();
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
        super.dispose();
        projectileTexture.dispose();
        
        // Libérer les ressources audio
        if (sonTir != null) sonTir.dispose();
        if (sonImpact != null) sonImpact.dispose();
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