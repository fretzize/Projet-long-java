package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class sbire implements entite{

    //La cible du sbire
    private personnage cible;

    //La position du sbire
    private Vector2 position;

    private float vitesseDeplacement; // Vitesse de déplacement du sbire
    private float vitesseProjectile; // Vitesse des projectiles

    //Texture du projectile lancé par le sbire
    private Texture projectileTexture;
    private float porteeMax = 300f;
    private int degats = 10;
    private float cooldown; // temps entre deux tirs (en secondes)
    private float tempsDepuisDernierTir = 0f;
    private Rectangle hitbox; // Hitbox du sbire


    public sbire(float x, float y,float vitesseDeplacement,float cooldown,Rectangle hitbox,Texture projectileTexture) {
        this.position = new Vector2(x, y);
        this.projectileTexture = projectileTexture;
        this.cooldown = cooldown;
        this.vitesseDeplacement = vitesseDeplacement;
        this.hitbox = hitbox;
    }

    public void setCible(personnage cible) {
        this.cible = cible;
    }

    public int getMana(){
        return this.mana;
    }

    public int getBouclier(){
        return this.bouclier;
    }

    public int getVie(){
        return this.vie;
    }

    public Vector2 getPosition(){
        return this.position;
    }

    public boolean enVie(){
        if(this.getVie() > 0){
            return true;
        }
        return false;
    }


    public void update(float delta, List<Projectile> projectiles) {
        tempsDepuisDernierTir += delta;

        if (cible != null && peutAttaquer()) {
            tirerSurCible(projectiles);
            tempsDepuisDernierTir = 0;
        }
    }

    private boolean peutAttaquer() {
        return tempsDepuisDernierTir >= cooldown;
    }

    private void tirerSurCible(List<Projectile> projectiles) {
        Vector2 positionCible = this.cible.getPosition();  // récupère la position du personnage
    
        // Calcule la direction normalisée du projectile (du sbire vers la cible)
        Vector2 direction = new Vector2(positionCible).sub(position).nor();
    
        Vector2 vitesseVecteur = direction.scl(vitesseProjectile);

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

    public void prendreDegats(int degats) {
        this.vie -= degats;
        if (this.vie < 0) {
            this.vie = 0; // Assurez-vous que la vie ne devienne pas négative
        }
    }

    public Rectangle getHitbox() {
        return hitbox;
    }


    public void deplacer(float deltaTime) {
        if (cible != null) {
            // Calcul de la direction vers la cible
            Vector2 direction = new Vector2(cible.getPosition()).sub(position).nor();
            
            // Déplacement
            position.x += direction.x * vitesseDeplacement * deltaTime;
            position.y += direction.y * vitesseDeplacement * deltaTime;
            
            // Mise à jour de la hitbox
            hitbox.setPosition(position.x, position.y);
        }
    }
}