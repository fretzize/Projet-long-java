// import com.badlogic.gdx.ApplicationAdapter;
// import com.badlogic.gdx.Gdx;
// import com.badlogic.gdx.graphics.GL20;
// import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// import com.badlogic.gdx.utils.Array;

// public class Armedejet extends ApplicationAdapter implements arme {
//     private String nom;
//     private int degats;
//     private int manadepense;
//     private personnage p1;
   

//     public armedejet(String nom, int degats, int mana) {
//         this.nom = nom;
//         this.degats = degats;
//         this.manadepense = mana;
//     }

//     //toujours pas fait p1.attaquer
//     // @Override
//     // public void attaquer_arme() {
//     //     p1.attaquer();
//     // }

    
//     // image du projectile, je pense le mettre en parametre de l'arme peut etre
//     @Override
//     public void create() {
//         SpriteBatch batch = new SpriteBatch();
//         Texture projectileTexture = new Texture("projectile.png");
//         Array<Projectile> projectiles = new Array<>();
//     }

//     public void tirerProjectile(float x, float y, float vx, float vy) {
//         Projectile projectile = new Projectile(x, y, vx, vy, projectileTexture);
//         projectiles.add(projectile);
//     }

//     @Override
//     public void render() {
//         Gdx.gl.glClearColor(0, 0, 0, 1);
//         Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//         float delta = Gdx.graphics.getDeltaTime();

//         // Mettre à jour les projectiles, les directions
//         for (Projectile projectile : projectiles) {
//             projectile.update(delta);
//         }

//         // Dessiner les projectiles
//         batch.begin();
//         for (Projectile projectile : projectiles) {
//             projectile.draw(batch);
//         }
//         batch.end();

//         // Exemple d'interaction : tirer un projectile
//         if (Gdx.input.isTouched()) {
//             tirerProjectile(50, 50, 100, 100); // Position et vitesse initiales
//         }
//     }

//     @Override
//     public void dispose() {
//         batch.dispose();
//         projectileTexture.dispose();
//     }
    
// }
