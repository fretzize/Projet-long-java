package projet.java;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import projet.java.Menu.Menu;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public SpriteBatch batch;
    public FitViewport viewport;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(1920, 1080);
        font = new BitmapFont();

        // Charger une police vectorielle
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("police.otf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 48; // Taille de la police
//        font = generator.generateFont(parameter);
        //generator.dispose(); // Libérer les ressources du générateur




        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());
        this.setScreen(new Menu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
