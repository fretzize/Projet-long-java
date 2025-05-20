package projet.java.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

import projet.java.Main;
import projet.java.animation.AnimationHandler;
import projet.java.entite.ArmeMelee;
import projet.java.entite.Personnage;

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
    private final Personnage personnage;
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
    public AttackManager(Main game, Personnage personnage, AnimationHandler animationHandler, float cooldownAttaque) {
        this.game = game;
        this.personnage = personnage;
        this.animationHandler = animationHandler;
        this.cooldownAttaque = cooldownAttaque;
        
        // Initialiser l'arme avec le même cooldown
        this.armeMelee = new ArmeMelee("Épée", 20, 0, 100f, cooldownAttaque, "menubackground.png", 90f, 20f);
        
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
            // Important: Ne pas utiliser la gestion de cooldown interne de l'arme
            // Uniquement déclencher l'effet de l'attaque
            Vector2 playerPos = new Vector2(personnage.getPositionX(), personnage.getPositionY());
            armeMelee.attaquer_arme(playerPos, direction);
            
            // Jouer le son d'attaque
            if (attackSound != null) {
                // Le volume peut être ajusté en fonction des paramètres du jeu
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
    
    // Ajouter une méthode dispose pour libérer les ressources
    public void dispose() {
        if (attackSound != null) {
            attackSound.dispose();
        }
    }
}