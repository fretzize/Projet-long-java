package projet.java.entite;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import projet.java.Main;
import projet.java.Menu.GameScreen;

public class Sbire implements Entite{

    // Attributs de santé de l'entité sbire
    int vie;
    int bouclier;
    int mana;

    //La cible du sbire
    private Personnage cible;

    //La position du sbire
    private float positionX ;
    private float positionY ;

    //Le comportement du sbire
    private ComportementSbire comportement;

    private float vitesseDeplacement; // Vitesse de déplacement du sbire
    private float vitesseProjectile; // Vitesse des projectiles

    private int degatsCaC;
    private float porteeCaC;

    //Texture du projectile lancé par le sbire
    private Texture projectileTexture;
    private Texture sbireTexture;
    private float porteeProjectile;  //Portée du projectile
    private int degats;
    private float cooldown; // temps entre deux tirs (en secondes)
    private float tempsDepuisDernierTir = 0f;
    private Rectangle hitbox; // Hitbox du sbire


    public Sbire(int vie, int shield,int mana, float x, float y,float vitesseDeplacement,float vitesseProjectile,float cooldown,Rectangle hitbox, float porteeProjectile,float porteeCaC, int degats, int degatsCaC, Personnage cible, ComportementSbire comportement,Texture projectileTexture,Texture sbireTexture) {
        this.vie = vie;
        this.bouclier = shield;
        this.mana = mana;
        
        this.positionX = x;
        this.positionY = y;
        this.projectileTexture = projectileTexture;
        this.cooldown = cooldown;
        this.vitesseDeplacement = vitesseDeplacement;
        this.vitesseProjectile = vitesseProjectile;
        this.hitbox = hitbox;
        this.porteeProjectile = porteeProjectile;
        this.porteeCaC = porteeCaC;
        this.degatsCaC = degatsCaC;
        this.degats = degats;
        this.cible = cible;
        this.comportement = comportement;
        this.projectileTexture = projectileTexture;
        this.sbireTexture = sbireTexture;
    }

    public void setCible(Personnage cible) {
        this.cible = cible;
    }

    public float getPorteeProjectile() {
        return this.porteeProjectile;
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

    public float getPositionX(){
        return this.positionX;
    }

    public float getPositionY(){
        return this.positionY;
    }

    public Personnage getCible(){
        return this.cible;
    }

    public float getDistanceCible(){
        float dx = this.positionX - cible.getPositionX();
        float dy = this.positionY - cible.getPositionY();
        return (float) Math.sqrt(dx * dx + dy * dy);
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

    public boolean cibleenVie(){
        if(this.cible != null && this.cible.getVie() > 0){
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
        try {
            tempsDepuisDernierTir += delta;

            if (cible != null && peutAttaquer()) {
                if(estAPorteeCaC()){
                    attaquerMelee();
                    tempsDepuisDernierTir = 0;
                }
                else if(estAPortee()){
                    tirerSurCible(projectiles);
                    tempsDepuisDernierTir = 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur dans update du sbire : " + e.getMessage());
        }
    }

    // Vérifie si le sbire peut tirer (si le cooldown est écoulé)
    private boolean peutAttaquer() {
        return tempsDepuisDernierTir >= cooldown;
    }

    // Vérifie si la cible est à portée (si la distance est inférieure à la portée maximale)
    private boolean estAPortee() {
        if (cible == null){
            return false;
        }
        float distance = getDistanceCible();
        return distance <= porteeProjectile;
    }

    private boolean estAPorteeCaC(){
        if(cible == null){
            return false;
        }
        return getDistanceCible() <= porteeCaC;
    }

    public void tirerSurCible(List<Projectile> projectiles) {
        Vector2 positionCible = new Vector2(this.cible.getPositionX(), this.cible.getPositionY());  // récupère la position du personnage
    

        // Calcule la direction normalisée du projectile (du sbire vers la cible)
        Vector2 direction = new Vector2(positionCible).sub(new Vector2(this.positionX, this.positionY)).nor();
    
        Vector2 vitesseVecteur = direction.scl(vitesseProjectile);

        Rectangle hitboxProjectile = new Rectangle(positionX, positionY, 100, 100);
        Projectile projectile = new Projectile(
            positionX,
            positionY,
            vitesseVecteur.x,
            vitesseVecteur.y,
            projectileTexture,
            degats,
            porteeProjectile,
            hitboxProjectile);

        //DEBUG
        System.out.println(projectile.getHitbox().getWidth() + " " + projectile.getHitbox().getHeight());
    
        projectiles.add(projectile);
    }

    public void attaquerMelee(){
        if (cible != null && estAPorteeCaC()) {
            cible.prendreDegat(degatsCaC);
        }
    }

    public void prendreDegats(int degats) {
        this.vie -= degats;
        if (this.vie <= 0) {
            mourir();
        }
    }

    public void appliquerKnockback(Vector2 directionKnockback, float forceKnockback){
        // TO COMPLETE
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    //Méthode pour se déplacer où l'on veut
    public void deplacer(float deltaTime, Vector2 direction) {
        if (cible != null) {
            // Calcul de la direction vers la cible
            direction.nor();
            
            // Déplacement
            positionX += direction.x * vitesseDeplacement * deltaTime;
            positionY += direction.y * vitesseDeplacement * deltaTime;
            
            // Mise à jour de la hitbox
            hitbox.setPosition(this.positionX, this.positionY);
        }
    }

    //Méthode pour se déplacer explicitement vers la cible
    public void deplacerVersCible(float deltaTime) {
        if (cible != null) {

            Vector2 position = new Vector2(this.positionX, this.positionY); //Position du sbire
            //Position de la cible
            Vector2 positionCible = new Vector2(cible.getPositionX(), cible.getPositionY());
            // Calcul de la direction vers la cible
            Vector2 direction = new Vector2(positionCible).sub(position);

            deplacer(deltaTime, direction);
        }
    }

    public void agir(float deltaTime, List<Projectile> projectiles) {
        if (comportement != null) {
            comportement.executerAction(this, deltaTime, projectiles);
        }
    }

    //affichage de l'entité
    public void draw(Main game,float scaledWidth,float scaledHeight){
        game.batch.draw(sbireTexture,this.positionX,this.positionY,scaledWidth,scaledHeight);
    }

    //USELESS

    @Override
    public void create_entite() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create_entite'");
    }

    @Override
    public void input_entite(float avance) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'input_entite'");
    }

    @Override
    public void draw_entite(Main game) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw_entite'");
    }

    @Override
    public void dispose_entite(Main game) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dispose_entite'");
    }

    @Override
    public void setHaut(int haut) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setHaut'");
    }

    @Override
    public void setBas(int bas) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBas'");
    }

    @Override
    public void setDroite(int droite) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDroite'");
    }

    @Override
    public void setGauche(int gauche) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setGauche'");
    }

    @Override
    public void setDash(int dash) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDash'");
    }

    @Override
    public int getHaut() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHaut'");
    }

    @Override
    public int getBas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBas'");
    }

    @Override
    public int getDroite() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDroite'");
    }

    @Override
    public int getGauche() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGauche'");
    }

    @Override
    public int getDash() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDash'");
    }
}