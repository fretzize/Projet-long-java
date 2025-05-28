package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import projet.java.Main;
import projet.java.Menu.MenuScreen;

public class VictoireScreen implements Screen {

    final Main game;

    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont font2;
    private GlyphLayout layout1;
    private GlyphLayout layout2;
    private Rectangle optionBounds;
    private OrthographicCamera camera;
    private FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    private FreeTypeFontParameter parameter2 = new FreeTypeFontParameter();
    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Korcy.ttf"));
    private final String[] menuOptions = {"Menu principal"};

    public VictoireScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Korcy.ttf"));
        
        parameter.size = 90;
        parameter.color = new Color(1f, 1f, 0f, 1f);
        font = generator.generateFont(parameter);

        parameter2.size = 40;
        parameter2.color = new Color(0.6f, 0.6f, 0.6f, 1f);
        font2 = generator.generateFont(parameter2);

        generator.dispose(); 

        layout1 = new GlyphLayout(font, "You WIN !!!!!!!");
        layout2 = new GlyphLayout(font2, menuOptions[0]);

        float textX = (Gdx.graphics.getWidth() - layout2.width) / 2;
        float textY = layout2.height * 2;

        optionBounds = new Rectangle(textX, textY - layout2.height, layout2.width, layout2.height);
        game.playGameOverSound();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0.84f, 0.6f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Dessiner "GAME OVER"
        float titleX = (Gdx.graphics.getWidth() - layout1.width) / 2;
        float titleY = (Gdx.graphics.getHeight() + layout1.height) / 2;
        font.draw(batch, layout1, titleX, titleY);

        if (isMouseOver(optionBounds)) {
            parameter2.size = 40;
            parameter2.color = new Color(1f, 1f, 0f, 1f);
            font2 = generator.generateFont(parameter2);
            if (Gdx.input.justTouched()) {
                game.disposeGameOverSound();
                game.startMenuMusic();
                game.setScreen(new MenuScreen(game));
            }
        } else {
            parameter2.size = 40;
            parameter2.color = new Color(0.6f, 0f, 0f, 1f);
            font2 = generator.generateFont(parameter2);
        }
        font2.draw(batch, layout2, optionBounds.x, optionBounds.y + layout2.height);

        batch.end();
    }

    private boolean isMouseOver(Rectangle bounds) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        Vector3 worldCoords = camera.unproject(new Vector3(mouseX, mouseY, 0));
        return bounds.contains(worldCoords.x, worldCoords.y);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        font2.dispose();
    }
}