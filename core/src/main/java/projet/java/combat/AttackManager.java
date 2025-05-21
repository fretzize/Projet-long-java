package projet.java.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import projet.java.Main;
import projet.java.animation.AnimationHandler;
import projet.java.entite.ArmeMelee;
import projet.java.entite.Entite;

/**
 * Cette classe gère toute la logique d'attaque, isolant ainsi cette fonctionnalité
 * du reste du code pour une meilleure lisibilité et maintenance.
 */
public class AttackManager {
    // Configuration
    private float cooldownAttaque;
    private boolean peutAttaquer = true;
    private float tempsDepuisAttaque = 0f;
    
    // Références
    private final Entite personnage;
    private final AnimationHandler animationHandler;
    private final ArmeMelee armeMelee;
    private final Main game;
    private com.badlogic.gdx.audio.Sound attackSound;
    
    /**
     * Constructeur du gestionnaire d'attaque
     * 
     * @param game Référence au jeu principal
     * @param personnage Le personnage qui attaque
     * @param animationHandler Le gestionnaire d'animations
     * @param cooldownAttaque Le temps entre deux attaques en secondes
     */
    public AttackManager(Main game, Entite personnage, AnimationHandler animationHandler, float cooldownAttaque) {
        this.game = game;
        this.personnage = personnage;
        this.animationHandler = animationHandler;
        this.cooldownAttaque = cooldownAttaque;
        
        // Initialiser l'arme avec une portée raisonnable
        this.armeMelee = new ArmeMelee("Épée", 20, 0, 35f, cooldownAttaque, "menubackground.png", 90f, 150f);
        
        // Charger le son d'attaque
        try {
            this.attackSound = Gdx.audio.newSound(Gdx.files.internal("sword_swing.mp3"));
        } catch (Exception e) {
            System.err.println("Impossible de charger le son d'attaque: " + e.getMessage());
        }
    }
    
    /**
     * Met à jour le système d'attaque.
     * À appeler dans la boucle de rendu principale.
     * 
     * @param delta Temps écoulé depuis la dernière mise à jour
     * @return true si une attaque a été déclenchée, false sinon
     */
    public boolean update(float delta) {
        boolean attackTriggered = false;
        
        // Gestion du cooldown de l'attaque
        if (!peutAttaquer) {
            tempsDepuisAttaque += delta;
            // Debug pour voir le cooldown en action
            // System.out.println("Cooldown: " + tempsDepuisAttaque + "/" + cooldownAttaque);
            
            if (tempsDepuisAttaque >= cooldownAttaque) {
                // On attend que l'animation soit terminée avant de permettre une nouvelle attaque
                if (!animationHandler.isAttacking()) {
                    peutAttaquer = true;
                    tempsDepuisAttaque = 0f;
                }
            }
        }
        
        // Vérifier si le joueur attaque et peut attaquer
        boolean inputAttack = false;
        if (game.toucheAttaque == Main.MOUSE_LEFT_CLICK) {
            inputAttack = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        } else {
            inputAttack = Gdx.input.isKeyJustPressed(game.toucheAttaque);
        }
        
        if (inputAttack && peutAttaquer) {
            // N'autoriser l'attaque que si l'animation précédente est terminée
            if (!animationHandler.isAttacking()) {
                executeAttack();
                attackTriggered = true;
                
                // IMPORTANT: Désactiver immédiatement la possibilité d'attaquer à nouveau
                peutAttaquer = false;
                tempsDepuisAttaque = 0f;
            }
        }
        
        return attackTriggered;
    }
    
    /**
     * Exécute l'attaque proprement dite
     */
    private void executeAttack() {
        Vector2 direction = getAttackDirection();
        
        try {
            // Récupérer la position du personnage avec un décalage pour la hitbox
            // Ajouter la différence entre la position du personnage et sa hitbox
            Vector2 playerPos = new Vector2(
                personnage.getPositionX() + 22, // Ajout du hitboxX (valeur de GameScreen)
                personnage.getPositionY() + 18  // Ajout du hitboxY (valeur de GameScreen)
            );
            
            // Obtenir la taille de la hitbox (estimée)
            float hitboxWidth = 10;  // Largeur estimée de la hitbox du joueur
            float hitboxHeight = 10; // Hauteur estimée de la hitbox du joueur
            
            // Créer un Vector2 avec les infos de la hitbox pour placer l'attaque correctement
            Vector2 hitboxInfo = new Vector2(hitboxWidth, hitboxHeight);
            
            // Passer la position et la taille de la hitbox à la méthode d'attaque
            armeMelee.attaquer_arme(playerPos, direction, hitboxInfo);
            
            // Jouer le son d'attaque
            if (attackSound != null) {
                attackSound.play(game.getSoundVolume());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'attaque: " + e.getMessage());
        }
    }
    
    /**
     * Détermine la direction de l'attaque en fonction de l'orientation du personnage
     * 
     * @return Vecteur de direction unitaire (normalisé)
     */
    private Vector2 getAttackDirection() {
        Vector2 direction = new Vector2();
        
        if (animationHandler.isFacingUp()) {
            direction.set(0, 1);
        } else if (animationHandler.isFacingDown()) {
            direction.set(0, -1);
        } else if (animationHandler.isFacingLeft()) {
            direction.set(-1, 0);
        } else if (animationHandler.isFacingRight()) {
            direction.set(1, 0);
        } else {
            direction.set(0, -1); // Direction par défaut
        }
        
        return direction;
    }
    
    /**
     * @return L'arme mêlée actuellement utilisée
     */
    public ArmeMelee getArmeMelee() {
        return armeMelee;
    }
    
    /**
     * @return true si le personnage peut attaquer, false sinon
     */
    public boolean peutAttaquer() {
        return peutAttaquer;
    }
    
    /**
     * Modifie la durée du cooldown d'attaque
     * 
     * @param cooldown Nouveau temps de cooldown en secondes
     */
    public void setCooldownAttaque(float cooldown) {
        this.cooldownAttaque = cooldown;
    }
    
    /**
     * Indique si une attaque est actuellement en cours d'exécution.
     * 
     * @return true si une attaque est en cours, false sinon
     */
    public boolean isAttacking() {
        return !peutAttaquer && tempsDepuisAttaque < 0.3f; // Les premières 300ms du cooldown sont considérées comme "attaque active"
    }
    
    // Ajouter une méthode dispose pour libérer les ressources
    public void dispose() {
        if (attackSound != null) {
            attackSound.dispose();
        }
    }
}