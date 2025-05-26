package projet.java.Menu;


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

import projet.java.Main;

<<<<<<< Updated upstream
=======
import projet.java.animation.AnimationHandler;
import projet.java.combat.AttackManager;
import projet.java.debug.GameDebugger;
import projet.java.entite.ComportementMelee;
import projet.java.entite.Niveau;
import projet.java.entite.Sbire;
import projet.java.entite.Projectile;
import projet.java.entite.Coin;
>>>>>>> Stashed changes

public class GameScreen implements Screen {

    final Main game;

<<<<<<< Updated upstream
=======
    //tout ce qui est utile à la map :
    private ShapeRenderer shapeRenderer;
    Array<Rectangle> mursHitboxes ;
    Array<Rectangle> porteHitboxes ;
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
    Texture porteHOpen;
    Texture porteVOpen;
    Texture videTexture;
    int nombreDeChambres = 6;
    int[] tailleChambre = {70, 70};
    Map carte = new Map(nombreDeChambres, tailleChambre);
    int[][] map;
    int[][] mapCollision;
    final int TILE_SIZE = 16;
    final float CAMERA_SPEED = 600f;

    private Texture mapTexture;
    private Texture skin;
    private Rectangle playerHitbox ;
    //private float playerX = 250;
    //private float playerY = 250;
    private float hitboxX = 22;
    private float hitboxY = 18;
    private float playerSpeed = 100; // Vitesse normale en pixels par seconde
    private float speed = 500; // Vitesse du dash réduite (était 10000)
    private float dashDuration = 0.08f; // Durée du dash en secondes pour maintenir la même distance
    private float currentDashTime = 0f; // Pour suivre la durée du dash en cours
    private boolean isDashing = false; // Pour savoir si on est en train de dasher
    private OrthographicCamera camera;
    private Personnage personnage1; // = new Personnage(4, 2, 3, "mathis", skin);
    private Texture dash_texture;
    private TextureRegion dash;
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
    private float scaleSbire = 1.0f;
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

    // Dans la section des déclarations d'attributs de GameScreen
    private AttackManager attackManager;  // Gestionnaire des attaques

    // sbire melee test
    Niveau niveau;

    private Sbire sbiretest;
    private ArrayList<Projectile> projectiles;
    
    // Ajouter comme attribut de classe
    public GameDebugger debugger;

    // Ajouter cette variable avec les autres attributs de la classe
    private float attackSpeedModifier = 0.3f; // Réduit à 30% de la vitesse normale pendant l'attaque

    // Ajouter ces variables avec les autres attributs
    private boolean damageEffect = false;
    private float damageEffectTime = 0;
    private final float DAMAGE_EFFECT_DURATION = 0.5f; // Durée en secondes
    private final Color DAMAGE_COLOR = new Color(1, 0, 0, 0.5f); // Rouge semi-transparent

    // Ajouter cette variable avec les autres attributs de la classe
    private boolean gameInitialized = false;

    // Coin system
    private ArrayList<Coin> coins = new ArrayList<>();

    // Add this to cache the coin texture
    private Texture coinTexture; // Add this to cache the coin texture

>>>>>>> Stashed changes
    public GameScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void show() {
<<<<<<< Updated upstream
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
=======
        // Si le jeu a déjà été initialisé, ne pas recréer les éléments
        if (gameInitialized) {
            return;
        }
        
        // Marquer le jeu comme initialisé
        gameInitialized = true;
        
        // Le reste du code d'initialisation reste inchangé
        mursHitboxes = new Array<>();
        porteHitboxes = new Array<>();
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
        porteHOpen = new Texture(Gdx.files.internal("map/Door_OPEN_H.png"));
        porteVOpen = new Texture(Gdx.files.internal("map/Door_OPEN_V.png"));

        mapTexture = new Texture(Gdx.files.internal("map.png")); // Créez une image "map.png"
        skin = new Texture(Gdx.files.internal("image_heracles_normal.png")); // Créez une image "player.png"
        largeur_skin = skin.getWidth();
        hauteur_skin = skin.getHeight();
        niveau = new Niveau();
        
        // Définir le listener pour les dégâts
        personnage1.setDamageListener(new Personnage.DamageListener() {
            @Override
            public void onDamageTaken(int damage) {
                // Activer l'effet de dégât
                damageEffect = true;
                damageEffectTime = 0;
            }
        });
        
        // Créer le sbire normal avec sa texture normale
        Rectangle sbireHitbox = new Rectangle(300, 300, 32, 32);
        sbiretest = new Sbire(
            300, 0, 3,            // vie, bouclier, mana
            300, 300,           // positionX, positionY
            25,                 // vitesseDeplacement (peut être augmenté si le sbire est trop lent)
            300, 3,             // vitesseProjectile, cooldown
            sbireHitbox,  // hitbox plus grande et correctement positionnée
            1500, 30,           // porteeProjectile, porteeCaC (augmentée pour faciliter l'attaque)
            1, 1,              // degats (projectile), degatsCaC (augmenté de 0 à 15)
            personnage1,         
            new ComportementMelee(),
            new Texture(Gdx.files.internal("coeur_plein.png")),
            new Texture("Hercule_haut.png")
        );
        niveau.ajouterSbire(sbiretest);
        
        // Créer le boss avec une hitbox plus grande et le ComportementBoss
        Rectangle bossHitbox = new Rectangle(300, 300, 48, 48); // Hitbox plus grande pour le boss
        sbireBoss = new Sbire(
            400, 50, 5,       // vie, bouclier, mana (plus élevés que le sbire normal)
            300, 300,         // positionX, positionY
            20, 300, 3,       // vitesseDeplacement, vitesseProjectile, cooldown
            bossHitbox,
            1500, 100,        // porteeProjectile, porteeCaC 
            0, 0,             // degats (projectile réduit de 3 à 1), degatsCaC (réduit de 10 à 5)
            personnage1, 
            new ComportementBoss(), // Important: utiliser ComportementBoss pour que isBoss=true
            new Texture(Gdx.files.internal("coeur_plein.png")),
            new Texture("Orc1/orc3_front_idle_1.png")              // La texture du sbire n'est plus utilisée directement
        );
        niveau.ajouterSbire(sbireBoss);
        projectiles = new ArrayList<>();
        // TEST SBIRE
        //sbireTest = new Sbire(3,3,3,300, 300,20,300,3,new Rectangle(0,0, 2,4), 1500,1, 1,0, personnage1, new ComportementDistanceMax(),new Texture(Gdx.files.internal("coeur_plein.png")),new Texture("Hercule_haut.png"));
        //Gestion projectiles du sbire
        projectiles = new ArrayList<>();
        //

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

        // Initialiser le gestionnaire d'animations
        animationHandler = new AnimationHandler();
        
        // Initialiser le gestionnaire d'attaques avec un cooldown de 3 secondes
        attackManager = new AttackManager(game, personnage1, animationHandler, 0.5f);
        attackManager.getArmeMelee().setNiveau(niveau);
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

        // Initialiser le débogueur après la création du personnage, niveau et attackManager
        debugger = new GameDebugger(personnage1, niveau, attackManager);
        arme1 = new Texture("epee1.png");
        arme2 = new Texture("epee2.png");
        arme3 = new Texture("epee3.png");
        Potion potionvie = new Potion(3);
        Texture potion = potionvie.getImage();

        Item Arme1 = new Item("arme1", arme1, Item.ItemType.ARME, 3);
        Item Arme2 = new Item("arme2", arme2, Item.ItemType.ARME, 2);
        Item Arme3 = new Item("arme3", arme3, Item.ItemType.ARME, 3);
        Item Potion = new Item("potion", potion, Item.ItemType.POTION, potionvie.getVie());

        if (une_fois == 1) {
            personnage1.getInventaire().addItem(Arme1);
            personnage1.getInventaire().addItem(Arme2);
            personnage1.getInventaire().addItem(Arme3);
            personnage1.getInventaire().addItem(Potion);
            une_fois ++;
        }

        coinTexture = new Texture(Gdx.files.internal("coin.png"));
        // Set up coin spawn listener for all sbires
        for (Sbire sbire : niveau.getSbires()) {
            sbire.setCoinSpawnListener((x, y) -> {
                coins.add(new Coin(x, y));
            });
        }
        // --- RANDOM COIN SPAWN AT START ---
        int randomCoinCount = 10; // Number of random coins to spawn
        int mapWidth = map[0].length * TILE_SIZE;
        int mapHeight = map.length * TILE_SIZE;
        for (int i = 0; i < randomCoinCount; i++) {
            float rx, ry;
            // Find a random walkable tile
            do {
                rx = (float)(Math.random() * mapWidth);
                ry = (float)(Math.random() * mapHeight);
                int tileX = (int)(rx / TILE_SIZE);
                int tileY = (int)(ry / TILE_SIZE);
                // Only place on walkable tiles (not walls)
                if (map[map.length - 1 - tileY][tileX] != 0) break;
            } while (true);
            coins.add(new Coin(rx, ry));
        }
>>>>>>> Stashed changes
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
    }

    private void logic() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logic'");
    }

    private void draw() {
        // TODO Auto-generated method stub
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
        throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }


    
}
