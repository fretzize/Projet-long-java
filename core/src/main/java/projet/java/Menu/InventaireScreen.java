
package projet.java.Menu;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import projet.java.entite.*;
import projet.java.Inventaire.Grille;
import projet.java.Inventaire.Inventaire;
import projet.java.Inventaire.Item;
import projet.java.Menu.GameScreen;
import projet.java.animation.AnimationHandler;
import projet.java.combat.AttackManager;
import projet.java.Main;

public class InventaireScreen implements Screen {

    final Main game;
    private GameScreen gameScreen; 
    
    private Texture backgroundTexture;
    private Texture caisse_inventaire;
    private int selectedIndex = 0; 
    private final String[] menuOptions = { "Reprendre" };
    private Rectangle[] optionBounds;
    private Personnage personnage;
    private AttackManager attackmanager;
    private AnimationHandler animationHandler;

    public InventaireScreen(final Main game, GameScreen gameScreen, Personnage personnage, AttackManager attackManager) {
        this.game = game;
        this.gameScreen = gameScreen; 
        this.personnage = personnage;
        this.attackmanager = attackManager;
        
        
        // Initialiser les rectangles pour chaque option
        optionBounds = new Rectangle[menuOptions.length];
        optionBounds[0] = new Rectangle();
    }

    int ligne_grille = 3;
    int colonne_grille = 5;
    int taillecase = 200;
    Grille grille = new Grille(ligne_grille, colonne_grille, taillecase);
    Inventaire inventaire;
    int place_item_ligne = ligne_grille-1;
    int place_item_colonne = 0;
    int taille_cote_case = 25;
    int nombre_element_grille = 0;

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0.7f); 

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        // Calculer les dimensions de l'écran
        float screenWidth = game.viewport.getWorldWidth();
        float screenHeight = game.viewport.getWorldHeight();
        
        game.batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);

        // Titre
        game.font.getData().setScale(3.0f);
        game.font.setColor(Color.GOLD);
        String title = "Inventaire";
        float titleWidth = game.font.draw(game.batch, title, 0, 0).width;
        float titleX = (screenWidth - titleWidth) / 2;
        float titleY = screenHeight - 100;
        game.font.draw(game.batch, title, titleX, titleY);

        // Options du menu
        game.font.getData().setScale(1.5f);


        String option = menuOptions[0];
        float textWidth = game.font.draw(game.batch, option, 0, 0).width;
        float textHeight = game.font.draw(game.batch, option, 0, 0).height;
        float x = (screenWidth - textWidth) / 2;
        float y = textHeight*2;

        // Mettre à jour la zone cliquable
        optionBounds[0].set(x, y - textHeight, textWidth, textHeight);

        // Vérifier si la souris survole l'option
        if (isMouseOver(optionBounds[0])) {
            game.font.setColor(Color.YELLOW);
            if (Gdx.input.justTouched()) {
                selectedIndex = 1;
                handleMenuSelection();
            }
        } else {
            game.font.setColor(Color.WHITE);
        }
        selectedIndex = 0;

       
        // System.out.println("nombre element grills :" + nombre_element_grille);
        inventaire = personnage.getInventaire();
        place_item_ligne = ligne_grille - 1;
        place_item_colonne = 0;
        
        if (inventaire.getItems().size() != nombre_element_grille) {
            nombre_element_grille = 0;
            for (Item item : inventaire.getItems()) {
                
            //game.batch.draw(item.getIcone(), taillecase, taillecase, taillecase, taillecase);
            //System.out.println(place_item_ligne + place_item_colonne);
                grille.setItem(place_item_ligne, place_item_colonne, item);
            
            //List<List<Item>> liste = grille.getItemgrille();
            //List<Item> liste_item = liste.get(place_item_ligne);
            //System.out.println("Taille de la liste : " + liste.size());
                System.out.println(colonne_grille);
                if (place_item_colonne < colonne_grille-1) {
                     place_item_colonne ++;
                    
                } else {
                    place_item_colonne = 0;
                    place_item_ligne --;
                }
                nombre_element_grille ++;
            }
        }
        
        

        

        for (int i = 0; i < ligne_grille; i++) {
            for (int j = 0; j < colonne_grille; j++) {
                Item item = grille.getItemgrille().get(i).get(j);
                game.batch.draw(caisse_inventaire, screenWidth/2 - taillecase*ligne_grille + taillecase/2+  j* taillecase, taillecase +i * taillecase, taillecase, taillecase);
                if (item != null) {
                    Texture texture = item.getIcone();
                    game.batch.draw(texture, taille_cote_case + screenWidth/2 - taillecase*ligne_grille + taillecase/2+  j* taillecase, taille_cote_case + taillecase +i * taillecase, taillecase - 2*taille_cote_case, taillecase-2*taille_cote_case);
                    if (isMouseOverTexture(screenWidth/2 - taillecase*ligne_grille + taillecase/2+  j* taillecase, taillecase +i * taillecase, taillecase, taillecase)) {
                        if (Gdx.input.justTouched()) {
                            if (item.getType() == Item.ItemType.POTION) {
                                personnage.addVie(item.getNombre());
                                // System.out.println(personnage.getVie());
                                personnage.getInventaire().removeItem(item);
                                grille.removeItem(i, j);
                                nombre_element_grille --;
                            }
                            if (item.getType() == Item.ItemType.POTIONVITESSE) {
                                personnage.setVitesse(personnage.getVitesse() + item.getNombre());
                                personnage.setAcceleration(true);
                                // System.out.println(personnage.getVie());
                                personnage.getInventaire().removeItem(item);
                                grille.removeItem(i, j);
                                nombre_element_grille --;
                            }
                            if (item.getType() == Item.ItemType.ARME) {
                                // attackmanager.getAttackMana().setArme(item.getNom(), item.getNombre(), item.getRange());
                                // attackmanager.getArmeMelee().setDegat(item.getNombre());
                                // attackmanager.getArmeMelee().setRange(item.getRange());
                                ArmeMelee armeMelee = new ArmeMelee(item.getNom(), item.getNombre(), 0, item.getRange(), item.getVitesse(), item.getTextureString(), item.getAngle(), item.getForceKnockback());
                                attackmanager.setArmeMelee(armeMelee);
                                if (item.getNom().equals("hache")) {
                                    // System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                                    // this.animationHandler = new AnimationHandler(2);
                                    // this.attackmanager.setAnimation(new AnimationHandler(2));

                                    gameScreen.setAnimation(2);
                                    // this.attackmanager.setArme("hache", item.getNombre(), item.getRange());
                                } else {
                                    gameScreen.setAnimation(1);
                                }
                                
                                // personnage.setArme(item.getNom(), item.getNombre(), item.getRange(), gameScreen.getNiveau());
                            }

                            if (item.getType() == Item.ItemType.PISTOLET) {
                                ArmeDistance armeDistance = new ArmeDistance(item.getNom(), item.getNombre(), item.getMana(), item.getRange(), item.getVitesse(),item.getTextureString(), item.getTexture_proj(), item.getVitesse(), 0, item.getAngle());
                                attackmanager.setArmeDistance(armeDistance);
                                gameScreen.setAnimation(1);
                            }
                            // personnage.setArme(null);
                        } else {
                            Color previousColor = game.batch.getColor().cpy();

                            // j'applique un filtre gris pour cette texture seulement
                            game.batch.setColor(Color.GRAY);
                            game.batch.draw(texture, taille_cote_case + screenWidth/2 - taillecase*ligne_grille + taillecase/2+  j* taillecase, taille_cote_case + taillecase +i * taillecase, taillecase - 2*taille_cote_case, taillecase-2*taille_cote_case);
                            // game.batch.draw(texture, taille_cote_case + screenWidth/2 - taillecase*ligne_grille + taillecase/2+  j* taillecase, taille_cote_case + taillecase +i * taillecase, taillecase - 2*taille_cote_case, taillecase-2*taille_cote_case);
                            // je veux afficher les caracteristiques de l'arme quand je mets la souris dessus
                            String caracteristique = item.getNom();
                            float caracteristique_titre = game.font.draw(game.batch, title, 0, 0).width;
                            float caracteristique_titreX =  screenWidth/2 - taillecase*ligne_grille + taillecase/2+  j* taillecase;
                            float caracteristique_titreY = taillecase * 3/2 +i * taillecase;
                            game.font.draw(game.batch, caracteristique, caracteristique_titreX, caracteristique_titreY);
                            //game.batch.draw(caisse_inventaire, screenWidth/2 - taillecase*ligne_grille + taillecase/2+  j* taillecase, taillecase +i * taillecase, taillecase, taillecase);

                            // je restaure ensuite la couleur d'origine
                            game.batch.setColor(previousColor);
                            
                        }
                    }
                }
            }
        }
        // grille.render(game);

        game.font.draw(game.batch, option, x, y);

        game.batch.end();

        // Gérer les entrées clavier
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            handleMenuSelection();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (Gdx.input.isKeyJustPressed(game.toucheInventaire))) {
            // Revenir directement au jeu avec Échap
            game.setScreen(gameScreen);
            dispose();
        }
    }

    private void handleMenuSelection() {
        if (selectedIndex == 1) {
            game.setScreen(gameScreen);
        dispose();
        }
    }

    private boolean isMouseOver(Rectangle bounds) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        Vector3 worldCoords = game.viewport.getCamera().unproject(new Vector3(mouseX, mouseY, 0));
        return bounds.contains(worldCoords.x, worldCoords.y);
    }

    public boolean isMouseOverTexture(float x, float y, float width, float height) {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        // Conversion coordonnées écran vers coordonnées monde
        game.viewport.getCamera().unproject(mousePos);

        return mousePos.x >= x && mousePos.x <= x + width && mousePos.y >= y && mousePos.y <= y + height;
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("menubackground.png"));
        caisse_inventaire = new Texture(Gdx.files.internal("case_inventaire.png"));
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
