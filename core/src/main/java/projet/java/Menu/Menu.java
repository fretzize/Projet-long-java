package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import projet.java.Main;

public class Menu implements Screen {
    
    final Main game;
    private Texture backgroundTexture;
    private int selectedIndex = 0; // Index de l'option sélectionnée
    private final String[] menuOptions = {"Commencer la partie", "Options", "Quitter"};

    public Menu(final Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin(); // Assurez-vous que le batch commence avant tout dessin

        // Calculer les dimensions de l'écran
        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        game.batch.draw(backgroundTexture, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());

        // Ajuster la taille de la police pour agrandir les options du menu
        game.font.getData().setScale(1.5f); // Augmentez l'échelle selon vos besoins

        // Afficher les options du menu
        for (int i = 0; i < menuOptions.length; i++) {
            String option = menuOptions[i];
            //float textWidth = game.font.getRegion().getRegionWidth();
            float textWidth = game.font.draw(game.batch,option,0,0).width; // Largeur du texte
            float textHeight = game.font.draw(game.batch,option,0,0).height; // Hauteur du texte
            float x = (screenWidth - textWidth) / 2;
            float y = (screenHeight + 5*textHeight) / 2 - i * 100; // Espacement entre les options

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
                game.setScreen(new GameScreen(game));
                dispose();
                break;
            case 1:
                // Implémenter l'écran des options
                // game.setScreen(new OptionsScreen(game));
                // dispose();
                // Pour l'instant, on va juste afficher un message dans la console
                System.out.println("Options sélectionnées !");
                
                break;
            case 2:
                Gdx.app.exit();
                break;
        }
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
