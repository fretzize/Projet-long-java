package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import projet.java.Main;

public class TouchesScreen implements Screen {

    final Main game;
    private OptionScreen optionScreen;
    private PauseScreen pauseScreen;
    private GameScreen gameScreen;
    private boolean fromPause;

    private Texture backgroundTexture;
    private int selectedIndex = 0;
    private final String[] keyOptions = {"Haut", "Bas", "Gauche", "Droite", "Dash", "Retour"};
    private Rectangle[] optionBounds;
    private int[] keyBindings;
    private boolean waitingForInput = false;


    public TouchesScreen(final Main game, OptionScreen optionScreen) {
        this.game = game;
        this.optionScreen = optionScreen;
        this.fromPause = false;

        optionBounds = new Rectangle[keyOptions.length];
        for (int i = 0; i < keyOptions.length; i++) {
            optionBounds[i] = new Rectangle();
        }
        // Initialisation avec les touches actuelles de Main
        keyBindings = new int[]{
            game.toucheHaut,
            game.toucheBas,
            game.toucheGauche,
            game.toucheDroite,
            game.toucheDash
        };
    }

    public TouchesScreen(final Main game, OptionScreen optionScreen, PauseScreen pauseScreen, GameScreen gameScreen) {
        this.game = game;
        this.optionScreen = optionScreen;
        this.pauseScreen = pauseScreen;
        this.gameScreen = gameScreen;
        this.fromPause = true;

        optionBounds = new Rectangle[keyOptions.length];
        for (int i = 0; i < keyOptions.length; i++) {
            optionBounds[i] = new Rectangle();
        }
        // Initialisation avec les touches actuelles de Main
        keyBindings = new int[]{
            game.toucheHaut,
            game.toucheBas,
            game.toucheGauche,
            game.toucheDroite,
            game.toucheDash
        };
    }
    private String getKeyName(int keycode) {
        if (keycode < 0) {
            return "Non défini";
        }
        return Input.Keys.toString(keycode);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());

        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();

        // Titre
        game.font.getData().setScale(2.0f);
        game.font.setColor(Color.GOLD);
        String title = "Changer les touches";
        float titleWidth = game.font.draw(game.batch, title, 0, 0).width;
        game.font.draw(game.batch, title, (screenWidth - titleWidth) / 2, screenHeight - 100);

        // Note QWERTY
        game.font.getData().setScale(1.0f);
        game.font.setColor(Color.GRAY);
        String noteQwerty = "Note : Les touches sont affichées en QWERTY";
        float noteWidth = game.font.draw(game.batch, noteQwerty, 0, 0).width;
        game.font.draw(game.batch, noteQwerty, (screenWidth - noteWidth) / 2, screenHeight - 200);

        // Retour à l'échelle normale pour les options
        game.font.getData().setScale(1.5f);
        game.font.setColor(Color.WHITE);

        for (int i = 0; i < keyOptions.length; i++) {
            String option = keyOptions[i];
            if (i < keyBindings.length) {
                option += ": " + getKeyName(keyBindings[i]);
            }
            
            float textWidth = game.font.draw(game.batch, option, 0, 0).width;
            float textHeight = game.font.draw(game.batch, option, 0, 0).height;
            float x = (screenWidth - textWidth) / 2;
            float y = (screenHeight + 5 * textHeight) / 2 - i * 100;

            optionBounds[i].set(x, y - textHeight, textWidth, textHeight);

            if (isMouseOver(optionBounds[i])) {
                selectedIndex = i;
                if (Gdx.input.justTouched() && !waitingForInput) {
                    handleSelection();
                }
            }

            if (i == selectedIndex) {
                game.font.setColor(Color.YELLOW);
            } else {
                game.font.setColor(Color.WHITE);
            }

            if (waitingForInput && i == selectedIndex) {
                option = keyOptions[i] + ": Appuyez sur une touche...";
            }

            game.font.draw(game.batch, option, x, y);
        }

        game.batch.end();

        if (waitingForInput) {
            for (int key = 0; key <= Input.Keys.MAX_KEYCODE; key++) {
                if (Gdx.input.isKeyJustPressed(key)) {
                    keyBindings[selectedIndex] = key;
                    waitingForInput = false;
                    break;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !waitingForInput) {
            for (int i = 0; i < keyBindings.length; i++) {
                game.setTouche(i, keyBindings[i]);
            }
            if (fromPause) {
                game.setScreen(new OptionScreen(game, pauseScreen, gameScreen));
            } else {
                game.setScreen(new OptionScreen(game));
            }
            dispose();
        }
    }

    private void handleSelection() {
        if (selectedIndex < keyBindings.length) {
            waitingForInput = true;
        } else if (selectedIndex == keyOptions.length - 1) {
            // Sauvegarder les touches avant de quitter
            for (int i = 0; i < keyBindings.length; i++) {
                game.setTouche(i, keyBindings[i]);
            }
            if (fromPause) {
            game.setScreen(new OptionScreen(game, pauseScreen, gameScreen));
        } else {
            game.setScreen(new OptionScreen(game));
        }
            dispose();
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