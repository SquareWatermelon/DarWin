package darWin.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import darWin.PlayField;

/**
 * This class handles the player Sprite and keeps track of its evolution.
 * Currently only one is allowed.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class Player extends EdibleSprite implements Faced{
    public static enum Colors{ RED, GREEN, BLUE }
    private static int red = 0, blue = 0, green = 0, lives = 4;
    private static Color color= new Color(BASE_COLOR_AMOUNT, 
            BASE_COLOR_AMOUNT, BASE_COLOR_AMOUNT);
    private static BufferedImage[] faces;
    private static PlayField playField;
    private boolean hasTailOut = false;
    private static Player player;
    
    private Player(Ellipse2D.Double bounds) {
        super(bounds);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Initialization
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * Passes a reference to the PlayField so the player can add Sprites to it.
     * @param p The playfield.
     */
    public static void setPlayField(PlayField p) {
        playField = p;
        
    }
    
    /**
     * Sets the face (images of the faces) of the Player.
     * @param face The player's faces.
     */
    public static void setFaces(BufferedImage[] faces){
        Player.faces = faces;
    }

    /**
     * Creates the instance of the player on the currently set PlayField.
     * New calls to this method overwrite the old player.
     * @precondition The face must be set.
     * @precondition The playField must be set.
     * @return a new instance of the player on the currently set PlayField.
     */
    public static Player create(){
        assert(faces != null && playField != null);
        Rectangle p = playField.getBounds();
        player = new Player(
                new Ellipse2D.Double(p.getWidth()/2, p.getHeight()/2,
                        ((double)p.getWidth())/10, ((double)p.getWidth())/10));
        Tail.setPlayer(player);
        return player;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Evolution Handlers
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * Evolves the player by the color picked.
     * @param s The color picked.
     */
    public static void evolve(Sprite s){
        if(!(s instanceof Prey)) return;
        if(s instanceof RedPrey && red < 3) red++;
        else if(s instanceof GreenPrey && green < 3) green++;
        else if(s instanceof BluePrey && blue < 3) blue++;
        Player.color = new Color(BASE_COLOR_AMOUNT + 30 * red,
                BASE_COLOR_AMOUNT + 30 * green,
                BASE_COLOR_AMOUNT + 30 * blue);
    }
    
    /**
     * Returns true if the player currently has a handle.
     * @return True if the player currently has a handle.
     */
    public static boolean hasHandle(){
//        return(green >= 1);
        return true;
    }
    
    /**
     * Returns true if the player currently has a tail.
     * @return True if the player currently has a tail.
     */
    public static boolean hasTail(){
        return(blue >= 1);
    }
    
    /**
     * Returns true if the player currently has air shot.
     * @return True if the player currently has air shot.
     */
    public static boolean hasAirShot(){
//        return(red >= 1);
        return true;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Controls
    ////////////////////////////////////////////////////////////////////////////    
    
    /**
     * Creates an AirShot towards the target.
     * @param target the target of the AirShot.
     */
    public void shootAir(Point2D.Double target) {
          playField.addSprite(AirShot.Create(target));
    }
    
    /**
     * Creates a tail. (not yet implemented)
     * @param end The end of the tail.
     */
    public void makeTail(Point2D.Double end){
        hasTailOut = true;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Life Handling
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * Returns the number of lives the player has. 
     * @return The number of lives the player has.
     */
    public static int getLives() {
        return lives;
    }

    /**
     * Grants lives for at the end of a round.
     */
    public static void gainLives() {
        lives += 4;
    }
    
    /**
     * Deducts a life for when a round is lost.
     */
    public static void loseLife() {
        lives--;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Overrides
    ////////////////////////////////////////////////////////////////////////////
    
    @Override
    public boolean isLayered(){
        return false;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public BufferedImage[] getFaces() {
        return faces;
    }
    
    @Override
    public void draw(Graphics2D g){
        if(isRemovable())return;
//        if(hasTailOut) Tail.drawTail(g);
        super.draw(g);
    }
    
    @Override
    public void stop(){
        super.stop();
    }
    
}
