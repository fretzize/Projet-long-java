import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class personnage implements entite {

    private String nom;

    @Override
    public getMana(){
        return this.mana;
    }

    @Override
    public getVie(){
        return this.vie;
    }

    @Override
    public getBouclier(){
        return this.bouclier;
    }
    
    // Méthode pour enlever des points de vies et afficher "Game over" lorsque le personnage meurt
    public void recevoirDegats(int degats) {
        if (personnage.enVie()) {
            hp -= degats;
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            font.draw(batch, "Game Over", 100, 150);
            batch.end();
        }
    }

    // Méthode pour vérifier l'état du personnage
    private void enVie() {
        if (hp > 0) {
            return true
        } else {
            return false
        }
    }

    

    private SpriteBatch batch;
    private BitmapFont font;
    private boolean gameOver;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
