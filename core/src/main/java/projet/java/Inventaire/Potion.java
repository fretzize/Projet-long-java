package projet.java.Inventaire;
import com.badlogic.gdx.graphics.Texture;

public class Potion {
    
    private int vie;
    private Texture imagepotion1 = new Texture("potion_de_vie.png");
    private Texture imagepotion2 = new Texture("potion_de_vitesse.png");
    public Potion(int vie) {
        this.vie= vie;
    }

    public int getVie() {
        return this.vie;
    }

    public Texture getImage(int i) {
        if (i==1) {
            return this.imagepotion1;
        } else {
            return this.imagepotion2;
        }
    }
}