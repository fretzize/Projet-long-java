package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


import projet.java.Main;

public class OptionScreen implements Screen {
    
    final Main game;
    private GameScreen gameScreen;
    private PauseScreen pauseScreen;
    private boolean fromPause;

    private Texture backgroundTexture;
    private int selectedIndex = 0;
    private final String[] optionItems = {"Son", "Touches", "Retour"};
    private Rectangle[] optionBounds; // Pour stocker les zones cliquables


    public OptionScreen(final Main game) {
        this.game = game;

        // Initialiser les rectangles pour chaque option
        optionBounds = new Rectangle[optionItems.length];
        for (int i = 0; i < optionItems.length; i++) {
            optionBounds[i] = new Rectangle();
        }

    }

    // Constructeur lorsqu'on vient de l'écran pause
    public OptionScreen(final Main game, PauseScreen pauseScreen, GameScreen gameScreen) {
        this(game);
        this.pauseScreen = pauseScreen;
        this.gameScreen = gameScreen;
        this.fromPause = true;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());

        // Calculer les dimensions de l'écran
        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        // Titre
        game.font.getData().setScale(2.0f);
        game.font.setColor(Color.GOLD);
        String title = "Options générales";
        float titleWidth = game.font.draw(game.batch, title, 0, 0).width;
        game.font.draw(game.batch, title, (screenWidth - titleWidth) / 2, screenHeight - 100);

        game.font.getData().setScale(1.5f);
        game.font.setColor(Color.WHITE);

        // Afficher les options du menu
        for (int i = 0; i < optionItems.length; i++) {
            String option = optionItems[i];
            //float textWidth = game.font.getRegion().getRegionWidth();
            float textWidth = game.font.draw(game.batch,option,0,0).width; // Largeur du texte
            float textHeight = game.font.draw(game.batch,option,0,0).height; // Hauteur du texte
            float x = (screenWidth - textWidth) / 2;
            float y = (screenHeight + 5*textHeight) / 2 - i * 100; // Espacement entre les options

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

        game.batch.end(); // Assurez-vous que le batch se termine après tout dessin

        // Gérer les entrées clavier
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + optionItems.length) % optionItems.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % optionItems.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            handleMenuSelection();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (fromPause) {
                game.setScreen(new PauseScreen(game, gameScreen));
            } else {
                game.setScreen(new MenuScreen(game));
            }
            dispose();
        }
    }

    private void handleMenuSelection() {
        switch (selectedIndex) {
            case 0:
                if (fromPause) {
                    game.setScreen(new SonScreen(game, this, pauseScreen, gameScreen));
                } else {
                    game.setScreen(new SonScreen(game, this));
                }
                dispose();
                break;
            case 1:
                if (fromPause) {
                    game.setScreen(new TouchesScreen(game, this, pauseScreen, gameScreen));
                } else {
                    game.setScreen(new TouchesScreen(game, this));
                }
                dispose();               
                break;
            case 2:
                if (fromPause) {
                    game.setScreen(new PauseScreen(game, gameScreen));
                } else {
                    game.setScreen(new MenuScreen(game));
                }
                dispose();
                break;
        }
    }

    private boolean isMouseOver(Rectangle bounds) {
        // Convertir les coordonnées de la souris en coordonnées du monde
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY(); 
        
        // Convertir en coordonnées du viewport
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
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
