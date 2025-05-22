package projet.java;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
    public Music gameMusic; // Musique de jeu
    public Music gameOverMusic; // Musique de fin de jeu

    private float menuMusicVolume = 0.2f;
    private float gameSoundVolume = 0.5f;
    

    // Ajout des touches configurables
    public int toucheHaut = Input.Keys.W;
    public int toucheBas = Input.Keys.S;
    public int toucheGauche = Input.Keys.A;
    public int toucheDroite = Input.Keys.D;
    public int toucheDash = Input.Keys.SPACE;
    public int toucheInventaire = Input.Keys.E;

    public void setTouche(int index, int keycode) {
        switch(index) {
            case 0: toucheHaut = keycode; break;
            case 1: toucheBas = keycode; break;
            case 2: toucheGauche = keycode; break;
            case 3: toucheDroite = keycode; break;
            case 4: toucheDash = keycode; break;
            case 5: toucheInventaire = keycode; break;
        }
    }

    public void playGameOverSound() {
        
        
        // Charger et jouer le son de game over
        if (gameOverMusic == null) {
            gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameoversound.wav"));
            gameOverMusic.setVolume(0.5f);
        }
        
        gameOverMusic.play();
    }
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

    public void saveAudioSettings(float menuVolume, float gameVolume) {
        this.menuMusicVolume = menuVolume;
        this.gameSoundVolume = gameVolume;
        
        if (menuMusic != null) {
            menuMusic.setVolume(menuMusicVolume);
        }
    }
    
    public float getSoundVolume() {
        return gameSoundVolume;
    }
    
    public void setSoundVolume(float volume) {
        this.gameSoundVolume = volume;
    }
    
    public void disposeGameOverSound() {
        if (gameOverMusic != null) {
            
            gameOverMusic.dispose();
            gameOverMusic = null; 
        }
    }
    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(1920, 1080);
        font = new BitmapFont();

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menumusic.mp3"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(menuMusicVolume);
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
        Screen screen = this.getScreen();
        if (screen != null) {
            screen.dispose();
        }
        batch.dispose();
        font.dispose();
        if (menuMusic != null) {
            menuMusic.stop();
            menuMusic.dispose();
        }
        System.exit(0); // Quitter l'application
    }
}
