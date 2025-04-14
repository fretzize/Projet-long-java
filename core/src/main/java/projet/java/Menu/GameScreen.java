package projet.java.Menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import projet.java.Main;


public class GameScreen implements Screen {
    final Main game;
    private Texture mapTexture;
    private Texture playerTexture;
    private float playerX = 0;
    private float playerY = 0;
    private float playerSpeed = 400; // Vitesse en pixels par seconde
    private float mapSize = 4000; // Taille de la map carrée
    private OrthographicCamera camera;

    public GameScreen(final Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
    }

    @Override
    public void show() {
        mapTexture = new Texture(Gdx.files.internal("map.png")); // Créez une image "map.png"
        playerTexture = new Texture(Gdx.files.internal("player.png")); // Créez une image "player.png"
    }

    @Override
    public void render(float delta) {
        input(delta);
        logic();
        draw();
    }

    private void input(float delta) {
        // Déplacement du joueur
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX -= playerSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += playerSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerY += playerSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerY -= playerSpeed * delta;
        }

        // Limiter le joueur à la map
        playerX = MathUtils.clamp(playerX, 0, mapSize - playerTexture.getWidth());
        playerY = MathUtils.clamp(playerY, 0, mapSize - playerTexture.getHeight());

        // Retour au menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.startMenuMusic();
            game.setScreen(new Menu(game));
            dispose();
        }
    }

    private void logic() {
        // Mise à jour de la caméra pour suivre le joueur
        camera.position.x = playerX + playerTexture.getWidth()/2;
        camera.position.y = playerY + playerTexture.getHeight()/2;
        
        // Limiter la caméra aux bords de la map
        float cameraHalfWidth = camera.viewportWidth/2;
        float cameraHalfHeight = camera.viewportHeight/2;
        
        camera.position.x = MathUtils.clamp(camera.position.x, 
                                          cameraHalfWidth, 
                                          mapSize - cameraHalfWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, 
                                          cameraHalfHeight, 
                                          mapSize - cameraHalfHeight);
        
        camera.update();
    }

    private void draw() {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.setProjectionMatrix(camera.combined);
        
        game.batch.begin();
        // Dessiner la map
        game.batch.draw(mapTexture, 0, 0, mapSize, mapSize);
        // Dessiner le joueur
        game.batch.draw(playerTexture, playerX, playerY);
        game.batch.end();
    }

    @Override
    public void dispose() {
        mapTexture.dispose();
        playerTexture.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 1920;
        camera.viewportHeight = 1080;
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
