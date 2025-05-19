package projet.java.entite;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

//Comportement d'un sbire qui veut s'éloigner en permanence au plus loin de sa cible (exemple arme portee infinie)
public class ComportementDistanceMax implements ComportementSbire{
    @Override
    public void executerAction(Sbire sbire, float deltaTime, List<Projectile> projectiles){
        if(sbire.enVie() && sbire.cibleenVie()){
            Vector2 positionCible = new Vector2(sbire.getCible().getPositionX(), sbire.getCible().getPositionY()); //Position de la cible
            Vector2 positionSbire = new Vector2(sbire.getPositionX(), sbire.getPositionY()); //Position du sbire
            Vector2 direction = new Vector2(positionCible).sub(positionSbire); //Direction vers la cible
            
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
