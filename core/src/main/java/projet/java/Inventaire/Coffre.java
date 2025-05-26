package projet.java.Inventaire;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import projet.java.Inventaire.DatabaseItem;
import projet.java.Inventaire.Item;

public class Coffre {
  
    private Rectangle hitbox_coffre = new Rectangle();
    private Item item;
    private Texture image_coffre;
    private float largeur_hit = 4;
    private float hauteur_hit = 3;
    private float positionX;
    private float positionY;
    private boolean Estouvert = false;
    private DatabaseItem databaseItem = new DatabaseItem();
    private boolean vientouvrir = false;
    // private Item itemcoffre;

    public Coffre(int x, int y, Item item, Rectangle hitbox, Texture image_coffre) {
        this.item = item;
        this.hitbox_coffre = hitbox;
        this.image_coffre = image_coffre;
    }

    public Texture getTexture() {
        return image_coffre;
    }

    public void setTexture(Texture image) {
        this.image_coffre = image;
    }

    public void setPositionX(float x){
        this.positionX = x;
        this.hitbox_coffre.setPosition(this.positionX, this.positionY);
    }
    public void setPositionY(float y){
        this.positionY = y;
        this.hitbox_coffre.setPosition(this.positionX, this.positionY);
    }

    public Rectangle getHitbox() {
        return this.hitbox_coffre;
    }

    public void setOuvert(boolean ouvert) {
        this.Estouvert = ouvert;
    }

    public boolean estOuvert() {
        return this.Estouvert;
    }

    public DatabaseItem getDatabase() {
        return this.databaseItem;
    }

    public boolean vientouvrir() {
        return this.vientouvrir;
    }

    public void setvientouvrir(boolean change) {
        this.vientouvrir = change;
    }
    // // à mettre dans game screen
    // if (coffre.estOuvert()) {
    //     Item gain = databaseItem.getItemAlea();
    //     personnage1.getInventaire().addItem(gain);
    //     armeOk = true;
    //     tempscoffre = 0;
    //     itemcoffre = gain.getIcone();
    //     positioncoffreX = coffre.getPositionX()
    //     positioncoffreY = coffre.getPositionY()
    //     //game.batch.draw(gain.getIcone(), coffre.getPositionX(), coffre.getPositionY(), largeur_coffre*2, hauteur_coffre*2);
    //     // j'essaye de faire un compteur pendant quelques secondes
    //     
    // }
    // Texture itemcoffre
    // int positioncoffreX
    // int positioncoffreY
    // int tempsitem = 3;
    // int tempscoffre = 0;
    
    // if (armeOk && (tempsitem > tempscoffre)) {
    //      tempscoffre += delta;
    //      game.batch.draw(itemcoffre, positioncoffreX, positioncoffreY, largeur_coffre*2, hauteur_coffre*2);
    // } else {
    //      tempscoffre = 0;
    // }
}