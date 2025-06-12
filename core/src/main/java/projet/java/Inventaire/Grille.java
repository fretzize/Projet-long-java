package projet.java.Inventaire;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.utils.ScreenUtils;
import projet.java.Main;


public class Grille {
    private int ligne;
    private int colonne;
    private int taillecase;
    private List<List<Item>> Itemgrille;

    public Grille(int ligne, int colonne, int taillecase) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.taillecase = taillecase;
        this.Itemgrille = new ArrayList<>();

        for (int i = 0; i < ligne; i++) {
            List<Item> lignes = new ArrayList<>();
            for (int j = 0; j < colonne; j++) {
                // Initialiser avec null
                lignes.add(null);
            }
            Itemgrille.add(lignes);
        }
    }

    public List<List<Item>> getItemgrille() {
        return this.Itemgrille;
    }

    public void setItem(int ligne_item, int colonne_item, Item item) {
        if (ligne_item >= 0 && ligne_item < ligne && colonne_item >= 0 && colonne_item < colonne) {
            Itemgrille.get(ligne_item).set(colonne_item, item);
        }
    }

    public void removeItem(int ligne_item, int colonne_item) {
        Itemgrille.get(ligne_item).set(colonne_item, null);
    }
    public void render(Main game) {
        for (int i = 0; i < ligne; i++) {
            for (int j = 0; j < colonne; j++) {
                Item item = Itemgrille.get(i).get(j);
                if (item != null) {
                    
                    Texture texture = item.getIcone();
                    game.batch.draw(texture, j * taillecase, i * taillecase, taillecase, taillecase);
                }
            }
        }
    }
}