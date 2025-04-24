package entite;
import com.badlogic.gdx.math.Vector2;
import projet.java.Main;

// import com.badlogic.gdx.math.Vector2;

public interface Entite {
    int getMana();
    int getBouclier();
    int getVie();
    Vector2 getPosition();

    void create_entite();
    void input_entite();
    void draw_entite(Main game);
    void dispose_entite(Main game);
}