package projet.java.lon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import entite.Entite;
import projet.java.lon.entite.Personnage;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    public static void main(String[] args) {

        SpriteBatch batch;
        Texture image;
        Entite personnage1;

        @Override
        public void create() {
            batch = new SpriteBatch();
            image = new Texture("libgdx.png");
            personnage1 = new Personnage(2, 3, "robert", new Texture("photo_profil.png"), batch);
            personnage1.create();
        }

        @Override
        public void render() {
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            batch.begin();
            batch.draw(image, 140, 210);
            personnage1.render();
            batch.end();
        }

        @Override
        public void dispose() {
            batch.dispose();
            image.dispose();
            personnage1.dispose();
        }
        //Entite Personnage1 = new Personnage(2 ,3 ,"robert", "photo_profil.png");
    }
}
