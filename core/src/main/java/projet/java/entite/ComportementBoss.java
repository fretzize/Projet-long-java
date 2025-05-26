package projet.java.entite;

import java.util.List;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class ComportementBoss implements ComportementSbire {
    private float tempsPhase = 0;
    private int phaseActuelle = 0;
    private float dureePhaseTir = 3f; // Durée de la phase de tir en secondes
    private float phaseCharge = 2f; // Durée de la charge
    private float phaseTeleport = 3f; // Durée après téléportatio
    private float phaseChasing = 5f; // Durée de la phase de poursuite
    private float phaseCooldown = 4f; // Durée de la phase de cooldown

    private boolean enCharge = false;
    private Vector2 directionCharge = new Vector2();

    @Override
    public void executerAction(Sbire sbire, float deltaTime, List<Projectile> projectiles) {
        if (!sbire.enVie() || !sbire.cibleenVie())
            return;

        tempsPhase += deltaTime;

        // Debug log
        //System.out.println("Phase actuelle: " + phaseActuelle + " | Temps: " + tempsPhase);

        // Change de phase toutes les X secondes
        switch (phaseActuelle) {

            case 0: // Phase de poursuite
                executerPhaseChasing(sbire, deltaTime, projectiles);
                if (tempsPhase >= phaseChasing)
                    changerPhase();
                break;

            case 1: // Phase de tir en cercle
                executerPhaseTirCercle(sbire, deltaTime, projectiles);
                if (tempsPhase >= dureePhaseTir)
                    changerPhase();
                break;

            case 2: // Phase de poursuite après tir
                executerPhaseChasing(sbire, deltaTime, projectiles);
                if (tempsPhase >= phaseCooldown)
                    changerPhase();
                break;

            case 3: // Phase de charge
                executerPhaseCharge(sbire, deltaTime, projectiles);
                if (tempsPhase >= phaseCharge)
                    changerPhase();
                break;

            case 4 : // Phase de cooldown après charge
                executerPhaseCooldown(sbire, deltaTime);
                if (tempsPhase >= phaseCooldown)
                    changerPhase();
                break;

            case 5: // Phase de téléportation et tir
                executerPhaseTeleport(sbire, deltaTime, projectiles);
                if (tempsPhase >= phaseTeleport)
                    changerPhase();
                break;
        }
    }

    private void executerPhaseChasing(Sbire sbire, float deltaTime, List<Projectile> projectiles) {
        // Phase de poursuite
        sbire.deplacerVersCible(deltaTime); // Déplacement vers la cible
        sbire.update(deltaTime, projectiles); // Met à jour la position et l'état du sbire
        
        if (tempsPhase >= phaseChasing)
            changerPhase();
    }


    private void executerPhaseCooldown(Sbire sbire, float deltaTime) {
        // Phase de cooldown - le sbire reste immobile

        // Pas de tir pendant cette phase
        if (tempsPhase >= phaseCooldown)
            changerPhase();
    }

    private void executerPhaseTirCercle(Sbire sbire, float deltaTime, List<Projectile> projectiles) {


        // Tir en cercle - 8 projectiles dans différentes directions
        if (tempsPhase % 0.5f < deltaTime) { // Tir toutes les 0.5 secondes
            for (int i = 0; i < 8; i++) {
                float angle = i * 45f * MathUtils.degreesToRadians;
                Vector2 direction = new Vector2(MathUtils.cos(angle), MathUtils.sin(angle));

                // Calcul de la vitesse du projectile
                Vector2 vitesseVecteur = new Vector2(direction).scl(sbire.getVitesseProjectile());

                // Création du projectile
                Rectangle hitboxProjectile = new Rectangle(
                        sbire.getPositionX() - 4f, // Centrer la hitbox
                        sbire.getPositionY() - 4f,
                        15f, // Taille de la hitbox
                        15f);

                Projectile projectile = new Projectile(
                        sbire.getPositionX(),
                        sbire.getPositionY(),
                        vitesseVecteur.x,
                        vitesseVecteur.y,
                        sbire.getProjectileTexture(),
                        sbire.getDegats() * 2, // Dégâts doublés pour cette attaque
                        sbire.getPorteeProjectile(),
                        hitboxProjectile);

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
        sbire.deplacer(deltaTime * 11, directionCharge); // Vitesse triplée pendant la charge

        // Attaque corps à corps puissante quand proche de la cible
        if (sbire.getDistanceCible() <= sbire.getPorteeCaC()) {
            // Inflige des dégâts massifs
            sbire.getCible().prendreDegat(sbire.getDegats() * 5); // 5 fois les dégâts normaux
            // Force le changement de phase
            tempsPhase = phaseCharge;
            enCharge = false;
        }
    }

    private void executerPhaseTeleport(Sbire sbire, float deltaTime, List<Projectile> projectiles) {
        // On vérifie si on vient juste de commencer la phase
        if (tempsPhase <= deltaTime) {
            // Position du joueur
            float playerX = sbire.getCible().getPositionX();
            float playerY = sbire.getCible().getPositionY();
            
            // Crée les leurres autour du joueur (et non du boss)
            sbire.creerLeurres(6, 50f, playerX, playerY); // Il faudra modifier la méthode pour accepter x,y
            
            // Téléportation aléatoire autour du joueur
            float angle = MathUtils.random(360f) * MathUtils.degreesToRadians;
            float distance = 50f;
            float newX = playerX + MathUtils.cos(angle) * distance;
            float newY = playerY + MathUtils.sin(angle) * distance;
            
            sbire.setPosition(newX, newY);

        }
    }

    // Changer la phase actuelle du boss
    private void changerPhase() {
        tempsPhase = 0;
        phaseActuelle = (phaseActuelle + 1) % 6;
        enCharge = false;
    }
}