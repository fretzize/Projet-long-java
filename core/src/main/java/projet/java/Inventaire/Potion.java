package projet.java.Inventaire;
import com.badlogic.gdx.graphics.Texture;

public class Potion {
    
    private int vie;
    private Texture imagepotion = new Texture("potion_de_vie.png");
    public Potion(int vie) {
        this.vie= vie;
    }

    public int getVie() {
        return this.vie;
    }

    public Texture getImage() {
        return this.imagepotion;
    }
}