package projet.java.entite;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import projet.java.Main;

import java.util.Timer;
import projet.java.Inventaire.Inventaire;
import java.util.TimerTask;


public class Personnage extends ApplicationAdapter implements Entite {

    // caractéristique du personnage
    private int vie;
    private int bouclier;
    private int mana;
    private String nom;
    private Texture skin;
    private float positionX;
    private float positionY;
    // private Vector2 position; // = new Vector2(0f, 0f);
    private int mana_max;
    private int vie_max;
    private int bouclier_max;
    private Arme arme;
    private Inventaire inventaire = new Inventaire();

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
    int acceleration = 2000;

    // draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation)

    // les quatres touches pour bouger sont dans personnage

    private int touche_haut = 51;
    private int touche_bas = 47;
    private int touche_droite = 32;
    private int touche_gauche = 29;
    private int touche_dash = 62;

    @Override
    public void setHaut(int haut) {
        this.touche_haut = haut;
    }

    @Override
    public void setBas(int bas) {
        this.touche_bas = bas;
    }

    @Override
    public void setDroite(int droite) {
        this.touche_droite = droite;
    }

    @Override
    public void setGauche(int gauche) {
        this.touche_gauche = gauche;
    }

    @Override
    public void setDash(int dash) {
        this.touche_dash = dash;
    }

    @Override
    public int getHaut() {
        return this.touche_haut;
    }

    @Override
    public int getBas() {
        return this.touche_bas;
    }

    @Override
    public int getDroite() {
        return this.touche_droite;
    }

    @Override
    public int getGauche() {
        return this.touche_gauche;
    }

    @Override
    public int getDash() {
        return this.touche_dash;
    }

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
    public float getPositionX(){
        return this.positionX;
    }

    @Override
    public float getPositionY(){
        return this.positionY;
    }
    public Inventaire getInventaire(){
            return this.inventaire;
    }
    public Personnage(int mana, int vie, int bouclier, String nom, Texture skin) {//}, Vector2 position) {
        this.nom = nom;
        this.vie = vie;
        this.mana = mana;
        // this.skin = skin;
        this.mana_max = mana;
        this.vie_max = vie;
        this.bouclier = bouclier;
        this.bouclier_max = bouclier;
        this.positionY = 0;
        this.positionX = 0;
    }
    


    //texture 

    private Texture coeur_plein;
    private Texture dash_texture;
    private TextureRegion dash;
    private Texture dash_gris;
    private float largeur_dash;
    private float hauteur_dash;
    private Texture bouclierIntact;
    
    // Texture bouclierCasse;    
    
    
    // on pourra créer deux images si le bouclier est cassé ou non
    @Override
    public void create_entite() {

        // bouclierIntact = new Texture("bouclier.png"); 
        // largeur_bouclier = bouclierIntact.getWidth();
        // hauteur_bouclier = bouclierIntact.getHeight();
        
        // bouclierCasse = new Texture("bouclier_casse.png"); 
        // coeurvide = new Texture("coeur_vide.png");
        
        // coeur_plein = new Texture("coeur_plein.png");
        // largeur_coeur = coeur_plein.getWidth();
        // hauteur_coeur = coeur_plein.getHeight();

        dash_texture = new Texture("dash.png");
        dash = new TextureRegion(dash_texture);
        dash_gris = new Texture("dash_gris.png");
        largeur_dash = dash_texture.getWidth();
        hauteur_dash = dash_texture.getHeight();
        
        
        timer = new Timer();
        timer2 = new Timer();
    }

    // public void decompter() {
    //     timer.scheduleAtFixedRate(new TimerTask() {
    //         @Override
    //         public void run() {
    //             Gdx.app.postRunnable(new Runnable() {
    //                 @Override
    //                 public void run() {
    //                     decompte--;
    //                     if (decompte < 0) {
    //                         timer.cancel();
    //                         dashOk = true;
    //                     }
    //                 }
    //             });
    //         }
    //     }, 0, 1000); // 1000 ms = 1s
    // }
    
    @Override
    public void input_entite(float avance) {
        
        // if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        //     // System.out.println("La touche espace est pressée !, le personnage dash");
        //     // this.getPosition().add(this.getPositionX() * acceleration, this.getPositionY() * acceleration);
        //     this.positionY += this.getPositionY()*avance;
        // }

        int sourisX = Gdx.input.getX();
        int sourisY = Gdx.input.getY();

        
    }
    
    @Override
    public void draw_entite(Main game) {
        float largeur_ecran = 2000;//game.viewport.getWorldWidth();
        float hauteur_ecran = 2000;//game.viewport.getWorldHeight();
        // game.batch.begin();
        // for (int i = 0; i < this.vie; i++) {
        //     game.batch.draw(coeur_plein, i*largeur_coeur +10, hauteur_ecran - hauteur_coeur);
        // }
        // for (int i = 0; i < this.bouclier; i++) {
        //     game.batch.draw(bouclierIntact, i*largeur_bouclier +10, hauteur_ecran - hauteur_bouclier - 1 - hauteur_coeur);
        // }

        // if (dashOk) {
        //     game.batch.draw(dash, largeur_dash, hauteur_dash);
        // } else {
        //     game.batch.draw(dash_gris, largeur_dash, hauteur_dash);
        // }

        // game.batch.draw(skin, this.getPositionX(), this.getPositionY());


        

        // game.batch.end();

    }

    

    public void prendreDegat(int degats) {
        int vieperdu = this.getBouclier() - degats;
        if (this.getBouclier() == 0) {
            perteVie(degats);
        } else if (vieperdu > 0) {
            this.bouclier -= degats;
        } else {
            this.bouclier = 0;
            // mettre une animation pour montrer que le bouclier casse
            // casserBouclier();
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
        decompte_bouclier = this.bouclier_max - this.getBouclier() + 3;
        if (this.getBouclier() < this.bouclier_max) {
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
                            if (decompte_bouclier == bouclier_max - bouclier) {
                                bouclier++;
                            }
                        }
                    });
                }
                }, 0, 1000);
            }
           
        }
    }


    // parametre ooldown

    // private float dashDuration = 0.2f;
    // private float dashCooldown = 2f;
    // private float cooldownTimer = 0f;

    // public void update(float deltaTime, boolean dashOk, float tempsDash) {

    //     // Mettre à jour le timer du dash
    //     if (!dashOk) {
    //         tempsDash += deltaTime;
    //         // if (tempsDash >= dashDuration) {
    //         //     dashTimer = 0; // Réinitialiser le timer du dash
    //         //     isDashing = false; // Fin du dash
    //         // cooldownTimer = dashCooldown; // Démarrer le cooldown
    //         // }
    //     }

    //     // Mettre à jour le timer du cooldown
    //     if (tempsDash > dashCooldown) {
    //         dashOk = true;
    //     }
    // }

    // public void attaquer(int manadepense) throws ManaInsuffisant{
    //     if (this.mana - manadepense > 0){
    //         this.mana-= manadepense;
    //     } else {
    //         throw new ManaInsuffisant();
    //     }
    // }

   

    
    @Override
    public void dispose_entite(Main game) {
        coeur_plein.dispose();
        // dash_texture.dispose();
        // dash_gris.dispose();
        bouclierIntact.dispose();
        game.batch.end();
    }


}
