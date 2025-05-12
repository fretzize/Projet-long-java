package projet.java.entite;

import java.util.List;

public interface ComportementSbire {
    void executerAction(Sbire sbire, float deltaTime, List<Projectile> projectiles);
}