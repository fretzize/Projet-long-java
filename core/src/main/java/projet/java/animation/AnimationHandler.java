package projet.java.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Pixmap;


public class AnimationHandler {
    
    // Constantes pour les différentes animations
    public static final int IDLE_DOWN = 0;    
    public static final int WALK_DOWN = 1;   
    public static final int IDLE_UP = 2;      
    public static final int WALK_UP = 3;      
    public static final int IDLE_LEFT = 4;
    public static final int WALK_LEFT = 5;
    public static final int IDLE_RIGHT = 6;
    public static final int WALK_RIGHT = 7;

    private Animation<TextureRegion>[] animations;
    private float stateTime = 0;
    private int currentAnimation = IDLE_DOWN;
    private boolean isMoving = false;
    
    // Chemin vers les assets
    private final String ASSETS_PATH = "HERCULEpng/HERCULEpng/";
    
    @SuppressWarnings("unchecked")
    public AnimationHandler() {
        animations = new Animation[8];

        // Charger les sprites pour chaque animation
        loadAnimations();
    }
    
    private void loadAnimations() {
        animations[IDLE_DOWN] = createStaticAnimation("Sword_Idle_front");
        
        animations[WALK_DOWN] = createWalkAnimation("Sword_Walk_front", 6);
        
        animations[IDLE_UP] = createStaticAnimation("Sword_Idle_back");
        
        animations[WALK_UP] = createWalkAnimation("Sword_Walk_back", 6);
        
        animations[IDLE_LEFT] = createStaticAnimation("Sword_Idle_side_left");
        
        animations[WALK_LEFT] = createWalkAnimation("Sword_Walk_side_left", 5);
        
        animations[IDLE_RIGHT] = createStaticAnimation("Sword_Idle_side_right");
        
        animations[WALK_RIGHT] = createWalkAnimation("Sword_Walk_side_right", 5);
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
                       boolean isMovingLeft, boolean isMovingRight) {
        stateTime += delta;
        isMoving = isMovingUp || isMovingDown || isMovingLeft || isMovingRight;
        
        // Déterminer quelle animation jouer en fonction de la direction
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
    
    public TextureRegion getCurrentFrame() {
        return animations[currentAnimation].getKeyFrame(stateTime, true);
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