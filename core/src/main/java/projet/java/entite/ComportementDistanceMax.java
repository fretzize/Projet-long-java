package projet.java.entite;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

//Comportement d'un sbire qui veut s'éloigner en permanence au plus loin de sa cible
public class ComportementDistanceMax implements ComportementSbire{
    public void executerAction(sbire sbire, float deltaTime, List<Projectile> projectiles){
        if(sbire.enVie() && sbire.cibleenVie()){
            Vector2 direction = new Vector2(sbire.getCible().getPosition()).sub(sbire.getPosition());
            direction.x = -direction.x; // Inverser la direction pour s'éloigner
            direction.y = -direction.y; // Inverser la direction pour s'éloigner
            direction.nor(); //normalise le vecteur

            sbire.deplacer(deltaTime, direction); //Déplace le sbire dans la direction opposée à sa cible
            sbire.update(deltaTime, projectiles); //Met à jour le sbire
        }
        else{
            return;
        }
    }

}
