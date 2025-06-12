package projet.java.entite;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import projet.java.Main;
import projet.java.animation.BossAnimationHandler;
import projet.java.animation.ImpactEffect;
import projet.java.animation.SbireAnimationHandler; // Ajouter cet import en haut de la classe avec les autres imports

public class Sbire implements Entite{

    // Attributs de santé de l'entité sbire
    int vie;
    int bouclier;
    int mana;
    private float vieInit;

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
    private int degats; // Dégâts infligés par les projectiles
    private float cooldown; // temps entre deux tirs (en secondes)
    private float tempsDepuisDernierTir = 0f;
    private Rectangle hitbox; // Hitbox du sbire

    // Ajouter ces variables d'instance à la classe Sbire
    private Vector2 knockbackVelocity = new Vector2(0, 0);
    private boolean isKnockedBack = false;
    private float knockbackFriction = 0.9f; // Valeur entre 0 et 1, plus elle est proche de 1, plus le knockback dure longtemps

    // Ajouter ces variables pour l'effet visuel de dégâts (en haut avec les autres variables d'instance)
    private boolean damageEffect = false;
    private float damageEffectTime = 0;
    private final float DAMAGE_EFFECT_DURATION = 0.5f; // Increased from 0.3f to 0.5f

    private ImpactEffect impactEffect = new ImpactEffect();
    private Vector2 lastKnockbackDirection = new Vector2();

    private SbireAnimationHandler animationHandler;
    private BossAnimationHandler bossAnimationHandler;
    private boolean isBoss = false;
    public boolean isAttacking = false;
    public Rectangle zoneAttaque; // Zone d'attaque du sbire

    private ArrayList<Leurre> leurres;
    private static final float DUREE_LEURRE = 5.0f; // Durée de vie des leurres en secondes

    // Ajouter ces variables à la classe Sbire avec les autres attributs
    private float attackAnimationTimer = 0;
    private final float ATTACK_ANIMATION_DURATION = 0.5f; // Durée en secondes de l'animation d'attaque



    // Direction de déplacement pour l'animation
    private Vector2 lastMovementDirection = new Vector2(0, -1); // Direction par défaut: vers le bas

    // Dans la classe Sbire, ajouter ces attributs avec les autres variables
    private com.badlogic.gdx.audio.Sound hitSound;
    private Main game;

    public Sbire(int vie, int shield,int mana, float x, float y,float vitesseDeplacement,
                float vitesseProjectile,float cooldown,Rectangle hitbox, float porteeProjectile,
                float porteeCaC, int degats, int degatsCaC, Personnage cible, ComportementSbire comportement,
                Texture projectileTexture,Texture sbireTexture,Rectangle zoneAttaque) {

        this.vie = vie;
        this.bouclier = shield;
        this.mana = mana;
        this.vieInit = vie;
        
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
        this.leurres = new ArrayList<>();
        this.zoneAttaque = zoneAttaque;

        // Récupérer une référence au jeu principal
        this.game = ((Main)Gdx.app.getApplicationListener());
        
        // Charger le son de dégât
        try {
            this.hitSound = Gdx.audio.newSound(Gdx.files.internal("degatmobsound.mp3"));
        } catch (Exception e) {
            System.err.println("Impossible de charger le son de dégât pour sbire: " + e.getMessage());
        }


        // Réduire la hitbox
        adjustHitbox();

        // Déterminer si c'est un boss basé sur le comportement
        this.isBoss = comportement instanceof ComportementBoss;
        
        // Initialiser le gestionnaire d'animation approprié
        if (this.isBoss) {
            this.bossAnimationHandler = new BossAnimationHandler();
            System.out.println("Boss créé avec son animation spéciale!");
        } else {
            this.animationHandler = new SbireAnimationHandler();
        }
    }

    public void setCible(Personnage cible) {
        this.cible = cible;
    }

    public float getPorteeProjectile() {
        return this.porteeProjectile;
    }

    public float getPorteeCaC() {
        return this.porteeCaC;
    }
    public float getVitesseProjectile() {
        return this.vitesseProjectile;
    }

    public Texture getProjectileTexture(){
        return this.projectileTexture;
    }

    public int getDegats(){
        return this.degats;
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

            // Gestion de la durée de l'animation d'attaque
            if (isAttacking) {
                attackAnimationTimer += delta;
                if (attackAnimationTimer >= ATTACK_ANIMATION_DURATION) {
                    isAttacking = false;
                    attackAnimationTimer = 0;
                }
            }
            if (cible != null && peutAttaquer()) {
                if(estAPorteeCaC()) {
                    attaquerMelee();
                    tempsDepuisDernierTir = 0;
                }
                else if(estAPortee()){
                    tirerSurCible(projectiles);
                    tempsDepuisDernierTir = 0;
                }
            }

            // Mettre à jour l'effet de dégâts visuel
            if (damageEffect) {
                damageEffectTime += delta;
                if (damageEffectTime >= DAMAGE_EFFECT_DURATION) {
                    damageEffect = false; // Arrêter l'effet après sa durée
                    damageEffectTime = 0;
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

    // Vérifier si cette méthode calcule correctement la distance
    private boolean estAPorteeCaC() {
        if (cible == null) {
            return false;
        }

        updateZoneAttaque(); // Mettre à jour la zone d'attaque avant de vérifier
        if (zoneAttaque.overlaps(cible.getHitbox())) {
            // System.out.println("Coordonnées hitbox joueur : "+ cible.getHitbox().getX() + " " + cible.getHitbox().getY());
            // System.out.println("Taille hitbox joueur : " + cible.getHitbox().getWidth() + " " + cible.getHitbox().getHeight());
            // System.out.println("Coordonnées attaque sbire" + zoneAttaque.getX() + " " + zoneAttaque.getY());
        
            return true; // La zone d'attaque intersecte la hitbox de la cible
        }
        
        return false; // La cible n'est pas à portée
    }

    public void tirerSurCible(List<Projectile> projectiles) {
        Vector2 positionCible = new Vector2(this.cible.getPositionX(), this.cible.getPositionY());  // récupère la position du personnage
        Vector2 playerPosition = new Vector2(this.getPositionX() + 22, // Décalage hitbox
                this.getPositionY() + 18  // Décalage hitbox
            );

        // Calcule la direction normalisée du projectile (du sbire vers la cible)
        // Vector2 direction = new Vector2(positionCible).sub(new Vector2(this.positionX, this.positionY)).nor();
        
        Vector2 direction = new Vector2(
            positionCible.x - playerPosition.x,
            positionCible.y - playerPosition.y
        );

        // Normaliser la direction (la transformer en vecteur unitaire)
        if (direction.len2() > 0) { // Éviter division par zéro
            direction.nor();
        } else {
            direction.set(0, 1); // Direction par défaut si le vecteur est nul
        }

        // playerPosition.add(direction.cpy().scl(20));

        Vector2 vitesseVecteur = direction.scl(vitesseProjectile);

        float hitboxWidth = 15;
        float hitboxHeight = 15;

        Rectangle hitboxProjectile = new Rectangle(playerPosition.x - hitboxWidth/2, playerPosition.y - hitboxHeight/2, hitboxWidth, hitboxHeight);
        Projectile projectile = new Projectile(playerPosition.x, playerPosition.y, vitesseVecteur.x, vitesseVecteur.y, projectileTexture, degats, porteeProjectile, hitboxProjectile);

        //DEBUG
        //System.out.println(projectile.getHitbox().getWidth() + " " + projectile.getHitbox().getHeight());
        
        projectiles.add(projectile);
    }

    public void attaquerMelee() {
        if (cible != null) {
            
            // Vérifier si la zone d'attaque intersecte avec la hitbox du joueur
            if (estAPorteeCaC()) {
                // Appliquer les dégâts
                this.getCible().prendreDegat(degatsCaC);
                
                // Ajouter des logs pour débugger
                System.out.println("Sbire attaque! Dégâts infligés: " + degatsCaC);
                System.out.println("Vie restante du personnage: " + cible.getVie() + ", Bouclier: " + cible.getBouclier());
                
                // Mettre à jour isAttacking pour l'animation
                isAttacking = true;
            }
        }
    }

    public void prendreDegats(int degats) {
        // S'assurer que le sbire est vivant avant d'appliquer les dégâts
        if (!enVie()) return;
        
        // Appliquer les dégâts
        this.vie -= degats;
        
        // Logs pour déboguer
        System.out.println("Sbire touché! Dégâts: " + degats + ", Vie restante: " + this.vie);
        
        // Jouer le son de dégât si le sbire est encore en vie
        if (hitSound != null && game != null) {
            // Calculer l'atténuation basée sur la distance au joueur (plus loin = moins fort)
            float distance = getDistanceCible();
            // Réduire l'atténuation par distance et ajouter un multiplicateur global (1.5f)
            float volumeScale = Math.max(0.6f, 1.8f - (distance / 800f));
            
            // Jouer le son en respectant le volume global des effets mais plus fort
            hitSound.play(game.getSoundVolume() * volumeScale);
        }
        
        // Activer l'effet visuel de dégât
        damageEffect = true;
        damageEffectTime = 0;
        
        // Vérifier si le sbire est mort
        if (this.vie <= 0) {
            System.out.println("Sbire tué!");
            mourir();
        }
    }


    // Méthode modifiée pour que l'effet d'impact apparaisse dans la bonne direction
    public void appliquerKnockback(Vector2 directionKnockback, float forceKnockback) {
        // Au lieu d'appliquer directement le déplacement, on initialise une vélocité
        knockbackVelocity.set(directionKnockback).scl(forceKnockback);
        isKnockedBack = true;
        
        // Sauvegarder la direction du knockback
        lastKnockbackDirection.set(directionKnockback);
        
        // Calculer le centre de la hitbox du sbire
        float centerX = this.positionX + (this.hitbox.width / 2);
        float centerY = this.positionY + (this.hitbox.height / 2);
        
        // La direction du knockback pointe du joueur vers le sbire
        // Nous voulons que l'effet apparaisse du côté où l'attaque frappe le sbire
        // C'est donc du côté d'où vient le knockback (direction opposée)
        
        // CORRECTION: Utiliser la direction du knockback directement, sans l'inverser
        Vector2 knockbackDir = new Vector2(directionKnockback).nor();
        
        // Calculer la distance du centre au bord de la hitbox dans cette direction
        float distanceToBorder;
        if (Math.abs(knockbackDir.x) > Math.abs(knockbackDir.y)) {
            // Si le mouvement est plus horizontal que vertical
            distanceToBorder = (knockbackDir.x > 0 ? this.hitbox.width / 2 : -this.hitbox.width / 2) / knockbackDir.x;
        } else {
            // Si le mouvement est plus vertical qu'horizontal
            distanceToBorder = (knockbackDir.y > 0 ? this.hitbox.height / 2 : -this.hitbox.height / 2) / knockbackDir.y;
        }
        
        // Calculer le point d'impact sur le bord de la hitbox
        float impactX = centerX + knockbackDir.x * distanceToBorder;
        float impactY = centerY + knockbackDir.y * distanceToBorder;
        
        // Lancer l'animation d'impact avec la direction inverse pour l'orientation correcte
        impactEffect.start(impactX, impactY, knockbackDir.cpy().scl(-1));
    }

    // Ajouter cette méthode à la classe Sbire
    public ImpactEffect getImpactEffect() {
        return impactEffect;
    }

    // Ajouter cette méthode qui sera appelée à chaque frame
    private void updateKnockback(float deltaTime) {
        if (isKnockedBack && knockbackVelocity.len() > 0.5f) {
            this.positionX += knockbackVelocity.x * deltaTime;
            this.positionY += knockbackVelocity.y * deltaTime;
            
            // Mettre à jour la hitbox
            updateHitboxPosition();
            
            // Réduire la vélocité du knockback
            knockbackVelocity.scl(knockbackFriction);
            
            // Arrêter le knockback si la vélocité est trop faible
            if (knockbackVelocity.len() < 0.5f) {
                isKnockedBack = false;
                knockbackVelocity.set(0, 0);
            }
        }
    }

    // Modifier la méthode agir pour intégrer la mise à jour du knockback
    public void agir(float deltaTime, List<Projectile> projectiles) {
        // Mise à jour de l'effet d'impact
        impactEffect.update(deltaTime);
        
        // Mise à jour du timer de l'effet de dégât
        if (damageEffect) {
            damageEffectTime += deltaTime;
            if (damageEffectTime >= DAMAGE_EFFECT_DURATION) {
                damageEffect = false;
                damageEffectTime = 0;
            }
        }
        
        // Mise à jour du knockback
        updateKnockback(deltaTime);
        
        // Calculer la direction de mouvement pour l'animation
        Vector2 movementDir = null;
        
        // Si le sbire est en knockback, utiliser cette direction pour l'animation
        if (isKnockedBack && knockbackVelocity.len() > 0.5f) {
            movementDir = new Vector2(knockbackVelocity).nor();
            isAttacking = false; // Ne pas attaquer pendant le knockback
        } 
        // Sinon, si le comportement normal est en cours
        else if (comportement != null) {
            // Pour un sbire melee ou boss, vérifier s'il est à portée d'attaque
            if ((comportement instanceof ComportementMelee || comportement instanceof ComportementBoss) && estAPorteeCaC()) {
                isAttacking = true;
            } else {
                isAttacking = false; // Ne pas attaquer si pas à portée

                if (cible != null) {
                    // Sbire se déplace vers la cible
                    float dx = cible.getPositionX() - this.positionX;
                    float dy = cible.getPositionY() - this.positionY;
                    movementDir = new Vector2(dx, dy).nor();
                    lastMovementDirection.set(movementDir);
                }

            }
        }
        
        // Mettre à jour l'animation appropriée
        if (isBoss && bossAnimationHandler != null) {
            bossAnimationHandler.update(deltaTime, movementDir, isAttacking);
        } else if (animationHandler != null) {
            animationHandler.update(deltaTime, movementDir, isAttacking);
        }
        
        // Continuer avec le comportement normal si pas en knockback
        if (!isKnockedBack && comportement != null) {
            comportement.executerAction(this, deltaTime, projectiles);
        }
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    //Méthode pour se déplacer où l'on veut
    public void deplacer(float deltaTime, Vector2 direction) {
        if (direction != null && !direction.isZero()) {
            direction.nor();
            
            this.positionX += direction.x * vitesseDeplacement * deltaTime;
            this.positionY += direction.y * vitesseDeplacement * deltaTime;
            
            // Mettre à jour la hitbox avec la nouvelle position
            updateHitboxPosition();
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

    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
        hitbox.setPosition(x, y);
    }



    //affichage de l'entité
    public void draw(Main game, float scaledWidth, float scaledHeight) {
        // Dessine d'abord les leurres
        for (int i = leurres.size() - 1; i >= 0; i--) {
            Leurre leurre = leurres.get(i);
            if (!leurre.update(Gdx.graphics.getDeltaTime())) {
                leurres.remove(i);
            } else {
                leurre.draw(game, scaledWidth, scaledHeight);
            }
        }
        
        // Dessine ensuite le sbire
        game.batch.draw(sbireTexture, 
            this.positionX - scaledWidth/2,
            this.positionY - scaledHeight/2,
            scaledWidth,
            scaledHeight);
    }

    //Méthodes pour gérer les leurres
    public void creerLeurres(int nombreLeurres, float distanceLeurre, float centreX, float centreY) {
        leurres.clear();
        
        for (int i = 0; i < nombreLeurres; i++) {
            float angle = i * (360f/nombreLeurres) * MathUtils.degreesToRadians;
            float leurreX = centreX + MathUtils.cos(angle) * distanceLeurre;
            float leurreY = centreY + MathUtils.sin(angle) * distanceLeurre;
            
            leurres.add(new Leurre(leurreX, leurreY, this.sbireTexture, DUREE_LEURRE));
        }
    }

    // Ajouter ces méthodes à la classe Sbire
    public boolean isDamageEffectActive() {
        return damageEffect;
    }

    public float getDamageEffectIntensity() {
        if (!damageEffect) return 0;
        // Retourne une valeur entre 0 et 1 qui diminue au fil du temps
        return 1 - (damageEffectTime / DAMAGE_EFFECT_DURATION);
    }

    public Texture getSbireTexture() {
        return this.sbireTexture;
    }

    // Add this method to explicitly update the damage effect
    public void updateDamageEffect(float deltaTime) {
        if (damageEffect) {
            damageEffectTime += deltaTime;
            if (damageEffectTime >= DAMAGE_EFFECT_DURATION) {
                damageEffect = false;
                damageEffectTime = 0;
            }
        }
    }

    // Ajouter une méthode pour obtenir la frame d'animation courante
    public TextureRegion getCurrentFrame() {
        if (isBoss && bossAnimationHandler != null) {
            return bossAnimationHandler.getCurrentFrame();
        } else if (animationHandler != null) {
            return animationHandler.getCurrentFrame();
        }
        return null;
    }

    // Ajouter cette méthode dans dispose() pour nettoyer les ressources
    public void dispose() {
        if (animationHandler != null) {
            animationHandler.dispose();
        }
        
        // Libérer le son
        if (hitSound != null) {
            hitSound.dispose();
        }
        if (bossAnimationHandler != null) {
            bossAnimationHandler.dispose();
        }
        // Autres nettoyages...
    }

    // Ajouter cette méthode dans la classe Sbire
    private void updateHitboxPosition() {
        // Réduire la taille de la hitbox à 60% pour les sbires normaux, 80% pour les boss
        float reductionFactor = isBoss ? 0.8f : 0.6f;
        
        // Calculer le décalage pour centrer la hitbox
        float offsetX = (this.hitbox.width / reductionFactor - this.hitbox.width) / 2;
        float offsetY = (this.hitbox.height / reductionFactor - this.hitbox.height) / 2;
        
        // Mettre à jour la position de la hitbox
        this.hitbox.x = this.positionX + offsetX;
        this.hitbox.y = this.positionY + offsetY;
    }

    // Ajouter cette méthode pour réduire et centrer la hitbox
    private void adjustHitbox() {
        // Réduire la taille de la hitbox à 60% pour les sbires normaux, 80% pour les boss
        float reductionFactor = isBoss ? 0.8f : 0.6f;
        float newWidth = this.hitbox.width * reductionFactor;
        float newHeight = this.hitbox.height * reductionFactor;
        
        // Calculer le décalage pour centrer la hitbox
        float offsetX = (this.hitbox.width - newWidth) / 2;
        float offsetY = (this.hitbox.height - newHeight) / 2;
        
        // Mettre à jour la hitbox avec les nouvelles dimensions et position
        this.hitbox.width = newWidth;
        this.hitbox.height = newHeight;
        this.hitbox.x = this.positionX + offsetX;
        this.hitbox.y = this.positionY + offsetY;
    }

    /**
     * Vérifie si ce sbire est un boss.
     * 
     * @return true si c'est un boss, false sinon
     */
    public boolean isBoss() {
        return this.isBoss;
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

    public ArrayList<Leurre> getLeurres() {
        return this.leurres;
    }


    public Rectangle getZoneAttaque() {
        return zoneAttaque;
    }

    public void updateZoneAttaque(){
        if (cible == null) return;

        float sbireX = this.positionX + this.hitbox.width / 2;
        float sbireY = this.positionY + this.hitbox.height / 2;
        
        // Calculer la direction vers le joueur
        float dirX = cible.getPositionX() - sbireX;
        float dirY = cible.getPositionY() - sbireY;
        Vector2 position = new Vector2(this.getPositionX()+22, this.getPositionY()+18);
        Vector2 direction = new Vector2(dirX, dirY);
        Vector2 hitboxSize = new Vector2(10, 10);

        // Calcule les dimensions de la zone d'attaque
        // portee = 45 f pour le debut plus grand que celui du joueur

        float portee = 45f;
        float zoneWidth = portee;
        float zoneHeight = portee / 1.5f; 
        
        // Ajuster les dimensions selon la direction pour une meilleure jouabilité
        if (Math.abs(direction.y) > Math.abs(direction.x)) {
            // Attaque verticale (haut/bas)
            float temp = zoneWidth;
            zoneWidth = zoneHeight;
            zoneHeight = temp;
        }
        
        // Position du centre de la hitbox du personnage
        Vector2 hitboxCenter = new Vector2(
            position.x + hitboxSize.x / 2 ,
            position.y + hitboxSize.y / 2 
        );
        
        // Normaliser la direction pour avoir un vecteur unitaire
        Vector2 normalizedDir = new Vector2(direction).nor();
        
        // Calculer où placer la hitbox d'attaque pour qu'elle soit adjacente à la hitbox du joueur
        float offsetX = (hitboxSize.x / 2) * Math.abs(normalizedDir.x);
        float offsetY = (hitboxSize.y / 2) * Math.abs(normalizedDir.y);
        
        // Position du point d'attaque (bord de la hitbox du joueur dans la direction de l'attaque)
        Vector2 attackPoint = new Vector2(hitboxCenter);
        attackPoint.x += normalizedDir.x * offsetX;
        attackPoint.y += normalizedDir.y * offsetY;
        
        // Positionner la zone d'attaque à partir de ce point
        Vector2 centerZone = new Vector2(attackPoint).add(
            normalizedDir.x * zoneWidth / 2,
            normalizedDir.y * zoneHeight / 2
        );
        
        // Définir la zone d'attaque comme un rectangle
        zoneAttaque.set(
            centerZone.x - zoneWidth / 2,
            centerZone.y - zoneHeight / 2,
            zoneWidth,
            zoneHeight
        );
        
    }

	@Override
	public void setPositionX(float x) {
		this.positionX = x;
	}

	@Override
	public void setPositionY(float y) {
		this.positionY = y;
	}

    public float getVieInit() {
        return this.vieInit;
    }
}
