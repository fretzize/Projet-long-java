package projet.java.Map;

import java.util.*;

public class Map {
    private int[][] coord;
    private int[][] mini_coord;
    private String[] usage;
    private int[] taille;
    private int[] tailleInitChambres;
    private Chambre[] chambres;
    private int nbChambre;
    private boolean[][] couloir;
    private int[] coordspawn;
    private int[] coordChBoss;
    private Chambre salleBoss;
    private int nombre_porte_boss;
    private boolean couloirboss = true;
    // int position_boss = 3;

    public Map(int nb_chambre, int[] taille_chambre) {
        // Créer les chambres
        this.usage = new String[]{"Mur", "Sol", "porteH", "porteV", "VideS"};
        this.nbChambre = nb_chambre;
        this.tailleInitChambres = taille_chambre;
        this.chambres = new Chambre[nbChambre];
        int[] initial = {0, 0};
        int[] coordSpawn = {0, 0};
        boolean spawn = true;
        boolean boss = false;
        for (int i = 0; i < nbChambre; i++) {
            // Initialisation correcte des chambres

            if (i == nbChambre -1) {
                boss = true;
            }
            Chambre chambre = new Chambre(i, initial.clone(), taille_chambre.clone());
            chambre.createur_chambre(spawn, boss);
            
            if (spawn){
                this.coordspawn = chambre.getSpawn();
            }
            
            if (i == 0){
                spawn = false;
            }
            if (boss) {
                // this.coordChBoss = chambre.getSpawn();
                // this.coordChBoss[0] = this.coordChBoss[0] + initial[0];
                // this.coordChBoss[1] = this.coordChBoss[0] + initial[1];
                chambre.setChambreBoss(true);
                this.salleBoss = chambre;
                boss = false;
                // coord[initial[0] + i][initial[1] + j] = 450;
            }

            

            this.chambres[i] = chambre; // Stocke la chambre dans le tableau
        }

        // Créer la map
        this.taille = new int[]{taille_chambre[0] * nb_chambre, taille_chambre[1] * nb_chambre};
        this.coord = new int[this.taille[0]][this.taille[1]];
        this.mini_coord =new int[nb_chambre][nb_chambre];
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                this.coord[i][j] = 0;
            }
        }
        int[] coordChBoss = {0, 0};
        this.coordChBoss = coordChBoss;
        this.nombre_porte_boss = 0;
    }

    
    public int getnombrechambreBoss()  {
        return this.nombre_porte_boss;
    }

    public void setSpawn(int x, int y) {
        coordspawn[0] = x;
        coordspawn[1] = y;
    }

    public Map(int[] taille, int[][] coord,int[][] mini_coord, int[] spawn) {
        this.taille = taille;
        this.coord = coord;
        this.mini_coord = mini_coord;
        this.coordspawn = spawn;
    }

    public void placerChambresGrille() {
        int compteur_ligne = 0;
        int compteur_collone = 0;
        int max_colllone = 0;
        int max_ligne =0;
        for (int i = 0; i < nbChambre; i++) {
            int x_init = compteur_collone * tailleInitChambres[0];
            int y_init = compteur_ligne * tailleInitChambres[1];

            mini_coord[compteur_collone][compteur_ligne] = i +1;
            //algo de placement
            if (compteur_collone == 0){
                max_colllone ++;
                compteur_collone = max_colllone;
                max_ligne ++;
                compteur_ligne = 0;
            } else if (compteur_ligne != max_ligne) {
                compteur_ligne ++;
            } else {
                compteur_collone --;
            }

            this.chambres[i].setInitial2(x_init, y_init);
            int[][] grille = chambres[i].getGrille();
            for (int a = 0; a < tailleInitChambres[0] && a + x_init < this.taille[0]; a++) {
                for (int b = 0; b < tailleInitChambres[1] && b + y_init < this.taille[1]; b++) {
                    if (grille[a][b] == 1) {
                        coord[a + x_init][b + y_init] = 1;
                    } else {
                        coord[a + x_init][b + y_init] = 0;
                    }
                }
            }
        }
    }

    public int getCoordspawnX() {
        return coordspawn[0];
    }
    public int getCoordspawnY() {
        return coordspawn[1];
    }

    public int getChBossX() {
        return coordChBoss[0];
    }

    public int getChBossY() {
        return coordChBoss[1];
    }

    public Chambre getSalleBoss() {
        return this.salleBoss;
    }


    public void corridor_creator() {
        this.couloir = new boolean[nbChambre][nbChambre];
        Random rand = new Random();

        // Trouver les positions de chaque chambre dans mini_coord
        int[][] chambrePositions = new int[nbChambre][2];
        for (int i = 0; i < mini_coord.length; i++) {
            for (int j = 0; j < mini_coord[0].length; j++) {
                if (mini_coord[i][j] > 0 && mini_coord[i][j] <= nbChambre) {
                    int chambreId = mini_coord[i][j] - 1; // Convertir de 1-base à 0-base
                    chambrePositions[chambreId][0] = i;
                    chambrePositions[chambreId][1] = j;
                }
            }
        }

        // Algorithme de génération d'arbre couvrant (pour assurer la connectivité)
        Set<Integer> chambresConnectees = new HashSet<>();
        Set<Integer> chambresNonConnectees = new HashSet<>();

        // Initialiser les ensembles
        for (int i = 0; i < nbChambre; i++) {
            chambresNonConnectees.add(i);
        }

        // Commencer avec une chambre aléatoire
        int premiereChambre = rand.nextInt(nbChambre);
        chambresConnectees.add(premiereChambre);
        chambresNonConnectees.remove(premiereChambre);

        // Continuer jusqu'à ce que toutes les chambres soient connectées
        while (!chambresNonConnectees.isEmpty()) {
            int chambreA = -1;
            int chambreB = -1;

            // Trouver une paire de chambres à connecter (une connectée, une non connectée)
            for (int a : chambresConnectees) {
                int xa = chambrePositions[a][0];
                int ya = chambrePositions[a][1];

                for (int b : chambresNonConnectees) {
                    int xb = chambrePositions[b][0];
                    int yb = chambrePositions[b][1];

                    // Vérifier si elles sont adjacentes (non diagonales)
                    if ((Math.abs(xa - xb) == 1 && ya == yb) || (xa == xb && Math.abs(ya - yb) == 1)) {
                        chambreA = a;
                        chambreB = b;
                        break;
                    }
                }
                if (chambreA != -1) break;
            }

            // Si on n'a pas trouvé de chambres adjacentes, prendre une paire quelconque
            if (chambreA == -1) {
                chambreA = chambresConnectees.iterator().next();
                chambreB = chambresNonConnectees.iterator().next();
            }

            // Créer le couloir
            couloir[chambreA][chambreB] = true;
            couloir[chambreB][chambreA] = true; // Symétrie

            // Mettre à jour les ensembles
            chambresConnectees.add(chambreB);
            chambresNonConnectees.remove(chambreB);
        }

        // Ajouter des connexions supplémentaires entre chambres adjacentes avec une certaine probabilité
        for (int i = 0; i < nbChambre; i++) {
            int xi = chambrePositions[i][0];
            int yi = chambrePositions[i][1];

            for (int j = i + 1; j < nbChambre; j++) {
                // Si elles ne sont pas déjà connectées
                if (!couloir[i][j]) {
                    int xj = chambrePositions[j][0];
                    int yj = chambrePositions[j][1];

                    // Vérifier si elles sont adjacentes (non diagonales)
                    if ((Math.abs(xi - xj) == 1 && yi == yj) || (xi == xj && Math.abs(yi - yj) == 1)) {
                        // Ajouter une connexion avec une probabilité de 30%
                        if (rand.nextDouble() < 0.3) {
                            couloir[i][j] = true;
                            couloir[j][i] = true;
                        }
                    }
                }
            }
        }
    }



    public void creuser_couloir() {
        // Pour chaque paire de chambres qui doivent être connectées
        for (int i = 0; i < nbChambre; i++) {
            for (int j = i + 1; j < nbChambre; j++) { // i+1 pour éviter de traiter deux fois la même paire
                if (couloir[i][j]) {
                    // Marquer comme traitée pour éviter la duplication
                    couloir[i][j] = false;
                    couloir[j][i] = false;

                    // Trouver les coordonnées des centres des chambres
                    int x_i = -1, y_i = -1, x_j = -1, y_j = -1;

                    // Chercher les positions des chambres dans mini_coord
                    for (int a = 0; a < mini_coord.length; a++) {
                        for (int b = 0; b < mini_coord[0].length; b++) {
                            if (mini_coord[a][b] == i + 1) {
                                // Calculer le centre de la chambre i
                                x_i = a * tailleInitChambres[0] + tailleInitChambres[0] / 2;
                                y_i = b * tailleInitChambres[1] + tailleInitChambres[1] / 2;
                                if (i == nbChambre - 1) {
                                    coordChBoss[0] = x_i;
                                    coordChBoss[1] = y_i;
                                }
                            }
                            else if (mini_coord[a][b] == j + 1) {
                                // Calculer le centre de la chambre j
                                x_j = a * tailleInitChambres[0] + tailleInitChambres[0] / 2;
                                y_j = b * tailleInitChambres[1] + tailleInitChambres[1] / 2;
                                if (j == nbChambre - 1) {
                                    coordChBoss[0] = x_j;
                                    coordChBoss[1] = y_j;
                                }
                            }
                        }
                    }

                    // Vérifier que les deux chambres ont été trouvées
                    if (x_i < 0 || y_i < 0 || x_j < 0 || y_j < 0) {
                        System.out.println("Avertissement: Chambre non trouvée dans mini_coord");
                        continue;
                    }

                    // Tracer le couloir en deux étapes: d'abord horizontal, puis vertical
                    // Étape 1: Tracer horizontalement
                    int currentX = x_i;
                    int compteur_porte = 0;

                    if ((i != nbChambre-1) && (j != nbChambre -1)) {
                        while (currentX != x_j) {
                            if (currentX < x_j) {
                                currentX++;
                            } else {
                                currentX--;
                            }

                            // Vérifier que nous sommes dans les limites de la grille
                            if (currentX >= 0 && currentX < taille[0] && y_i >= 0 && y_i < taille[1]) {
                                // Si on rencontre un mur (0) et qu'on n'a pas encore créé de porte
                                if (coord[currentX][y_i] == 0 && compteur_porte == 0) {
                                    // Marquer comme porte horizontale (2)
                                    coord[currentX][y_i] = 2;
                                    compteur_porte++;
                                    if ((i==nbChambre-1) || (j==nbChambre-1)) {
                                        nombre_porte_boss ++;
                                    }

                                    // S'assurer que les cases adjacentes sont des murs
                                    if (y_i > 0) coord[currentX][y_i-1] = 0;
                                    if (y_i+1 < taille[1]) coord[currentX][y_i+1] = 0;
                                }
                                // Si on rencontre un mur (0) et qu'on a déjà créé une porte
                                // (on est dans un autre mur, probablement de la deuxième chambre)
                                else if (coord[currentX][y_i] == 0 && compteur_porte > 0) {
                                    // Marquer comme porte horizontale (2)
                                    coord[currentX][y_i] = 2;
                                    compteur_porte++;

                                    // S'assurer que les cases adjacentes sont des murs
                                    if (y_i > 0) coord[currentX][y_i-1] = 0;
                                    if (y_i+1 < taille[1]) coord[currentX][y_i+1] = 0;
                                }
                                // Si ce n'est pas un mur ou si on a déjà créé deux portes
                                else {
                                    // Marquer comme sol (1) pour le couloir
                                    coord[currentX][y_i] = 1;
                                }
                            }
                        }

                        // Étape 2: Tracer verticalement
                        int currentY = y_i;
                        compteur_porte = 0; // Réinitialiser le compteur pour la partie verticale

                        while (currentY != y_j) {
                            if (currentY < y_j) {
                                currentY++;
                            } else {
                                currentY--;
                            }

                            // Vérifier que nous sommes dans les limites de la grille
                            if (x_j >= 0 && x_j < taille[0] && currentY >= 0 && currentY < taille[1]) {
                                // Si on rencontre un mur (0) et qu'on n'a pas encore créé de porte
                                if (coord[x_j][currentY] == 0 && compteur_porte == 0) {
                                    // Marquer comme porte verticale (3)
                                    coord[x_j][currentY] = 3;
                                    compteur_porte++;

                                    // S'assurer que les cases adjacentes sont des murs
                                    if (x_j > 0) coord[x_j-1][currentY] = 0;
                                    if (x_j+1 < taille[0]) coord[x_j+1][currentY] = 0;
                                }
                                // Si on rencontre un mur (0) et qu'on a déjà créé une porte
                                else if (coord[x_j][currentY] == 0 && compteur_porte > 0) {
                                    // Marquer comme porte verticale (3)
                                    coord[x_j][currentY] = 3;
                                    compteur_porte++;

                                    // S'assurer que les cases adjacentes sont des murs
                                    if (x_j > 0) coord[x_j-1][currentY] = 0;
                                    if (x_j+1 < taille[0]) coord[x_j+1][currentY] = 0;
                                }
                                // Si ce n'est pas un mur ou si on a déjà créé deux portes
                                else {
                                    // Marquer comme sol (1) pour le couloir
                                    coord[x_j][currentY] = 1;
                                }
                            }
                        }
                        } else if (couloirboss) {
                            if ((i == nbChambre-1) || (j == nbChambre -1)) {
                                while (currentX != x_j) {
                                if (currentX < x_j) {
                                    currentX++;
                                } else {
                                    currentX--;
                                }

                                // Vérifier que nous sommes dans les limites de la grille
                                if (currentX >= 0 && currentX < taille[0] && y_i >= 0 && y_i < taille[1]) {
                                    // Si on rencontre un mur (0) et qu'on n'a pas encore créé de porte
                                    if (coord[currentX][y_i] == 0 && compteur_porte == 0) {
                                        // Marquer comme porte horizontale (2)
                                        coord[currentX][y_i] = 2;
                                        compteur_porte++;
                                        if ((i==nbChambre-1) || (j==nbChambre-1)) {
                                            nombre_porte_boss ++;
                                        }

                                        // S'assurer que les cases adjacentes sont des murs
                                        if (y_i > 0) coord[currentX][y_i-1] = 0;
                                        if (y_i+1 < taille[1]) coord[currentX][y_i+1] = 0;
                                    }
                                    // Si on rencontre un mur (0) et qu'on a déjà créé une porte
                                    // (on est dans un autre mur, probablement de la deuxième chambre)
                                    else if (coord[currentX][y_i] == 0 && compteur_porte > 0) {
                                        // Marquer comme porte horizontale (2)
                                        coord[currentX][y_i] = 2;
                                        compteur_porte++;

                                        // S'assurer que les cases adjacentes sont des murs
                                        if (y_i > 0) coord[currentX][y_i-1] = 0;
                                        if (y_i+1 < taille[1]) coord[currentX][y_i+1] = 0;
                                    }
                                    // Si ce n'est pas un mur ou si on a déjà créé deux portes
                                    else {
                                        // Marquer comme sol (1) pour le couloir
                                        coord[currentX][y_i] = 1;
                                    }
                                }
                            }

                            // Étape 2: Tracer verticalement
                            int currentY = y_i;
                            compteur_porte = 0; // Réinitialiser le compteur pour la partie verticale

                            while (currentY != y_j) {
                                if (currentY < y_j) {
                                    currentY++;
                                } else {
                                    currentY--;
                                }

                                // Vérifier que nous sommes dans les limites de la grille
                                if (x_j >= 0 && x_j < taille[0] && currentY >= 0 && currentY < taille[1]) {
                                    // Si on rencontre un mur (0) et qu'on n'a pas encore créé de porte
                                    if (coord[x_j][currentY] == 0 && compteur_porte == 0) {
                                        // Marquer comme porte verticale (3)
                                        coord[x_j][currentY] = 3;
                                        compteur_porte++;

                                        // S'assurer que les cases adjacentes sont des murs
                                        if (x_j > 0) coord[x_j-1][currentY] = 0;
                                        if (x_j+1 < taille[0]) coord[x_j+1][currentY] = 0;
                                    }
                                    // Si on rencontre un mur (0) et qu'on a déjà créé une porte
                                    else if (coord[x_j][currentY] == 0 && compteur_porte > 0) {
                                        // Marquer comme porte verticale (3)
                                        coord[x_j][currentY] = 3;
                                        compteur_porte++;

                                        // S'assurer que les cases adjacentes sont des murs
                                        if (x_j > 0) coord[x_j-1][currentY] = 0;
                                        if (x_j+1 < taille[0]) coord[x_j+1][currentY] = 0;
                                    }
                                    // Si ce n'est pas un mur ou si on a déjà créé deux portes
                                    else {
                                        // Marquer comme sol (1) pour le couloir
                                        coord[x_j][currentY] = 1;
                                    }
                                }
                            }
                            }
                            couloirboss = false;
                        }
                    
                }
            }
        }
        this.nettoyerPortes();
    }

    private void nettoyerPortes() {
        // Nettoyer les portes horizontales (2)
        for (int j = 0; j < taille[1]; j++) {
            // Trouver des séquences de portes horizontales
            for (int i = 0; i < taille[0]; i++) {
                if (coord[i][j] == 2) {
                    int debut = i;
                    while (i+1 < taille[0] && coord[i+1][j] == 2) {
                        i++;
                    }
                    int fin = i;

                    // Si on a plus de 2 portes consécutives
                    if (fin - debut > 1) {
                        // Garder seulement la première et la dernière porte
                        for (int k = debut + 1; k < fin; k++) {
                            coord[k][j] = 1; // Transformer en sol
                        }
                    }
                }
            }
        }

        // Nettoyer les portes verticales (3)
        for (int i = 0; i < taille[0]; i++) {
            // Trouver des séquences de portes verticales
            for (int j = 0; j < taille[1]; j++) {
                if (coord[i][j] == 3) {
                    int debut = j;
                    while (j+1 < taille[1] && coord[i][j+1] == 3) {
                        j++;
                    }
                    int fin = j;

                    // Si on a plus de 2 portes consécutives
                    if (fin - debut > 1) {
                        // Garder seulement la première et la dernière porte
                        for (int k = debut + 1; k < fin; k++) {
                            coord[i][k] = 1; // Transformer en sol
                        }
                    }
                }
            }
        }
    }

    public void afficherCouloirs() {
        System.out.println("Matrice des couloirs (true = couloir entre les chambres) :");
        for (int i = 0; i < nbChambre; i++) {
            for (int j = 0; j < nbChambre; j++) {
                System.out.print(couloir[i][j] ? "1 " : "0 ");
            }
            System.out.println();
        }
    }


    public void afficherMap() {
        for  (int i = 0; i < this.taille[0]+1; i++) {
            System.out.print("--");
        }
        System.out.println();
        for  (int i = 0; i < this.taille[0]; i++) {
            System.out.print("|");
            for (int j = 0; j < this.taille[1]; j++) {
                if (coord[i][j]==2) {
                    System.out.print("$ ");
                }else if (coord[i][j]==3) {
                    System.out.print("€ ");

                } else if (coord[i][j]==4) {
                    System.out.print("  ");
                } else if (coord[i][j]==500) {  // Nouveau : affichage des coffres
                    System.out.print("C ");
                } else if (coord[i][j]==450) {
                    System.out.print("Spawn ");
                }
                else {
                    System.out.print(this.coord[i][j] == 1 ? "  " : "# ");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        for  (int i = 0; i < this.taille[0]+1; i++) {
            System.out.print("--");
        }

        
    }

    public void afficherMapTest() {
        for  (int i = 0; i < this.taille[0]+1; i++) {
            System.out.print("--");
        }
        System.out.println();
        for  (int i = 0; i < this.taille[0]; i++) {
            System.out.print("|");
            for (int j = 0; j < this.taille[1]; j++) {
                System.out.print(coord[i][j] +" ");
            }
            System.out.print("|");
            System.out.println();
        }
        for  (int i = 0; i < this.taille[0]+1; i++) {
            System.out.print("--");
        }
    }

    public Map reducteur() {
        // Trouver les limites extrêmes des sols (valeur 1)
        int minX = this.taille[0] - 1;
        int minY = this.taille[1] - 1;
        int maxX = 0;
        int maxY = 0;

        // Trouver les coordonnées minimales et maximales où il y a des sols
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                if (this.coord[i][j] == 1) {
                    if (i > maxX) {
                        maxX = i;
                    }
                    if (i < minX) {
                        minX = i;
                    }
                    if (j > maxY) {
                        maxY = j;
                    }
                    if (j < minY) {
                        minY = j;
                    }
                }
            }
        }

        // Ajouter un mur d'épaisseur 1 autour de la zone utile
        // On soustrait 1 aux minimums et on ajoute 1 aux maximums
        minX = Math.max(0, minX - 1);
        minY = Math.max(0, minY - 1);
        maxX = Math.min(this.taille[0] - 1, maxX + 1);
        maxY = Math.min(this.taille[1] - 1, maxY + 1);

        // Calculer les dimensions de la nouvelle carte
        int nouvelleLargeur = maxX - minX + 1;
        int nouvelleHauteur = maxY - minY + 1;

        // Créer la nouvelle matrice de coordonnées
        int[][] nouvelleCoord = new int[nouvelleLargeur][nouvelleHauteur];

        // Copier les valeurs de l'ancienne matrice vers la nouvelle
        for (int i = 0; i < nouvelleLargeur; i++) {
            for (int j = 0; j < nouvelleHauteur; j++) {
                int iOriginal = i + minX;
                int jOriginal = j + minY;
                if (this.coordspawn[0] == iOriginal && this.coordspawn[1] == jOriginal) {
                    this.coordspawn[0] = i;
                    this.coordspawn[1] = j;
                }
                // Copier la valeur de l'ancienne matrice
                nouvelleCoord[i][j] = this.coord[iOriginal][jOriginal];
            }
        }

        // Créer une nouvelle carte avec les dimensions réduites
        int[] nouvelleTaille = {nouvelleLargeur, nouvelleHauteur};
        this.reducteur_mini_coord();
        return new Map(nouvelleTaille, nouvelleCoord,this.mini_coord,this.coordspawn);
    }

    public void afficherMiniCoord() {
        System.out.println("Matrice mini_coord :");
        for (int i = 0; i < mini_coord.length; i++) {
            for (int j = 0; j < mini_coord[0].length; j++) {
                System.out.print(mini_coord[i][j] + "\t");
            }
            System.out.println();
        }
    }
    public void rotation90Horaire() {
        // Création d'une nouvelle matrice avec dimensions inversées
        int nouvelleLargeur = this.taille[1];  // La nouvelle largeur sera l'ancienne hauteur
        int nouvelleHauteur = this.taille[0]; // La nouvelle hauteur sera l'ancienne largeur
        int[][] nouvelleCoord = new int[nouvelleLargeur][nouvelleHauteur];

        // Effectuer la rotation de -90 degrés (sens horaire)
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                // Dans une rotation de -90 degrés:
                // La nouvelle x devient l'ancienne y
                // La nouvelle y devient (largeur - 1 - ancienne x)
                nouvelleCoord[j][this.taille[0] - 1 - i] = this.coord[i][j];
            }
        }

        // Mettre à jour la matrice et les dimensions de la map actuelle
        this.coord = nouvelleCoord;

        // Inverser les dimensions
        int temp = this.taille[0];
        this.taille[0] = this.taille[1];
        this.taille[1] = temp;
    }

    public void rotation90Trigo() {
        // Création d'une nouvelle matrice avec dimensions inversées
        int nouvelleLargeur = this.taille[1];  // La nouvelle largeur sera l'ancienne hauteur
        int nouvelleHauteur = this.taille[0]; // La nouvelle hauteur sera l'ancienne largeur
        int[][] nouvelleCoord = new int[nouvelleLargeur][nouvelleHauteur];

        // Effectuer la rotation de +90 degrés (sens trigonométrique/antihoraire)
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                // Dans une rotation de +90 degrés:
                // La nouvelle x devient (hauteur - 1 - ancienne y)
                // La nouvelle y devient l'ancienne x
                nouvelleCoord[this.taille[1] - 1 - j][i] = this.coord[i][j];
            }
        }

        // Mettre à jour la matrice et les dimensions de la map actuelle
        this.coord = nouvelleCoord;

        // Inverser les dimensions
        int temp = this.taille[0];
        this.taille[0] = this.taille[1];
        this.taille[1] = temp;
        this.rotation90Trigo_mini_coord();
    }

    public void creation_vide() {
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                if (this.coord[i][j] == 0) {
                    boolean estmur = false;

                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            if (di == 0 && dj == 0) continue;

                            int ni = i + di;
                            int nj = j + dj;
                            if (ni >= 0 && ni < this.taille[0] && nj >= 0 && nj < this.taille[1]) {
                                if (this.coord[ni][nj] == 1) {
                                    estmur = true;
                                    break;
                                }
                            }
                        }
                        if (estmur) break;
                    }
                    if (!estmur) {
                        this.coord[i][j] = 4;
                    }
                }
            }
        }
    }

    public void rotation90Trigo_mini_coord() {
        int hauteur = this.mini_coord.length;
        int largeur = this.mini_coord[0].length;
        int[][] nouvelle_mini_Coord = new int[largeur][hauteur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                nouvelle_mini_Coord[largeur - 1 - j][i] = this.mini_coord[i][j];
            }
        }
        this.mini_coord = nouvelle_mini_Coord;
    }
    public void coupureCoord() {
        int nbColonne = this.mini_coord.length;
        int nbLigne = this.mini_coord[0].length;

        // Vérification des colonnes
        for (int i = 0; i < this.taille[0]; i++) {
            // Vérifier si i est toujours valide après des suppressions potentielles
            if (i >= this.taille[0]) break;

            int compteur = 0;
            int porte = 0;
            for (int j = 0; j < this.taille[1]; j++) {
                if (this.coord[i][j] == 1) {
                    compteur++;
                }
                if (this.coord[i][j] == 2 || this.coord[i][j] == 3) porte++;
            }

            if (compteur < nbLigne + 1 && porte == 0) {
                supColonne(i);
                // Retourner en arrière d'un indice car l'élément courant a été supprimé
                i--;
            }
        }

        // Vérification des lignes
        for (int j = 0; j < this.taille[1]; j++) {
            // Vérifier si j est toujours valide après des suppressions potentielles
            if (j >= this.taille[1]) break;

            int compteur = 0;
            int porte = 0;
            for (int i = 0; i < this.taille[0]; i++) {
                if (this.coord[i][j] == 1) {
                    compteur++;
                }
                if (this.coord[i][j] == 2 || this.coord[i][j] == 3) porte++;
            }

            if (compteur < nbColonne + 1 && porte == 0) {
                supLigne(j);
                // Retourner en arrière d'un indice car l'élément courant a été supprimé
                j--;
            }
        }

        //verificateur cadre
        for (int i = 0; i < this.taille[0]; i++) {
            if (this.coord[i][0] == 1) {
                this.coord[i][0] = 0;
            }
            if (this.coord[i][this.taille[1]-1] == 1) {
                this.coord[i][this.taille[1]-1] = 0;
            }
        }
        for (int i = 0; i < this.taille[1]; i++) {
            if (this.coord[0][i] == 1) {
                this.coord[0][i] = 0;
            }
            if (this.coord[this.taille[0]-1][i] == 1) {
                this.coord[this.taille[0]-1][i] = 0;
            }
        }
    }

    public void supColonne(int i) {
        // Vérifier que l'indice est valide
        if (i < this.coordspawn[0]) {
            this.coordspawn[0] --;
        }
        if (i < 0 || i >= this.taille[0]) return;

        int nouvelleLargeur = this.taille[0] - 1;
        int[][] newCoord = new int[nouvelleLargeur][this.taille[1]];

        // Copier toutes les colonnes sauf celle à supprimer
        for (int j = 0; j < this.taille[0]; j++) {
            if (j == i) continue; // Sauter la colonne à supprimer

            for (int k = 0; k < this.taille[1]; k++) {
                int nouvelIndiceJ = j < i ? j : j - 1;
                newCoord[nouvelIndiceJ][k] = this.coord[j][k];
            }
        }

        this.coord = newCoord;
        this.taille[0] = nouvelleLargeur;
    }

    public void supLigne(int i) {
        // Vérifier que l'indice est valide
        if (i < this.coordspawn[1]){
            this.coordspawn[1] --;
        }
        if (i < 0 || i >= this.taille[1]) return;

        int nouvelleHauteur = this.taille[1] - 1;
        int[][] newCoord = new int[this.taille[0]][nouvelleHauteur];

        // Copier toutes les lignes sauf celle à supprimer
        for (int k = 0; k < this.taille[1]; k++) {
            if (k == i) continue; // Sauter la ligne à supprimer

            for (int j = 0; j < this.taille[0]; j++) {
                int nouvelIndiceK = k < i ? k : k - 1;
                newCoord[j][nouvelIndiceK] = this.coord[j][k];
            }
        }

        this.coord = newCoord;
        this.taille[1] = nouvelleHauteur;
    }
    public void reducteur_mini_coord() {
        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < this.mini_coord.length; i++) {
            for (int j = 0; j < this.mini_coord[0].length; j++) {
                if (this.mini_coord[i][j] != 0) {
                    if (i > maxX) maxX = i;
                    if (j > maxY) maxY = j;
                }
            }
        }
        int[][] newmini_Coord = new int[maxX + 1][maxY + 1];
        for (int i = 0; i < newmini_Coord.length; i++) {
            for (int j = 0; j < newmini_Coord[0].length; j++) {
                newmini_Coord[i][j] = this.mini_coord[i][j];
            }
        }
        this.mini_coord = newmini_Coord;
    }

    public int[][] getCoord(){
        return this.coord;
    }

    public void randomiseur_sol() {
        Random rand = new Random();
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                if (this.coord[i][j] == 1) {
                    double aleatoire = rand.nextDouble();
                    if (aleatoire >= 0.12 && aleatoire < 0.20) {
                        this.coord[i][j] = 100;
                    }
                    if (aleatoire >= 0.20 && aleatoire < 0.28) {
                        this.coord[i][j] = 101;
                    }
                    if (aleatoire >= 0.28 && aleatoire < 0.36) {
                        this.coord[i][j] = 102;
                    }
                    if (aleatoire >= 0.36 && aleatoire < 0.44) {
                        this.coord[i][j] = 103;
                    }
                    if (aleatoire >= 0.44 && aleatoire < 0.52) {
                        this.coord[i][j] = 104;
                    }
                    if (aleatoire >= 0.52 && aleatoire < 0.6) {
                        this.coord[i][j] = 105;
                    }
                    if (aleatoire >= 0.6 && aleatoire < 0.68) {
                        this.coord[i][j] = 106;
                    }
                    if (aleatoire >= 0.68 && aleatoire < 0.76) {
                        this.coord[i][j] = 107;
                    }
                    if (aleatoire >= 0.76 && aleatoire < 0.84) {
                        this.coord[i][j] = 108;
                    }
                    if (aleatoire >= 0.84 && aleatoire < 0.92) {
                        this.coord[i][j] = 109;
                    }
                    if (aleatoire >= 0.92) {
                        this.coord[i][j] = 110;
                    }
                }
            }
        }
    }

    public void naturalisation_mur(){
        for (int i = 0; i < this.taille[0]-1; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                if (this.coord[i][j] == 0 && this.coord[i+1][j] == 0) {
                    this.coord[i][j] = 200;
                }
            }
        }
    }



   public int[] decoupage_spawn(){
        int X = 0 ;
        int Y = 0 ;
        boolean continuex = true;
        boolean continuey = true;
        int compteurX = this.taille[0]-1;
        int compteurY = 0;
        int compteurMinX = this.taille[0]-1;
        int compteurMaxY = 0;
        while (continuex || continuey) {
            System.out.println(compteurX + " " + compteurY + " " + this.coord[compteurX][compteurY] );
            if (this.coord[compteurX][compteurY] == 3 || this.coord[compteurX][compteurY] == 2) {
                X = compteurX;
                Y = compteurY;
                continuex = false;
                continuey = false;
            }
//            if (this.coord[compteurX][compteurY] == 2 && continuey) {
//                Y = compteurY;
//                continuey = false;
//            }

            if (compteurX == compteurMinX && compteurY == compteurMaxY) {
                compteurMaxY ++;
                compteurMinX --;
                compteurX = this.taille[0]-1;
                compteurY ++;
            }
            else if(compteurX > compteurMinX + 1) {
                compteurX --;
            }
            else if (compteurX == compteurMinX + 1) {
                compteurY = 0;
                compteurX = compteurMinX;
            }
            else if (compteurX == compteurMinX ){
                compteurY ++;
            }

        }


        int[] coord = new int[2
                ];
        coord[0] = X;
        coord[1] = Y;
        return coord;
   }

   public void testY(){
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                this.coord[i][j] = j;
            }
        }
   }
   public void testX(){
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                this.coord[i][j] = i;
            }
        }
   }


   public Chambre[] getChambres() {
    return this.chambres;
   }
   public void placerCoffresDansChambres() {
    for (Chambre chambre : this.chambres) {
        boolean coffrePlace = chambre.placerCoffreAvecProbabilite(0.6f);
        if (coffrePlace) {
            int[][] grille = chambre.getGrille();
            int[] initial = chambre.getInitial();
            for (int i = 0; i < grille.length; i++) {
                for (int j = 0; j < grille[0].length; j++) {
                    if (grille[i][j] == 500) {
                        coord[initial[0] + i][initial[1] + j] = 500;
                    }
                }
            }
        }
    }
    }


    public void placerSpawnBoss() {
    for (Chambre chambre : this.chambres) {
            int[][] grille = chambre.getGrille();
            int[] initial = chambre.getInitial();
            for (int i = 0; i < grille.length; i++) {
                for (int j = 0; j < grille[0].length; j++) {
                    if (grille[i][j] == 450) {
                        coord[initial[0] + i][initial[1] + j] = 450;
                    }
                }
            }
        }
    }

    public int compterCoffres() {
        int compteur = 0;
        for (int i = 0; i < coord.length; i++) {
            for (int j = 0; j < coord[0].length; j++) {
                if (coord[i][j] == 500) compteur++;
            }
        }
        return compteur;
    }

}