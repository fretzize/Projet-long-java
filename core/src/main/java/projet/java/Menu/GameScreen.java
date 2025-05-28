package projet.java.Menu;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import projet.java.entite.ArmeMelee;
import projet.java.entite.ComportementBoss;
import projet.java.entite.ComportementDistanceMax;
import projet.java.entite.ComportementMelee;
import projet.java.entite.Entite;
import projet.java.entite.Leurre;
import projet.java.entite.Personnage;
import projet.java.entite.Projectile;
import projet.java.entite.Sbire;
import projet.java.Main;
import projet.java.Map.Chambre;
import projet.java.Map.Map;
import projet.java.Inventaire.DatabaseItem;
import projet.java.Inventaire.Inventaire;
import projet.java.Menu.InventaireScreen;
import projet.java.Inventaire.Item;
import projet.java.Inventaire.Inventaire;
import projet.java.Inventaire.Coffre;
import projet.java.Inventaire.Item.ItemType;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.math.MathUtils;

import projet.java.Menu.GameOverScreen;
import projet.java.Menu.VictoireScreen;
import projet.java.Inventaire.Potion;


import projet.java.animation.AnimationHandler;
import projet.java.combat.AttackManager;
import projet.java.combat.FireballManager;
import projet.java.debug.GameDebugger;
import projet.java.entite.ComportementMelee;
import projet.java.entite.Niveau;
import projet.java.entite.Sbire;
import projet.java.entite.Projectile;

import static com.badlogic.gdx.math.MathUtils.random;

public class GameScreen implements Screen {
    final Main game;

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
    Texture coffreTexture;
    Texture coffreTextureouvert;
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
    private float playerSpeed; // Vitesse normale en pixels par seconde
    private float speed = 500; // Vitesse du dash réduite (était 10000)
    private float dashDuration = 0.08f; // Durée du dash en secondes pour maintenir la même distance
    private float currentDashTime = 0f; // Pour suivre la durée du dash en cours
    private boolean isDashing = false; // Pour savoir si on est en train de dasher
    private OrthographicCamera camera;
    private Personnage personnage1; // = new Personnage(4, 2, 3, "mathis", skin);

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
    // private Texture arme2;
    // private Texture arme3;

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

    private ArrayList<Sbire> sbiretest = new ArrayList<>();
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

    private float tempsAcceleration = 0;
    private float tempsMaxAcceleration = 10f;


    // creation de la liste de coffre present sur la liste

    private List<Coffre> coffres = new ArrayList<>();

    Texture itemcoffre;
    float tempsitem = 2;
    float tempscoffre = 0;
    
   

    // Ajouter cette variable avec les autres attributs
    private FireballManager fireballManager;

    public GameScreen(final Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);
        playerHitbox = new Rectangle(250+hitboxX, 250+hitboxY, 10, 10);
        this.personnage1 = new Personnage(skin,playerHitbox);
    }

    ArmeMelee armeMelee;
    // et dans le show armeMelee = attackManager.getArme()
    // et dans le render attackManager.setArme(armeMelee)
    // et dans le game screen
    // public void setArme(String nom, int nombre, int range) {
    //     this.armeMelee = new ArmeMelee(nom, nombre, range, range, nombre, nom, range);
    // }
    //TEST SBIRE

    private Sbire sbireBoss;
    
    int une_fois = 1;
    @Override
    public void show() {
        // Si le jeu a déjà été initialisé, ne pas recréer les éléments
        if (gameInitialized) {
            if (game.gameMusic != null) {
                game.stopMenuMusic();
                game.gameMusic.play();
            }
            return;
        }
        
        // Marquer le jeu comme initialisé
        gameInitialized = true;
        
        if (game.gameMusic == null) {
            game.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("OST.mp3"));
            game.gameMusic.setLooping(true);
            game.gameMusic.setVolume(game.getSoundVolume()); // Volume de la musique de jeu
        }

        game.stopMenuMusic();
        game.gameMusic.play(); // Démarrer la musique de jeu

        // Le reste du code d'initialisation reste inchangé
        mursHitboxes = new Array<>();
        porteHitboxes = new Array<>();
        carte.placerChambresGrille();
        carte.corridor_creator();
        carte.creuser_couloir();
        carte.placerCoffresDansChambres();
        System.out.println(carte.compterCoffres());
        shapeRenderer = new ShapeRenderer();
        Map carteReduite = carte.reducteur();
        carteReduite.rotation90Trigo();
        // carteReduite.afficherMap();
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
        coffreTexture = new Texture(Gdx.files.internal("Chest1.png"));
        coffreTextureouvert = new Texture(Gdx.files.internal("Chest3.png"));
        mapTexture = new Texture(Gdx.files.internal("map.png")); // Créez une image "map.png"
        skin = new Texture(Gdx.files.internal("image_heracles_normal.png")); // Créez une image "player.png"
        largeur_skin = skin.getWidth();
        hauteur_skin = skin.getHeight();
        
        // Créer le niveau avant l'AttackManager
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
        
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                Random rand  = new Random();
                if ((map[i][j] == 100 || map[i][j] ==101 || map[i][j] == 102 || map[i][j] ==103 || map[i][j] == 104 || map[i][j] ==105 || map[i][j] == 106
                        || map[i][j] ==107 || map[i][j] == 108 || map[i][j] ==109 || map[i][j] == 110 ) && rand.nextFloat() < 0.006 && voisinNotMur(map,i,j)) {
                    Sbire sbiretemp;
                    System.out.println("map = " + map[i][j]);
                    int posX = j * TILE_SIZE;
                    int posY = (map.length - 1 - i) * TILE_SIZE;
                    sbiretemp = new Sbire(
                            300, 0, 3,            // vie, bouclier, mana
                            posX , posY ,           // positionX, positionY
                            25,                 // vitesseDeplacement (peut être augmenté si le sbire est trop lent)
                            300, 3,             // vitesseProjectile, cooldown
                            new Rectangle(posX+1, posY, 30, 30),  // hitbox plus grande et correctement positionnée
                            1500, 30,           // porteeProjectile, porteeCaC (augmentée pour faciliter l'attaque)
                            0, 0,              // degats (projectile), degatsCaC (augmenté de 0 à 15)
                            personnage1,
                            new ComportementMelee(),
                            new Texture(Gdx.files.internal("bomba.png")),
                            new Texture("Hercule_haut.png"),
                            new Rectangle(300,300, 32, 32)
                    );
                    sbiretest.add(sbiretemp);
                }
            }
        }

        // Créer le boss avec une hitbox plus grande et le ComportementBoss
        Rectangle bossHitbox = new Rectangle(300, 300, 48, 48); // Hitbox plus grande pour le boss
        sbireBoss = new Sbire(
            400, 50, 5,       // vie, bouclier, mana (plus élevés que le sbire normal)
            300, 300,         // positionX, positionY
            45, 300, 1.5f,       // vitesseDeplacement, vitesseProjectile, cooldown
            bossHitbox,
            1500, 100,        // porteeProjectile, porteeCaC 
            1, 1,             // degats (projectile réduit de 3 à 1), degatsCaC (réduit de 10 à 5)
            personnage1, 
            new ComportementBoss(), // Important: utiliser ComportementBoss pour que isBoss=true
            new Texture(Gdx.files.internal("bomba.png")),
            new Texture("Orc1/orc3_front_idle_1.png"),// La texture du sbire n'est plus utilisée directement
            new Rectangle(300, 300, 48, 48) // Hitbox du boss
        );
        sbiretest.add(sbireBoss); // Ajouter le boss à la liste des sbires

//        Sbire sbiretemp;
//        System.out.println("map = " + carteR.getCoordspawnX()*16 + " " + carteR.getCoordspawnX()*16);
//        sbiretemp = new Sbire(
//                300, 0, 3,            // vie, bouclier, mana
//                carteR.getCoordspawnX()*16,  carteR.getCoordspawnX()*16,           // positionX, positionY
//                25,                 // vitesseDeplacement (peut être augmenté si le sbire est trop lent)
//                300, 3,             // vitesseProjectile, cooldown
//                new Rectangle(carteR.getCoordspawnX()*16+7, carteR.getCoordspawnX()*16, 15, 15),  // hitbox plus grande et correctement positionnée
//                1500, 30,           // porteeProjectile, porteeCaC (augmentée pour faciliter l'attaque)
//                0, 0,              // degats (projectile), degatsCaC (augmenté de 0 à 15)
//                personnage1,
//                new ComportementMelee(),
//                new Texture(Gdx.files.internal("coeur_plein.png")),
//                new Texture("Hercule_haut.png")
//        );
//        sbiretest.add(sbiretemp);
        for (int i = 0; i < sbiretest.size(); i++) {
            niveau.ajouterSbire(sbiretest.get(i));
        }

        projectiles = new ArrayList<>();

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

        // Définir le listener pour les dégâts
        personnage1.setDamageListener(new Personnage.DamageListener() {
            @Override
            public void onDamageTaken(int damage) {
                // Activer l'effet de dégât
                damageEffect = true;
                damageEffectTime = 0;
            }
        });
        
        // Initialiser le gestionnaire d'animations
        animationHandler = new AnimationHandler();
        
        // Créer le gestionnaire d'attaques avec le niveau
        attackManager = new AttackManager(game, personnage1, animationHandler, 0.4f, niveau);
        // armeMelee = attackManager.getArme();
        // personnage1.setArme(armeMelee.getNom(), armeMelee.getDegats(), armeMelee.getPortee(), niveau);
        // Initialiser FireballManager après avoir créé le niveau
        fireballManager = new FireballManager(game, personnage1, niveau);
        
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
    
        if (timer2 != null) {
            timer2.cancel();
        }
        timer2 = new Timer();

        //HitBoxes pour les murs
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
        // arme2 = new Texture("epee2.png");
        // arme3 = new Texture("epee3.png");
        Potion potionvie = new Potion(3);
        Texture potion = potionvie.getImage(1);
        Potion potion_vit = new Potion(60);
        Texture potion_vitesse = potion_vit.getImage(2);
        DatabaseItem database = new DatabaseItem();
        List<Item> data = database.getData();

        Item Arme1 = data.get(2);
        // Item Arme2 = new Item("arme2", arme2, Item.ItemType.ARME, 2);
        // Item Arme3 = new Item("arme3", arme3, Item.ItemType.ARME, 3);
        Item Potion = new Item("potion", potion, Item.ItemType.POTION, potionvie.getVie(), 0);
        Item Potion_Vitesse = new Item("potion", potion_vitesse, Item.ItemType.POTIONVITESSE, potion_vit.getVie(), 0);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == 500) {
                    Rectangle hitbox = new Rectangle(x * TILE_SIZE, (map.length - 1 - y) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    Item itemAlea = new DatabaseItem().getItemAlea(); // ou un item défini
                    Coffre coffre = new Coffre(x, y, itemAlea, hitbox, coffreTexture);
                    coffre.setPositionX(x * TILE_SIZE);
                    coffre.setPositionY((map.length - 1 - y) * TILE_SIZE);
                    coffres.add(coffre);
                }
            }
        }
        if (une_fois == 1) {
            personnage1.getInventaire().addItem(Arme1);
            // personnage1.getInventaire().addItem(Arme2);
            // personnage1.getInventaire().addItem(Arme3);
            personnage1.getInventaire().addItem(Potion);
            personnage1.getInventaire().addItem(Potion_Vitesse);
            une_fois ++;
        }

    }

    @Override
    public void render(float delta) {
        input(delta);
        // this.armeMelee = personnage1.getArme();
        // attackManager.setArme(armeMelee.getNom(), armeMelee.getDegats(), armeMelee.getPortee());
        playerSpeed = personnage1.getVitesse();
        boolean isMovingUp = Gdx.input.isKeyPressed(game.toucheHaut) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean isMovingDown = Gdx.input.isKeyPressed(game.toucheBas) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean isMovingLeft = Gdx.input.isKeyPressed(game.toucheGauche) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean isMovingRight = Gdx.input.isKeyPressed(game.toucheDroite) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        

        Vector2 mouseDirection = getMouseDirection();

        // Mettre à jour le système d'attaque et récupérer l'état d'attaque
        boolean isAttacking = attackManager.update(delta,mouseDirection);
        
        // Ne mettre à jour l'animation en fonction du mouvement que si on n'est pas en train d'attaquer
        if (!isAttacking && !animationHandler.isAttacking()) {
            animationHandler.update(delta, isMovingUp, isMovingDown, isMovingLeft, isMovingRight, false);
        } else {
            // Juste mettre à jour le timer d'animation sans changer la direction
            animationHandler.updateTimerOnly(delta);
        }
        
        // Mémoriser l'état de mouvement pour le dash
        wasMovingUp = isMovingUp;
        wasMovingDown = isMovingDown;
        wasMovingLeft = isMovingLeft;
        wasMovingRight = isMovingRight;
        
        // Mettre à jour le timer de l'effet de dégât
        if (damageEffect) {
            damageEffectTime += delta;
            if (damageEffectTime >= DAMAGE_EFFECT_DURATION) {
                damageEffect = false;
            }
        }
        
        // Update damage effects for all sbires
        for (Sbire sbire : niveau.getSbires()) {
            if (sbire.enVie()) {
                // Update damage effect timer
                if (sbire.isDamageEffectActive()) {
                    sbire.updateDamageEffect(delta);
                }
            }
        }
        
        logic();
        draw();
        if (!dashOk) {
            tempsDash += delta;
        }
        if (tempsDash > dashCooldown) {
            dashOk = true;
        }

        if (personnage1.getAcceleration()) {
            tempsAcceleration += delta;
        }
        if (tempsAcceleration > tempsMaxAcceleration) {
            personnage1.setAcceleration(false);
            personnage1.setVitesse(personnage1.getVitesseBase());
            tempsAcceleration = 0;
        }

        if (sbiretest.size() == 0) {
            game.setScreen(new VictoireScreen(game));
        }

        if (projectiles.size() != 0) {
            for (Iterator<Projectile> iterator = projectiles.iterator(); iterator.hasNext();) {
                Projectile projectile = iterator.next();
                Rectangle hitbox = projectile.getHitbox();
                for (int k = 0; k < mursHitboxes.size; k++) {
                    if (hitbox.overlaps(mursHitboxes.get(k))) {
                        System.out.println("Collision avec un mur en X");
                        iterator.remove();
                        break;
                    }
                }
            }
        } 
    }

    private void input(float avance) {
        // Gestion de la pause
        if (firstFrame) {
            firstFrame = false;
            personnage1.setPositionX(carteR.getCoordspawnX()*16);
            personnage1.setPositionY(carteR.getCoordspawnY()*16);
            //playerY = carteR.getCoordspawnY()*16;
        }
        // Gestion de la pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            // Assurez-vous que les timers sont sauvegardés avant la pause
            // au lieu de les annuler dans hide()
            game.setScreen(new PauseScreen(game, this));
            return;
        }
        // Gestion du game Over
        if (!personnage1.enVie()) {
            if (game.gameMusic != null) {
                game.gameMusic.stop();
            }
            game.setScreen(new GameOverScreen(game));
            return; // Sortir de la méthode pour éviter de traiter d'autres entrées
        }
        // Gestion de l'inventaire
        if (Gdx.input.isKeyJustPressed(game.toucheInventaire)) {
            game.setScreen(new InventaireScreen(game, this, personnage1, attackManager));
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

        float currentSpeed;
        if (isDashing) {
            currentSpeed = speed; // La vitesse de dash reste inchangée
        } else if (attackManager.isAttacking()) {
            // Réduire la vitesse pendant l'attaque
            currentSpeed = playerSpeed * attackSpeedModifier;
        } else {
            // Vitesse normale
            currentSpeed = playerSpeed;
        }

        float oldX = personnage1.getPositionX();
        float oldY = personnage1.getPositionY();
        int xp=0;
        int yp=0;

        // Déplacement du joueur
        if (Gdx.input.isKeyPressed(game.toucheHaut) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //playerY += currentSpeed * avance;
            personnage1.changePositionY(currentSpeed * avance);
        }
        if (Gdx.input.isKeyPressed(game.toucheGauche) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //playerX -= currentSpeed * avance;
            personnage1.changePositionX(-currentSpeed * avance);
        }
        if (Gdx.input.isKeyPressed(game.toucheDroite) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //playerX += currentSpeed * avance;
            personnage1.changePositionX(currentSpeed * avance);
        }
        if (Gdx.input.isKeyPressed(game.toucheBas) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            //playerY -= currentSpeed * avance;
            personnage1.changePositionY(-currentSpeed * avance);
        }
        
        
        // Mise à jour de la hitbox du joueur
        gererCollisionsEntite(
            personnage1, oldX, oldY,
            mursHitboxes, porteHitboxes,
            hitboxX, hitboxY,
            map, TILE_SIZE
        );

        // //test mur en X
        // playerHitbox.setPosition(personnage1.getPositionX()+hitboxX, oldY+hitboxY);
        // for (int i = 0; i < mursHitboxes.size; i++) {
        //     if (playerHitbox.overlaps(mursHitboxes.get(i))) {
        //         // collision détectée, on annule le déplacement
        //         personnage1.setPositionX(oldX);
        //         playerHitbox.setPosition(personnage1.getPositionX()+hitboxX, personnage1.getPositionY()+hitboxY);
        //         break;
        //     }
        // }

        // //test porte en x
        // playerHitbox.setPosition(personnage1.getPositionX()+hitboxX, personnage1.getPositionY()+hitboxY);
        // for (int i = 0; i < porteHitboxes.size; i++) {
        //     if (playerHitbox.overlaps(porteHitboxes.get(i))) {
        //         // collision détectée, on annule le déplacement
        //         personnage1.setPositionX(oldX);
        //         playerHitbox.setPosition(oldY+hitboxX, oldY+hitboxY);
        //         xp = (int) Math.floor(porteHitboxes.get(i).x/TILE_SIZE);
        //         yp = (int) Math.floor(porteHitboxes.get(i).y/TILE_SIZE);
        //         map[map.length - 1 - yp][xp] = 30;
        //         break;
        //     }
        // }
        // //test mur en Y
        // playerHitbox.setPosition(personnage1.getPositionX()+hitboxX, personnage1.getPositionY()+hitboxY);
        // for (int i = 0; i < mursHitboxes.size; i++) {
        //     if (playerHitbox.overlaps(mursHitboxes.get(i))) {
        //         // collision détectée, on annule le déplacement
        //         //playerY = oldY;
        //         personnage1.setPositionY(oldY);
        //         playerHitbox.setPosition(personnage1.getPositionX()+hitboxX, personnage1.getPositionY()+hitboxY);
        //         break;
        //     }
        // }

        // //test porte en Y
        // for (int i = 0; i < porteHitboxes.size; i++) {
        //     if (playerHitbox.overlaps(porteHitboxes.get(i))) {
        //         // collision détectée, on annule le déplacement
        //         personnage1.setPositionY(oldY);
        //         playerHitbox.setPosition(personnage1.getPositionX()+hitboxX, personnage1.getPositionY()+hitboxY);
        //         xp = (int) Math.floor(porteHitboxes.get(i).x/TILE_SIZE);
        //         yp = (int) Math.floor(porteHitboxes.get(i).y/TILE_SIZE);
        //         map[map.length - 1 - yp][xp] = 20;
        //         break;
        //     }
        // }

        // Mise à jour du dash
        if (isDashing) {
            currentDashTime += avance;
            if (currentDashTime >= dashDuration) {
                isDashing = false;
            }
        }

        // Activer/désactiver l'affichage des hitboxes avec la touche H
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            if (debugger != null) {
                // Activer le debug explicitement si ce n'est pas déjà fait
                if (!debugger.isDebugEnabled()) {
                    debugger.toggleDebug();
                }
                
                // Puis basculer l'affichage des hitboxes
                debugger.toggleHitboxDisplay();
                System.out.println("État hitboxes après toggle: " + debugger.isShowingHitboxes());
            }
        }

        if (tempsitem > tempscoffre) {
                tempscoffre += avance;
            } else {
                tempscoffre = 0;
        }

    }

    private void logic() {
        // Calculer le centre du personnage pour un meilleur suivi
        float playerCenterX = personnage1.getPositionX() + ((largeur_skin * scalePlayer) / 2);
        float playerCenterY = personnage1.getPositionY() + ((hauteur_skin * scalePlayer) / 2);
        
        // Utiliser une interpolation linéaire pour des mouvements plus fluides
        float lerpFactor = 0.1f; // Ajustez entre 0.01 (très lent) et 0.2 (très rapide)
        
        camera.position.x += (playerCenterX - camera.position.x) * lerpFactor;
        camera.position.y += (playerCenterY - camera.position.y) * lerpFactor;


        
        // Mettre à jour le sbire avec la position correcte du joueur SEULEMENT s'il est en vie
        for (int i =0 ; i < sbiretest.size(); i++) {
            if (sbiretest.get(i).enVie()) {

                boolean estVisible = isEntityOnScreen(sbiretest.get(i), 200);
                if (estVisible) {
                    float oldX = sbiretest.get(i).getPositionX();
                    float oldY = sbiretest.get(i).getPositionY();
                    sbiretest.get(i).agir(Gdx.graphics.getDeltaTime(), projectiles);
                    gererCollisionsEntite(
                        sbiretest.get(i), oldX, oldY,
                        mursHitboxes, porteHitboxes,
                        hitboxX, hitboxY,
                        map, TILE_SIZE);
                } else {
                    sbiretest.get(i).updateDamageEffect(Gdx.graphics.getDeltaTime());
                }

                
            } else {
                sbiretest.remove(sbiretest.get(i));
            }
        }

        // if (sbiretest != null && sbiretest.enVie()) {
        //     sbiretest.agir(Gdx.graphics.getDeltaTime(), projectiles);
        // }

        // sbireBoss.agir(Gdx.graphics.getDeltaTime(), projectiles);

        // Calculer les dimensions effectives de la vue caméra
        cameraHalfWidth = camera.viewportWidth / 2;
        cameraHalfHeight = camera.viewportHeight / 2;
        
        // Calculer les limites réelles de la map en pixels
        float mapWidthPixels = map[0].length * TILE_SIZE;
        float mapHeightPixels = map.length * TILE_SIZE;

        // Limiter la caméra pour qu'elle ne sorte pas de la map
        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, mapWidthPixels - cameraHalfWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, mapHeightPixels - cameraHalfHeight);
        
        //sbireTest.agir(Gdx.graphics.getDeltaTime(), projectiles);

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
        camera.update();
        // Mettre à jour le gestionnaire de boules de feu
        if (fireballManager != null) {
            fireballManager.update(Gdx.graphics.getDeltaTime());
        }

        for (Coffre coffre : coffres) {
            if (!coffre.estOuvert() && coffre.getHitbox().overlaps(personnage1.getHitbox())) {
                coffre.setOuvert(true);
                coffre.setvientouvrir(true);
                coffre.setTexture(coffreTextureouvert);
                Item item = coffre.estOuvert() ? coffre.getDatabase().getItemAlea() : null;
                if (item != null) {
                    personnage1.getInventaire().addItem(item);
                    itemcoffre = item.getIcone();
                }
            }
        }
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
        porteHitboxes.clear();

         // Ne dessiner que les tuiles visibles
        for (int y = startY; y <= endY; y++) {
            int mapY = map.length - 1 - y;
            if (mapY < 0 || mapY >= map.length) continue;
            
            for (int x = startX; x <= endX; x++) {
                if (x < 0 || x >= map[0].length) continue;
                
                Texture texture = getTextureForTile(map[mapY][x]);
                if (texture == porteH || texture == porteV || texture == porteHOpen || texture == porteVOpen) {
                    game.batch.draw(solTexture, x * TILE_SIZE, y * TILE_SIZE);
                    if (texture == porteH){
                        //porteHitboxes.add(new Rectangle(x * TILE_SIZE  , y * TILE_SIZE+ 4, TILE_SIZE , TILE_SIZE-8));
                    }
                    else if (texture == porteV){
                        //porteHitboxes.add(new Rectangle(x * TILE_SIZE+2, y * TILE_SIZE, TILE_SIZE-12, TILE_SIZE));

                    }
                }else if (texture != null ) {
                    game.batch.draw(texture, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    if (texture == murTexture) {
                        //mursHitboxes.add(new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
                    }
                }
            }
        }

        
        for (Coffre coffre : coffres) {
            if (!coffre.estOuvert()) {
                game.batch.draw(solTexture, coffre.getHitbox().x, coffre.getHitbox().y, TILE_SIZE, TILE_SIZE);
                game.batch.draw(coffre.getTexture(), coffre.getHitbox().x, coffre.getHitbox().y, TILE_SIZE, TILE_SIZE);
            } else {
                game.batch.draw(solTexture, coffre.getHitbox().x, coffre.getHitbox().y, TILE_SIZE, TILE_SIZE);
                game.batch.draw(coffre.getTexture(), coffre.getHitbox().x, coffre.getHitbox().y, TILE_SIZE, TILE_SIZE); 
                if (coffre.vientouvrir()) {
                    if ((tempsitem > tempscoffre)) {
                        game.batch.draw(itemcoffre, coffre.getHitbox().x, coffre.getHitbox().y, TILE_SIZE, TILE_SIZE);
                    } else {
                        coffre.setvientouvrir(false);
                    }
                }
            }
        }
        for (int y = 0; y < map.length ; y++) {
            int mapY = map.length - 1 - y;
            if (mapY < 0 || mapY >= map.length) continue;
            for (int x = 0; x < map[0].length; x++) {
                if (x < 0 || x >= map[0].length) continue;

                Texture texture = getTextureForTile(map[mapY][x]);
                if (texture == porteH || texture == porteV || texture == porteHOpen || texture == porteVOpen) {
                    if (texture == porteH){
                        porteHitboxes.add(new Rectangle(x * TILE_SIZE  , y * TILE_SIZE+ 4, TILE_SIZE , TILE_SIZE-8));
                    }
                    else if (texture == porteV){
                        porteHitboxes.add(new Rectangle(x * TILE_SIZE+2, y * TILE_SIZE, TILE_SIZE-12, TILE_SIZE));

                    }
                }else if (texture != null ) {
                    if (texture == murTexture) {
                        mursHitboxes.add(new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE));
                    }
                }
            }
        }
        //porte
        for (int y = startY; y <= endY; y++) {
            int mapY = map.length - 1 - y;
            if (mapY < 0 || mapY >= map.length) continue;

            for (int x = startX; x <= endX; x++) {
                if (x < 0 || x >= map[0].length) continue;
                Texture texture = getTextureForTile(map[mapY][x]);
                if (texture == porteH || texture == porteV || texture == porteHOpen || texture == porteVOpen) {
                    game.batch.draw(texture, x * TILE_SIZE, y * TILE_SIZE);

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
        
        //Dessiner les projectiles
        for (Projectile projectile : projectiles) {
            // Récupérer les dimensions de la hitbox du projectile
            Rectangle hitbox = projectile.getHitbox();
            
            // Calculer le centre de la hitbox
            float centerX = hitbox.x + hitbox.width / 2;
            float centerY = hitbox.y + hitbox.height / 2;
            
            // Calculer les dimensions du sprite à dessiner
            float projWidth = scaledWidth / 2;
            float projHeight = scaledHeight / 2;
            
            // Dessiner le projectile en centrant le sprite sur la hitbox
            projectile.draw(
                game,
                projWidth, 
                projHeight,
                centerX - projWidth / 2,  // Ajuster la position X pour centrer
                centerY - projHeight / 2   // Ajuster la position Y pour centrer
            );
        }

        // Dessiner les sbires
        for (Sbire sbire : niveau.getSbires()) {
            if (sbire.enVie()) {
                // Récupérer la frame courante pour obtenir ses dimensions
                TextureRegion currentFrame2 = sbire.getCurrentFrame();
                
                // Calculer le ratio d'aspect de la texture actuelle
                float sbireAspectRatio = (float)currentFrame2.getRegionWidth() / (float)currentFrame2.getRegionHeight();
                
                // Définir des tailles différentes selon le type de sbire
                float sbireScaledHeight = hauteur_skin * scalePlayer;
                // Calculer la largeur en fonction du ratio d'aspect pour éviter l'étirement
                float sbireScaledWidth = sbireScaledHeight * sbireAspectRatio;
                if (sbire.isBoss()) {
                    // Le boss est 50% plus grand que les sbires normaux
                    sbireScaledHeight = hauteur_skin * scalePlayer * 1.5f;
                    sbireScaledWidth = sbireScaledHeight * sbireAspectRatio;

                    if (sbire.getLeurres() != null && sbire.getLeurres().size() > 0) {
                        // Parcourir la liste à l'envers pour pouvoir supprimer sans problème
                        for (int i = sbire.getLeurres().size() - 1; i >= 0; i--) {
                            Leurre leurre = sbire.getLeurres().get(i);
                            // Mettre à jour le leurre et vérifier s'il doit être supprimé
                            if (!leurre.update(Gdx.graphics.getDeltaTime())) {
                                sbire.getLeurres().remove(i);
                            } else {
                                // Dessiner le leurre uniquement s'il est encore actif
                                leurre.draw(game, sbireScaledWidth, sbireScaledHeight);
                            }
                        }
                    }
                } else {
                }

                // Sauvegarder la couleur originale du batch
                Color originalColor = new Color(game.batch.getColor());
                
                // Appliquer l'effet de flash blanc si actif
                if (sbire.isDamageEffectActive()) {
                    float intensity = sbire.getDamageEffectIntensity();
                    float flash = 1f + 0.5f * intensity; // Entre 1.5 et 1
                    game.batch.setColor(flash, flash, flash, 1f);
                }
                
                // Dessiner le sbire avec les dimensions adaptées
                // Utiliser la hitbox du sbire pour le positionnement
                game.batch.draw(
                    currentFrame2,
                    sbire.getHitbox().x - (sbireScaledWidth - sbire.getHitbox().width) / 2,
                    sbire.getHitbox().y - (sbireScaledHeight - sbire.getHitbox().height) / 2,
                    sbireScaledWidth,
                    sbireScaledHeight
                );
                
                // PUIS dessiner l'effet d'impact par-dessus le sbire
                if (sbire.getImpactEffect().isActive()) {
                    sbire.getImpactEffect().draw(game.batch);
                }
                
                // Restaurer la couleur originale
                game.batch.setColor(originalColor);
            }
        }
        
        // Sauvegarder la couleur originale du batch
        Color originalColor = new Color(game.batch.getColor());
        
        // Appliquer l'effet de dégât si actif
        if (damageEffect) {
            // Calculer l'intensité de l'effet (fade out)
            float intensity = 1 - (damageEffectTime / DAMAGE_EFFECT_DURATION);
            
            // Définir une couleur rouge avec alpha basé sur l'intensité
            game.batch.setColor(
                1,                                  // rouge à 100%
                originalColor.g * (1 - intensity),  // réduire le vert
                originalColor.b * (1 - intensity),  // réduire le bleu
                originalColor.a                     // conserver l'alpha original
            );
        }
    
        // Dessiner le joueur
        game.batch.draw(currentFrame, personnage1.getPositionX(), personnage1.getPositionY(), scaledWidth, scaledHeight);
        
        // Restaurer la couleur originale
        game.batch.setColor(originalColor);
        
        // Dessiner les boules de feu avant le personnage
        if (fireballManager != null) {
            fireballManager.render(game.batch);
        }
        // cooldown

        float progress = tempsDash / dashCooldown;
        float barWidth = 30;
        float barHeight = 5;
        float x = personnage1.getPositionX() - 3;
        float y = personnage1.getPositionY() - 8;

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

        // À la fin de la méthode draw(), après game.batch.end():

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
                } else if (map[mapY][mapX] == 500) {
                    game.batch.setColor(1f, 0.843f, 0f, 1f); // Or
                } else { // Sol
                    game.batch.setColor(0.7f, 0.7f, 0.7f, 1); // Gris clair
                }
                
                float tileX = minimapX + mapX * TILE_SIZE * scale;
                float tileY = minimapY + (map.length - 1 - mapY) * TILE_SIZE * scale;
                if (map[mapY][mapX] == 500) {
                    game.batch.draw(solTexture, tileX, tileY, TILE_SIZE * scale*4, TILE_SIZE * scale*4);
                } else {
                    game.batch.draw(solTexture, tileX, tileY, TILE_SIZE * scale, TILE_SIZE * scale);
                }
            }
        }

        // position du joueur sur minimap
        game.batch.setColor(1, 0, 0, 1); // Rouge
        float playerMinimapX = minimapX + personnage1.getPositionX() * scale;
        float playerMinimapY = minimapY + personnage1.getPositionY() * scale;
        game.batch.draw(solTexture, playerMinimapX, playerMinimapY, 3, 3);

        // Réinitialiser la couleur
        game.batch.setColor(1, 1, 1, 1);
        game.batch.end();


        // === DESSIN DES HITBOXES ===
        if (debugger != null && debugger.isShowingHitboxes()) {
            // Configuration du shape renderer
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            
            // Hitbox du joueur
            shapeRenderer.setColor(0, 1, 0, 1);  // Vert
            shapeRenderer.rect(personnage1.getPositionX() + hitboxX, personnage1.getPositionY() + hitboxY, 10, 10);
            
            // Hitboxes des sbires
            shapeRenderer.setColor(1, 0, 0, 1); // Rouge
            // for (Sbire sbire : niveau.getSbires()) {
            //     if (sbire.enVie()) {
            //         Rectangle hitbox = sbire.getHitbox();
            //         shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
            //     }
            // }

            for (int i = 0; i < sbiretest.size(); i++) {
                if (sbiretest.get(i).enVie()) {
                    Rectangle hitbox = sbiretest.get(i).getHitbox();
                    shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
                }
            }
            
            // Hitbox de l'attaque si active
            if (attackManager.isAttacking()) {
                shapeRenderer.setColor(0, 0, 1, 1);  // Bleu
                Rectangle attackZone = attackManager.getArmeMelee().getZoneAttaque();
                shapeRenderer.rect(attackZone.x, attackZone.y, attackZone.width, attackZone.height);
            }
            
            // Hitbox de l'attaque du sbire boss
            if (sbireBoss.isAttacking) {
                shapeRenderer.setColor(1, 0.5f, 0, 1); // Orange
                Rectangle attackZone = sbireBoss.getZoneAttaque();
                shapeRenderer.rect(attackZone.x, attackZone.y, attackZone.width, attackZone.height);
            }

            // for (Sbire sbire : niveau.getSbires()) {
            //     if (sbire.isAttacking) {
            //         shapeRenderer.setColor(1, 0.5f, 0, 1); // Orange
            //         Rectangle attackZone = sbire.getZoneAttaque();
            //         shapeRenderer.rect(attackZone.x, attackZone.y, attackZone.width, attackZone.height);
            //     }

            for (int i = 0; i < sbiretest.size(); i++) {
                if (sbiretest.get(i).isAttacking) {
                    shapeRenderer.setColor(1, 0.5f, 0, 1); // Orange
                    Rectangle attackZone = sbiretest.get(i).getZoneAttaque();
                    shapeRenderer.rect(attackZone.x, attackZone.y, attackZone.width, attackZone.height);
                }

            }
            // Hitboxes des projectiles
            shapeRenderer.setColor(1, 0, 1, 1); // Violet/Purple
            for (Projectile projectile : projectiles) {
                Rectangle hitbox = projectile.getHitbox();
                shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
                // for (int i = 0; i < mursHitboxes.size; i++) {
                //     if (hitbox.overlaps(mursHitboxes.get(i))) {
                //         //System.out.println("Collision avec un mur en X");
                //         projectiles.remove(i);
                //         break;
                //     }
                // }

                // for (int i = 0; i < mursHitboxes.size; i++) {
                //     if (hitbox.overlaps(mursHitboxes.get(i))) {
                //         projectiles.remove(i);
                //         break;
                //     }
                // }

            
                // Dessiner une ligne indiquant la direction du projectile
                Vector2 vitesse = projectile.getVitesse();
                if (vitesse != null && vitesse.len() > 0) {
                    Vector2 direction = new Vector2(vitesse).nor().scl(10); // Normaliser et mettre à l'échelle
                    shapeRenderer.line(
                        hitbox.x + hitbox.width/2, 
                        hitbox.y + hitbox.height/2,
                        hitbox.x + hitbox.width/2 + direction.x,
                        hitbox.y + hitbox.height/2 + direction.y
                    );
                }
            }
            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        
    mapTexture.dispose();
    skin.dispose();
    coeur_plein.dispose();
    bouclierIntact.dispose();
    Hercule_bas.dispose();
    Hercule_haut.dispose();
    Hercule_gauche.dispose();
    Hercule_droite.dispose();
    game.gameMusic.dispose();
    coffreTexture.dispose();

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
    
    // Libérer les ressources du gestionnaire d'attaque
    if (attackManager != null) {
        attackManager.dispose();
    }

    // Libérer les ressources des sbires
    // if (niveau != null && niveau.getSbires() != null) {
    //     for (Sbire sbire : niveau.getSbires()) {
    //         if (sbire != null) {
    //             sbire.dispose();
    //         }
    //     }
    // }

    for ( int i = 0; i < sbiretest.size(); i++) {
        if (sbiretest.get(i) != null) {
            sbiretest.get(i).dispose();
        }
    }
    if (fireballManager != null) {
        fireballManager.dispose();
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
        // Reprendre la musique du jeu quand on revient sur l'écran
        if (game.gameMusic != null && !game.gameMusic.isPlaying()) {
            game.gameMusic.play();
        }
    }

    @Override
    public void hide() {
        // Mettre en pause la musique du jeu quand on quitte l'écran
        if (game.gameMusic != null && game.gameMusic.isPlaying()) {
            game.gameMusic.pause();
        }
    }

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

    // Ajoutez cette méthode pour permettre l'accès à la caméra
    public OrthographicCamera getCamera() {
        return camera;
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
        } else if (value == 500) {
            return coffreTexture;
        }else if (value == 20) {
            return porteHOpen;
        }else if (value == 30) {
            return porteVOpen;
        }else {
            return solTexture; // 1 ou autre = sol
        }

    }

    private Vector2 getMouseDirection() {
        float characterCenterX = personnage1.getPositionX() + hitboxX + 5; // Centre approximatif
        float characterCenterY = personnage1.getPositionY() + hitboxY + 5;
        
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos); 
        
        // Calculer et normaliser la direction
        Vector2 direction = new Vector2(mousePos.x - characterCenterX, mousePos.y - characterCenterY);
        direction.nor(); // Normaliser pour avoir un vecteur unitaire
        
        return direction;
    }

    private void gererCollisionsEntite(
        Entite entite,
        float oldX, float oldY,
        Array<Rectangle> mursHitboxes,
        Array<Rectangle> porteHitboxes,
        float hitboxX, float hitboxY,
        int[][] map, int TILE_SIZE){
            Rectangle hitbox = entite.getHitbox();
            int xp, yp;

            // Test mur en X
            hitbox.setPosition(entite.getPositionX() + hitboxX, oldY + hitboxY);
            for (int i = 0; i < mursHitboxes.size; i++) {
                if (hitbox.overlaps(mursHitboxes.get(i))) {
                    //System.out.println("Collision avec un mur en X");
                    entite.setPositionX(oldX);
                    hitbox.setPosition(entite.getPositionX() + hitboxX, entite.getPositionY() + hitboxY);
                    break;
                }
            }

            // Test porte en X
            hitbox.setPosition(entite.getPositionX() + hitboxX, entite.getPositionY() + hitboxY);
            for (int i = 0; i < porteHitboxes.size; i++) {
                if (hitbox.overlaps(porteHitboxes.get(i))) {
                    entite.setPositionX(oldX);
                    hitbox.setPosition(oldY + hitboxX, oldY + hitboxY);
                    xp = (int) Math.floor(porteHitboxes.get(i).x / TILE_SIZE);
                    yp = (int) Math.floor(porteHitboxes.get(i).y / TILE_SIZE);
                    if (entite instanceof Personnage) {
                        map[map.length - 1 - yp][xp] = 30;
                    }
                    break;
                }
            }

            // Test mur en Y
            hitbox.setPosition(entite.getPositionX() + hitboxX, entite.getPositionY() + hitboxY);
            for (int i = 0; i < mursHitboxes.size; i++) {
                if (hitbox.overlaps(mursHitboxes.get(i))) {
                    entite.setPositionY(oldY);
                    hitbox.setPosition(entite.getPositionX() + hitboxX, entite.getPositionY() + hitboxY);
                    break;
                }
            }

            // Test porte en Y
            for (int i = 0; i < porteHitboxes.size; i++) {
                if (hitbox.overlaps(porteHitboxes.get(i))) {
                    entite.setPositionY(oldY);
                    hitbox.setPosition(entite.getPositionX() + hitboxX, entite.getPositionY() + hitboxY);
                    xp = (int) Math.floor(porteHitboxes.get(i).x / TILE_SIZE);
                    yp = (int) Math.floor(porteHitboxes.get(i).y / TILE_SIZE);
                    if (entite instanceof Personnage) {
                        map[map.length - 1 - yp][xp] = 20;
                    }
                    break;
                }
            }
    }

    // private void gererCollisionsProjectiles(
    //     Projectile projectile,
    //     float oldX, float oldY,
    //     Array<Rectangle> mursHitboxes,
    //     Array<Rectangle> porteHitboxes,
    //     float hitboxX, float hitboxY,
    //     int[][] map, int TILE_SIZE){
    //         Rectangle hitbox = projectile.getHitbox();
    //         int xp, yp;

    //         // Test mur en X
    //         hitbox.setPosition(projectile.getPosition().x + hitboxX, oldY + hitboxY);
    //         for (int i = 0; i < mursHitboxes.size; i++) {
    //             if (hitbox.overlaps(mursHitboxes.get(i))) {
    //                 //System.out.println("Collision avec un mur en X");
    //                 projectile.setPositionX(oldX);
    //                 System.out.println("detexte");;
    //                 hitbox.setPosition(projectile.getPosition().x + hitboxX, projectile.getPosition().y + hitboxY);
    //                 break;
    //             }
    //         }


    //         // Test mur en Y
    //         hitbox.setPosition(projectile.getPosition().x + hitboxX, projectile.getPosition().y + hitboxY);
    //         for (int i = 0; i < mursHitboxes.size; i++) {
    //             if (hitbox.overlaps(mursHitboxes.get(i))) {
    //                 projectile.setPositionY(oldY);
    //                 hitbox.setPosition(projectile.getPosition().x + hitboxX, projectile.getPosition().y + hitboxY);
    //                 break;
    //             }
    //         }
    // }

    public boolean voisinNotMur(int[][] map, int i, int j) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int k = 0; k < 4; k++) {
            int ni = i + dx[k];
            int nj = j + dy[k];

            if (ni >= 0 && ni < map.length && nj >= 0 && nj < map[0].length) {
                int val = map[ni][nj];
                if (val == 0 || val == 2 || val == 3 || val == 4) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isEntityOnScreen(Entite entite, float margin) {
        // Récupérer les dimensions de l'écran
        float screenLeft = camera.position.x - camera.viewportWidth / 2 - margin;
        float screenRight = camera.position.x + camera.viewportWidth / 2 + margin;
        float screenBottom = camera.position.y - camera.viewportHeight / 2 - margin;
        float screenTop = camera.position.y + camera.viewportHeight / 2 + margin;
        
        // Récupérer la hitbox de l'entité
        Rectangle hitbox = entite.getHitbox();
        
        // Vérifier si la hitbox intersecte avec l'écran
        return hitbox.x + hitbox.width >= screenLeft && 
            hitbox.x <= screenRight && 
            hitbox.y + hitbox.height >= screenBottom && 
            hitbox.y <= screenTop;
    }

    public void setArme(String nom, int nombre, int range) {
        this.armeMelee = new ArmeMelee(nom, nombre, range, range, nombre, "epee1.png", range);
        this.armeMelee.setNiveau(this.niveau);
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

}