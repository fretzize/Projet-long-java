package projet.java.entite;

import java.util.List;

public class ComportementMelee implements ComportementSbire {
    @Override
    public void executerAction(Sbire sbire, float deltaTime, List<Projectile> projectiles) {
        // Vérifier si le sbire et la cible sont en vie
        if (!sbire.enVie() || !sbire.cibleenVie()) {
            return;
        }

        // Se déplacer vers la cible
        sbire.deplacerVersCible(deltaTime);
        
        // Mettre à jour l'état et attaquer si possible
        sbire.update(deltaTime, projectiles);
    }
}
