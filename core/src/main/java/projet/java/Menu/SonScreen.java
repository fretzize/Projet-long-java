package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import projet.java.Main;


public class SonScreen implements Screen {

    final Main game;

    private Texture backgroundTexture;
    private ShapeRenderer shapeRenderer;
    private Rectangle menuMusicSlider;
    private Rectangle gameSoundSlider;
    private boolean isDraggingMenu = false;
    private boolean isDraggingGame = false;
    private float menuMusicVolume = 0.5f; // Volume de la musique de fond
    private float gameSoundVolume = 0.5f; // Volume du son du jeu

    public SonScreen(final Main game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        menuMusicSlider = new Rectangle(0, 0, 300, 20); // Initialiser la zone du curseur de volume de la musique
        gameSoundSlider = new Rectangle(0, 0, 300, 20); // Initialiser la zone du curseur de volume du son du jeu
        menuMusicVolume = game.menuMusic.getVolume(); // Volume de la musique de fond   
    }




    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

    // Calcul des dimensions
    float screenWidth = game.viewport.getWorldWidth();
    float screenHeight = game.viewport.getWorldHeight();

    // Dessiner le fond
    game.batch.begin();
    game.batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);

    // Titre
    game.font.getData().setScale(2.0f);
    game.font.setColor(Color.GOLD);
    String title = "Options sonores";
    float titleWidth = game.font.draw(game.batch, title, 0, 0).width;
    game.font.draw(game.batch, title, (screenWidth - titleWidth) / 2, screenHeight - 100);

    // Labels
    game.font.getData().setScale(1.5f);
    game.font.setColor(Color.WHITE);
    game.font.draw(game.batch, "Musique du menu: " + (int)(menuMusicVolume * 100) + "%", screenWidth/2 - 400, screenHeight/2 + 50);
    game.font.draw(game.batch, "Sons du jeu: " + (int)(gameSoundVolume * 100) + "%", screenWidth/2 - 400, screenHeight/2 - 50);

    // Bouton retour
    game.font.draw(game.batch, "Retour", 50, 50);
    game.batch.end();

    // Dessiner les curseurs
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);

    // Barres de fond
    shapeRenderer.setColor(Color.DARK_GRAY);
    shapeRenderer.rect(screenWidth/2 - 150, screenHeight/2 + 30, 300, 20);
    shapeRenderer.rect(screenWidth/2 - 150, screenHeight/2 - 70, 300, 20);

    // Curseurs
    shapeRenderer.setColor(Color.WHITE);
    shapeRenderer.rect(screenWidth/2 - 150 + (menuMusicVolume * 300) - 5, screenHeight/2 + 25, 10, 30);
    shapeRenderer.rect(screenWidth/2 - 150 + (gameSoundVolume * 300) - 5, screenHeight/2 - 75, 10, 30);

    shapeRenderer.end();

    // Gestion des entrées
    handleInput(screenWidth, screenHeight);
    }

    private void handleInput(float screenWidth, float screenHeight) {
        if (Gdx.input.isTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touch);

            // Zone du curseur de la musique du menu
            Rectangle menuSliderArea = new Rectangle(screenWidth/2 - 150, screenHeight/2 + 30, 300, 20);
            if (menuSliderArea.contains(touch.x, touch.y)) {
                menuMusicVolume = (touch.x - (screenWidth/2 - 150)) / 300f;
                menuMusicVolume = Math.max(0, Math.min(1, menuMusicVolume));
                game.menuMusic.setVolume(menuMusicVolume);
            }

            // Zone du curseur des sons du jeu
            Rectangle gameSliderArea = new Rectangle(screenWidth/2 - 150, screenHeight/2 - 70, 300, 20);
            if (gameSliderArea.contains(touch.x, touch.y)) {
                gameSoundVolume = (touch.x - (screenWidth/2 - 150)) / 300f;
                gameSoundVolume = Math.max(0, Math.min(1, gameSoundVolume));
                // À utiliser plus tard pour les sons du jeu
            }

            // Zone du bouton retour
            if (touch.x < 150 && touch.y < 70) {
                game.setScreen(new OptionScreen(game));
                dispose();
            }
        }

        // Gestion des touches clavier
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new OptionScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {
    backgroundTexture = new Texture(Gdx.files.internal("menubackground.png"));
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        shapeRenderer.dispose();
        }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
    
}