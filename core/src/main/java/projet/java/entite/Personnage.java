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

import projet.java.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.Vector;
import java.util.Vector;


public class Personnage extends ApplicationAdapter implements Entite {

    // caractéristique du personnage
    private int vie;
    private int bouclier;
    private int mana;
    private String nom;
    private Texture skin;
    private Vector2 position; // = new Vector2(0f, 0f);
    private int mana_max;
    private int vie_max;
    private int bouclier_max;
    

    // etat bouclier et dash personnage
    private boolean etatbouclier = false;
    private boolean dashOk = true;


    //timer pour savoir tous les combiens de temps il peut utiliser son dash, cooldown
    
    Timer timer;
    Timer timer2;
    
    // cooldown fash et combien de temps se remet le bouclier
    int decompte = 3;
    int decompte_bouclier = 6;

    boolean prendre_des_degats = false;
    boolean gameOver =false;
    int acceleration = 2;

    // draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation)

    // on perd d'abord en bouclier et ensuite en vide si on n'a plus de vie


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

    public Personnage(int mana, int vie, String nom, Texture skin, Vector2 position) {
        this.nom = nom;
        this.vie = vie;
        this.mana = mana;
        // this.skin = skin;
        this.mana_max = mana;
        this.vie_max = vie;
        this.bouclier_max = bouclier;
        this.position = Vector2(0f, 0f);
    }
    

    // largeur et longueur

    float largeur_coeur;
    float hauteur_coeur;
    float largeur_bouclier;
    float hauteur_bouclier;
    float largeur_dash;
    float hauteur_dash;

    float largeur_ecran = game.viewport.getWorldWidth();
    float hauteur_ecran = game.viewport.getWorldHeight();

    //texture 

    Texture coeurplein;
    Texture dash;
    Texture dash_gris;
    Texture bouclierIntact;
    
    // Texture bouclierCasse;    
    
    
    // on pourra créer deux images si le bouclier est cassé ou non
    //@Override
    public void create() {

        bouclierIntact = new Texture("bouclier.png"); 
        largeur_bouclier = bouclierIntact.getWidth();
        hauteur_bouclier = bouclierIntact.getHeight();
        
        // bouclierCasse = new Texture("bouclier_casse.png"); 
        // coeurvide = new Texture("coeur_vide.png");
        
        coeurplein = new Texture("coeur_plein.png");
        largeur_coeur = coeur_plein.getWidth();
        hauteur_coeur = coeur_plein.getHeight();

        // sprint ou dash   //mettre un boutton dash pour montrer quand il a de nouveau acces au dash, par exemple dans un coin le symbole de dash gris si il n'y a pas acces et en couleur sinon

        dash = new Texture("dash.png");
        dash_gris = new Texture("dash_gris.png");
        largeur_dash = dash.getWidth();
        hauteur_dash = dash.getHeight();
        
        
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
        }, 0, 1000); // 1000 ms = 1s
    }
    

    public void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            System.out.println("La touche Z est pressée !, le personnage avance");
            personnage1.getPosition().add(personnage1.getPosition().x, personnage1.getPosition().y + 0.5f );
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            System.out.println("La touche Q est pressée !, le personnage va vers la gauche");
            personnage1.getPosition().add(personnage1.getPosition().x -0.5f, personnage1.getPosition().y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            System.out.println("La touche D est pressée !, le personnage va vers la droite");
            personnage1.getPosition().add(personnage1.getPosition().x +0.5f, personnage1.getPosition().y );
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            System.out.println("La touche S est pressée !, le personnage va vers le bas");
            personnage1.getPosition().add(personnage1.getPosition().x, personnage1.getPosition().y - 0.5f );
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println("La touche espace est pressée !, le personnage dash");
            personnage1.getPosition().add(personnage1.getPosition().x * acceleration, personnage1.getPosition().y * acceleration);
        }

        int sourisX = Gdx.input.getX();
        int sourisY = Gdx.input.getY();

        // afficher le dash selon la direction, on fera une image du dash du haut vers le bas
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (dashOk) {
                if (Gdx.input.isKeyPressed(Input.Keys.Z) && Gdx.input.isKeyPressed(Input.Keys.D)){
                    game.batch.draw(dash, personnage1.getPosition().x, personnage1.getPosition().y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 45);
                } else if (Gdx.input.isKeyPressed(Input.Keys.Z) && Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    game.batch.draw(dash, personnage1.getPosition().x, personnage1.getPosition().y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -45);
                } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
                    game.batch.draw(dash, personnage1.getPosition().x, personnage1.getPosition().y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 135);
                } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    game.batch.draw(dash, personnage1.getPosition().x, personnage1.getPosition().y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -135);
                } else if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
                    game.batch.draw(dash, personnage1.getPosition().x, personnage1.getPosition().y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 0);
                } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    game.batch.draw(dash, personnage1.getPosition().x, personnage1.getPosition().y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 90);
                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    game.batch.draw(dash, personnage1.getPosition.x, personnage1.getPosition.y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, 180);
                } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                    game.batch.draw(dash, personnage1.getPosition().x, personnage1.getPosition().y, largeur_dash, hauteur_dash, largeur_dash, hauteur_dash, 1, 1, -90);
                }
                decompte = 3;
                dashOk = false;
                decompter();
            }
        }
    }
    

    public void draw(Main game) {
        game.batch.begin();
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

        game.batch.draw(skin, personnage1.getPosition().x, personnage1.getPosition().y);
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
            gameOver = true;
            // batch.begin();
            // font.draw(batch2, "Game Over", 100, 150);
            // batch.end();
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

    // public void attaquer(int manadepense) throws ManaInsuffisant{
    //     if (this.mana - manadepense > 0){
    //         this.mana-= manadepense;
    //     } else {
    //         throw new ManaInsuffisant();
    //     }
    // }

   

    

    public void dispose(Main game) {
        game.batch.end();
    }


}
