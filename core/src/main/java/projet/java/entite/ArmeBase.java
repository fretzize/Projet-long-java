package projet.java.entite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Classe abstraite implémentant les fonctionnalités communes à toutes les armes.
 * Sert de base pour les classes concrètes d'armes.
 */
public abstract class ArmeBase implements arme {
    // Attributs communs à toutes les armes
    protected String nom;                     // Nom de l'arme
    protected int degats;                     // Dégâts infligés par l'arme
    protected int manaRequis;                 // Coût en mana pour utiliser l'arme
    protected float portee;                   // Distance maximale d'effet
    protected float vitesseAttaque;           // Délai entre deux attaques
    protected Texture texture;                // Représentation visuelle de l'arme
    protected float tempsDepuisDerniereAttaque;  // Compteur pour le cooldown
    protected boolean peutAttaquer;           // Indique si l'arme peut être utilisée actuellement
    
    /**
     * Constructeur pour initialiser les propriétés communes à toutes les armes.
     * 
     * @param nom Nom de l'arme
     * @param degats Dégâts infligés par l'arme
     * @param manaRequis Coût en mana pour utiliser l'arme
     * @param portee Distance maximale d'effet
     * @param vitesseAttaque Délai entre deux attaques (en secondes)
     * @param texturePath Chemin vers la texture de l'arme
     */
    public ArmeBase(String nom, int degats, int manaRequis, float portee, float vitesseAttaque, String texturePath) {
        this.nom = nom;
        this.degats = degats;
        this.manaRequis = manaRequis;
        this.portee = portee;
        this.vitesseAttaque = vitesseAttaque;
        this.texture = new Texture(texturePath);  // Charge la texture depuis le chemin spécifié
        this.tempsDepuisDerniereAttaque = vitesseAttaque;  // Initialise le compteur pour permettre une première attaque immédiate
        this.peutAttaquer = true;  // L'arme est utilisable dès le début
    }
    
    /**
     * Met à jour l'état de l'arme, notamment la gestion du cooldown entre les attaques.
     * 
     * @param delta Temps écoulé depuis la dernière mise à jour (en secondes)
     */
    @Override
    public void update(float delta) {
        // Gestion du cooldown des attaques
        if (!peutAttaquer) {  // Si l'arme est en cooldown
            tempsDepuisDerniereAttaque += delta;  // Incrémente le compteur
            if (tempsDepuisDerniereAttaque >= vitesseAttaque) {  // Si le cooldown est terminé
                peutAttaquer = true;  // L'arme peut être utilisée à nouveau
                tempsDepuisDerniereAttaque = 0;  // Réinitialise le compteur
            }
        }
    }
    
    // Implémentation des getters de l'interface
    
    /**
     * @return Nom de l'arme
     */
    @Override
    public String getNom() { return nom; }
    
    /**
     * @return Dégâts infligés par l'arme
     */
    @Override
    public int getDegats() { return degats; }
    
    /**
     * @return Coût en mana pour utiliser l'arme
     */
    @Override
    public int getManaRequis() { return manaRequis; }
    
    /**
     * @return Distance maximale d'effet
     */
    @Override
    public float getPortee() { return portee; }
    
    /**
     * @return Délai entre deux attaques
     */
    @Override
    public float getVitesseAttaque() { return vitesseAttaque; }
    
    /**
     * @return Texture de l'arme
     */
    @Override
    public Texture getTexture() { return texture; }
    
    /**
     * Libère les ressources utilisées par l'arme.
     * Essentiel pour éviter les fuites mémoire.
     */
    @Override
    public void dispose() {
        texture.dispose();  // Libère la mémoire occupée par la texture
    }
}