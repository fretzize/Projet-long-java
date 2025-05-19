package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import projet.java.entite.*;
import projet.java.Main;

import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Timer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import projet.java.animation.AnimationHandler;

public class GameScreen implements Screen {
    final Main game;

    private Texture mapTexture;
    private Texture skin;
    private float playerSpeed = 500; // Vitesse normale en pixels par seconde
    private float speed = 2500; // Vitesse du dash réduite (était 10000)
    private float dashDuration = 0.08f; // Durée du dash en secondes pour maintenir la même distance
    private float currentDashTime = 0f; // Pour suivre la durée du dash en cours
    private boolean isDashing = false; // Pour savoir si on est en train de dasher
    private float mapSize = 2000; // Taille de la map carrée
    private OrthographicCamera camera;
    private Personnage personnage1; // = new Personnage(4, 2, 3, "mathis", skin);
    private Texture dash_texture;
    private TextureRegion dash;
    private Texture dash_gris;
    private float largeur_dash;
    private float hauteur_dash;
    private Texture coeur_plein;
    private Texture bouclierIntact;

    private AnimationHandler animationHandler;

    // Variables pour le suivi de la dernière direction
    private boolean wasMovingUp = false;
    private boolean wasMovingDown = false;
    private boolean wasMovingLeft = false;
    private boolean wasMovingRight = false;

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

    private float scalePlayer = 8.0f; // Facteur d'échelle pour le personnage
    private float scaleSbire = 8.0f;

    // etat bouclier et dash personnage
    private boolean etatbouclier = false;
    private boolean dashOk = true;
    // je rajoute un dash_texture car le dashOk se met trop vite à false donc il
    // affiche pas le dash
    private boolean dash_afficher = true;

    // cooldown fash et combien de temps se remet le bouclier
    int decompte = 3;
    int decompte_bouclier = 6;

    // timer pour savoir tous les combiens de temps il peut utiliser son dash,
    // cooldown

    Timer timer;
    Timer timer2;

    // private long startTime;
    // private final long temps_recharge = 3000;
    private float tempsDash = 3;
    private float dashCooldown = 2f;

    public GameScreen(final Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
    }

    //TEST SBIRE
    private Sbire sbireTest;
    private ArrayList<Projectile> projectiles;

    @Override
    public void show() {
        mapTexture = new Texture(Gdx.files.internal("map.png")); // Créez une image "map.png"
        skin = new Texture("HERCULEpng/HERCULEpng/Sword_Idle_front.png"); // Créez une image "player.png"
        largeur_skin = skin.getWidth();
        hauteur_skin = skin.getHeight();

        personnage1 = new Personnage(4, 4, 4, "mathis", skin, new Rectangle(0, 0, 2, 4));
        personnage1.create_entite();

        // TEST SBIRE
        sbireTest = new Sbire(3,3,3,10, 10,100,2,new Rectangle(0,0, 2,4), 10,2, 1,1, personnage1, new ComportementMelee(),new Texture(Gdx.files.internal("coeur_plein.png")),new Texture("Hercule_haut.png"));
        //Gestion projectiles du sbire
        projectiles = new ArrayList<>();
        //

        // sprint ou dash //mettre un boutton dash pour montrer quand il a de nouveau
        // acces au dash, par exemple dans un coin le symbole de dash gris si il n'y a
        // pas acces et en couleur sinon

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

        animationHandler = new AnimationHandler();

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
    
        if (timer2 != null) {
            timer2.cancel();
        }
        timer2 = new Timer();

    }

    @Override
    public void render(float delta) {
        input(delta);
        boolean isMovingUp = Gdx.input.isKeyPressed(game.toucheHaut) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean isMovingDown = Gdx.input.isKeyPressed(game.toucheBas) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean isMovingLeft = Gdx.input.isKeyPressed(game.toucheGauche) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean isMovingRight = Gdx.input.isKeyPressed(game.toucheDroite) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        
        animationHandler.update(delta, isMovingUp, isMovingDown, isMovingLeft, isMovingRight);
        
        // Mémoriser l'état de mouvement pour le dash
        wasMovingUp = isMovingUp;
        wasMovingDown = isMovingDown;
        wasMovingLeft = isMovingLeft;
        wasMovingRight = isMovingRight;
        
        logic();
        draw();
        if (!dashOk) {
            tempsDash += delta;
        }
        if (tempsDash > dashCooldown) {
            dashOk = true;
        }


        //
    }

    private void input(float avance) {
        // Gestion de la pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.setScreen(new PauseScreen(game, this));
            return; // Sortir de la méthode pour éviter de traiter d'autres entrées
        }
        // Gestion du dash
        if (Gdx.input.isKeyPressed(game.toucheDash)) {
            if (dashOk && !isDashing) {
                isDashing = true;
                currentDashTime = 0f;
                dashOk = false;
                tempsDash = 0;
                dash_afficher = true;
            }
        }

        float currentSpeed = isDashing ? speed : playerSpeed;

        // Déplacement du joueur
        if (Gdx.input.isKeyPressed(game.toucheHaut) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            personnage1.changePositionY(currentSpeed * avance);
        }
        if (Gdx.input.isKeyPressed(game.toucheGauche) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            personnage1.changePositionX(-currentSpeed * avance);
        }
        if (Gdx.input.isKeyPressed(game.toucheDroite) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            personnage1.changePositionX(currentSpeed * avance);
        }
        if (Gdx.input.isKeyPressed(game.toucheBas) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            personnage1.changePositionY(-currentSpeed * avance);
        }

        // Mise à jour du dash
        if (isDashing) {
            currentDashTime += avance;
            if (currentDashTime >= dashDuration) {
                isDashing = false;
            }
        }

        // Limiter le joueur à la map
        personnage1.changePositionX(MathUtils.clamp(personnage1.getPositionX(), 0, mapSize - skin.getWidth())-personnage1.getPositionX());
        personnage1.changePositionY(MathUtils.clamp(personnage1.getPositionY(), 0, mapSize - skin.getWidth())-personnage1.getPositionY());


    }

    private void logic() {
        // Mise à jour de la caméra pour suivre le joueur
        camera.position.x = personnage1.getPositionX();// + skin.getWidth()/2;
        camera.position.y = personnage1.getPositionY();// + skin.getHeight()/2;

        // Limiter la caméra aux bords de la map
        cameraHalfWidth = camera.viewportWidth / 2;
        cameraHalfHeight = camera.viewportHeight / 2;

        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, mapSize - cameraHalfWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, mapSize - cameraHalfHeight);


        //Gestion du sbire
        sbireTest.agir(Gdx.graphics.getDeltaTime(), projectiles);

        // Mise à jour et gestion des projectiles (parcours la liste à l'envers pour éviter les problèmes de suppression)
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.update(Gdx.graphics.getDeltaTime());
            
            // Vérifier collision avec le joueur : si c'est le cas on lui enlève des points de vie et on supprime le projectile de la liste
            if (projectile.getHitbox().overlaps(personnage1.getHitbox())) {
                personnage1.prendreDegat(projectile.getDegats());
                projectiles.remove(i);
                continue;
            }
            
            // Vérifier si le projectile est hors portée : si c'est le cas on le supprime de la liste
            if (projectile.doitEtreDetruit()) {
                projectiles.remove(i);
                continue;
            }
        }
        //Gestion de la caméra
        camera.update();

        


    }

    private void draw() {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        // Dessiner la map
        game.batch.draw(mapTexture, 0, 0, mapSize, mapSize);

        // Dessiner le joueur avec l'animation actuelle

        TextureRegion currentFrame = animationHandler.getCurrentFrame();
        float originalWidth = currentFrame.getRegionWidth();
        float originalHeight = currentFrame.getRegionHeight();
    
        // Préserver le ratio d'aspect
        float aspectRatio = originalWidth / originalHeight;
        float scaledHeight = hauteur_skin * scalePlayer;
        float scaledWidth = scaledHeight * aspectRatio;

        sbireTest.draw(game,scaledWidth/2, scaledHeight/2); 


        //Dessiner les projectiles

        for (Projectile projectile : projectiles) {
            projectile.draw(game, scaledWidth / 2, scaledHeight / 2);
        }
        //Dessiner le personnage
        game.batch.draw(currentFrame, personnage1.getPositionX(), personnage1.getPositionY(), scaledWidth, scaledHeight);
        

        // afficher le dash selon la direction
        if (Gdx.input.isKeyPressed(game.toucheDash)) {
            if (dash_afficher) {
                if ((Gdx.input.isKeyPressed(game.toucheHaut) && Gdx.input.isKeyPressed(game.toucheDroite)) ||
                        (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    game.batch.draw(dash, personnage1.getPositionX(), personnage1.getPositionY(), largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 45);
                } else if ((Gdx.input.isKeyPressed(game.toucheHaut) && Gdx.input.isKeyPressed(game.toucheGauche)) ||
                        (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    game.batch.draw(dash, personnage1.getPositionX(), personnage1.getPositionY(), largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, -45);
                } else if ((Gdx.input.isKeyPressed(game.toucheBas) && Gdx.input.isKeyPressed(game.toucheDroite)) ||
                        (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                    game.batch.draw(dash, personnage1.getPositionX(), personnage1.getPositionY(), largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 135);
                } else if ((Gdx.input.isKeyPressed(game.toucheBas) && Gdx.input.isKeyPressed(game.toucheGauche)) ||
                        (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                    game.batch.draw(dash, personnage1.getPositionX(), personnage1.getPositionY(), largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, -135);
                } else if (Gdx.input.isKeyPressed(game.toucheHaut) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    game.batch.draw(dash, personnage1.getPositionX(), personnage1.getPositionY(), largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 90);
                } else if (Gdx.input.isKeyPressed(game.toucheGauche) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    game.batch.draw(dash, personnage1.getPositionX(), personnage1.getPositionY(), largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 0);
                } else if (Gdx.input.isKeyPressed(game.toucheBas) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    game.batch.draw(dash, personnage1.getPositionX(), personnage1.getPositionY(), largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, -90);
                } else if (Gdx.input.isKeyPressed(game.toucheDroite) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    game.batch.draw(dash, personnage1.getPositionX(), personnage1.getPositionY(), largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 180);
                }
                dash_afficher = false;
            }
        }
        // cooldown

        float progress = tempsDash / dashCooldown;
        float barWidth = 30;
        float barHeight = 5;
        float x = personnage1.getPositionX() - 3;
        float y = personnage1.getPositionY() - 8;

        //game.batch.draw(barre_vide, x, y, barWidth, barHeight);
        //game.batch.draw(barre_pleine, x, y, barWidth * progress, barHeight);

        for (int i = 0; i < this.personnage1.getVie(); i++) {
            game.batch.draw(coeur_plein, camera.position.x - cameraHalfWidth + i * hauteur_skin + 10,
                    camera.position.y + cameraHalfHeight - hauteur_skin, hauteur_skin, hauteur_skin);
        }
        for (int i = 0; i < this.personnage1.getBouclier(); i++) {
            game.batch.draw(bouclierIntact, camera.position.x - cameraHalfWidth + i * hauteur_skin + 10,
                    camera.position.y + cameraHalfHeight - hauteur_skin - 1 - hauteur_skin, hauteur_skin, hauteur_skin);
        }

        if (dashOk) {
            game.batch.draw(dash, camera.position.x - cameraHalfWidth + largeur_dash,
                    camera.position.y - cameraHalfHeight + hauteur_dash);
        } else {
            game.batch.draw(dash_gris, camera.position.x - cameraHalfWidth + largeur_dash,
                    camera.position.y - cameraHalfHeight + hauteur_dash);
        }

        game.batch.end();
    }

    @Override
    public void dispose() {
        
    mapTexture.dispose();
    skin.dispose();
    dash_texture.dispose();
    dash_gris.dispose();
    coeur_plein.dispose();
    bouclierIntact.dispose();
    Hercule_bas.dispose();
    Hercule_haut.dispose();
    Hercule_gauche.dispose();
    Hercule_droite.dispose();
    
    if (barre_vide != null) barre_vide.dispose();
    if (barre_pleine != null) barre_pleine.dispose();
    
    if (timer != null) {
        timer.cancel();
        timer.purge();
        timer = null;
    }
    if (timer2 != null) {
        timer2.cancel();
        timer2.purge();
        timer2 = null;
    }
    
    // Libérer l'animation
    if (animationHandler != null) {
        animationHandler.dispose();
    }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
    }

    // public void update() {
    // if (TimeUtils.timeSinceMillis(startTime) >= temps_recharge) {
    // // System.out.println("Tâche exécutée après " + DELAY + " millisecondes !");
    // dashOk = true;
    // startTime = TimeUtils.millis(); // Réinitialiser le timer
    // }
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