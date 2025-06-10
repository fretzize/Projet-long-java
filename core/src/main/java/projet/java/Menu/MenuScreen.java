package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import projet.java.Inventaire.DatabaseItem;
import projet.java.Inventaire.Inventaire;
import projet.java.Menu.InventaireScreen;
import projet.java.Inventaire.Item;
import projet.java.Inventaire.Inventaire;
import projet.java.Inventaire.Coffre;
import projet.java.Inventaire.Item.ItemType;


import projet.java.Main;
import projet.java.Inventaire.Inventaire;

public class MenuScreen implements Screen {

    final Main game;

    private Texture backgroundTexture;
    private int selectedIndex = 0; // Index de l'option sélectionnée
    private final String[] menuOptions = { "Commencer", "Options", "Quitter" };
    private Rectangle[] optionBounds; 

    public MenuScreen(final Main game) {
        this.game = game;
        optionBounds = new Rectangle[menuOptions.length];
        for (int i = 0; i < menuOptions.length; i++) {
            optionBounds[i] = new Rectangle();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin(); 

        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        game.batch.draw(backgroundTexture, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());

        game.font.getData().setScale(3.0f); 
        game.font.setColor(Color.GOLD); 
        String title = "Héraclès";
        float titleWidth = game.font.draw(game.batch, title, 0, 0).width;
        float titleX = (screenWidth - titleWidth) / 2;
        float titleY = screenHeight - 100; 
        game.font.draw(game.batch, title, titleX, titleY);

        game.font.getData().setScale(1.5f);

        game.font.getData().setScale(1.5f); 

        // Afficher les options du menu
        for (int i = 0; i < menuOptions.length; i++) {
            String option = menuOptions[i];
            float textWidth = game.font.draw(game.batch, option, 0, 0).width; // Largeur du texte
            float textHeight = game.font.draw(game.batch, option, 0, 0).height; // Hauteur du texte
            float x = (screenWidth - textWidth) / 2;
            float y = (screenHeight + 5 * textHeight) / 2 - i * 100; // Espacement entre les options
            // float y = screenHeight / 2 + ((menuOptions.length/2) - i) * 100; // Modifié
            // le calcul de y

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
            selectedIndex = (selectedIndex - 1 + menuOptions.length) % menuOptions.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % menuOptions.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            handleMenuSelection();
        }
    }

    private void handleMenuSelection() {
        switch (selectedIndex) {
            case 0:
                game.stopMenuMusic();
                game.setScreen(new GameScreen(game, 0.006, new Inventaire()));
                dispose();
                break;
            case 1:
                game.setScreen(new OptionScreen(game));
                dispose();
                break;
            case 2:
                // S'assurer que toutes les ressources sont libérées avant de quitter
                dispose();
                game.dispose();
                Gdx.app.exit();
                break;
        }
    }

    private boolean isMouseOver(Rectangle bounds) {
        // Convertir les coordonnées de la souris en coordonnées du monde
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY(); // Inverser Y car LibGDX utilise un repère bas-gauche

        // Convertir en coordonnées du viewport
        Vector3 worldCoords = game.viewport.getCamera().unproject(new Vector3(mouseX, mouseY, 0));

        return bounds.contains(worldCoords.x, worldCoords.y);
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("menubackground.png"));
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }

    }
}
