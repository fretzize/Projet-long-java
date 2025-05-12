package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class sbire implements Entite {

    // Attributs de santé de l'entité sbire
    private int vie = 100; // Valeur par défaut
    private int bouclier = 0;
    private int mana = 0;

    // La cible du sbire
    private Personnage cible;

    // La position du sbire
    private Vector2 position;

    // Le comportement du sbire
    private ComportementSbire comportement;

    private float vitesseDeplacement; // Vitesse de déplacement du sbire
    private float vitesseProjectile = 200f; // Vitesse des projectiles - valeur par défaut

    private int degatsCaC;
    private float porteeCaC;

    // Texture du projectile lancé par le sbire
    private Texture projectileTexture;
    private float porteeMax;
    private int degats;
    private float cooldown; // temps entre deux tirs (en secondes)
    private float tempsDepuisDernierTir = 0f;
    private Rectangle hitbox; // Hitbox du sbire

    /**
     * Constructeur du sbire.
     */
    public sbire(float x, float y, float vitesseDeplacement, float cooldown, Rectangle hitbox, 
               float porteeMax, float porteeCaC, int degats, int degatsCaC, Personnage cible, 
               ComportementSbire comportement, Texture projectileTexture, float vitesseProjectile) {
        this.position = new Vector2(x, y);
        this.projectileTexture = projectileTexture;
        this.cooldown = cooldown;
        this.vitesseDeplacement = vitesseDeplacement;
        this.vitesseProjectile = vitesseProjectile;
        this.hitbox = hitbox;
        this.porteeMax = porteeMax;
        this.porteeCaC = porteeCaC;
        this.degatsCaC = degatsCaC;
        this.degats = degats;
        this.cible = cible;
        this.comportement = comportement;
        // Initialisation des stats de santé
        this.vie = 100;
        this.bouclier = 0;
        this.mana = 0;
    }
    
    /**
     * Constructeur alternatif sans vitesse de projectile explicite.
     */
    public sbire(float x, float y, float vitesseDeplacement, float cooldown, Rectangle hitbox, 
               float porteeMax, float porteeCaC, int degats, int degatsCaC, Personnage cible, 
               ComportementSbire comportement, Texture projectileTexture) {
        this(x, y, vitesseDeplacement, cooldown, hitbox, porteeMax, porteeCaC, 
             degats, degatsCaC, cible, comportement, projectileTexture, 200f);
    }

    public void setCible(Personnage cible) {
        this.cible = cible;
    }

    @Override
    public void create_entite() {
        // Implémentation à compléter selon l'interface Entite
    }
    
    // Getters et setters
    
    public int getMana() {
        return this.mana;
    }

    public int getBouclier() {
        return this.bouclier;
    }

    public int getVie() {
        return this.vie;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Personnage getCible() {
        return this.cible;
    }

    // Méthodes pour modifier les attributs
    
    public void augmenterMana(int mana) {
        this.mana += mana;
    }

    public void augmenterBouclier(int bouclier) {
        this.bouclier += bouclier;
    }

    public void augmenterVie(int vie) {
        this.vie += vie;
    }

    public void perdreBouclier(int valeur) {
        this.bouclier -= valeur;
        if (this.bouclier < 0) {
            this.bouclier = 0;
        }
    }
    
    /**
     * Méthode pour prendre des dégâts.
     * D'abord absorbe les dégâts avec le bouclier, puis diminue la vie.
     */
    public void prendreDegat(int degats) {
        // Si le sbire a du bouclier, on réduit d'abord le bouclier
        if (this.bouclier > 0) {
            int degatsBouclier = Math.min(this.bouclier, degats);
            this.perdreBouclier(degatsBouclier);
            degats -= degatsBouclier;
        }
        
        // Les dégâts restants affectent la vie
        if (degats > 0) {
            this.vie -= degats;
            if (this.vie <= 0) {
                mourir();
            }
        }
    }

    // Version alternative pour compatibilité avec le code existant
    public void prendreDegats(int degats) {
        prendreDegat(degats);
    }

    public boolean enVie() {
        return this.vie > 0;
    }

    public boolean cibleenVie() {
        return this.cible != null && this.cible.getVie() > 0;
    }

    // Pour l'instant ne sert pas à grand chose, mais après y aura des sons et des animations quand il meurt
    public void mourir() {
        this.vie = 0;
        this.bouclier = 0;
        this.mana = 0;
    }

    public void update(float delta, List<Projectile> projectiles) {
        tempsDepuisDernierTir += delta;

        if (cible != null && enVie() && cibleenVie() && peutAttaquer()) {
            if (estAPorteeCaC()) {
                attaquerMelee();
                tempsDepuisDernierTir = 0;
            } else if (estAPortee()) {
                tirerSurCible(projectiles);
                tempsDepuisDernierTir = 0;
            }
        }
    }

    // Vérifie si le sbire peut tirer (si le cooldown est écoulé)
    private boolean peutAttaquer() {
        return tempsDepuisDernierTir >= cooldown;
    }

    // Vérifie si la cible est à portée (si la distance est inférieure à la portée maximale)
    private boolean estAPortee() {
        if (cible == null) {
            return false;
        }
        float distance = position.dst(cible.getPosition());
        return distance <= porteeMax;
    }

    private boolean estAPorteeCaC() {
        if (cible == null) {
            return false;
        }
        float distance = position.dst(cible.getPosition());
        return distance <= porteeCaC;
    }

    public void tirerSurCible(List<Projectile> projectiles) {
        if (projectiles == null || cible == null) return;
        
        Vector2 positionCible = this.cible.getPosition();
    
        // Calcule la direction normalisée du projectile (du sbire vers la cible)
        Vector2 direction = new Vector2(positionCible).sub(new Vector2(position)).nor();
    
        // Éviter de modifier le vecteur original avec scl()
        Vector2 vitesseVecteur = new Vector2(direction).scl(vitesseProjectile);

        Projectile projectile = new Projectile(
            position.x,
            position.y,
            vitesseVecteur.x,
            vitesseVecteur.y,
            projectileTexture,
            degats,
            porteeMax
        );
    
        projectiles.add(projectile);
    }

    public void attaquerMelee() {
        if (cible != null && estAPorteeCaC()) {
            cible.prendreDegat(degatsCaC);
        }
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    // Méthode pour se déplacer où l'on veut
    public void deplacer(float deltaTime, Vector2 direction) {
        if (direction == null) return;
        
        // Assurer que la direction est normalisée sans modifier l'original
        Vector2 directionNormalisee = new Vector2(direction).nor();
        
        // Déplacement
        position.x += directionNormalisee.x * vitesseDeplacement * deltaTime;
        position.y += directionNormalisee.y * vitesseDeplacement * deltaTime;
        
        // Mise à jour de la hitbox
        hitbox.setPosition(position.x, position.y);
    }

    // Méthode pour se déplacer explicitement vers la cible
    public void deplacerVersCible(float deltaTime) {
        if (cible != null) {
            // Calcul de la direction vers la cible
            Vector2 direction = new Vector2(cible.getPosition()).sub(new Vector2(position));
            deplacer(deltaTime, direction);
        }
    }

    public void agir(float deltaTime, List<Projectile> projectiles) {
        if (comportement != null && enVie()) {
            comportement.executerAction(this, deltaTime, projectiles);
        }
    }

    public void setComportement(ComportementSbire comportement) {
        this.comportement = comportement;
    }

    public ComportementSbire getComportement() {
        return comportement;
    }
    
    /**
     * Méthode pour appliquer un effet de knockback (recul)
     * 
     * @param direction Direction du knockback (vecteur normalisé)
     * @param force Force du knockback (distance à appliquer)
     */
    public void appliquerKnockback(Vector2 direction, float force) {
        if (direction == null) return;
        
        // Utiliser une copie du vecteur pour ne pas modifier l'original
        Vector2 knockbackVector = new Vector2(direction).nor().scl(force);
        
        // Appliquer le knockback
        position.add(knockbackVector);
        
        // Mettre à jour la hitbox
        hitbox.setPosition(position.x, position.y);
    }
}