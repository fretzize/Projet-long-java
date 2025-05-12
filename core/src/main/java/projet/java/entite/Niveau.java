package projet.java.entite;

import com.badlogic.gdx.utils.Array;

/**
 * Classe représentant un niveau du jeu.
 * Gère les entités, les collisions, etc.
 */
public class Niveau {
    private Array<Sbire> sbires; // Liste des sbires actifs
    
    /**
     * Constructeur de niveau.
     */
    public Niveau() {
        sbires = new Array<>();
    }
    
    /**
     * @return Liste des sbires actifs dans le niveau
     */
    public Array<Sbire> getSbires() {
        return sbires;
    }
    
    /**
     * Ajoute un sbire au niveau.
     * 
     * @param sbire Sbire à ajouter
     */
    public void ajouterSbire(Sbire sbire) {
        sbires.add(sbire);
    }
    
    // Autres méthodes comme mise à jour, rendu, etc.
}