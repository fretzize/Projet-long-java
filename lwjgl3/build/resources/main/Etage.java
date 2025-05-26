package com.libgdx.roguelike;

import com.libgdx.roguelike.salles.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Etage {

    /** Nombre de directions : EST, OUEST, NORD, SUD. */
    private static final int NB_DIRECTIONS = 4;

    /** Bornes pour le nombre de salles dans l'étage. */
    private static final int BORNE_MIN = 8, BORNE_MAX = 10;

    /** Générateur aléatoire. */
    private static final Random GENERATEUR = new Random();

    /** Liste des salles présentes dans l'étage. */
    private ArrayList<Salle> sallesPresentes;

    /** Liste des salles disponibles dans l'étage.
     * <p>Clé : le nom de la salle</p>
     * <p>Valeur : le nombre restant maximal de placement pour la salle</p>
     */
    private HashMap<String, Integer> sallesDisponibles;

    /** Liste des noms des salles présentes dans l'étage. */
    private ArrayList<String> nomSalles;

    /** Numéro de l'étage. */
    private final int numero;

    /** Construire un étage à partir d'un numéro d'étage.
     * @param numero numéro de l'étage
     */
    public Etage(int numero, Carte carte) {
        this.sallesDisponibles = new HashMap<>();
        sallesDisponibles.put("MONSTRE", 15);

        this.nomSalles = new ArrayList<>(sallesDisponibles.keySet());

        this.sallesPresentes = new ArrayList<>();
        this.numero = numero;
    }


    /** Tirer une salle aléatoirement.
     * @return une salle parmis toutes celles existantes
     */
    private Salle tirerSalle(Position position) {
        int nbAleatoire;
        String nomSalleTiree;
        Salle salleTiree = null;
        boolean salleEstTiree = false;

        while (!salleEstTiree) {
            nbAleatoire = GENERATEUR.nextInt(this.nomSalles.size());
            nomSalleTiree = this.nomSalles.get(nbAleatoire);

            if (this.estSalleDisponible(nomSalleTiree)) {
                switch (nomSalleTiree) {
                    case "MONSTRE":
                        salleTiree = new SalleMonstre(position);
                        this.actualiserDisponibilite(nomSalleTiree);
                        break;

                    default:
                        throw new RuntimeException("Etage erreur : salle inexistante");
                }
                salleEstTiree = true;
            }
        }
        return salleTiree;
    }

    /** Vérifie la disponibilité d'une salle.
     * @param salle la salle dont la disponibilite est à vérifier
     * @return Vrai si la salle est disponible, Faux sinon
     */
    private boolean estSalleDisponible(String salle) {
        return this.sallesDisponibles.get(salle) > 0;
    }

    /** Actualiser la disponibilité d'une salle.
     * @param salle la salle
     */
    private void actualiserDisponibilite(String salle) {
        int ancienNbDispo = this.sallesDisponibles.get(salle);
        this.sallesDisponibles.put(salle, ancienNbDispo - 1);
    }

    /** Tirer un emplacement aléatoirement
     * représenté par deux entiers correpondant aux coordonnées
     * du coin supérieur gauche de la salle.
     * @return l'emplacement tiré aléatoirement
     */
    private Case tirerCase() {
        int abscisse = GENERATEUR.nextInt(Case.MAX_X + 1);
        int ordonnee = GENERATEUR.nextInt(Case.MAX_Y + 1);
        return new Case(abscisse, ordonnee);
    }

    /** Génère l'étage de manière procédurale. */
    public void generer() {
        int nbSallesMax = BORNE_MIN + GENERATEUR.nextInt(BORNE_MAX - BORNE_MIN + 1);
        int entierAleatoire;

        List<Position> listePositions = new ArrayList<>();

        // Placer la première Salle

        // Choisir une case de départ
        Case caseDepart = tirerCase();
        System.out.println("Case de départ aux coordonnées : " + caseDepart);

        // Construire la salle de départ
        this.sallesPresentes.add(new SalleDepart(caseDepart));

        // Choisir la case suivante
        // PEUT ETRE A PLACER DANS UNE FONCTION PRIVATE ET STATIQUE DE LA CLASSE
        // POUR ALLEGER UN PEU LE CODE
        Case caseSuivante = null;
        boolean choixCaseSuivante = false;
        while (!choixCaseSuivante) {
            entierAleatoire = GENERATEUR.nextInt(NB_DIRECTIONS);
            switch (entierAleatoire) {
                case 0 :
                    // EST
                    caseSuivante = new Case(caseDepart.x + 1, caseDepart.y);
                    break;
                case 1 :
                    // OUEST
                    caseSuivante = new Case(caseDepart.x - 1, caseDepart.y);
                    break;
                case 2 :
                    // NORD
                    caseSuivante = new Case(caseDepart.x, caseDepart.y + 1);
                    break;
                case 3 :
                    // SUD
                    caseSuivante = new Case(caseDepart.x, caseDepart.y - 1);
                    break;
            }
            if (caseSuivante.estValide(this.sallesPresentes)) {
                choixCaseSuivante = true;
            }
        }

        // Placer la porte de la salle de Départ
        SalleDepart salleDepart = (SalleDepart) this.sallesPresentes.get(0);
        salleDepart.construirePorte(caseSuivante);

        // Initialiser la première position
        listePositions.add(new Position(caseSuivante, caseDepart));

        Position positionTiree;
        Salle salleTiree;
        while (this.sallesPresentes.size() < (nbSallesMax - 1)) {
            // Tirer position dans la SD (Structure de Donnée)
            entierAleatoire = GENERATEUR.nextInt(listePositions.size());
            positionTiree = listePositions.get(entierAleatoire);

            // Choisir une nouvelle salle
            salleTiree = tirerSalle(positionTiree);

            // Ajouter la salleTiree à l'étage
            this.sallesPresentes.add(salleTiree);

            // Relier la salle tirée à la case source
            // La salle tirée est directement reliée à la case source
            // grâce à son constructeur

            // Choisir des nouvelles positions
            Position nouvellePosition;
            Case caseCourante = salleTiree.getCase();
            for (int i = 0; i < Salle.NB_PORTES; i++) {
                switch (i) {
                case 0 :
                    caseSuivante = new Case(caseCourante.x + 1, caseCourante.y);
                    break;
                case 1 :
                    caseSuivante = new Case(caseCourante.x - 1, caseCourante.y);
                    break;
                case 2 :
                    caseSuivante = new Case(caseCourante.x, caseCourante.y + 1);
                    break;
                case 3 :
                    caseSuivante = new Case(caseCourante.x, caseCourante.y - 1);
                    break;
                }
                if (caseSuivante.estValide(this.sallesPresentes)) {
                    nouvellePosition = new Position(caseSuivante, salleTiree.getCase());
                    listePositions.add(nouvellePosition);
                }
            }
        }

        // Placer la salle étage suivant
        // À FAIRE
    }
}
