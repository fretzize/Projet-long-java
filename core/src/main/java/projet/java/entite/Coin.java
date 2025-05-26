package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import projet.java.Main;

public class Coin {
    private float x, y;
    private Texture texture;
    private Rectangle hitbox;
    private boolean collected = false;

    public Coin(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = new Texture("coin.png"); // Add coin.png to assets!
        this.hitbox = new Rectangle(x, y, 32, 32); // Adjust size as needed
    }

    public void draw(Main game) {
        if (!collected) {
            game.batch.draw(texture, x, y, 32, 32);
        }
    }

    public boolean checkCollision(Rectangle playerHitbox) {
        if (!collected && hitbox.overlaps(playerHitbox)) {
            collected = true;
            return true;
        }
        return false;
    }

    public boolean isCollected() {
        return collected;
    }

    public void dispose() {
        texture.dispose();
    }
}
