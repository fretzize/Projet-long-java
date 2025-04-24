package projet.java.Menu;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import projet.java.entite.Entite;
import projet.java.entite.Personnage;
import projet.java.Main;


public class GameScreen implements Screen {

    final Main game;

    // float largeur_ecran = game.viewport.getWorldWidth();
    // float hauteur_ecran = game.viewport.getWorldHeight();
    
    Texture skin = new Texture("image_heracles_normal.png");
    

    // Personnage exemple
    Entite personnage1 = new Personnage(4, 2, 3, "mathis", skin);

    public GameScreen(final Main game) {
        this.game = game;
        personnage1.create_entite();

    }

    // @Override
    // public void create() {
    //     personnage1.create_entite();
    // }


    @Override
    public void show() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'show'");
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
            game.setScreen(new Menu(game));
            dispose();
        }
        personnage1.input_entite();
    }


    private void logic() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logic'");
    }

    private void draw() {
        // TODO Auto-generated method stub
        // game.batch.begin();
        personnage1.draw_entite(this.game);

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
        personnage1.dispose_entite(game);
        throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }


    
}
