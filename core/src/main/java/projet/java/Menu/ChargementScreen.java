package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import projet.java.Main;
import projet.java.Menu.GameScreen;
import projet.java.Inventaire.DatabaseItem;
import projet.java.Inventaire.Inventaire;
import projet.java.Menu.InventaireScreen;
import projet.java.Inventaire.Item;
import projet.java.Inventaire.Inventaire;
import projet.java.Inventaire.Coffre;
import projet.java.Inventaire.Item.ItemType;


import projet.java.Main;
import projet.java.Inventaire.Inventaire;

public class ChargementScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private Animation<TextureRegion> portalAnimation;
    private float stateTime = 0f;

    private OrthographicCamera camera;
    private FitViewport viewport;

    private BitmapFont font;

    private String loadingText = "Loading";
    private float dotTimer = 0f;
    private int dotCount = 0;
    private float temps;
    private float temps_chargement = 2;
    private double pourcentage_mob;
    private Inventaire inventaire;
    private int nombre_sbire;

    public ChargementScreen(Main game, double pourcentage_mob, Inventaire inventaire, int nombre_sbire) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.pourcentage_mob = pourcentage_mob;
        this.inventaire = inventaire;
        this.nombre_sbire = nombre_sbire;

        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera); 
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        font = new BitmapFont(); 

        loadPortalAnimation();
    }

    private void loadPortalAnimation() {
        TextureRegion[] frames = new TextureRegion[36];
        for (int i = 0; i < 36; i++) {
            String path = "portail/portail_" + (i + 1) + ".png"; 
            Texture texture = new Texture(Gdx.files.internal(path));
            frames[i] = new TextureRegion(texture);
        }

        portalAnimation = new Animation<>(0.1f, frames); 
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        dotTimer += delta;

        if (dotTimer >= 0.5f) {
            dotCount = (dotCount + 1) % 4; 
            dotTimer = 0f;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1); // Fond noir
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Dessiner l'animation du portail au centre
        TextureRegion currentFrame = portalAnimation.getKeyFrame(stateTime, true);
        float scale = 2f;
        float width = currentFrame.getRegionWidth() * scale;
        float height = currentFrame.getRegionHeight() * scale;

        float portalX = (viewport.getWorldWidth() - width) / 2f;
        float portalY = (viewport.getWorldHeight() - height) / 2f + 50;

        batch.draw(currentFrame, portalX, portalY, width, height);

        // Dessiner "Loading..."
        String dots = ".".repeat(dotCount);
        font.draw(batch, loadingText + dots, viewport.getWorldWidth() / 2f, portalY - 70, 0, Align.center, false);

        batch.end();

        if (temps_chargement < temps) {
            game.setScreen(new GameScreen(game, pourcentage_mob, inventaire, nombre_sbire));
            temps = 0;
        } else {
            temps += delta;
        }
        
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        for (TextureRegion frame : portalAnimation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
    }
}
