package projet.java.debug;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import projet.java.combat.AttackManager;
import projet.java.entite.Niveau;
import projet.java.entite.Personnage;
import projet.java.entite.Sbire;

public class GameDebugger {
    private static final int MAX_LOG_ENTRIES = 10;
    private static final float LOG_DISPLAY_DURATION = 5f; // Secondes
    
    private boolean debugEnabled = false;
    private boolean showHitboxes = true;
    private boolean showStats = true;
    private boolean showLogs = true;
    
    private ShapeRenderer shapeRenderer;
    private BitmapFont debugFont;
    private LinkedList<LogEntry> logEntries;
    
    // Références aux objets du jeu
    private Personnage personnage;
    private Niveau niveau;
    private AttackManager attackManager;
    
    // Mode pas à pas
    private boolean stepByStepMode = false;
    private boolean nextStep = false;
    
    public GameDebugger(Personnage personnage, Niveau niveau, AttackManager attackManager) {
        this.personnage = personnage;
        this.niveau = niveau;
        this.attackManager = attackManager;
        
        this.shapeRenderer = new ShapeRenderer();
        this.debugFont = new BitmapFont();
        this.debugFont.setColor(Color.WHITE);
        this.logEntries = new LinkedList<>();
        
        // Log initial
        //log("Débuggeur initialisé", LogType.INFO);
    }
    
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            debugEnabled = !debugEnabled;
            log("Debug " + (debugEnabled ? "activé" : "désactivé"), LogType.INFO);
        }
        
        if (!debugEnabled) return;
        
        // Contrôles de débogage
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            showHitboxes = !showHitboxes;
            log("Affichage des hitboxes " + (showHitboxes ? "activé" : "désactivé"), LogType.INFO);
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            showStats = !showStats;
            log("Affichage des statistiques " + (showStats ? "activé" : "désactivé"), LogType.INFO);
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            showLogs = !showLogs;
            log("Affichage des logs " + (showLogs ? "activé" : "désactivé"), LogType.INFO);
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            stepByStepMode = !stepByStepMode;
            log("Mode pas à pas " + (stepByStepMode ? "activé" : "désactivé"), LogType.INFO);
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && stepByStepMode) {
            nextStep = true;
            log("Étape suivante", LogType.INFO);
        }
        
        // Touches pour modifier les valeurs de débogage
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            // Augmenter les dégâts du sbire
            for (Sbire sbire : niveau.getSbires()) {
                // Modifier degatsCaC directement via reflection pour éviter d'ajouter un setter
                try {
                    java.lang.reflect.Field field = Sbire.class.getDeclaredField("degatsCaC");
                    field.setAccessible(true);
                    int currentDamage = field.getInt(sbire);
                    field.set(sbire, currentDamage + 5);
                    log("Dégâts sbire augmentés à " + (currentDamage + 5), LogType.INFO);
                } catch (Exception e) {
                    log("Erreur accès degatsCaC: " + e.getMessage(), LogType.ERROR);
                }
            }
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            // Augmenter la portée du sbire
            for (Sbire sbire : niveau.getSbires()) {
                try {
                    java.lang.reflect.Field field = Sbire.class.getDeclaredField("porteeCaC");
                    field.setAccessible(true);
                    float currentRange = field.getFloat(sbire);
                    field.set(sbire, currentRange + 10f);
                    log("Portée sbire augmentée à " + (currentRange + 10f), LogType.INFO);
                } catch (Exception e) {
                    log("Erreur accès porteeCaC: " + e.getMessage(), LogType.ERROR);
                }
            }
        }
        
        // Mettre à jour le statut des logs
        updateLogs(delta);
    }
    
    public void render(SpriteBatch batch) {
        if (!debugEnabled) return;
        
        // Terminer le SpriteBatch courant
        batch.end();
        
        // Dessiner les hitboxes
        if (showHitboxes) {
            renderHitboxes();
        }
        
        // Redémarrer le SpriteBatch
        batch.begin();
        
        // Afficher les statistiques
        if (showStats) {
            renderStats(batch);
        }
        
        // Afficher les logs
        if (showLogs) {
            renderLogs(batch);
        }
    }
    
    private void renderHitboxes() {
        shapeRenderer.setProjectionMatrix(Gdx.graphics.getWidth() > 800 ? 
            new com.badlogic.gdx.math.Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) : 
            Gdx.graphics.getWidth() > 800 ? new com.badlogic.gdx.math.Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) : new com.badlogic.gdx.math.Matrix4());
            
        // Dessiner les hitboxes en contours
        shapeRenderer.begin(ShapeType.Line);
        
        // Hitbox du personnage
        shapeRenderer.setColor(Color.GREEN);
        Rectangle playerHitbox = new Rectangle(personnage.getPositionX(), personnage.getPositionY(), 10, 10); // À ajuster
        shapeRenderer.rect(playerHitbox.x, playerHitbox.y, playerHitbox.width, playerHitbox.height);
        
        // Hitboxes des sbires
        shapeRenderer.setColor(Color.RED);
        for (Sbire sbire : niveau.getSbires()) {
            if (sbire.enVie()) {
                Rectangle sbireHitbox = sbire.getHitbox();
                shapeRenderer.rect(sbireHitbox.x, sbireHitbox.y, sbireHitbox.width, sbireHitbox.height);
                
                // Distance entre personnage et sbire
                shapeRenderer.setColor(Color.YELLOW);
                shapeRenderer.line(
                    personnage.getPositionX() + playerHitbox.width/2, 
                    personnage.getPositionY() + playerHitbox.height/2,
                    sbire.getPositionX() + sbireHitbox.width/2, 
                    sbire.getPositionY() + sbireHitbox.height/2
                );
                shapeRenderer.setColor(Color.RED);
            }
        }
        

        // Hitbox des projectiles
        //shapeRenderer.setColor(Color.PURPLE);
        //for (var projectile : niveau.getProjectiles()) {
        //    Rectangle projectileHitbox = projectile.getHitbox();
        //    shapeRenderer.rect(projectileHitbox.x, projectileHitbox.y, projectileHitbox.width, projectileHitbox.height);
        //}

        // Hitbox de l'attaque si active
        if (attackManager.isAttacking()) {
            shapeRenderer.setColor(Color.BLUE);
            Rectangle attackZone = attackManager.getArmeMelee().getZoneAttaque();
            shapeRenderer.rect(attackZone.x, attackZone.y, attackZone.width, attackZone.height);
        }
        
        shapeRenderer.end();
        
        // Dessiner les zones d'attaque des sbires
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(new Color(1, 0, 0, 0.7f)); // Rouge plus vif et visible
        
        for (Sbire sbire : niveau.getSbires()) {
            if (sbire.enVie()) {
                try {
                    java.lang.reflect.Field field = Sbire.class.getDeclaredField("porteeCaC");
                    field.setAccessible(true);
                    float portee = field.getFloat(sbire);
                    
                    // Dessiner un cercle représentant la portée
                    Rectangle hitbox = sbire.getHitbox();
                    float centerX = hitbox.x + hitbox.width/2;
                    float centerY = hitbox.y + hitbox.height/2;
                    
                    // Dessiner des points formant un cercle
                    final int segments = 30;
                    for (int i = 0; i < segments; i++) {
                        float angle1 = (float) (Math.PI * 2 * i / segments);
                        float angle2 = (float) (Math.PI * 2 * (i+1) / segments);
                        
                        shapeRenderer.line(
                            centerX + (float)Math.cos(angle1) * portee,
                            centerY + (float)Math.sin(angle1) * portee,
                            centerX + (float)Math.cos(angle2) * portee,
                            centerY + (float)Math.sin(angle2) * portee
                        );
                    }
                    
                    // Ajouter une ligne du sbire au joueur pour visualiser la direction d'attaque
                    if (sbire.getCible() != null) {
                        shapeRenderer.setColor(Color.YELLOW);
                        shapeRenderer.line(
                            centerX,
                            centerY,
                            personnage.getPositionX() + 5,
                            personnage.getPositionY() + 5
                        );
                        shapeRenderer.setColor(new Color(1, 0, 0, 0.7f));
                    }
                } catch (Exception e) {
                    // Ignorer l'erreur
                }
            }
        }
        
        shapeRenderer.end();
    }
    
    private void renderStats(SpriteBatch batch) {
        float y = Gdx.graphics.getHeight() - 20;
        float x = 10;
        
        debugFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), x, y);
        y -= 20;
        
        debugFont.draw(batch, "Personnage - Position: (" + personnage.getPositionX() + ", " + personnage.getPositionY() + ")", x, y);
        y -= 20;
        
        debugFont.draw(batch, "Personnage - Vie: " + personnage.getVie() + ", Bouclier: " + personnage.getBouclier(), x, y);
        y -= 20;
        
        if (attackManager.isAttacking()) {
            debugFont.draw(batch, "ATTAQUE EN COURS", x, y);
            y -= 20;
        }
        
        debugFont.draw(batch, "Sbires: " + niveau.getSbires().size, x, y);
        y -= 20;
        
        for (int i = 0; i < niveau.getSbires().size; i++) {
            Sbire sbire = niveau.getSbires().get(i);
            if (sbire.enVie()) {
                debugFont.draw(batch, "Sbire " + i + " - Vie: " + sbire.getVie(), x, y);
                y -= 20;
                
                debugFont.draw(batch, "Position: (" + sbire.getPositionX() + ", " + sbire.getPositionY() + ")", x, y);
                y -= 20;
                
                // Afficher la distance entre le sbire et le personnage
                float dx = sbire.getPositionX() - personnage.getPositionX();
                float dy = sbire.getPositionY() - personnage.getPositionY();
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                
                debugFont.draw(batch, "Distance au joueur: " + distance, x, y);
                y -= 20;
                
                try {
                    // Obtenir et afficher la portée du sbire
                    java.lang.reflect.Field field = Sbire.class.getDeclaredField("porteeCaC");
                    field.setAccessible(true);
                    float portee = field.getFloat(sbire);
                    
                    debugFont.draw(batch, "Portée: " + portee + " (À portée: " + (distance <= portee) + ")", x, y);
                    y -= 20;
                    
                    // Obtenir et afficher les dégâts du sbire
                    field = Sbire.class.getDeclaredField("degatsCaC");
                    field.setAccessible(true);
                    int degats = field.getInt(sbire);
                    
                    debugFont.draw(batch, "Dégâts CàC: " + degats, x, y);
                    y -= 20;
                } catch (Exception e) {
                    debugFont.draw(batch, "Erreur accès champs: " + e.getMessage(), x, y);
                    y -= 20;
                }
            }
        }
    }
    
    private void renderLogs(SpriteBatch batch) {
        float y = 100;
        float x = 10;
        
        for (LogEntry entry : logEntries) {
            Color originalColor = debugFont.getColor();
            
            // Changer la couleur en fonction du type de log
            switch (entry.type) {
                case ERROR:
                    debugFont.setColor(Color.RED);
                    break;
                case WARNING:
                    debugFont.setColor(Color.YELLOW);
                    break;
                case EVENT:
                    debugFont.setColor(Color.CYAN);
                    break;
                default:
                    debugFont.setColor(Color.WHITE);
            }
            
            debugFont.draw(batch, entry.message, x, y);
            y += 20;
            
            // Rétablir la couleur originale
            debugFont.setColor(originalColor);
        }
    }
    
    private void updateLogs(float delta) {
        // Mettre à jour la durée d'affichage de chaque log
        for (int i = logEntries.size() - 1; i >= 0; i--) {
            LogEntry entry = logEntries.get(i);
            entry.remainingTime -= delta;
            
            // Supprimer les logs expirés
            if (entry.remainingTime <= 0) {
                logEntries.remove(i);
            }
        }
    }
    
    public void log(String message, LogType type) {
        logEntries.addFirst(new LogEntry(message, type));
        System.out.println("[" + type + "] " + message);
        
        // Limiter le nombre de logs affichés
        while (logEntries.size() > MAX_LOG_ENTRIES) {
            logEntries.removeLast();
        }
    }
    
    public boolean isStepMode() {
        return stepByStepMode;
    }
    
    public boolean canProceed() {
        if (!stepByStepMode) return true;
        
        if (nextStep) {
            nextStep = false;
            return true;
        }
        
        return false;
    }
    
    public void dispose() {
        shapeRenderer.dispose();
        debugFont.dispose();
    }
    
    public enum LogType {
        INFO,
        WARNING,
        ERROR,
        EVENT
    }
    
    private static class LogEntry {
        String message;
        LogType type;
        float remainingTime;
        
        LogEntry(String message, LogType type) {
            this.message = message;
            this.type = type;
            this.remainingTime = LOG_DISPLAY_DURATION;
        }
    }

    // Ajouter cette méthode pour activer/désactiver l'affichage des hitboxes
    public void toggleHitboxDisplay() {
        showHitboxes = !showHitboxes;
        log("Affichage des hitboxes " + (showHitboxes ? "activé" : "désactivé"), LogType.INFO);
    }

    // Ajouter cette méthode pour vérifier si les hitboxes doivent être affichées
    public boolean isShowingHitboxes() {
        return debugEnabled && showHitboxes;
    }

    // Ajouter cette méthode pour vérifier si le débogage est activé
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    // Ajouter cette méthode
    public void toggleDebug() {
        debugEnabled = !debugEnabled;
        log("Mode debug " + (debugEnabled ? "activé" : "désactivé"), LogType.INFO);
    }
}