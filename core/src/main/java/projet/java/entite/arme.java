package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Interface définissant le comportement commun à toutes les armes du jeu.
 * Permet d'unifier les fonctionnalités des armes de mêlée et à distance.
 */
public interface arme {
    /**
     * Méthode principale pour déclencher l'attaque de l'arme.
     * @param position Position du joueur/porteur de l'arme
     * @param direction Direction dans laquelle l'attaque est effectuée
     */
    void attaquer_arme(Vector2 position, Vector2 direction);
    
    /**
     * Renvoie le nom de l'arme.
     * @return Le nom de l'arme sous forme de chaîne de caractères
     */
    String getNom();
    
    /**
     * Renvoie les dégâts infligés par l'arme.
     * @return Valeur numérique des dégâts
     */
    int getDegats();
    
    /**
     * Renvoie le coût en mana pour utiliser l'arme.
     * @return Quantité de mana nécessaire
     */
    int getManaRequis();
    
    /**
     * Renvoie la portée de l'arme.
     * @return Distance maximale d'effet de l'arme
     */
    float getPortee();
    
    /**
     * Renvoie la vitesse d'attaque de l'arme (délai entre deux attaques).
     * @return Délai en secondes entre deux attaques
     */
    float getVitesseAttaque();
    
    /**
     * Renvoie la texture représentant l'arme visuellement.
     * @return Texture de l'arme
     */
    Texture getTexture();
    
    /**
     * Dessine l'arme et ses effets (projectiles, animations...) à l'écran.
     * @param batch SpriteBatch utilisé pour le rendu
     */
    void render(SpriteBatch batch);
    
    /**
     * Met à jour l'état de l'arme (cooldown, positions des projectiles, etc.).
     * @param delta Temps écoulé depuis la dernière mise à jour (en secondes)
     */
    void update(float delta);
    
    /**
     * Libère les ressources utilisées par l'arme (textures, etc.).
     * Doit être appelé lorsque l'arme n'est plus utilisée.
     */
    void dispose();
}

