package projet.java.entite;

import java.util.List;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class ComportementBoss implements ComportementSbire {
    private float tempsPhase = 0;
    private int phaseActuelle = 0;
    private float dureePhaseTir = 3f;    // Durée de la phase de tir en secondes
    private float phaseCharge = 2f;      // Durée de la charge
    private float phaseTeleport = 1f;    // Durée après téléportation
    private boolean enCharge = false;
    private Vector2 directionCharge = new Vector2();
    
    @Override
    public void executerAction(Sbire sbire, float deltaTime, List<Projectile> projectiles) {
        if (!sbire.enVie() || !sbire.cibleenVie()) return;
        
        tempsPhase += deltaTime;
        
        // Change de phase toutes les X secondes
        switch(phaseActuelle) {
            case 0: // Phase de tir en cercle
                executerPhaseTirCercle(sbire, deltaTime, projectiles);
                if (tempsPhase >= dureePhaseTir) changerPhase();
                break;
                
            case 1: // Phase de charge
                executerPhaseCharge(sbire, deltaTime, projectiles);
                if (tempsPhase >= phaseCharge) changerPhase();
                break;
                
            case 2: // Phase de téléportation et tir
                executerPhaseTeleport(sbire, deltaTime, projectiles);
                if (tempsPhase >= phaseTeleport) changerPhase();
                break;
        }
    }
    
    private void executerPhaseTirCercle(Sbire sbire, float deltaTime, List<Projectile> projectiles) {
        // Tir en cercle - 8 projectiles dans différentes directions
        if (tempsPhase % 0.5f < deltaTime) {  // Tir toutes les 0.5 secondes
            for (int i = 0; i < 8; i++) {
                float angle = i * 45f * MathUtils.degreesToRadians;
                Vector2 direction = new Vector2(MathUtils.cos(angle), MathUtils.sin(angle));
                
                // Calcul de la vitesse du projectile
                Vector2 vitesseVecteur = new Vector2(direction).scl(sbire.getVitesseProjectile());
                
                // Création du projectile
                Rectangle hitboxProjectile = new Rectangle(
                    sbire.getPositionX() - 4f,  // Centrer la hitbox
                    sbire.getPositionY() - 4f,
                    8f,  // Taille de la hitbox
                    8f
                );
                
                Projectile projectile = new Projectile(
                    sbire.getPositionX(),
                    sbire.getPositionY(),
                    vitesseVecteur.x,
                    vitesseVecteur.y,
                    sbire.getProjectileTexture(),
                    sbire.getDegats() * 2,  // Dégâts doublés pour cette attaque
                    sbire.getPorteeProjectile(),
                    hitboxProjectile
                );
                
                projectiles.add(projectile);
            }
        }
    }
    
    private void executerPhaseCharge(Sbire sbire, float deltaTime, List<Projectile> projectiles) {
        if (!enCharge) {
            // Début de la charge - calcul de la direction vers le joueur
            directionCharge.set(sbire.getCible().getPositionX() - sbire.getPositionX(),
                              sbire.getCible().getPositionY() - sbire.getPositionY()).nor();
            enCharge = true;
        }
        
        // Charge à grande vitesse
        sbire.deplacer(deltaTime * 10, directionCharge); // Vitesse triplée pendant la charge


        // Création de projectiles pendant la charge (effet de traînée)
        if (tempsPhase % 0.1f < deltaTime) {
            Rectangle hitboxProjectile = new Rectangle(
                sbire.getPositionX() - 4f,
                sbire.getPositionY() - 4f,
                8f,
                8f
            );
            
            Projectile projectile = new Projectile(
                sbire.getPositionX(),
                sbire.getPositionY(),
                0, 0,  // Projectile stationnaire
                sbire.getProjectileTexture(),
                sbire.getDegats(),
                1f,  // Courte portée
                hitboxProjectile
            );
            
            projectiles.add(projectile);
        }
    }
    
    
    private void executerPhaseTeleport(Sbire sbire, float deltaTime, List<Projectile> projectiles) {
        if (tempsPhase < deltaTime) {  // Au début de la phase
            // Téléportation aléatoire autour du joueur
            float angle = MathUtils.random(360f) * MathUtils.degreesToRadians;
            float distance = 200f; // Distance de téléportation
            float newX = sbire.getCible().getPositionX() + MathUtils.cos(angle) * distance;
            float newY = sbire.getCible().getPositionY() + MathUtils.sin(angle) * distance;
            
            sbire.setPosition(newX, newY);
            
            // Tir en étoile après téléportation
            for (int i = 0; i < 12; i++) {
                float shotAngle = i * 30f * MathUtils.degreesToRadians;
                Vector2 direction = new Vector2(MathUtils.cos(shotAngle), MathUtils.sin(shotAngle));
                Vector2 vitesseVecteur = new Vector2(direction).scl(sbire.getVitesseProjectile());
                
                Rectangle hitboxProjectile = new Rectangle(newX - 4f, newY - 4f, 8f, 8f);
                
                Projectile projectile = new Projectile(
                    newX,
                    newY,
                    vitesseVecteur.x,
                    vitesseVecteur.y,
                    sbire.getProjectileTexture(),
                    sbire.getDegats() * 2,
                    sbire.getPorteeProjectile(),
                    hitboxProjectile
                );
                
                projectiles.add(projectile);
            }
        }
    }
    

    //Changer la phase actuelle du boss
    private void changerPhase() {
        tempsPhase = 0;
        phaseActuelle = (phaseActuelle + 1) % 3;
        enCharge = false;
    }
}