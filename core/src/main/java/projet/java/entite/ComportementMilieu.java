package projet.java.entite;

import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.math.Vector2;

//Comportement d'un sbire qui veut s'éloigner de sa cible mais en restant à distance de tir (projectiles)
public class ComportementMilieu implements ComportementSbire{
    
    @Override
    public void executerAction(Sbire sbire, float deltaTime, List<Projectile> projectiles){
        if(sbire.enVie() && sbire.cibleenVie()){

            Vector2 positionCible = new Vector2(sbire.getCible().getPositionX(), sbire.getCible().getPositionY()); //Position de la cible
            Vector2 positionSbire = new Vector2(sbire.getPositionX(), sbire.getPositionY()); //Position du sbire
            
            Vector2 direction = new Vector2(positionCible).sub(positionSbire); //Direction vers la cible
            float porteeProjectile = sbire.getPorteeProjectile(); //Portée à distance du sbire
            
            //Se rapprocher de la cible si elle est hors de portée
            if(porteeProjectile < sbire.getDistanceCible()){
                sbire.deplacerVersCible(deltaTime);
            }

            //S'éloigner de la cible si l'on peut encore tirer en le faisant
            else if(porteeProjectile > sbire.getDistanceCible()){
                direction.x = -direction.x; // Inverser la direction pour s'éloigner
                direction.y = -direction.y; // Inverser la direction pour s'éloigner
                direction.nor(); //normalise le vecteur
    
                sbire.deplacer(deltaTime, direction); //Déplace le sbire dans la direction opposée à sa cible
            }

            //Else (la cible est pile à portée) : ne pas bouger

            sbire.update(deltaTime, projectiles); //Met à jour le sbire
        }
    }
}
