import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class personnage implements entite {

    private String nom;

    @Override
    public getMana(){
        return this.mana;
    }

    @Override
    public getVie(){
        return this.vie;
    }

    @Override
    public getBouclier(){
        return this.bouclier;
    }
    
    // on perd d'abord en bouclier et ensuite en vide si on n'a plus de vie


    SpriteBatch batch;
    Texture bouclierIntact;
    Texture bouclierCasse;
    BitmapFont font;
    boolean etatbouclier = false;

    // on pourra créer deux images si le bouclier est cassé ou non
    @Override
    public void create() {
        batch = new SpriteBatch();
        bouclierIntact = new Texture("bouclier.png"); 
        bouclierCasse = new Texture("bouclier_casse.png");  
        font = new BitmapFont();
    }

    public void casserBouclier() {
        bouclierCasseState = true;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (bouclierCasseState) {
            batch.draw(bouclierCasse, 50, 50);
        } else {
            batch.draw(bouclierIntact, 50, 50);
        }
        batch.end();
    }

    public void prendreDegat(int degats) {
        int vieperdu = this.bouclier - degats;
        if (this.bouclier = 0) {
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
        if (personnage.enVie()) {
            this.vie -= degats;
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            font.draw(batch, "Game Over", 100, 150);
            batch.end();
        }
    }

    // Méthode pour vérifier l'état du personnage
    private void enVie() {
        if (this.getVie() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void attaquer(int manadepense) throws ManaInsuffisant{
        if (this.mana - manadepense > 0){
            this.mana-= manadepense;
        } else {
            throw new ManaInsuffisant();
        }
    }

    private SpriteBatch batch;
    private BitmapFont font;
    private boolean gameOver;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
