package projet.java.entite;

import com.badlogic.gdx.utils.Array;

/**
 * Classe représentant un niveau du jeu.
 * Gère les entités, les collisions, etc.
 */
public class Niveau {
    private Array<Ennemi> ennemis; // Liste des ennemis actifs
    
    /**
     * Constructeur de niveau.
     */
    public Niveau() {
        ennemis = new Array<>();
    }
    
    /**
     * @return Liste des ennemis actifs dans le niveau
     */
    public Array<Ennemi> getEnnemis() {
        return ennemis;
    }
    
    /**
     * Ajoute un ennemi au niveau.
     * 
     * @param ennemi Ennemi à ajouter
     */
    public void ajouterEnnemi(Ennemi ennemi) {
        ennemis.add(ennemi);
    }
    
    // Autres méthodes comme mise à jour, rendu, etc.
}