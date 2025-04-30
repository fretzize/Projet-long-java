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
import com.badlogic.gdx.utils.TimeUtils;



public class GameScreen implements Screen {
    final Main game;
    private Texture mapTexture;
    private Texture skin;
    private float playerX = 50;
    private float playerY = 50;
    private float playerSpeed = 500; // Vitesse en pixels par seconde
    private float speed = 10000;
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

    // état du personnage

    private Texture Hercule_bas;
    private Texture Hercule_haut;
    private Texture Hercule_gauche;
    private Texture Hercule_droite;


    // texture pour le cooldown

    private Texture barre_vide;
    private Texture barre_pleine;

    // largeur et longueur

    float largeur_coeur;
    float hauteur_coeur;
    float largeur_bouclier;
    float hauteur_bouclier;
    float largeur_skin;
    float hauteur_skin;

    float cameraHalfWidth;
    float cameraHalfHeight;


    // etat bouclier et dash personnage
    private boolean etatbouclier = false;
    private boolean dashOk = true;
    // je rajoute un dash_texture car le dashOk se met trop vite à false donc il affiche pas le dash
    private boolean dash_afficher = true;


    // cooldown fash et combien de temps se remet le bouclier
    int decompte = 3;
    int decompte_bouclier = 6;

    //timer pour savoir tous les combiens de temps il peut utiliser son dash, cooldown
    
    Timer timer;
    Timer timer2;

    //touche

    int touche_haut;
    int touche_bas;
    int touche_gauche;
    int touche_droite;
    int touche_dash;

    // private long startTime;
    // private final long temps_recharge = 3000;
    private float tempsDash = 3;
    private float dashCooldown = 2f;

    public GameScreen(final Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
    }

    @Override
    public void show() {
        mapTexture = new Texture(Gdx.files.internal("map.png")); // Créez une image "map.png"
        skin = new Texture(Gdx.files.internal("image_heracles_normal.png")); // Créez une image "player.png"
        largeur_skin = skin.getWidth();
        hauteur_skin = skin.getHeight();

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

        Hercule_haut = new Texture("Hercule_haut.png");
        Hercule_bas = new Texture("Hercule_bas.png");
        Hercule_gauche = new Texture("Hercule_gauche.png");
        Hercule_droite = new Texture("Hercule_droite.png");

        barre_pleine = new Texture("barres_pleine.png");
        barre_vide = new Texture("barres_vide.png");

        touche_haut = personnage1.getHaut(); // 51= W
        touche_bas = personnage1.getBas();
        touche_gauche = personnage1.getGauche();
        touche_droite = personnage1.getDroite();
        touche_dash = personnage1.getDash();
    }

    
    

    @Override
    public void render(float delta) {
        input(delta);
        // personnage1.update(delta, dashOk);
        logic();
        draw();
        if (!dashOk) {
            tempsDash += delta;
        }
        if (tempsDash > dashCooldown) {
            dashOk = true;
        }
    }

    private void input(float avance) {
        // Déplacement du joueur
        if (Gdx.input.isKeyPressed(touche_haut) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // System.out.println("La touche Z est pressée !, le personnage avance");
            // this.getPosition().add(this.getPositionX(), this.getPositionY() + 0.5f );
            playerY += playerSpeed * avance;
        }

        if (Gdx.input.isKeyPressed(touche_gauche) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // System.out.println("La touche Q est pressée !, le personnage va vers la gauche");
            // this.getPosition().add(this.getPositionX() -0.5f, this.getPositionY());
            playerX -= playerSpeed * avance;
        }

        if (Gdx.input.isKeyPressed(touche_droite) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // System.out.println("La touche D est pressée !, le personnage va vers la droite");
            // this.getPosition().add(this.getPositionX() +0.5f, this.getPositionY() );
            playerX += playerSpeed * avance;
        }

        if (Gdx.input.isKeyPressed(touche_bas) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // System.out.println("La touche S est pressée !, le personnage va vers le bas");
            // this.getPosition().add(this.getPositionX(), this.getPositionY() - 0.5f );
            playerY -= playerSpeed * avance;
        }
        
        // mettre le dash

        if (Gdx.input.isKeyPressed(touche_dash)) {
            if (dashOk) {
                if (Gdx.input.isKeyPressed(touche_haut)  || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    playerY += speed * avance;
                } else if (Gdx.input.isKeyPressed(touche_gauche)  || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    playerX -= speed * avance;
                } else if (Gdx.input.isKeyPressed(touche_bas) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    playerY -= speed * avance;
                } else if (Gdx.input.isKeyPressed(touche_droite)  || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    playerX += speed * avance;
                }
                // decompte = 3;
                dashOk = false;
                tempsDash = 0;
                dash_afficher = true;
                // decompter();
            }
        }

        // Limiter le joueur à la map
        playerX = MathUtils.clamp(playerX, 0, mapSize - skin.getWidth());
        playerY = MathUtils.clamp(playerY, 0, mapSize - skin.getHeight());

        // Retour au menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.startMenuMusic();
            game.setScreen(new MenuScreen(game));
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

        if (Gdx.input.isKeyPressed(touche_gauche) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            game.batch.draw(Hercule_gauche, playerX, playerY, largeur_skin, hauteur_skin);
        } else if (Gdx.input.isKeyPressed(touche_droite) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            game.batch.draw(Hercule_droite, playerX, playerY, largeur_skin, hauteur_skin);
        } else if (Gdx.input.isKeyPressed(touche_haut) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            game.batch.draw(Hercule_haut, playerX, playerY, largeur_skin, hauteur_skin);
        } else {
            game.batch.draw(Hercule_bas, playerX, playerY, largeur_skin, hauteur_skin);
        }

        // afficher le dash selon la direction, on fera une image du dash du haut vers le bas
        if (Gdx.input.isKeyPressed(touche_dash)) {
            if (dash_afficher) {
                if ((Gdx.input.isKeyPressed(touche_haut) && Gdx.input.isKeyPressed(touche_droite)) || (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 45);
                } else if ((Gdx.input.isKeyPressed(touche_haut) && Gdx.input.isKeyPressed(touche_gauche))  || (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -45);
                } else if ((Gdx.input.isKeyPressed(touche_bas) && Gdx.input.isKeyPressed(touche_droite))  || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 135);
                } else if ((Gdx.input.isKeyPressed(touche_bas) && Gdx.input.isKeyPressed(touche_gauche))  || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -135);
                } else if ((Gdx.input.isKeyPressed(touche_haut))  || Gdx.input.isKeyPressed(Input.Keys.UP)){
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 90);
                } else if ((Gdx.input.isKeyPressed(touche_gauche))   || Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 0);
                } else if ((Gdx.input.isKeyPressed(touche_bas))   || Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -90);
                } else if ((Gdx.input.isKeyPressed(touche_droite))   || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 180);
                }
                // startTime = TimeUtils.millis();
                // decompte = 3;
                // dashOk = false;
                // decompter();
                dash_afficher = false;
            }
        }

        //cooldown

        float progress = tempsDash / dashCooldown;
        float barWidth = 30; 
        float barHeight = 5; 
        float x = playerX - 3;
        float y = playerY - 8;

        game.batch.draw(barre_vide, x, y, barWidth, barHeight);
        game.batch.draw(barre_pleine, x, y, barWidth*progress, barHeight);

        for (int i = 0; i < personnage1.getVie(); i++) {
            game.batch.draw(coeur_plein, camera.position.x - cameraHalfWidth + i*hauteur_skin +10, camera.position.y + cameraHalfHeight - hauteur_skin, hauteur_skin, hauteur_skin);
        }
        for (int i = 0; i < personnage1.getBouclier(); i++) {
            game.batch.draw(bouclierIntact, camera.position.x - cameraHalfWidth + i*hauteur_skin +10, camera.position.y + cameraHalfHeight - hauteur_skin - 1 - hauteur_skin, hauteur_skin, hauteur_skin);
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

    // public void update() {
    //     if (TimeUtils.timeSinceMillis(startTime) >= temps_recharge) {
    //         // System.out.println("Tâche exécutée après " + DELAY + " millisecondes !");
    //         dashOk = true;
    //         startTime = TimeUtils.millis(); // Réinitialiser le timer
    //     }
    // }

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
