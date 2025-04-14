package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class sbire implements entite{

    // Attributs de santé de l'entité sbire
    int vie;
    int bouclier;
    int mana;

    //La cible du sbire
    private personnage cible;

    //La position du sbire
    private Vector2 position;

    private float vitesseDeplacement; // Vitesse de déplacement du sbire
    private float vitesseProjectile; // Vitesse des projectiles

    //Texture du projectile lancé par le sbire
    private Texture projectileTexture;
    private float porteeMax;
    private int degats = 10;
    private float cooldown; // temps entre deux tirs (en secondes)
    private float tempsDepuisDernierTir = 0f;
    private Rectangle hitbox; // Hitbox du sbire


    public sbire(float x, float y,float vitesseDeplacement,float cooldown,Rectangle hitbox, float porteeMax,Texture projectileTexture) {
        this.position = new Vector2(x, y);
        this.projectileTexture = projectileTexture;
        this.cooldown = cooldown;
        this.vitesseDeplacement = vitesseDeplacement;
        this.hitbox = hitbox;
        this.porteeMax = porteeMax;
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

    public void augmenterMana(int mana){
        this.mana += mana;
    }

    public void augmenterBouclier(int bouclier){
        this.bouclier += bouclier;
    }

    public void augmenterVie(int vie){
        this.vie += vie;
    }

    public void perdreBouclier(int valeur) {
        this.bouclier -= valeur;
        if (this.bouclier < 0) {
            this.bouclier = 0;
        }
    }

    public boolean enVie(){
        if(this.getVie() > 0){
            return true;
        }
        return false;
    }

    //Pour linstant ne sert pas a grand chose, mais apres y aura des sons et des animations quand il meurt
    public void mourir() {
        this.vie = 0;
        this.bouclier = 0;
        this.mana = 0;
    }


    public void update(float delta, List<Projectile> projectiles) {
        tempsDepuisDernierTir += delta;

        if (cible != null && peutAttaquer() && estAPortee()) {
            tirerSurCible(projectiles);
            tempsDepuisDernierTir = 0;
        }
    }

    // Vérifie si le sbire peut tirer (si le cooldown est écoulé)
    private boolean peutAttaquer() {
        return tempsDepuisDernierTir >= cooldown;
    }

    // Vérifie si la cible est à portée (si la distance est inférieure à la portée maximale)
    private boolean estAPortee() {
        if (cible == null) return false;
        float distance = position.dst(cible.getPosition());
        return distance <= porteeMax;
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
        if (this.vie <= 0) {
            mourir();
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