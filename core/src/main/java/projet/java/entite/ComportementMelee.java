package projet.java.entite;

import java.util.List;

//Comportement d'un sbire qui attaque principalement au corps à corps et qui doit se rapprocher de la cible pour attaquer
public class ComportementMelee implements ComportementSbire {
    public void executerAction(sbire sbire, float deltaTime, List<Projectile> projectiles){
        while(sbire.enVie() && sbire.cibleenVie()){
            sbire.deplacerVersCible(deltaTime);
            sbire.update(deltaTime, projectiles);
        }
    }
}
