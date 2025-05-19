package projet.java.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    }
    
    /**
     * Met à jour le système d'attaque.
     * À appeler dans la boucle de rendu principale.
     * 
     * @param delta Temps écoulé depuis la dernière mise à jour
     * @return true si une attaque a été déclenchée, false sinon
     */
    public boolean update(float delta) {
        // Mettre à jour le cooldown de l'arme
        armeMelee.update(delta);
        
        // Gestion du cooldown de l'attaque
        if (!peutAttaquer) {
            tempsDepuisAttaque += delta;
            if (tempsDepuisAttaque >= cooldownAttaque && !animationHandler.isAttacking()) {
                peutAttaquer = true;
            }
        }
        
        // Vérifier si le joueur attaque
        boolean isAttacking = false;
        if (peutAttaquer) {
            // Vérifier selon la touche configurée
            if (game.toucheAttaque == Main.MOUSE_LEFT_CLICK) {
                isAttacking = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
            } else {
                isAttacking = Gdx.input.isKeyJustPressed(game.toucheAttaque);
            }
        }
        
        // Si le joueur attaque et peut attaquer
        if (isAttacking && peutAttaquer && !animationHandler.isAttacking()) {
            executeAttack();
            return true;
        }
        
        return false;
    }
    
    /**
     * Exécute l'attaque proprement dite
     */
    private void executeAttack() {
        // Obtenir la direction d'attaque basée sur la direction du personnage
        Vector2 direction = getAttackDirection();
        
        // Effectuer l'attaque
        try {
            armeMelee.attaquer_arme(
                new Vector2(personnage.getPositionX(), personnage.getPositionY()),
                direction
            );
            
            // Réinitialiser le cooldown
            peutAttaquer = false;
            tempsDepuisAttaque = 0f;
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
}