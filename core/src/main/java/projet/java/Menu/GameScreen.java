package projet.java.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import projet.java.entite.Entite;
import projet.java.entite.Personnage;
import projet.java.Main;
import projet.java.Map.Chambre;
import projet.java.Map.Map;
import projet.java.Inventaire.Inventaire;
import projet.java.Menu.InventaireScreen;
import projet.java.Inventaire.Item;
import projet.java.Inventaire.Inventaire;
import projet.java.Inventaire.Item.ItemType;
import java.util.TimerTask;
import java.util.Timer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.MathUtils;
import projet.java.Menu.GameOverScreen;

import projet.java.animation.AnimationHandler;

public class GameScreen implements Screen {
    final Main game;

    //tout ce qui est utile à la map :
    private ShapeRenderer shapeRenderer;
    Array<Rectangle> mursHitboxes ;
    Texture solTexture;
    Texture solTexture2;
    Texture solTexture3;
    Texture solTexture4;
    Texture solTexture5;
    Texture solTexture6;
    Texture solTexture7;
    Texture solTexture8;
    Texture solTexture9;
    Texture solTexture10;
    Texture solTexture11;
    Texture solTexture12;
    Texture murTexture;
    Texture murTexture2;
    Texture porteH;
    Texture porteV;
    Texture videTexture;
    int nombreDeChambres = 9;
    int[] tailleChambre = {70, 70};
    Map carte = new Map(nombreDeChambres, tailleChambre);
    int[][] map;
    int[][] mapCollision;
    final int TILE_SIZE = 16;
    final float CAMERA_SPEED = 600f;

    private Texture mapTexture;
    private Texture skin;
    private Rectangle playerHitbox ;
    private float playerX = 250;
    private float playerY = 250;
    private float hitboxX = 22;
    private float hitboxY = 18;
    private float playerSpeed = 100; // Vitesse normale en pixels par seconde
    private float speed = 500; // Vitesse du dash réduite (était 10000)
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

    private boolean firstFrame = true;
    private Map carteR;
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

    // texture pour essayer inventaire

    private Texture arme1;
    private Texture arme2;
    private Texture arme3;

    // largeur et longueur

    float largeur_coeur;
    float hauteur_coeur;
    float largeur_bouclier;
    float hauteur_bouclier;
    float largeur_skin;
    float hauteur_skin;

    float cameraHalfWidth;
    float cameraHalfHeight;

    private float scalePlayer = 1.0f; // Facteur d'échelle pour le personnage

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
        camera.setToOrtho(false, 320, 180);
    }

    @Override
    public void show() {
        //map
        mursHitboxes = new Array<>();
        carte.placerChambresGrille();
        carte.corridor_creator();
        carte.creuser_couloir();
        shapeRenderer = new ShapeRenderer();
        Map carteReduite = carte.reducteur();
        carteReduite.rotation90Trigo();
        //carteReduite.afficherMap();
        carteReduite.coupureCoord();
        carteReduite.creation_vide();
        //carteReduite.afficherMap();
        mapCollision = carteReduite.getCoord();
        carteReduite.randomiseur_sol();
        //carteReduite.naturalisation_mur();
        map = carteReduite.getCoord();
        carteR = carteReduite;
        videTexture = new Texture(Gdx.files.internal("map/Tile_30.png"));
        solTexture = new Texture(Gdx.files.internal("map/Tile_20.png"));
        murTexture = new Texture(Gdx.files.internal("map/Tile_11.png"));
        murTexture2 = new Texture(Gdx.files.internal("map/Tile_92.png"));
        porteH = new Texture(Gdx.files.internal("map/1.png"));
        porteV = new Texture(Gdx.files.internal("map/2.png"));
        solTexture2 = new Texture(Gdx.files.internal("map/Tile_21.png"));
        solTexture3 = new Texture(Gdx.files.internal("map/Tile_22.png"));
        solTexture4 = new Texture(Gdx.files.internal("map/Tile_36.png"));
        solTexture5 = new Texture(Gdx.files.internal("map/Tile_45.png"));
        solTexture6 = new Texture(Gdx.files.internal("map/Tile_72.png"));
        solTexture7 = new Texture(Gdx.files.internal("map/Tile_74.png"));
        solTexture8 = new Texture(Gdx.files.internal("map/Tile_76.png"));
        solTexture9 = new Texture(Gdx.files.internal("map/Tile_82.png"));
        solTexture10 = new Texture(Gdx.files.internal("map/Tile_83.png"));
        solTexture11 = new Texture(Gdx.files.internal("map/Tile_84.png"));
        solTexture12 = new Texture(Gdx.files.internal("map/Tile_95.png"));

        mapTexture = new Texture(Gdx.files.internal("map.png")); // Créez une image "map.png"
        skin = new Texture(Gdx.files.internal("image_heracles_normal.png")); // Créez une image "player.png"
        largeur_skin = skin.getWidth();
        hauteur_skin = skin.getHeight();
        playerHitbox = new Rectangle(playerX+hitboxX, playerY+hitboxY, 10, 10);


        personnage1 = new Personnage(4, 4, 4, "mathis", skin);
        personnage1.create_entite();

        // sprint ou dash //mettre un boutton dash pour montrer quand il a de nouveau
        // acces au dash, par exemple dans un coin le symbole de dash gris si il n'y a
        // pas acces et en couleur sinon

        dash_texture = new Texture("dash.png");
        dash = new TextureRegion(dash_texture);
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


        for (int y = 0; y < mapCollision.length; y++) {
            for (int x = 0; x < mapCollision[0].length; x++) {
                if (mapCollision[y][x] == 0) {
                    mursHitboxes.add(new Rectangle(x * TILE_SIZE, (map.length - 1 - y) * TILE_SIZE, TILE_SIZE, TILE_SIZE));
                }
            }
        }

        // texture pour tester la grille d'inventaire
        arme1 = new Texture("epee1.png");
        arme2 = new Texture("epee2.png");
        arme3 = new Texture("epee3.png");

        Item Arme1 = new Item("arme1", arme1, Item.ItemType.ARME);
        Item Arme2 = new Item("arme2", arme2, Item.ItemType.ARME);
        Item Arme3 = new Item("arme3", arme3, Item.ItemType.ARME);

        personnage1.getInventaire().addItem(Arme1);
        personnage1.getInventaire().addItem(Arme2);
        personnage1.getInventaire().addItem(Arme3);


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
    }

    private void input(float avance) {
        // Gestion de la pause
        if (firstFrame) {
            firstFrame = false;
            playerX = carteR.getCoordspawnX()*16;
            playerY = carteR.getCoordspawnY()*16;
        }
        // Gestion de la pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.setScreen(new PauseScreen(game, this));
            return; // Sortir de la méthode pour éviter de traiter d'autres entrées
        }
        // Gestion du game Over
        if (!personnage1.enVie()) {
            game.setScreen(new GameOverScreen(game));
            return; // Sortir de la méthode pour éviter de traiter d'autres entrées
        }
        // Gestion de l'inventaire
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.setScreen(new InventaireScreen(game, this, personnage1));
            return; // Sortir de la méthode pour éviter de traiter d'autres entrées
        }

        // Gestion du dash
        if (Gdx.input.isKeyPressed(game.toucheDash)) {
            if (dashOk && !isDashing) {
                isDashing = true;
                currentDashTime = 0f;
                dashOk = false;
                dash_afficher = true;
                tempsDash = 0;
            }
        }

        float currentSpeed = isDashing ? speed : playerSpeed;

        float oldX = playerX;
        float oldY = playerY;

        // Déplacement du joueur
        if (Gdx.input.isKeyPressed(game.toucheHaut) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerY += currentSpeed * avance;
        }
        if (Gdx.input.isKeyPressed(game.toucheGauche) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerX -= currentSpeed * avance;
        }
        if (Gdx.input.isKeyPressed(game.toucheDroite) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerX += currentSpeed * avance;
        }
        if (Gdx.input.isKeyPressed(game.toucheBas) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerY -= currentSpeed * avance;
        }

        playerHitbox.setPosition(playerX+hitboxX, oldY+hitboxY);
        for (int i = 0; i < mursHitboxes.size; i++) {
            if (playerHitbox.overlaps(mursHitboxes.get(i))) {
                // collision détectée, on annule le déplacement
                playerX = oldX;
                playerHitbox.setPosition(oldY+hitboxX, oldY+hitboxY);
                break;
            }
        }
        playerHitbox.setPosition(playerX+hitboxX, playerY+hitboxY);
        for (int i = 0; i < mursHitboxes.size; i++) {
            if (playerHitbox.overlaps(mursHitboxes.get(i))) {
                // collision détectée, on annule le déplacement
                playerY = oldY;
                playerHitbox.setPosition(playerX+hitboxX, playerY+hitboxY);
                break;
            }
        }



        // Mise à jour du dash
        if (isDashing) {
            currentDashTime += avance;
            if (currentDashTime >= dashDuration) {
                isDashing = false;
            }
        }

        // Limiter le joueur à la map
        //personnage1.changePositionX(MathUtils.clamp(personnage1.getPositionX(), 0, mapSize - skin.getWidth())-personnage1.getPositionX());
        //personnage1.changePositionY(MathUtils.clamp(personnage1.getPositionY(), 0, mapSize - skin.getWidth())-personnage1.getPositionY());

    }

    private void logic() {
        // Calculer le centre du personnage pour un meilleur suivi
        float playerCenterX = playerX + ((largeur_skin * scalePlayer) / 2);
        float playerCenterY = playerY + ((hauteur_skin * scalePlayer) / 2);
        
        // Utiliser une interpolation linéaire pour des mouvements plus fluides
        float lerpFactor = 0.1f; // Ajustez entre 0.01 (très lent) et 0.2 (très rapide)
        
        camera.position.x += (playerCenterX - camera.position.x) * lerpFactor;
        camera.position.y += (playerCenterY - camera.position.y) * lerpFactor;

        // Calculer les dimensions effectives de la vue caméra
        cameraHalfWidth = camera.viewportWidth / 2;
        cameraHalfHeight = camera.viewportHeight / 2;
        
        // Calculer les limites réelles de la map en pixels
        float mapWidthPixels = map[0].length * TILE_SIZE;
        float mapHeightPixels = map.length * TILE_SIZE;

        // Limiter la caméra pour qu'elle ne sorte pas de la map
        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, mapWidthPixels - cameraHalfWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, mapHeightPixels - cameraHalfHeight);

        camera.update();
    }

    private void draw() {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        // Dessiner la map
        int startX = Math.max(0, (int)((camera.position.x - cameraHalfWidth) / TILE_SIZE));
        int startY = Math.max(0, (int)((camera.position.y - cameraHalfHeight) / TILE_SIZE));
        int endX = Math.min(map[0].length - 1, (int)((camera.position.x + cameraHalfWidth) / TILE_SIZE + 1));
        int endY = Math.min(map.length - 1, (int)((camera.position.y + cameraHalfHeight) / TILE_SIZE + 1));

        mursHitboxes.clear();
        // Ne dessiner que les tuiles visibles
        for (int y = startY; y <= endY; y++) {
            int mapY = map.length - 1 - y;
            if (mapY < 0 || mapY >= map.length) continue;
            
            for (int x = startX; x <= endX; x++) {
                if (x < 0 || x >= map[0].length) continue;
                
                Texture texture = getTextureForTile(map[mapY][x]);
                if (texture != null && texture != porteH && texture != porteV) {
                    game.batch.draw(texture, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    if (texture == murTexture) {
                        mursHitboxes.add(new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
                    }
                } else if (texture == porteH || texture == porteV) {
                    game.batch.draw(solTexture, x * TILE_SIZE, y * TILE_SIZE);
                    game.batch.draw(texture, x * TILE_SIZE, y * TILE_SIZE);
                    if (texture == porteH){
                        mursHitboxes.add(new Rectangle(x * TILE_SIZE  , y * TILE_SIZE+ 4, TILE_SIZE , TILE_SIZE-8));
                    }
                    else if (texture == porteV){
                        mursHitboxes.add(new Rectangle(x * TILE_SIZE+2, y * TILE_SIZE, TILE_SIZE-12, TILE_SIZE));

                    }
                }
            }
        }



        // Dessiner le joueur avec l'animation actuelle

        TextureRegion currentFrame = animationHandler.getCurrentFrame();
        float originalWidth = currentFrame.getRegionWidth();
        float originalHeight = currentFrame.getRegionHeight();
    
        // Préserver le ratio d'aspect
        float aspectRatio = originalWidth / originalHeight;
        float scaledHeight = hauteur_skin * scalePlayer;
        float scaledWidth = scaledHeight * aspectRatio;

        game.batch.draw(currentFrame, playerX, playerY, scaledWidth, scaledHeight);
        

        // afficher le dash selon la direction
        if (Gdx.input.isKeyPressed(game.toucheDash)) {
            if (dash_afficher) {
                if ((Gdx.input.isKeyPressed(game.toucheHaut) && Gdx.input.isKeyPressed(game.toucheDroite)) ||
                        (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 45);
                } else if ((Gdx.input.isKeyPressed(game.toucheHaut) && Gdx.input.isKeyPressed(game.toucheGauche)) ||
                        (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, -45);
                } else if ((Gdx.input.isKeyPressed(game.toucheBas) && Gdx.input.isKeyPressed(game.toucheDroite)) ||
                        (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 135);
                } else if ((Gdx.input.isKeyPressed(game.toucheBas) && Gdx.input.isKeyPressed(game.toucheGauche)) ||
                        (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, -135);
                } else if (Gdx.input.isKeyPressed(game.toucheHaut) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 90);
                } else if (Gdx.input.isKeyPressed(game.toucheGauche) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 0);
                } else if (Gdx.input.isKeyPressed(game.toucheBas) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, -90);
                } else if (Gdx.input.isKeyPressed(game.toucheDroite) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    game.batch.draw(dash, playerX, playerY, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1,
                            1, 180);
                }
                dash_afficher = false;
            }
        }
        // cooldown

        float progress = tempsDash / dashCooldown;
        float barWidth = 30;
        float barHeight = 5;
        float x = playerX - 3;
        float y = playerY - 8;

        //game.batch.draw(barre_vide, x, y, barWidth, barHeight);
        //game.batch.draw(barre_pleine, x, y, barWidth * progress, barHeight);

        for (int i = 0; i < personnage1.getVie(); i++) {
            // Réduire la taille des icônes de vie pour qu'elles soient proportionnées
            float iconSize = Math.min(hauteur_skin, 16); // Maximum 16 pixels
            game.batch.draw(coeur_plein, 
                            camera.position.x - cameraHalfWidth + 5 + (i * (iconSize + 2)), 
                            camera.position.y + cameraHalfHeight - iconSize - 5, 
                            iconSize, iconSize);
        }
        for (int i = 0; i < personnage1.getBouclier(); i++) {
            float iconSize = Math.min(hauteur_skin, 16); // Maximum 16 pixels
            game.batch.draw(bouclierIntact, 
                            camera.position.x - cameraHalfWidth + 5 + (i * (iconSize + 2)), 
                            camera.position.y + cameraHalfHeight - iconSize*2 - 7, 
                            iconSize, iconSize);
        }

        // À la fin de la méthode draw(), avant game.batch.end():
        // Dessiner une minimap simplifiée
        float minimapSize = 40;
        float minimapX = camera.position.x + cameraHalfWidth - minimapSize - 10;
        float minimapY = camera.position.y - cameraHalfHeight + 10;

        // Dessiner le fond de la minimap
        game.batch.setColor(0, 0, 0, 0.7f);
        game.batch.draw(solTexture, minimapX - 2, minimapY - 2, minimapSize + 4, minimapSize + 4);
        game.batch.setColor(Color.WHITE);

        // Calculer l'échelle
        float mapWidthPixels = map[0].length * TILE_SIZE;
        float mapHeightPixels = map.length * TILE_SIZE;
        float scaleX = minimapSize / mapWidthPixels;
        float scaleY = minimapSize / mapHeightPixels;
        float scale = Math.min(scaleX, scaleY);

        
        for (int mapY = 0; mapY < map.length; mapY++) {
            for (int mapX = 0; mapX < map[0].length; mapX++) {
                if (map[mapY][mapX] == 0) { // Mur
                    game.batch.setColor(0.3f, 0.3f, 0.3f, 1); // Gris foncé
                } else if (map[mapY][mapX] == 2 || map[mapY][mapX] == 3) { // Portes
                    game.batch.setColor(0.9f, 0.6f, 0.2f, 1); // Orange
                } else if (map[mapY][mapX] == 4) { // Vide
                    continue;
                } else { // Sol
                    game.batch.setColor(0.7f, 0.7f, 0.7f, 1); // Gris clair
                }
                
                float tileX = minimapX + mapX * TILE_SIZE * scale;
                float tileY = minimapY + (map.length - 1 - mapY) * TILE_SIZE * scale;
                game.batch.draw(solTexture, tileX, tileY, TILE_SIZE * scale, TILE_SIZE * scale);
            }
        }

        // position du joueur sur minimap
        game.batch.setColor(1, 0, 0, 1); // Rouge
        float playerMinimapX = minimapX + playerX * scale;
        float playerMinimapY = minimapY + playerY * scale;
        game.batch.draw(solTexture, playerMinimapX, playerMinimapY, 3, 3);

        // Réinitialiser la couleur
        game.batch.setColor(1, 1, 1, 1);
        game.batch.end();
        //affichage de hitbox
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (Rectangle mur : mursHitboxes) {
            shapeRenderer.rect(mur.x, mur.y, mur.width, mur.height);    
        }

        shapeRenderer.setColor(Color.GREEN);
        float scaledWidth1 = largeur_skin * scalePlayer;
        float scaledHeight1 = hauteur_skin * scalePlayer;

        shapeRenderer.rect(playerX+hitboxX, playerY+hitboxY, 10,10);

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        
    mapTexture.dispose();
    skin.dispose();
    dash_texture.dispose();
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
        // Maintenir le ratio d'aspect tout en gardant le même niveau de zoom
        float aspectRatio = (float)width / (float)height;
        float targetRatio = 16f/9f; // Ratio cible
        
        if (aspectRatio > targetRatio) {
            // Plus large que 16:9
            camera.viewportWidth = camera.viewportHeight * aspectRatio;
        } else {
            // Plus haut que 16:9
            camera.viewportHeight = camera.viewportWidth / aspectRatio;
        }
        
        camera.update();
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

    private Texture getTextureForTile(int value) {
        if (value == 0 ) {
            return murTexture;
        } else if (value == 3) {
            return porteH;
        } else if (value == 2) {
            return porteV;
        } else if (value == 4){
            // return videTexture;
            return null;
        } else if (value == 100){
            return solTexture2;
        } else if (value == 101){
            return solTexture3 ;
        } else if (value == 102) {
            return solTexture4;
        } else if (value == 103) {
            return solTexture5;
        } else if (value == 104) {
            return solTexture6;
        }else if (value == 105) {
            return solTexture7;
        }else if (value == 106) {
            return solTexture8;
        }else if (value == 107) {
            return solTexture9;
        }else if (value == 108) {
            return solTexture10;
        }else if (value == 109) {
            return solTexture11;
        }else if (value == 110) {
            return solTexture12;
        } else if (value == 200) {
            return murTexture2;
        } else {
            return solTexture; // 1 ou autre = sol
        }

    }
}