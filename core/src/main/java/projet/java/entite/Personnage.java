package entite;

//import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;


//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.Vector;
import java.util.Vector;


public class Personnage extends ApplicationAdapter implements Entite {

    private int vie;
    private int bouclier;
    private int mana;
    private String nom;
    private Texture skin;
    private Vector2 position; // = new Vector2(0f, 0f);
    private int mana_max;
    private int vie_max;

    private int bouclier_max;

    private boolean etatbouclier = false;
    private boolean dashOk = true;
    //timer pour savoir tous les combiens de temps il peut utiliser son dash, cooldown
    

    Timer timer;
    Timer timer2;
    Timer timer_mana;
    boolean prendre_des_degats = false;
    int decompte = 3;
    int decompte_bouclier = 6;

    // draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation)

    // on perd d'abord en bouclier et ensuite en vide si on n'a plus de vie
    // j'ajoute des obstacles
    List<Rectangle> obstacles;
    

    SpriteBatch batch;
    Texture bouclierIntact;
    Texture bouclierCasse;
    BitmapFont font;


    @Override
    public int getMana(){
        return this.mana;
    }

    @Override
    public int getVie(){
        return this.vie;
    }

    @Override
    public int getBouclier(){
        return this.bouclier;
    }
    
    @Override
    public Vector2 getPosition(){
        return this.position;
    }

    public Personnage(int mana, int vie, String nom) { //}, Texture skin, Vector2 position) {
        this.nom = nom;
        this.vie = vie;
        this.mana = mana;
        // this.skin = skin;
        this.mana_max = mana;
        this.vie_max = vie;
        this.bouclier_max = bouclier;
        // this.position = position;
    }
    

    // largeur et longueur

    private int largeur_coeur;
    private int hauteur_coeur;
    private int largeur_bouclier;
    private int hauteur_bouclier;
    private int largeur_dash;
    private int hauteur_dash;

    //texture 

    private Texture coeurplein;
    private Texture dash;
    private Texture dash_gris;
    private Texture obstacles_texture;
    
    
    // SpriteBatch batch2;
    // BitmapFont font2;

    private SpriteBatch batch2;
    private BitmapFont font2;
    private boolean gameOver =false;

    int largeur_ecran = Gdx.graphics.getWidth();
    int hauteur_ecran = Gdx.graphics.getHeight();
    int acceleration = 2;
    
    // on pourra créer deux images si le bouclier est cassé ou non
    //@Override
    public void create(SpriteBatch batch) {
        this.batch = batch;
        bouclierIntact = new Texture("bouclier.png"); 
        bouclierCasse = new Texture("bouclier_casse.png");  
        coeurplein = new Texture("coeur_plein.png");
        // sprint ou dash
        dash = new Texture("dash.png");
        largeur_coeur = coeur_plein.getWidth();
        hauteur_coeur = coeur_plein.getHeight();
        largeur_bouclier = bouclierIntact.getWidth();
        hauteur_bouclier = bouclierIntact.getHeight();
        //coeurvide = new Texture("coeur_vide.png");
        //mettre un boutton dash pour montrer quand il a de nouveau acces au dash, par exemple dans un coin le symbole de dash gris si il n'y a pas acces et en couleur sinon
        dash_gris = new Texture("dash_gris.png");
        largeur_dash = dash.getWidth();
        hauteur_dash = dash.getHeight();
        font = new BitmapFont();
        obstacles = new ArrayList<>();
        obstacles.add(new Rectangle(100, 100, 32, 32));
        obstacles.add(new Rectangle(200, 200, 64, 32));
        obstacles_texture = new Texture("texture_obstacles.png");

        batch2 = new SpriteBatch();
        font2 = new BitmapFont();
    
        

        timer = new Timer();
        timer2 = new Timer();
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
        }, 0, 1000);
    }
    

    //     decompte.scheduleAtFixedRate(new TimerTask() {
    //         @Override
    //         public void run() {
    //             if (timer == 0) {
    //                 decompte.cancel();
    //                 dashOk = true;
    //             } else {
    //                 dashOk = false;
    //                 timer--;
    //             }
    //         }
    //     } , 0, 1000); //afin d'executer toutes les secondes, 1000ms = 1s
    // public void casserBouclier() {
    //     etatbouclier = true;
    // }


    

    //@Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //regarder si la touche Z est pressée
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            System.out.println("La touche Z est pressée !, le personnage avance");
            this.getPosition().add(this.getPosition().x, this.getPosition().y + 0.5f );
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            System.out.println("La touche Q est pressée !, le personnage va vers la gauche");
            this.getPosition().add(this.getPosition().x -0.5f, this.getPosition().y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            System.out.println("La touche D est pressée !, le personnage va vers la droite");
            this.getPosition().add(this.getPosition().x +0.5f, this.getPosition().y );
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            System.out.println("La touche S est pressée !, le personnage va vers le bas");
            this.getPosition().add(this.getPosition().x, this.getPosition().y - 0.5f );
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println("La touche espace est pressée !, le personnage dash");
            this.getPosition().add(this.getPosition().x * acceleration, this.getPosition().y * acceleration);
        }

        int sourisX = Gdx.input.getX();
        int sourisY = Gdx.input.getY();

        batch.begin();

        // code si on veut afficher un bouclier sur le personnage
        // if (etatbouclier) {
        //     batch.draw(bouclierCasse, getPosition().x, getPosition().y);
        // } else {
        //     batch.draw(bouclierIntact, getPosition().x, getPosition().y);
        // }

        for (int i = 1; i <= this.getVie(); i++) {
            batch.draw(coeur_plein, largeur_coeur + i, hauteur_ecran - hauteur_coeur);
        }
        for (int i = 1; i <= this.getBouclier(); i++) {
            if (etatbouclier) {
                batch.draw(coeur_plein, largeur_bouclier + i, hauteur_ecran - hauteur_bouclier - 1 - hauteur_coeur);
            } else {
                batch.draw(coeur_plein, largeur_bouclier + i, hauteur_ecran - hauteur_bouclier - 1 - hauteur_coeur);
            }
        }

        if (dashOk) {
            batch.draw(dash, largeur_dash, hauteur_dash);
        } else {
            batch.draw(dash_gris, largeur_dash, hauteur_dash);
        }


        
        // dashEffect.setPosition(position.x, position.y);
        // dashEffect.setRotation(velocity.angleDeg()); // Faire pivoter l'effet pour suivre la direction
        // dashEffect.draw(batch);

        //batch.draw(dashEffectTexture, this.getPosition.x, position.y, largeur_dash / 2, hauteur_dash / 2, largeur_dash, hauteur_dash, 1, 1, rotation);
        

        // afficher le dash selon la direction, on fera une image du dash du haut vers le bas
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (dashOk) {
                if (Gdx.input.isKeyPressed(Input.Keys.Z) && Gdx.input.isKeyPressed(Input.Keys.D)){
                    batch.draw(dash, this.getPosition.x, position.y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 45);
                } else if (Gdx.input.isKeyPressed(Input.Keys.Z) && Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    batch.draw(dash, this.getPosition.x, position.y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -45);
                } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
                    batch.draw(dash, this.getPosition.x, position.y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 135);
                } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    batch.draw(dash, this.getPosition.x, position.y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -135);
                } else if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
                    batch.draw(dash, this.getPosition.x, position.y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 0);
                } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    batch.draw(dash, this.getPosition.x, position.y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 90);
                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    batch.draw(dash, this.getPosition.x, position.y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 180);
                } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    batch.draw(dash, this.getPosition.x, position.y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -90);
                }
                decompte = 3;
                dashOk = false;
                decompter();
            }
        }
        batch.draw(this.skin, this.getPosition().x, this.getPosition().y);
        batch.draw();

        batch.end();
    }

    public void prendreDegat(int degats) {
        int vieperdu = this.bouclier - degats;
        if (this.bouclier == 0) {
            perteVie(degats);
        } else if (vieperdu > 0) {
            this.bouclier -= degats;
        } else {
            this.bouclier = 0;
            // mettre une animation pour montrer que le bouclier casse
            casserBouclier();
            perteVie(vieperdu);
        }
    }

    // Méthode pour enlever des points de vies et afficher "Game over" lorsque le personnage meurt
    public void perteVie(int degats) {
        if (this.enVie()) {
            this.vie -= degats;
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            font.draw(batch2, "Game Over", 100, 150);
            batch.end();
        }
    }

    // Méthode pour vérifier l'état du personnage
    public boolean enVie() {
        if (this.getVie() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //Methode pour récupérer le bouclier

    public void recupBouclier() {
        decompte_bouclier = this.bouclier_max - this.bouclier + 3;
        if (this.bouclier < this.bouclier_max) {
            if (!prendre_des_degats) {
                timer2.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            decompte_bouclier--;
                            if (decompte_bouclier < 0) {
                                timer2.cancel();
                            }
                            if (decompte_bouclier == 3) {
                                this.bouclier = this.getBouclier() + 1;
                            }
                        }
                    });
                }
                }, 0, 500);
            }
           
        }
    }

    public void attaquer(int manadepense) throws ManaInsuffisant{
        if (this.mana - manadepense > 0){
            this.mana-= manadepense;
        } else {
            throw new ManaInsuffisant();
        }
    }

   

    

    public void dispose() {
        batch2.dispose();
        font2.dispose();
        bouclierIntact.dispose();
        bouclierCasse.dispose();
        dash.dispose();
        dash_gris.dispose();

    }


}
