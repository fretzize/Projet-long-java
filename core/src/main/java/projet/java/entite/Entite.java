package projet.java.entite;

import projet.java.Main;

// import com.badlogic.gdx.math.Vector2;

public interface Entite {
    int getMana();
    int getBouclier();
    int getVie();
    float getPositionX();
    float getPositionY();

    void create_entite();
    void input_entite(float avance);
    void draw_entite(Main game);
    void dispose_entite(Main game);

    void setHaut(int haut);
    void setBas(int bas);
    void setDroite(int droite);
    void setGauche(int gauche);
    void setDash(int dash);

    int getHaut();
    int getBas();
    int getDroite();
    int getGauche();
    int getDash();

}