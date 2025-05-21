package projet.java.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ImpactEffect {
    private static Animation<TextureRegion> impactAnimation;
    private float stateTime;
    private Vector2 position;
    private boolean isActive;
    private float rotation;
    private float scale = 1.5f; // Taille de l'effet d'impact
    
    // Chargement statique des textures
    static {
        // Charger les textures des frames d'impact
        TextureRegion[] frames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal("Impact_" + (i + 1) + ".png")));
        }
        // Réduire la durée de chaque frame pour une animation plus rapide
        impactAnimation = new Animation<>(0.035f, frames); // Animation très rapide (40fps au lieu de 20fps)
    }
    
    public ImpactEffect() {
        this.stateTime = 0;
        this.position = new Vector2();
        this.isActive = false;
        this.rotation = 0;
    }
    
    public void start(float x, float y, Vector2 knockbackDirection) {
        this.position.set(x, y);
        this.stateTime = 0;
        this.isActive = true;
        
        // Calculer la rotation en fonction de la direction du knockback
        // Ajouter 90 degrés pour corriger l'orientation
        this.rotation = knockbackDirection.angleDeg() + 90;
    }
    
    public void update(float delta) {
        if (isActive) {
            stateTime += delta;
            // Vérifier si l'animation est terminée
            if (impactAnimation.isAnimationFinished(stateTime)) {
                isActive = false;
            }
        }
    }
    
    public void draw(SpriteBatch batch) {
        if (isActive) {
            TextureRegion currentFrame = impactAnimation.getKeyFrame(stateTime, false);
            
            float width = currentFrame.getRegionWidth() * scale;
            float height = currentFrame.getRegionHeight() * scale;
            
            // Dessiner avec rotation
            batch.draw(
                currentFrame,
                position.x - width/2, // Centrer horizontalement
                position.y - height/2, // Centrer verticalement
                width/2, height/2, // Origine au centre pour la rotation
                width, height, // Dimensions
                1, 1, // Scale X et Y
                rotation // Rotation
            );
        }
    }
    
    public boolean isActive() {
        return isActive;
    }
}