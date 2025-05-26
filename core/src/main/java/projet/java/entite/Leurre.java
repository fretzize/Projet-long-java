package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import projet.java.Main;

public class Leurre {
    private float x, y;
    private Texture texture;
    private float dureeVie;
    private float tempsEcoule;
    private float alpha; // Pour la transparence

    public Leurre(float x, float y, Texture texture, float dureeVie) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.dureeVie = dureeVie;
        this.tempsEcoule = 0;
        this.alpha = 1.0f;
    }

    public boolean update(float deltaTime) {
        tempsEcoule += deltaTime;
        alpha = 1.0f - (tempsEcoule / dureeVie); // Diminue progressivement l'opacité
        return tempsEcoule < dureeVie;
    }

    public void draw(Main game, float width, float height) {
        game.batch.setColor(1, 1, 1, alpha);
        game.batch.draw(texture, x - width/2, y - height/2, width, height);
        game.batch.setColor(1, 1, 1, 1); // Reset la couleur
    }
}