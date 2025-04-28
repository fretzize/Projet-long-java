package projet.java.Menu;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import projet.java.entite.Entite;
import projet.java.entite.Personnage;
import projet.java.Main;

import java.util.TimerTask;
import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.MathUtils;


public class GameScreen implements Screen {
    final Main game;
    private Texture mapTexture;
    private Texture skin;
    private float playerX = 0;
    private float playerY = 0;
    private float playerSpeed = 1000; // Vitesse en pixels par seconde
    private float speed = 5000;
    private float mapSize = 2000; // Taille de la map carrée
    private OrthographicCamera camera;
    private Entite personnage1; // = new Personnage(4, 2, 3, "mathis", skin);
    private Texture dash_texture;
    private TextureRegion dash;
    private Texture dash_gris;
    private float largeur_dash;
    private float hauteur_dash;
    private Texture coeur_plein;
    private Texture bouclierIntact;


    // largeur et longueur

    float largeur_coeur;
    float hauteur_coeur;
    float largeur_bouclier;
    float hauteur_bouclier;

    float cameraHalfWidth;
    float cameraHalfHeight;


    // etat bouclier et dash personnage
    private boolean etatbouclier = false;
    private boolean dashOk = true;


    // cooldown fash et combien de temps se remet le bouclier
    int decompte = 3;
    int decompte_bouclier = 6;

    //timer pour savoir tous les combiens de temps il peut utiliser son dash, cooldown
    
    Timer timer;
    Timer timer2;
    public GameScreen(final Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
    }

    @Override
    public void show() {
        mapTexture = new Texture(Gdx.files.internal("map.png")); // Créez une image "map.png"
        skin = new Texture(Gdx.files.internal("image_heracles_normal.png")); // Créez une image "player.png"
        personnage1 = new Personnage(4, 3, 4, "mathis", skin);
        personnage1.create_entite();

        // sprint ou dash   //mettre un boutton dash pour montrer quand il a de nouveau acces au dash, par exemple dans un coin le symbole de dash gris si il n'y a pas acces et en couleur sinon

        dash_texture = new Texture("dash.png");
        dash = new TextureRegion(dash_texture);
        dash_gris = new Texture("dash_gris.png");
        largeur_dash = dash_texture.getWidth();
        hauteur_dash = dash_texture.getHeight();

        bouclierIntact = new Texture("bouclier.png"); 
        largeur_bouclier = bouclierIntact.getWidth();
        hauteur_bouclier = bouclierIntact.getHeight();

        coeur_plein = new Texture("coeur_plein.png");
        largeur_coeur = coeur_plein.getWidth();
        hauteur_coeur = coeur_plein.getHeight();
    }

    @Override
    public void render(float delta) {
        input(delta);
        logic();
        draw();
    }

    private void input(float avance) {
        // Déplacement du joueur
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // System.out.println("La touche Z est pressée !, le personnage avance");
            // this.getPosition().add(this.getPositionX(), this.getPositionY() + 0.5f );
            playerY += playerSpeed * avance;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // System.out.println("La touche Q est pressée !, le personnage va vers la gauche");
            // this.getPosition().add(this.getPositionX() -0.5f, this.getPositionY());
            playerX -= playerSpeed * avance;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // System.out.println("La touche D est pressée !, le personnage va vers la droite");
            // this.getPosition().add(this.getPositionX() +0.5f, this.getPositionY() );
            playerX += playerSpeed * avance;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // System.out.println("La touche S est pressée !, le personnage va vers le bas");
            // this.getPosition().add(this.getPositionX(), this.getPositionY() - 0.5f );
            playerY -= playerSpeed * avance;
        }
        
        
        // afficher le dash selon la direction, on fera une image du dash du haut vers le bas
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // if (dashOk) {
                if (Gdx.input.isKeyPressed(Input.Keys.W)  || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    playerY += speed * avance;
                } else if (Gdx.input.isKeyPressed(Input.Keys.A)  || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    playerX -= speed * avance;
                } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    playerY -= speed * avance;
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)  || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    playerX += speed * avance;
                }
                // decompte = 3;
                // dashOk = false;
                // decompter();
            // }
        }

        // Limiter le joueur à la map
        playerX = MathUtils.clamp(playerX, 0, mapSize - skin.getWidth());
        playerY = MathUtils.clamp(playerY, 0, mapSize - skin.getHeight());

        // Retour au menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.startMenuMusic();
            game.setScreen(new Menu(game));
            dispose();
        }
    }

    private void logic() {
        // Mise à jour de la caméra pour suivre le joueur
        camera.position.x = playerX;// + skin.getWidth()/2;
        camera.position.y = playerY;// + skin.getHeight()/2;
        
        // Limiter la caméra aux bords de la map
        cameraHalfWidth = camera.viewportWidth/2;
        cameraHalfHeight = camera.viewportHeight/2;
        
        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, mapSize - cameraHalfWidth); 
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, mapSize - cameraHalfHeight);
        
        camera.update();
    }

    private void draw() {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.setProjectionMatrix(camera.combined);
        
        game.batch.begin();
        // Dessiner la map
        game.batch.draw(mapTexture, 0, 0, mapSize, mapSize);
        // Dessiner le joueur
        game.batch.draw(skin, playerX, playerY);
        personnage1.draw_entite(game);

        // afficher le dash selon la direction, on fera une image du dash du haut vers le bas
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // if (dashOk) {
                if ((Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) || (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 45);
                } else if ((Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A))  || (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -45);
                } else if ((Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D))  || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 135);
                } else if ((Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A))  || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -135);
                } else if ((Gdx.input.isKeyPressed(Input.Keys.W))  || Gdx.input.isKeyPressed(Input.Keys.UP)){
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 90);
                } else if ((Gdx.input.isKeyPressed(Input.Keys.A))   || Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 0);
                } else if ((Gdx.input.isKeyPressed(Input.Keys.S))   || Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -90);
                } else if ((Gdx.input.isKeyPressed(Input.Keys.D))   || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 180);
                }
                // decompte = 3;
                // dashOk = false;
                // decompter();
        }


        for (int i = 0; i < personnage1.getVie(); i++) {
            game.batch.draw(coeur_plein, camera.position.x - cameraHalfWidth + i*largeur_coeur +10, camera.position.y + cameraHalfHeight - hauteur_coeur);
        }
        for (int i = 0; i < personnage1.getBouclier(); i++) {
            game.batch.draw(bouclierIntact, camera.position.x - cameraHalfWidth + i*largeur_bouclier +10, camera.position.y + cameraHalfHeight - hauteur_bouclier - 1 - hauteur_coeur);
        }


        if (dashOk) {
            game.batch.draw(dash, camera.position.x - cameraHalfWidth + largeur_dash, camera.position.y - cameraHalfHeight + hauteur_dash);
        } else {
            game.batch.draw(dash_gris, camera.position.x - cameraHalfWidth + largeur_dash, camera.position.y - cameraHalfHeight + hauteur_dash);
        }

        game.batch.end();
    }

    @Override
    public void dispose() {
        mapTexture.dispose();
        skin.dispose();
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

    public void decompter() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        decompte--;
                        if (decompte < 0) {
                            timer.cancel();
                            dashOk = true;
                        }
                    }
                });
            }
        }, 0, 1000); // 1000 ms = 1s
    }
}




// public class GameScreen implements Screen {

//     final Main game;
    
//     // float largeur_ecran = game.viewport.getWorldWidth();
//     // float hauteur_ecran = game.viewport.getWorldHeight();
    
//     private Texture skin;
//     private Texture mapTexture;
//     private float mapSize = 2000;
//     private OrthographicCamera camera;
//     // Personnage exemple
//     Entite personnage1 = new Personnage(4, 2, 3, "mathis", skin);

//     public GameScreen(final Main game) {
//         this.game = game;
//         personnage1.create_entite();

//     }

//     // @Override
//     // public void create() {
//     //     personnage1.create_entite();
//     // }


//     @Override
//     public void show() {
//         mapTexture = new Texture(Gdx.files.internal("map.png"));
//         skin = new Texture(Gdx.files.internal("image_heracles_normal.png"));
//         // TODO Auto-generated method stub
//         // throw new UnsupportedOperationException("Unimplemented method 'show'");
//     }

//     @Override
//     public void render(float delta) {
//         input();
//         logic();
//         draw();
//     }

//     private void input() {
//         // Handle input here
//         if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
//             game.setScreen(new Menu(game));
//             dispose();
//         }
//         personnage1.input_entite();
//     }


//     private void logic() {
//         // TODO Auto-generated method stub
//         // Mise à jour de la caméra pour suivre le joueur
//         camera.position.x = personnage1.getPosition().x + skin.getWidth()/2;
//         camera.position.y = personnage1.getPosition().y + skin.getHeight()/2;
        
//         // Limiter la caméra aux bords de la map
//         float cameraHalfWidth = camera.viewportWidth/2;
//         float cameraHalfHeight = camera.viewportHeight/2;
        
//         // camera.position.x = MathUtils.clamp(camera.position.x, 
//         //                                   cameraHalfWidth, 
//         //                                   mapSize - cameraHalfWidth);
//         // camera.position.y = MathUtils.clamp(camera.position.y, 
//         //                                   cameraHalfHeight, 
//         //                                   mapSize - cameraHalfHeight);
        
//         camera.update();
//         // throw new UnsupportedOperationException("Unimplemented method 'logic'");
//     }

//     private void draw() {
//         // TODO Auto-generated method stub
//         ScreenUtils.clear(0, 0, 0, 1);
//         game.batch.setProjectionMatrix(camera.combined);
//         game.batch.begin();
//         game.batch.draw(mapTexture, 0, 0, mapSize, mapSize);
//         personnage1.draw_entite(this.game);
//         game.batch.end();
//         // throw new UnsupportedOperationException("Unimplemented method 'draw'");
//     }

//     @Override
//     public void resize(int width, int height) {
//         game.viewport.update(width, height, true);
//     }

//     @Override
//     public void pause() {
//         // TODO Auto-generated method stub
//         // throw new UnsupportedOperationException("Unimplemented method 'pause'");
//     }

//     @Override
//     public void resume() {
//         // TODO Auto-generated method stub
//         // throw new UnsupportedOperationException("Unimplemented method 'resume'");
//     }

//     @Override
//     public void hide() {
//         // TODO Auto-generated method stub
//         // throw new UnsupportedOperationException("Unimplemented method 'hide'");
//     }

//     @Override
//     public void dispose() {
//         // TODO Auto-generated method stub
//         skin.dispose();
//         mapTexture.dispose();
//         personnage1.dispose_entite(game);
//         // throw new UnsupportedOperationException("Unimplemented method 'dispose'");
//     }


    
// }
