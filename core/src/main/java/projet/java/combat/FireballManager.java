package projet.java.combat;

import java.util.concurrent.RecursiveAction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import projet.java.Main;
import projet.java.Menu.GameScreen;
import projet.java.entite.Entite;
import projet.java.entite.Niveau;
import projet.java.entite.Projectile;
import projet.java.entite.Sbire;
import com.badlogic.gdx.Screen;

public class FireballManager {
    // Configuration
    private float cooldown = 1.0f; // 1 seconde entre chaque boule de feu
    private boolean canCast = true;
    private float timeSinceLastCast = 0f;
    private float projectileSpeed = 200f; // Vitesse de la boule de feu
    private int damage = 20; // Dégâts de la boule de feu
    private float range = 500f; // Portée de la boule de feu
    
    // Références
    private final Main game;
    private final Entite personnage;
    private final Niveau niveau;
    private final Array<FireballProjectile> fireballs = new Array<>();
    private Array<Rectangle> mursHitboxes;
    private boolean pouvoirOk;
    
    // Animation de la boule de feu
    private Animation<TextureRegion> fireballAnimation;
    private float scaleFactor = 0.7f; // Taille de la boule de feu (ratio)
    private GameScreen gameScreen;

    public FireballManager(Main game, Entite personnage, Niveau niveau, Array<Rectangle> mursHitboxes, GameScreen gameScreen) {
        this.game = game;
        this.personnage = personnage;
        this.niveau = niveau;
        this.mursHitboxes = mursHitboxes;
        this.pouvoirOk = gameScreen.getPouvoir();
        this.gameScreen = gameScreen;
        
        // Charger l'animation de la boule de feu
        loadFireballAnimation();
        
        System.out.println("FireballManager initialisé avec succès");
    }
    
    private void loadFireballAnimation() {
        TextureRegion[] frames = new TextureRegion[5];
        
        try {
            // Essayer d'abord de charger les textures de boule de feu
            for (int i = 0; i < 5; i++) {
                String frameName = "FB00" + (i+1) + ".png";
                frames[i] = new TextureRegion(new Texture(Gdx.files.internal(frameName)));
            }
            System.out.println("Animation de boule de feu chargée avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des images de boule de feu: " + e.getMessage());
            
            // Fallback: créer une texture rouge simple pour les boules de feu
            try {
                Texture fallbackTexture = new Texture(Gdx.files.internal("coeur_plein.png"));
                for (int i = 0; i < 5; i++) {
                    frames[i] = new TextureRegion(fallbackTexture);
                }
                System.out.println("Utilisation de texture de secours pour la boule de feu");
            } catch (Exception ex) {
                System.err.println("Erreur critique lors du chargement de la texture de secours: " + ex.getMessage());
            }
        }
        
        // Créer l'animation (0.1f = 10 frames par seconde)
        fireballAnimation = new Animation<>(0.1f, frames);
    }
    
    public void update(float delta, boolean pouvoirOk) {
        this.pouvoirOk = pouvoirOk;
        if (pouvoirOk && (Gdx.input.isKeyPressed(game.touchePouvoir))) {
            for (int i = 0; i < 10; i++) {
                float angle = i * 36f * MathUtils.degreesToRadians;
                Vector2 direction = new Vector2(MathUtils.cos(angle), MathUtils.sin(angle));


                // Lancer la boule de feu
                castFireball(direction);
                gameScreen.setPouvoir(false);
            }
        } else {
            // Gestion du cooldown
            if (!canCast) {
                timeSinceLastCast += delta;
                if (timeSinceLastCast >= cooldown) {
                    canCast = true;
                    timeSinceLastCast = 0f;
                    System.out.println("Boule de feu prête");
                }
            }
            
            // Vérifier si le joueur veut lancer une boule de feu
            boolean fireballInput = false;
            
            // Ajout de debug pour voir si le clic est détecté
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                System.out.println("Clic droit détecté");
            }
            
            if (game.toucheBouleFeu == Main.MOUSE_RIGHT_CLICK) {
                fireballInput = Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT);
            } else {
                fireballInput = Gdx.input.isKeyJustPressed(game.toucheBouleFeu);
            }
            
            if (fireballInput) {
                System.out.println("Tentative de lancer une boule de feu (canCast=" + canCast + ")");
            }

            if (fireballInput && canCast) {
                // Obtenir la direction de visée (souris - joueur)
                Vector2 direction = getFireballDirection();
                
                // Lancer la boule de feu
                if (!canCast) return;
                castFireball(direction);
            }
        }
        
        
        
        
        // Mettre à jour les boules de feu existantes
        updateFireballs(delta);
    }
    
    private Vector2 getFireballDirection() {
        // PROBLÈME: La conversion des coordonnées écran vers coordonnées monde ne fonctionne pas correctement
        // SOLUTION: Utiliser directement camera.unproject au lieu du viewport

        // Position du joueur dans le monde
        Vector2 playerPosition = new Vector2(
            personnage.getPositionX() + 22, // Décalage hitbox
            personnage.getPositionY() + 18  // Décalage hitbox
        );
        
        // Position de la souris à l'écran
        float mouseScreenX = Gdx.input.getX();
        float mouseScreenY = Gdx.input.getY();
        
        // Obtenir une référence à la caméra depuis GameScreen
        OrthographicCamera camera = null;
        
        try {
            // Tenter d'accéder à la caméra à travers la méthode getScreen() de Main
            Object currentScreen = game.getScreen();
            if (currentScreen instanceof GameScreen) {
                camera = ((GameScreen)currentScreen).getCamera();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'accès à la caméra: " + e.getMessage());
        }
        
        Vector3 mouseInWorld;
        if (camera != null) {
            // Convertir les coordonnées écran en coordonnées monde avec la caméra du jeu
            mouseInWorld = new Vector3(mouseScreenX, mouseScreenY, 0);
            camera.unproject(mouseInWorld);
        } else {
            // Fallback avec le viewport du jeu
            mouseInWorld = new Vector3(mouseScreenX, mouseScreenY, 0);
            game.viewport.unproject(mouseInWorld);
        }
        
        // Calcul explicite de la direction en soustrayant les positions
        Vector2 direction = new Vector2(
            mouseInWorld.x - playerPosition.x,
            mouseInWorld.y - playerPosition.y
        );
        
        // Normaliser la direction (la transformer en vecteur unitaire)
        if (direction.len2() > 0) { // Éviter division par zéro
            direction.nor();
        } else {
            direction.set(0, 1); // Direction par défaut si le vecteur est nul
        }
        
        // Debug pour vérifier les coordonnées
        System.out.println("Position joueur: " + playerPosition + 
                          ", Position souris: " + mouseInWorld.x + "," + mouseInWorld.y + 
                          ", Direction: " + direction);
        
        return direction;
    }
    
    private void castFireball(Vector2 direction) {
        // if (!canCast) return;
        
        System.out.println("Lancement d'une boule de feu dans la direction: " + direction);
        
        // Position de départ (légèrement décalée dans la direction pour éviter les collisions avec le joueur)
        Vector2 startPos = new Vector2(
            personnage.getPositionX() + 26,
            personnage.getPositionY() +22
        );
        
        // Ajouter un décalage pour faire partir la boule de feu du joueur
        startPos.add(direction.cpy().scl(5));
        
        // Créer le projectile animé avec une vitesse et direction correctes
        FireballProjectile fireball = new FireballProjectile(
            startPos.x, startPos.y,
            direction.x * projectileSpeed, direction.y * projectileSpeed,
            damage, range,
            direction.angleDeg() // Angle en degrés pour la rotation
        );
        
        // Ajouter à la liste des boules de feu
        fireballs.add(fireball);
        
        // Activer le cooldown
        canCast = false;
        timeSinceLastCast = 0f;
    }
    
    private void updateFireballs(float delta) {
        // Mettre à jour toutes les boules de feu
        for (int i = fireballs.size - 1; i >= 0; i--) {
            FireballProjectile fireball = fireballs.get(i);
            
            // Mettre à jour la position
            fireball.update(delta);
            
            // Vérifier si la boule de feu doit être détruite
            if (fireball.shouldBeDestroyed()) {
                fireballs.removeIndex(i);
                continue;
            }
            
            // Vérifier les collisions avec les sbires
            checkCollisions(fireball, mursHitboxes);
        }
    }
    
    private void checkCollisions(FireballProjectile fireball, Array<Rectangle> mursHitboxes) {
        // Vérifier la collision avec tous les sbires
        for (Sbire sbire : niveau.getSbires()) {
            if (!sbire.enVie()) continue;
            
            if (fireball.getHitbox().overlaps(sbire.getHitbox())) {
                // Appliquer les dégâts au sbire
                sbire.prendreDegats(fireball.getDamage());
                System.out.println("Boule de feu a touché un sbire! Dégâts: " + fireball.getDamage());
                
                // Calculer la direction du knockback (depuis le joueur)
                Vector2 knockbackDir = new Vector2(
                    sbire.getPositionX() - personnage.getPositionX(),
                    sbire.getPositionY() - personnage.getPositionY()
                ).nor();
                
                // Appliquer un knockback
                sbire.appliquerKnockback(knockbackDir, 150f);
                
                // Marquer le projectile comme "touché" pour le détruire
                fireball.hit();
                break;
            }
        }

        
        Rectangle hitbox = fireball.getHitbox();
        for (int k = 0; k < mursHitboxes.size; k++) {
            if (hitbox.overlaps(mursHitboxes.get(k))) {
                System.out.println("Collision avec un mur en X");
                fireball.hit();
                break;
            }
        }
    }
    
    public void render(SpriteBatch batch) {
        // Dessiner toutes les boules de feu
        for (FireballProjectile fireball : fireballs) {
            fireball.render(batch);
        }
    }
    
    public void dispose() {
        // Libérer les textures
        if (fireballAnimation != null) {
            for (TextureRegion frame : fireballAnimation.getKeyFrames()) {
                if (frame != null && frame.getTexture() != null) {
                    frame.getTexture().dispose();
                }
            }
        }
    }
    
    // Getter pour affichage du cooldown dans l'interface
    public boolean canCast() {
        return canCast;
    }
    
    public float getCooldownProgress() {
        if (canCast) return 1.0f;
        return timeSinceLastCast / cooldown;
    }
    
    // Classe interne pour les projectiles de boule de feu avec animation
    private class FireballProjectile {
        private Vector2 position;
        private Vector2 velocity;
        private Vector2 initialPosition;
        private float stateTime = 0;
        private boolean isActive = true;
        private int damage;
        private float range;
        private float rotation;
        private Rectangle hitbox;
        
        public FireballProjectile(float x, float y, float vx, float vy, int damage, float range, float rotation) {
            this.position = new Vector2(x, y);
            this.initialPosition = new Vector2(x, y);
            this.velocity = new Vector2(vx, vy);
            this.damage = damage;
            this.range = range;
            this.rotation = rotation;
            
            // Créer une hitbox pour la boule de feu
            this.hitbox = new Rectangle(x - 8, y - 8, 16, 16);
        }
        
        public void update(float delta) {
            stateTime += delta;
            
            // Mettre à jour explicitement la position avec les composantes de vitesse
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;
            
            // Mettre à jour la hitbox
            hitbox.setPosition(position.x - 8, position.y - 8);
            
            // Vérifier si la boule de feu a dépassé sa portée maximale
            if (position.dst(initialPosition) > range) {
                isActive = false;
            }
        }
        
        public void render(SpriteBatch batch) {
            if (fireballAnimation != null) {
                TextureRegion currentFrame = fireballAnimation.getKeyFrame(stateTime, true);
                
                if (currentFrame != null && currentFrame.getTexture() != null) {
                    float width = 32 * scaleFactor;  // Taille fixe si la texture varie
                    float height = 32 * scaleFactor;
                    
                    batch.draw(
                        currentFrame,
                        position.x - width/2,
                        position.y - height/2,
                        width/2, height/2,
                        width, height,
                        1, 1,
                        rotation
                    );
                }
            }
        }
        
        public Rectangle getHitbox() {
            return hitbox;
        }
        
        public int getDamage() {
            return damage;
        }
        
        public boolean shouldBeDestroyed() {
            return !isActive;
        }
        
        public void hit() {
            isActive = false;
        }
    }

    public void castManualFireball(Vector2 direction) {
        // if (!canCast) return;

        castFireball(direction);
    }

    public void setDamage(int degat) {
        this.damage = degat;
    }
}