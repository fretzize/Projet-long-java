package projet.java.Menu;


import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import entite.Entite;
import entite.Personnage;
import projet.java.Main;


public class GameScreen implements Screen {

    final Main game;

    float largeur_ecran = game.viewport.getWorldWidth();
    float hauteur_ecran = game.viewport.getWorldHeight();
    

    

    // Personnage exemple
    Entite personnage1 = new Personnage(4, 2, "mathis");

    public GameScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void create() {
        personnage1.create();
    }


    @Override
    public void show() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    private void input() {
        // Handle input here
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isTouched()) {
            game.setScreen(new Menu(game));
            dispose();
        }
        personnage1.input();
    }


    private void logic() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logic'");
    }

    private void draw() {
        // TODO Auto-generated method stub
        // game.batch.begin();
        personnage1.draw(this.game);

        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hide'");
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        personnage1.dispose();
        throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }


    
}
