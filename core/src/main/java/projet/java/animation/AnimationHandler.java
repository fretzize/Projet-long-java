package projet.java.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class AnimationHandler {
    
    // Constantes pour les différentes animations
    // Animations existantes
    public static final int IDLE_DOWN = 0;    
    public static final int WALK_DOWN = 1;   
    public static final int IDLE_UP = 2;      
    public static final int WALK_UP = 3;      
    public static final int IDLE_LEFT = 4;
    public static final int WALK_LEFT = 5;
    public static final int IDLE_RIGHT = 6;
    public static final int WALK_RIGHT = 7;
    
    // Nouvelles constantes pour les animations d'attaque
    public static final int ATTACK_DOWN = 8;
    public static final int ATTACK_UP = 9;
    public static final int ATTACK_LEFT = 10;
    public static final int ATTACK_RIGHT = 11;

    private Animation<TextureRegion>[] animations;
    private float stateTime = 0;
    private int currentAnimation = IDLE_DOWN;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private float attackDuration = 0.4f;  // Augmenter de 0.3f à 0.4f pour une animation plus longue
    private float attackTimer = 0;  // Compteur pour la durée de l'attaque
    
    // Chemin vers les assets
    private final String ASSETS_PATH = "HERCULEpng/HERCULEpng/";
    
    @SuppressWarnings("unchecked")
    public AnimationHandler() {
        // Augmenter la taille pour inclure les animations d'attaque
        animations = new Animation[12];

        // Charger les sprites pour chaque animation
        loadAnimations();
    }
    
    private void loadAnimations() {
        // Animations existantes
        animations[IDLE_DOWN] = createStaticAnimation("Sword_Idle_front");
        animations[WALK_DOWN] = createWalkAnimation("Sword_Walk_front", 6);
        animations[IDLE_UP] = createStaticAnimation("Sword_Idle_back");
        animations[WALK_UP] = createWalkAnimation("Sword_Walk_back", 6);
        animations[IDLE_LEFT] = createStaticAnimation("Sword_Idle_side_left");
        animations[WALK_LEFT] = createWalkAnimation("Sword_Walk_side_left", 5);
        animations[IDLE_RIGHT] = createStaticAnimation("Sword_Idle_side_right");
        animations[WALK_RIGHT] = createWalkAnimation("Sword_Walk_side_right", 5);
        
        // Nouvelles animations d'attaque
        animations[ATTACK_DOWN] = createAttackAnimation("Sword_Attack_front", 8);
        animations[ATTACK_UP] = createAttackAnimation("Sword_Attack_back", 8);
        animations[ATTACK_LEFT] = createAttackAnimation("Sword_Attack_side_left", 8);
        animations[ATTACK_RIGHT] = createAttackAnimation("Sword_Attack_side_right", 8);
    }
    
    // Méthode pour créer une animation d'attaque
    private Animation<TextureRegion> createAttackAnimation(String baseName, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        
        for (int i = 0; i < frameCount; i++) {
            String filePath = ASSETS_PATH + baseName + "_" + (i + 1) + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(filePath));
                frames[i] = new TextureRegion(texture);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image " + filePath + ": " + e.getMessage());
                Texture emptyTexture = new Texture(1, 1, Pixmap.Format.RGBA8888);
                frames[i] = new TextureRegion(emptyTexture);
            }
        }
        
        // Animation d'attaque plus rapide: 0.05f signifie ~20 frames par seconde
        return new Animation<>(0.05f, frames);
    }
    
    // créer une animation statique
    private Animation<TextureRegion> createStaticAnimation(String fileName) {
        TextureRegion[] frame = new TextureRegion[1];
        String filePath = ASSETS_PATH + fileName + ".png";
        try {
            Texture texture = new Texture(Gdx.files.internal(filePath));
            frame[0] = new TextureRegion(texture);
            return new Animation<>(0.1f, frame);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image " + filePath + ": " + e.getMessage());
            Texture emptyTexture = new Texture(1, 1, Pixmap.Format.RGBA8888);
            frame[0] = new TextureRegion(emptyTexture);
            return new Animation<>(0.1f, frame);
        }
    }
    
    // animation de marche 
    private Animation<TextureRegion> createWalkAnimation(String baseName, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        
        for (int i = 0; i < frameCount; i++) {
        
            String filePath = ASSETS_PATH + baseName + "_" + (i + 1) + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(filePath));
                frames[i] = new TextureRegion(texture);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image " + filePath + ": " + e.getMessage());
                Texture emptyTexture = new Texture(1, 1, Pixmap.Format.RGBA8888);
                frames[i] = new TextureRegion(emptyTexture);
            }
        }
        
        // Vitesse d'animation: 0.1f signifie 10 frames par seconde
        return new Animation<>(0.1f, frames);
    }
    
    public void update(float delta, boolean isMovingUp, boolean isMovingDown, 
                       boolean isMovingLeft, boolean isMovingRight, boolean attacking) {
        stateTime += delta;
        isMoving = isMovingUp || isMovingDown || isMovingLeft || isMovingRight;
        
        // Si une attaque est en cours, continuer l'animation
        if (isAttacking) {
            attackTimer += delta;
            if (attackTimer >= attackDuration) {
                isAttacking = false;
                attackTimer = 0;
                // Rétablir la dernière animation non-attaque
                if (currentAnimation == ATTACK_UP) currentAnimation = IDLE_UP;
                else if (currentAnimation == ATTACK_DOWN) currentAnimation = IDLE_DOWN;
                else if (currentAnimation == ATTACK_LEFT) currentAnimation = IDLE_LEFT;
                else if (currentAnimation == ATTACK_RIGHT) currentAnimation = IDLE_RIGHT;
                
                // IMPORTANT: Permettre une nouvelle attaque immédiatement si le bouton est toujours pressé
                if (attacking) {
                    isAttacking = true;
                    attackTimer = 0;
                    
                    // Détermine la nouvelle animation d'attaque
                    if (currentAnimation == IDLE_UP) currentAnimation = ATTACK_UP;
                    else if (currentAnimation == IDLE_DOWN) currentAnimation = ATTACK_DOWN;
                    else if (currentAnimation == IDLE_LEFT) currentAnimation = ATTACK_LEFT;
                    else if (currentAnimation == IDLE_RIGHT) currentAnimation = ATTACK_RIGHT;
                    
                    stateTime = 0;
                    return;
                }
            } else {
                // Ne pas changer d'animation jusqu'à la fin de l'attaque
                return;
            }
        }
        
        // Commencer une nouvelle attaque si demandée
        if (attacking && !isAttacking) {
            isAttacking = true;
            attackTimer = 0;
            
            // Choisir l'animation d'attaque en fonction de la dernière direction
            if (currentAnimation == WALK_UP || currentAnimation == IDLE_UP) {
                currentAnimation = ATTACK_UP;
            } else if (currentAnimation == WALK_DOWN || currentAnimation == IDLE_DOWN) {
                currentAnimation = ATTACK_DOWN;
            } else if (currentAnimation == WALK_LEFT || currentAnimation == IDLE_LEFT) {
                currentAnimation = ATTACK_LEFT;
            } else if (currentAnimation == WALK_RIGHT || currentAnimation == IDLE_RIGHT) {
                currentAnimation = ATTACK_RIGHT;
            } else {
                // Par défaut, attaque vers le bas
                currentAnimation = ATTACK_DOWN;
            }
            
            // Réinitialiser le stateTime pour commencer l'animation du début
            stateTime = 0;
            return;
        }
        
        // Si pas d'attaque en cours, gérer les animations de déplacement normales
        if (!isAttacking) {
            if (isMovingUp) {
                currentAnimation = WALK_UP;
            } else if (isMovingDown) {
                currentAnimation = WALK_DOWN;
            } else if (isMovingLeft) {
                currentAnimation = WALK_LEFT;
            } else if (isMovingRight) {
                currentAnimation = WALK_RIGHT;
            } else {
                // Si le personnage ne bouge pas, jouer l'animation idle correspondante
                if (currentAnimation == WALK_UP) currentAnimation = IDLE_UP;
                else if (currentAnimation == WALK_DOWN) currentAnimation = IDLE_DOWN;
                else if (currentAnimation == WALK_LEFT) currentAnimation = IDLE_LEFT;
                else if (currentAnimation == WALK_RIGHT) currentAnimation = IDLE_RIGHT;
            }
        }
    }
    
    public TextureRegion getCurrentFrame() {
        return animations[currentAnimation].getKeyFrame(stateTime, !isAttacking); // N'utiliser le looping que pour les animations non-attaque
    }
    
    // Méthodes helper pour vérifier la direction du personnage
    public boolean isFacingUp() {
        return currentAnimation == IDLE_UP || currentAnimation == WALK_UP || currentAnimation == ATTACK_UP;
    }

    public boolean isFacingDown() {
        return currentAnimation == IDLE_DOWN || currentAnimation == WALK_DOWN || currentAnimation == ATTACK_DOWN;
    }

    public boolean isFacingLeft() {
        return currentAnimation == IDLE_LEFT || currentAnimation == WALK_LEFT || currentAnimation == ATTACK_LEFT;
    }

    public boolean isFacingRight() {
        return currentAnimation == IDLE_RIGHT || currentAnimation == WALK_RIGHT || currentAnimation == ATTACK_RIGHT;
    }
    
    public boolean isAttacking() {
        return isAttacking;
    }
    
    // Ajouter une méthode pour réinitialiser l'état d'attaque
    public void resetAttackState() {
        isAttacking = false;
        attackTimer = 0;
    }
    
    public void dispose() {
        for (Animation<TextureRegion> animation : animations) {
            if (animation != null) {
                for (TextureRegion region : animation.getKeyFrames()) {
                    if (region != null && region.getTexture() != null) {
                        region.getTexture().dispose();
                    }
                }
            }
        }
    }
}