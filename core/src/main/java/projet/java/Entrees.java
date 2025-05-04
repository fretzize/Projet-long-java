package projet.java;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

import java.util.*;

/**
 * Classe qui permet de gérer les entrées (modifications, ajout, suppression)
 */
public class Entrees {
    /**
     * Actions possibles.
     */
    private final static String[] actionsPossibles = {"HAUT", "DROITE", "BAS", "GAUCHE",
            "DASH", "RETOUR", "SELECTIONNER"};

    /**
     * Touches par défaut.
     */
    private final int haut1 = Input.Keys.W,
            haut2 = Input.Keys.UP,
            droite1 = Input.Keys.D,
            droite2 = Input.Keys.RIGHT,
            bas1 = Input.Keys.S,
            bas2 = Input.Keys.DOWN,
            gauche1 = Input.Keys.A,
            gauche2 = Input.Keys.LEFT,
            dash = Input.Keys.SPACE,
            retour = Input.Keys.ESCAPE,
            selectionner1 = Input.Keys.ENTER,
            selectionner2 = Input.Keys.SPACE;

    private Map<String, Set<Integer>> mapEntrees;

    public Entrees() {
        // Initialistion des entrées
        mapEntrees = new HashMap<>();

        for (String action : actionsPossibles) {
            mapEntrees.put(action, new HashSet<>());
        }

        // Remplissage de la map
        mapEntrees.get("HAUT").add(haut1);
        mapEntrees.get("HAUT").add(haut2);
        mapEntrees.get("DROITE").add(droite1);
        mapEntrees.get("DROITE").add(droite2);
        mapEntrees.get("BAS").add(bas1);
        mapEntrees.get("BAS").add(bas2);
        mapEntrees.get("GAUCHE").add(gauche1);
        mapEntrees.get("GAUCHE").add(gauche2);
        mapEntrees.get("DASH").add(dash);
        mapEntrees.get("RETOUR").add(retour);
        mapEntrees.get("SELECTIONNER").add(selectionner1);
        mapEntrees.get("SELECTIONNER").add(selectionner2);
    }

    /**
     * Ajouter une entrée et l'action qui lui est liée.
     * <p>
     * Crée l'action si elle n'existe pas. Ne fait rien si le lien existe déjà.
     * L'action n'est pas dépedante de la case (Haut == HAUT)
     *
     * @param action Identifiant de l'action
     * @param entree Touche du clavier
     */
    public void ajouterEntree(String action, int entree) {
        String actionMaj = action.toUpperCase(); // Mettre l'action en majuscules pour éviter les erreurs

        // Ajouter l'action si elle n'existe pas
        mapEntrees.putIfAbsent(actionMaj, new HashSet<>());

        // Ajouter l'entrée liée à l'action
        mapEntrees.get(actionMaj).add(entree);
    }

    /**
     * Supprimer une entrée liée à l'action précisée.
     * <p>
     * Si une telle entrée n'existe pas, ne fait rien.
     * L'action n'est pas dépedante de la case (Haut == HAUT)
     *
     * @param action Identifiant de l'action
     * @param entree Touche du clavier
     */
    public void supprimerEntree(String action, int entree) {
        String actionMaj = action.toUpperCase(); // Mettre l'action en majuscules pour éviter les erreurs

        mapEntrees.get(actionMaj).remove(entree);
    }

    /**
     * Changer une l'entrée d'une action en une autre.
     * <p>
     * Si l'entrée à changer n'existe pas, la fonction est équivalente à ajouter.
     * Si l'entrée par laquelle on change existe déjà, ne fait rien.
     * <p>
     * L'action n'est pas dépedante de la case (Haut == HAUT)
     *
     * @param action      Identifiant de l'action
     * @param entreeAvant Touche du clavier à changer
     * @param entreeApres Touche par laquelle remplacer
     */
    public void modifierEntree(String action, int entreeAvant, int entreeApres) {
        String actionMaj = action.toUpperCase();

        if (mapEntrees.containsKey(actionMaj)) {
            mapEntrees.get(actionMaj).remove(entreeAvant);
            mapEntrees.get(actionMaj).add(entreeApres);
        }
    }

    /** Indique si une action est choisie.
     *
     * L'action n'est pas dépedante de la case (Haut == HAUT)
     *
     * @param action Action dont on vérifie si elle est choisie ou non.
     * @return Vrai si l'action est selectionnée
     */
    public boolean actionEstChoisie(String action) {
        String actionMaj = action.toUpperCase();

        for (int touche : mapEntrees.get(actionMaj)) {
            if (Gdx.input.isKeyPressed(touche)) {
                return true;
            }
        }

        return false;
    }
}
