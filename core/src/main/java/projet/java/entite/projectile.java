import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private Vector2 position;
    private Vector2 vitesse;
    private Texture texture;

    public Projectile(float x, float y, float vx, float vy, Texture texture) {
        this.position = new Vector2(x, y);
        this.vitesse = new Vector2(vx, vy);
        this.texture = texture;
    }

    // si on veut augmenter la vitesse des projectiles
    public void update(float augmentervitesse) {
        position.add(vitesse.x * augmentervitesse, vitesse.y * augmentervitesse);
    }

    // permet d'afficher le projectile
    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }
}