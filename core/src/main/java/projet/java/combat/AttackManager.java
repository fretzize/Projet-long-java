package projet.java.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import projet.java.Main;
import projet.java.animation.AnimationHandler;
import projet.java.entite.Arme;
import projet.java.entite.ArmeDistance;
import projet.java.entite.ArmeMelee;
import projet.java.entite.Entite;
import projet.java.entite.Niveau;
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
    private Personnage personnage;
    private AnimationHandler animationHandler;
    private  ArmeMelee armeMelee;
    private ArmeDistance armeDistance;
    private final Main game;
    private com.badlogic.gdx.audio.Sound attackSound;
    
    // Ajouter un champ niveau
    private Niveau niveau;
    private boolean armeCorps;
    private FireballManager fireballManager;
    boolean inputAttack = false;
    
    /**
     * Constructeur du gestionnaire d'attaque
     * 
     * @param game Référence au jeu principal
     * @param personnage Le personnage qui attaque
     * @param animationHandler Le gestionnaire d'animations
     * @param cooldownAttaque Le temps entre deux attaques en secondes
     * @param niveau Le niveau actuel contenant les sbires
     */
    public AttackManager(Main game, Personnage personnage, AnimationHandler animationHandler, float cooldownAttaque, Niveau niveau, Arme arme) {
        this.game = game;
        this.personnage = personnage;
        this.animationHandler = animationHandler;
        this.cooldownAttaque = cooldownAttaque;
        this.niveau = niveau;
        inputAttack = false;
        
        // Initialiser l'arme avec une portée raisonnable
        if (arme instanceof ArmeMelee) {
            this.armeMelee = (ArmeMelee) arme;
            // IMPORTANT: Associer le niveau à l'arme
            this.armeMelee.setNiveau(niveau);
            armeCorps = true;
        }

        if (arme instanceof ArmeDistance) {
            this.armeDistance = (ArmeDistance) arme;
            this.armeDistance.setNiveau(niveau);
            armeCorps = false;
        }
        
        
        
        
        // Charger le son d'attaque
        try {
            this.attackSound = Gdx.audio.newSound(Gdx.files.internal("swordattacksound.mp3"));
        } catch (Exception e) {
            System.err.println("Impossible de charger le son d'attaque: " + e.getMessage());
        }
    }

    public void setAnimation(AnimationHandler animationHandler) {
        this.animationHandler = animationHandler;
    }
    
    // Ajouter ce constructeur pour la compatibilité
    public AttackManager(Main game, Personnage personnage, AnimationHandler animationHandler, float cooldownAttaque, Arme arme) {
        this(game, personnage, animationHandler, cooldownAttaque, null, arme);
        System.err.println("ATTENTION: AttackManager créé sans niveau. Les attaques ne fonctionneront pas correctement.");
    }
    
    /**
     * Met à jour le système d'attaque.
     * À appeler dans la boucle de rendu principale.
     * 
     * @param delta Temps écoulé depuis la dernière mise à jour
     * @param mouseDirection Direction vers la souris (normalisée)
     * @return true si une attaque a été déclenchée, false sinon
     */
    public boolean update(float delta, Vector2 mouseDirection) {
        boolean attackTriggered = false;
        
        // Gestion du cooldown de l'attaque
        if (!peutAttaquer) {
            tempsDepuisAttaque += delta;
            
            if (tempsDepuisAttaque >= cooldownAttaque) {
                // On attend que l'animation soit terminée avant de permettre une nouvelle attaque
                if (!animationHandler.isAttacking()) {
                    peutAttaquer = true;
                    tempsDepuisAttaque = 0f;
                }
            }
        }
        
        // Vérifier si le joueur attaque et peut attaquer
        
        if (armeCorps) {
            if (game.toucheAttaque == Main.MOUSE_LEFT_CLICK) {
                inputAttack = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
            } else {
                inputAttack = Gdx.input.isKeyJustPressed(game.toucheAttaque);
            }
        } else {
            if (game.toucheAttaque == Main.MOUSE_LEFT_CLICK) {
                inputAttack = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
            } else {
                inputAttack = Gdx.input.isKeyPressed(game.toucheAttaque);
            }
        }
        
        
        
        if (inputAttack && peutAttaquer) {
            if (armeCorps) {
                // N'autoriser l'attaque que si l'animation précédente est terminée
                if (!animationHandler.isAttacking()) {
                    // Utiliser la direction de la souris pour l'attaque
                    executeAttack(mouseDirection);
                    attackTriggered = true;
                    
                    // Désactiver immédiatement la possibilité d'attaquer à nouveau
                    peutAttaquer = false;
                    tempsDepuisAttaque = 0f;
                }
            } else {
                if (fireballManager != null && personnage.getMana() != 0) {
                    fireballManager.setDamage(armeDistance.getDegats());
                    fireballManager.castManualFireball(mouseDirection);
                    // System.out.println("Mana du joueur avant attaque : " + personnage.getMana()); 
                    personnage.setMana(armeDistance.getManaRequis());
                    // System.out.println("Mana du joueur après attaque : " + personnage.getMana()); 

                }
                // Jouer le son d'attaque avec le volume réduit
                if (attackSound != null) {
                    // Utiliser le modificateur de volume (0.3f = 30% du volume normal)
                    attackSound.play(game.getSoundVolume() * 0.3f);
                }
            }
            } 
        
        
        return attackTriggered;
    }
    
    /**
     * Exécute l'attaque proprement dite avec la direction donnée
     * 
     * @param mouseDirection Direction vers la souris (normalisée)
     */
    private void executeAttack(Vector2 mouseDirection) {
        try {
            // Position ajustée du personnage (centrée sur la hitbox)
            Vector2 playerPos = new Vector2(
                personnage.getPositionX() + 22, // Décalage horizontal de la hitbox
                personnage.getPositionY() + 18  // Décalage vertical de la hitbox
            );
            
            // Taille de la hitbox du joueur
            Vector2 hitboxInfo = new Vector2(10, 10);
            
            // Déterminer l'animation correspondant à la direction de la souris
            updateAnimationForMouseDirection(mouseDirection);
            
            // Passer la position et la taille de la hitbox à la méthode d'attaque
            if (armeCorps) {
                armeMelee.attaquer_arme(playerPos, mouseDirection, hitboxInfo);
            } 
        } catch (Exception e) {
            System.err.println("Erreur lors de l'attaque: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Met à jour l'animation en fonction de la direction de la souris
     * 
     * @param mouseDirection Direction vers la souris
     */
    private void updateAnimationForMouseDirection(Vector2 mouseDirection) {
        // Calculer l'angle de la direction (en radians)
        float angle = (float) Math.atan2(mouseDirection.y, mouseDirection.x);
        
        // Convertir l'angle en degrés (-180 à 180)
        float degrees = angle * MathUtils.radiansToDegrees;
        
        // Déterminer la direction principale (haut, bas, gauche, droite)
        // Diviser l'espace en 4 quadrants de 90 degrés
        if (degrees >= -45 && degrees < 45) {
            // Droite (animation vers la droite)
            animationHandler.setAttackAnimationRight();
        } else if (degrees >= 45 && degrees < 135) {
            // Haut (animation vers le haut)
            animationHandler.setAttackAnimationUp();
        } else if (degrees >= -135 && degrees < -45) {
            // Bas (animation vers le bas)
            animationHandler.setAttackAnimationDown();
        } else {
            // Gauche (animation vers la gauche)
            animationHandler.setAttackAnimationLeft();
        }
    }
    
    /**
     * Indique si une attaque est actuellement en cours d'exécution.
     * 
     * @return true si une attaque est en cours, false sinon
     */
    public boolean isAttacking() {
        return !peutAttaquer && tempsDepuisAttaque < 0.3f; // Les premières 300ms du cooldown sont considérées comme "attaque active"
    }
    
    /**
     * @return L'arme mêlée actuellement utilisée
     */
    public ArmeMelee getArmeMelee() {
        return armeMelee;
    }

    public void setArmeMelee(ArmeMelee armeMelee) {
        this.armeMelee = armeMelee;
        this.armeMelee.setNiveau(this.niveau);
        this.armeCorps = true;
    }

    public void setArmeDistance(ArmeDistance armeDistance) {
        this.armeDistance = armeDistance;
        this.armeDistance.setNiveau(this.niveau);
        this.armeCorps = false;
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
     * Libère les ressources
     */
    public void dispose() {
        if (attackSound != null) {
            attackSound.dispose();
        }
    }
    
    // Ajouter une méthode pour définir le niveau après construction
    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
        if (armeMelee != null) {
            armeMelee.setNiveau(niveau);
        }
        if (armeDistance != null) {
            armeDistance.setNiveau(niveau);
        }
    }

    public ArmeMelee getArme() {
        return this.armeMelee;
    }

    public void setFireballManager(FireballManager manager) {
        this.fireballManager = manager;
    }


}