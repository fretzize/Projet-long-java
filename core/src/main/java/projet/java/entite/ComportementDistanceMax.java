package projet.java.entite;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

//Comportement d'un sbire qui veut s'éloigner en permanence au plus loin de sa cible
public class ComportementDistanceMax implements ComportementSbire{
    public void executerAction(sbire sbire, float deltaTime, List<Projectile> projectiles){
        while(sbire.enVie() && sbire.cibleenVie()){
            Vector2 direction = new Vector2(sbire.getCible().getPosition()).sub(sbire.getPosition());
            
        }
    }

}
