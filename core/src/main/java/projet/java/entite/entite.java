package projet.java.entite;
import java.util.Vector;
import com.badlogic.gdx.math.Vector2;

public interface entite {
    int vie;
    int bouclier;
    int mana;
    Vector2 position;

    public int getMana();
    public int getBouclier();
    public int getVie();
    public Vector2 getPosition();
    
}
