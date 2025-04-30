package projet.java;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.audio.Music;


import projet.java.Menu.MenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public SpriteBatch batch;
    public FitViewport viewport;
    public BitmapFont font;
    public Music menuMusic; // Musique de fond

    public void stopMenuMusic() {
        if (menuMusic != null && menuMusic.isPlaying()) {
            menuMusic.stop(); // Arrêter la musique de fond
        }
    }
    public void startMenuMusic() {
        if (menuMusic != null && !menuMusic.isPlaying()) {
            menuMusic.play(); // Démarrer la musique de fond
        }
    }
    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(1920, 1080);
        font = new BitmapFont();
        // Initialiser la musique une seule fois
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menumusic.mp3"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5F);
        menuMusic.play(); // Démarrer la musique de fond
        
        // Charger une police vectorielle
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("police.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48; // Taille de la police
        font = generator.generateFont(parameter);
        generator.dispose(); // Libérer les ressources du générateur




        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        if (menuMusic != null) {
            menuMusic.dispose(); // Libérer les ressources de la musique
        }
    }
}
