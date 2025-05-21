package projet.java.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SbireAnimationHandler {
    
    // Constantes pour les différentes animations
    public static final int IDLE_FRONT = 0;
    public static final int IDLE_BACK = 1;
    public static final int IDLE_LEFT = 2;
    public static final int IDLE_RIGHT = 3;
    public static final int WALK_FRONT = 4;
    public static final int WALK_BACK = 5;
    public static final int WALK_LEFT = 6;
    public static final int WALK_RIGHT = 7;
    public static final int ATTACK_FRONT = 8;
    public static final int ATTACK_BACK = 9;
    public static final int ATTACK_LEFT = 10;
    public static final int ATTACK_RIGHT = 11;
    
    // Nombre total d'animations
    private static final int ANIMATION_COUNT = 12;
    
    private Animation<TextureRegion>[] animations;
    private float stateTime = 0;
    private int currentAnimation = IDLE_FRONT;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private float attackDuration = 0.96f; // 8 frames * 0.12f = 0.96 secondes
    private float attackTimer = 0;
    
    // Direction du sbire
    private Vector2 lastDirection = new Vector2(0, -1); // Direction par défaut: vers le bas
    
    // Préfixe des fichiers
    private final String PREFIX = "orc1";
    
    // Texture de secours
    private Texture fallbackTexture;
    
    @SuppressWarnings("unchecked")
    public SbireAnimationHandler() {
        animations = new Animation[ANIMATION_COUNT];
        
        // Créer une texture de secours colorée
        createFallbackTexture();
        
        // Charger les animations
        loadAnimations();
    }
    
    private void createFallbackTexture() {
        // Créer une texture 32x32 avec un carré coloré
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fillRectangle(0, 0, 32, 32);
        pixmap.setColor(Color.BLACK);
        pixmap.drawRectangle(0, 0, 31, 31);
        
        // Dessiner un visage simplifié pour indiquer la direction
        pixmap.drawLine(8, 8, 8, 16);   // Œil gauche
        pixmap.drawLine(24, 8, 24, 16); // Œil droit
        pixmap.drawLine(8, 24, 24, 24); // Bouche
        
        fallbackTexture = new Texture(pixmap);
        pixmap.dispose();
    }
    
    private void loadAnimations() {
        System.out.println("Chargement des animations du sbire (format: " + PREFIX + "_direction_action_#)...");
        
        try {
            // Animations IDLE
            animations[IDLE_FRONT] = createAnimation(PREFIX + "_front_idle", 4, 0.2f);
            animations[IDLE_BACK] = createAnimation(PREFIX + "_back_idle", 4, 0.2f);
            animations[IDLE_LEFT] = createAnimation(PREFIX + "_left_idle", 4, 0.2f);
            animations[IDLE_RIGHT] = createAnimation(PREFIX + "_right_idle", 4, 0.2f);
            
            // Animations WALK - vitesse normale
            animations[WALK_FRONT] = createAnimation(PREFIX + "_front_run", 8, 0.1f);
            animations[WALK_BACK] = createAnimation(PREFIX + "_back_run", 8, 0.1f);
            animations[WALK_LEFT] = createAnimation(PREFIX + "_left_run", 8, 0.1f);
            animations[WALK_RIGHT] = createAnimation(PREFIX + "_right_run", 8, 0.1f);
            
            // Animations ATTACK - ralentir en augmentant la durée par frame (de 0.05f à 0.12f)
            animations[ATTACK_FRONT] = createAnimation(PREFIX + "_front_run_attack", 8, 0.12f);
            animations[ATTACK_BACK] = createAnimation(PREFIX + "_back_run_attack", 8, 0.12f);
            animations[ATTACK_LEFT] = createAnimation(PREFIX + "_left_run_attack", 8, 0.12f);
            animations[ATTACK_RIGHT] = createAnimation(PREFIX + "_right_run_attack", 8, 0.12f);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des animations: " + e.getMessage());
            
            // Remplir toutes les animations avec l'animation par défaut
            for (int i = 0; i < ANIMATION_COUNT; i++) {
                if (animations[i] == null) {
                    animations[i] = createFallbackAnimation();
                }
            }
        }
    }
    
    private Animation<TextureRegion> createFallbackAnimation() {
        TextureRegion[] frame = new TextureRegion[1];
        frame[0] = new TextureRegion(fallbackTexture);
        return new Animation<>(0.1f, frame);
    }
    
    private Animation<TextureRegion> createAnimation(String baseName, int frameCount, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        boolean allFramesLoaded = true;
        
        for (int i = 0; i < frameCount; i++) {
            String filePath = "Orc1/" + baseName + "_" + (i + 1) + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(filePath));
                frames[i] = new TextureRegion(texture);
                System.out.println("Chargé: " + filePath);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image " + filePath + ": " + e.getMessage());
                frames[i] = new TextureRegion(fallbackTexture);
                allFramesLoaded = false;
            }
        }
        
        if (!allFramesLoaded) {
            System.out.println("Certaines frames n'ont pas pu être chargées pour " + baseName);
        }
        
        return new Animation<>(frameDuration, frames);
    }
    
    public void update(float delta, Vector2 movementDirection, boolean attacking) {
        stateTime += delta;
        
        // Sauvegarder la direction si elle n'est pas nulle
        if (movementDirection != null && !movementDirection.isZero()) {
            lastDirection.set(movementDirection);
            isMoving = true;
        } else {
            isMoving = false;
        }
        
        // Gérer l'animation d'attaque
        if (isAttacking) {
            attackTimer += delta;
            if (attackTimer >= attackDuration) {
                isAttacking = false;
                attackTimer = 0;
            }
        }
        
        // Démarrer une nouvelle attaque si demandé
        if (attacking && !isAttacking) {
            isAttacking = true;
            attackTimer = 0;
            stateTime = 0;
        }
        
        // Déterminer la direction principale
        int direction;
        if (Math.abs(lastDirection.x) > Math.abs(lastDirection.y)) {
            // Mouvement horizontal
            direction = lastDirection.x > 0 ? 3 : 2; // RIGHT : LEFT
        } else {
            // Mouvement vertical
            direction = lastDirection.y > 0 ? 1 : 0; // BACK : FRONT
        }
        
        // Déterminer l'animation à utiliser
        if (isAttacking) {
            // Animation d'attaque en fonction de la direction
            currentAnimation = ATTACK_FRONT + direction;
        } else if (isMoving) {
            // Animation de marche en fonction de la direction
            currentAnimation = WALK_FRONT + direction;
        } else {
            // Animation d'idle en fonction de la direction
            currentAnimation = IDLE_FRONT + direction;
        }
    }
    
    public TextureRegion getCurrentFrame() {
        return animations[currentAnimation].getKeyFrame(stateTime, currentAnimation < ATTACK_FRONT);
    }
    
    public boolean isAttacking() {
        return isAttacking;
    }
    
    public void dispose() {
        if (fallbackTexture != null) {
            fallbackTexture.dispose();
        }
        
        for (Animation<TextureRegion> animation : animations) {
            if (animation != null) {
                for (TextureRegion region : animation.getKeyFrames()) {
                    if (region != null && region.getTexture() != null && 
                        region.getTexture() != fallbackTexture) {
                        region.getTexture().dispose();
                    }
                }
            }
        }
    }
}