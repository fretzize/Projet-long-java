package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;

import projet.java.Main;

public class PauseScreen implements Screen {

    final Main game;
    private GameScreen gameScreen; 
    
    private Texture backgroundTexture;
    private int selectedIndex = 0; 
    private final String[] menuOptions = { "Reprendre", "Options", "Menu Principal" };
    private Rectangle[] optionBounds;

    public PauseScreen(final Main game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen; 
        
        // Initialiser les rectangles pour chaque option
        optionBounds = new Rectangle[menuOptions.length];
        for (int i = 0; i < menuOptions.length; i++) {
            optionBounds[i] = new Rectangle();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0.7f); 

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        // Calculer les dimensions de l'écran
        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();
        
        game.batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);

        // Titre
        game.font.getData().setScale(3.0f);
        game.font.setColor(Color.GOLD);
        String title = "Pause";
        float titleWidth = game.font.draw(game.batch, title, 0, 0).width;
        float titleX = (screenWidth - titleWidth) / 2;
        float titleY = screenHeight - 100;
        game.font.draw(game.batch, title, titleX, titleY);

        // Options du menu
        game.font.getData().setScale(1.5f);

        for (int i = 0; i < menuOptions.length; i++) {
            String option = menuOptions[i];
            float textWidth = game.font.draw(game.batch, option, 0, 0).width;
            float textHeight = game.font.draw(game.batch, option, 0, 0).height;
            float x = (screenWidth - textWidth) / 2;
            float y = (screenHeight + 5 * textHeight) / 2 - i * 100;

            // Mettre à jour la zone cliquable
            optionBounds[i].set(x, y - textHeight, textWidth, textHeight);

            // Vérifier si la souris survole l'option
            if (isMouseOver(optionBounds[i])) {
                selectedIndex = i;
                if (Gdx.input.justTouched()) {
                    handleMenuSelection();
                }
            }

            // Changer la couleur pour l'option sélectionnée
            if (i == selectedIndex) {
                game.font.setColor(Color.YELLOW);
            } else {
                game.font.setColor(Color.WHITE);
            }

            game.font.draw(game.batch, option, x, y);
        }

        game.batch.end();

        // Gérer les entrées clavier
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + menuOptions.length) % menuOptions.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % menuOptions.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            handleMenuSelection();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // Revenir directement au jeu avec Échap
            game.setScreen(gameScreen);
            dispose();
        }
    }

    private void handleMenuSelection() {
        switch (selectedIndex) {
            case 0: // Reprendre
                game.setScreen(gameScreen);
                dispose();
                break;
            case 1: // Options
                game.setScreen(new OptionScreen(game,this, gameScreen));
                dispose();
                break;
            case 2: // Menu Principal
                game.startMenuMusic();
                game.setScreen(new MenuScreen(game));
                gameScreen.dispose(); // Libérer les ressources du jeu
                dispose();
                break;
        }
    }

    private boolean isMouseOver(Rectangle bounds) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        Vector3 worldCoords = game.viewport.getCamera().unproject(new Vector3(mouseX, mouseY, 0));
        return bounds.contains(worldCoords.x, worldCoords.y);
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("menubackground.png"));
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

    @Override
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}