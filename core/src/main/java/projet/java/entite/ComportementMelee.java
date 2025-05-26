package projet.java.entite;

import java.util.List;

public class ComportementMelee implements ComportementSbire {
    @Override
    public void executerAction(Sbire sbire, float deltaTime, List<Projectile> projectiles) {
        // Vérifier si le sbire et la cible sont en vie
        if (!sbire.enVie() || !sbire.cibleenVie()) {
            return;
        }

        sbire.deplacerVersCible(deltaTime); // Déplacement vers la cible
        sbire.update(deltaTime, projectiles); // Met à jour la position et l'état du sbire
        
    }
}