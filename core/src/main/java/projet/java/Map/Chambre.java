package projet.java.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Chambre {
    private int[][] grille;
    private int numero;
    private int[] initial;
    private int[] taille;
    private int[] spawn;

    public Chambre(int numero,int[] initial,int[] taille) {
        this.numero = numero;
        this.initial = initial;
        this.taille = taille;
        this.grille = new int[this.taille[0]][this.taille[1]];
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                this.grille[i][j] = 0;
            }
        }
    }
    public Chambre(int numero,int[] initial,int[] taille, int[][] grille) {
        this.numero = numero;
        this.initial = initial;
        this.taille = taille;
        this.grille = grille;
    }

    public int[] getSpawn() {
        System.out.println("spawn4" + spawn[0] + " " + spawn[1]);
        return spawn;
    }
    public void setSpawn(int x, int y) {
        this.spawn = new int[]{x, y};
        System.out.println("spawn3");
    }

    public int[] getDimension(){
        return this.taille;
    }

    public int getNumero(){
        return this.numero;
    }

    public int[] getInitial(){
        return this.initial;
    }

    public int estVoisin(int x, int y) {
        int voisin = 0;
        if (x > 0 && this.grille[x - 1][y] == 1) voisin++;
        if (x < this.taille[0] - 1 && this.grille[x + 1][y] == 1) voisin++;
        if (y > 0 && this.grille[x][y - 1] == 1) voisin++;
        if (y < this.taille[1] - 1 && this.grille[x][y + 1] == 1) voisin++;
        return voisin;
    }

    public void solisation(int x, int y) {
        this.grille[x][y] = 1;
    }

    public void solisation3(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = x + i;
                int newY = y + j;
                // Vérification pour éviter les dépassements hors de la grille
                if (newX >= 0 && newX < this.taille[0] && newY >= 0 && newY < this.taille[1]) {
                    this.grille[newX][newY] = 1;
                }
            }
        }
    }

    public void solisation5(int x, int y) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                int newX = x + i;
                int newY = y + j;
                // Vérification pour éviter les dépassements hors de la grille
                if (newX >= 0 && newX < this.taille[0] && newY >= 0 && newY < this.taille[1]) {
                    this.grille[newX][newY] = 1;
                }
            }
        }
    }



    public void afficherGrille() {
        for  (int i = 0; i < this.taille[0]+1; i++) {
            System.out.print("--");
        }
        System.out.println();
        for  (int i = 0; i < this.taille[0]; i++) {
            System.out.print("|");
            for (int j = 0; j < this.taille[1]; j++) {
                System.out.print(this.grille[i][j] == 1 ? "  " : "# ");
            }
            System.out.print("|");
            System.out.println();
        }
        for  (int i = 0; i < this.taille[0]+1; i++) {
            System.out.print("--");
        }
    }

    public void trouAleatoire() {
        float pbInit = 0.20f;
        for  (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                float pb = ThreadLocalRandom.current().nextFloat(0.0f, 1.0f);
                if (pb < pbInit * (this.estVoisin(i,j)+1.0f)) {
                    this.grille[i][j] = 1;
                }
            }
        }
    }

    public void marcheurAleatoire(int maxpas,int xStart, int yStart) {
        Random rand = new Random();
        int[] coordonnees = new int[2];
        coordonnees[0] = xStart;
        coordonnees[1] = yStart;

        //int nbPas = rand.nextInt(maxpas);
        for (int i = 0; i < maxpas; i++) {
            float rd = ThreadLocalRandom.current().nextFloat(0.0f, 1.0f);
            if (rd < 0.25 && coordonnees[0] >0){
                coordonnees[0] --;
                solisation3(coordonnees[0],coordonnees[1]);
            }
            else if (rd < 0.50 && coordonnees[1] < this.taille[1]-1) {
                coordonnees[1] ++;
                solisation3(coordonnees[0],coordonnees[1]);
            }
            else if (rd < 0.75 && coordonnees[1] > 0 ) {
                coordonnees[1] --;
                solisation3(coordonnees[0],coordonnees[1]);
            }
            else if (coordonnees[0] < this.taille[0]-1){
                coordonnees[0] ++;
                solisation3(coordonnees[0],coordonnees[1]);
            }
        }
    }

    public void megaMarcheurAleatoire(int maxpas,int xStart, int yStart) {
        Random rand = new Random();
        int[] coordonnees = new int[2];
        coordonnees[0] = xStart;
        coordonnees[1] = yStart;

        //int nbPas = rand.nextInt(maxpas);
        for (int i = 0; i < maxpas; i++) {
            float rd = ThreadLocalRandom.current().nextFloat(0.0f, 1.0f);
            if (rd < 0.25 && coordonnees[0] >0){
                coordonnees[0] --;
                solisation5(coordonnees[0],coordonnees[1]);
            }
            else if (rd < 0.50 && coordonnees[1] < this.taille[1]-1) {
                coordonnees[1] ++;
                solisation5(coordonnees[0],coordonnees[1]);
            }
            else if (rd < 0.75 && coordonnees[1] > 0 ) {
                coordonnees[1] --;
                solisation5(coordonnees[0],coordonnees[1]);
            }
            else if (coordonnees[0] < this.taille[0]-1){
                coordonnees[0] ++;
                solisation5(coordonnees[0],coordonnees[1]);
            }
        }
    }

    public void smoother(int rang){
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                if (this.estVoisin(i,j)>rang) {
                    this.grille[i][j] = 1;
                }
            }
        }
    }

    public void multimarcheurAleatoire(int nbMarcheurs) {
        Random rand = new Random();
        for (int i = 0; i < nbMarcheurs; i++) {
            int pas = ThreadLocalRandom.current().nextInt(this.taille[0] + (int) Math.ceil((0.5)*this.taille[0]), this.taille[0]*3);
            int xSpawnM = this.taille[0]/10;
            int ySpawnM = this.taille[1]/10;
            int xSpawn = ThreadLocalRandom.current().nextInt(this.taille[0]/2-xSpawnM, this.taille[0]/2+xSpawnM);
            int ySpawn = ThreadLocalRandom.current().nextInt(this.taille[1]/2-ySpawnM, this.taille[1]/2+ySpawnM);
            if (this.taille[0] < 100) {
                this.marcheurAleatoire(pas, xSpawn, ySpawn);
            } else {
                this.megaMarcheurAleatoire(pas,xSpawn, ySpawn);
            }
        }
    }

    public void multiSmoother( int nbSmoothing){
        for (int i = 0; i < nbSmoothing; i++) {

            this.smoother(3-i);
            this.smoother(3-i);
        }
    }

    public Chambre reducteur(){
        int minX = this.taille[0]-1;
        int minY = this.taille[1]-1;
        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                if (this.grille[i][j] == 1) {
                    if (i > maxX ){
                        maxX = i;
                    }
                    if (i < minX ) {
                        minX = i;
                    }
                    if (j > maxY ){
                        maxY = j;
                    }
                    if (j < minY ) {
                        minY = j;
                    }
                }
            }
        }
        int[][] newtableau = new int[maxX-minX+2][maxY-minY+2];
        for (int i = minX-1; i < maxX; i++) {
            for (int j = minY-1; j < maxY; j++) {
                int inew = i - minX +1 ;
                int jnew = j - minY +1 ;
                if (this.grille[i][j] == 1) {
                    newtableau[inew][jnew] = 1;
                }
                else {
                    newtableau[inew][jnew] = 0;
                }
            }
        }
        int[] taille = new int[2];
        taille[0] = maxX-minX+2;
        taille[1] = maxY-minY+2;
        Chambre newChambre = new Chambre(this.numero, this.initial, taille,newtableau);
        newChambre.afficherGrille();
        return newChambre;


    }

    public int[][] getGrille() {
        return grille;
    }

    public void setInitial(int[] initial) {
        this.initial = initial;
    }
    public void setInitial2(int x,int y){
        this.initial[0] = x;
        this.initial[1] = y;
    }

    public int[] getTaille() {
        return taille;
    }

    public void specialRonde(int spawn){
        int[] centre = new int[2];
        centre[0] = (int) Math.ceil(this.taille[0]*0.5);
        centre[1] = (int) Math.ceil(this.taille[1]*0.5);
        Random rand = new Random();
        int rayonMax = Math.min(Math.min(centre[0], this.taille[0] - centre[0]), Math.min(centre[1], this.taille[1] - centre[1]));
        int rayonMin = Math.max(1, (int) Math.ceil(0.2* rayonMax)); // ou ajuste 0.3 à ton goût
        int rayonsous = Math.max(1, (int) Math.ceil(0.65* rayonMax)); // ou ajuste 0.3 à ton goût
        int rayon = rand.nextInt(rayonMax - rayonsous+ 1) +rayonMin;

        if (spawn == 1) {
            System.out.println("spawn2 " + centre[0] + " " + centre[1] );

            this.setSpawn(centre[0], centre[1]);

        }
        for (int i = 0; i < this.taille[0]; i++) {
            for (int j = 0; j < this.taille[1]; j++) {
                int dist = (int) Math.sqrt(Math.pow(i-centre[0],2)+Math.pow(j-centre[1],2));
                if (dist <=rayon) {
                    if (rayon > (int) Math.ceil(rayonMax*0.37)) {
                        if (dist != (int) Math.ceil(0.5*rayon) || i == centre[0] || j == centre[1]){
                            this.grille[i][j] = 1;
                        }
                    }
                    else {
                        this.grille[i][j] = 1;
                    }


                }
            }
        }
    }

    public void createur_chambre(boolean spawn) {
        Random rand = new Random();
        double alea = rand.nextDouble();
        if (alea < 0.15 || spawn) {
            if (spawn) {
                System.out.println("spawn1");
                this.specialRonde(1);

            }
            else {
                this.specialRonde(0);
            }
        }
        else {
            this.multimarcheurAleatoire(5);
            this.faciliteurDeCouloir();
            this.multiSmoother(2);
        }
    }

    public void faciliteurDeCouloir(){
        int minX = this.taille[0]-1;
        int minY = this.taille[1]-1;
        int maxX = 0;
        int maxY = 0;
        int centreX = (int) Math.ceil(this.taille[0]*0.5);
        int centreY = (int) Math.ceil(this.taille[1]*0.5);
        for (int i = 0; i < this.taille[0]; i++) {
            if (this.grille[i][centreY] == 1) {
                if (i > maxX ){
                    maxX = i;
                }
                if (i < minX ) {
                    minX = i;
                }

            }
        }
        for (int j = 0; j < this.taille[1]; j++) {
            if (this.grille[centreX][j] == 1) {
                if (j > maxY ){
                    maxY = j;
                }
                if (j < minY ) {
                    minY = j;
                }
            }
        }
        for (int i =minX; i < maxX+1; i++) {
            this.grille[i][centreY] = 1;
        }
        for (int i =minY; i < maxY+1; i++) {
            this.grille[centreX][i] = 1;
        }
    }
}