package projet.java.Map;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Portail {
    
    private Rectangle hitbox_portail = new Rectangle(300, 300, 48, 48);
    private float portailX;
    private float portailY;
    private Texture image;
    private boolean niveau_fini;

    public Portail(float x, float y, Texture image_portail) {
        this.portailX = x;
        this.portailY = y;
        this.image = image_portail;
    }

    public void setX(float x) {
        this.portailX = x;
        this.hitbox_portail.setPosition(this.portailX, this.portailY);
    }

    public void setY(float y) {
        this.portailY = y;
        this.hitbox_portail.setPosition(this.portailX, this.portailY);
    }

    public float getX() {
        return this.portailX;
    }

    public float getY() {
        return this.portailY;
    }

    public void portailOK(boolean fini){
        this.niveau_fini = fini;
    }

    public boolean afficherPortail() {
        return this.niveau_fini;
    }

    public Rectangle getHitbox() {
        return hitbox_portail;
    }

    public Texture getTexture() {
        return this.image;
    }
}
