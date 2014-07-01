package darWin.sprites;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * A Predator Sprite. Tracks down other Sprites and eats them.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class Predator extends Sprite implements Faced{
    private static Sprite player;
    private Sprite eating = null;
    private boolean isFull = false;
    private static int number = 0, fullNumber = 0;
    private static BufferedImage[] faces;
    private int counter = 0;

    private Predator(Ellipse2D.Double bounds, int weight, double friction) {
        super(bounds, weight, friction);
    }
    
    /**
     * Sets the target of the Predators.
     * @param faces The Predators' target.
     */
    public static void setTarget( Sprite inTarget){
        player = inTarget;
    }
    
    /**
     * Sets the faces (images of the faces) of the Predators.
     * @param faces The Predators' faces.
     */
    public static void setFaces(BufferedImage[] faces){
        Predator.faces = faces;
    }
    
    /**
     * Creates a new instance of a Predator.
     * @precondition The face (pictures of faces) for Predator must be set.
     * @precondition The target must be set;
     * @precondition Sprite.playBounds must be set;
     * @return a new Predator instance.
     */
    public static Predator create(){
        assert(faces != null && player != null && Sprite.getPlayBounds() != null);
        Rectangle p = Sprite.getPlayBounds();
        number ++;
        return new Predator(
                new Ellipse2D.Double(p.getWidth()*number/5, p.getHeight()*7/8,
                        ((double)p.getWidth())/6, ((double)p.getWidth())/6),
                 60, .96);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Setters
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * Cancels the eating process. (Not currently used)
     */
    public void cancelEat(){ eating = null; }

    /**
     * Sets the Predator to be full.
     */
    public void setFull() {
        isFull=true;
        fullNumber++;
    }
    
    /**
     * Resets the numbering of the Predators for a new level.
     */
    public static void resetNumber(){
        number = 0;
        fullNumber = 0;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Getters
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Returns true if all Predators are full.
     * @return True if all Predators are full.
     */
    public static boolean areAllFull() {
        return (fullNumber >= number);
    }
        
    /**
     * Returns true if the Predator is full.
     * @return True if the Predator is full.
     */
    public boolean isFull() {
        return isFull;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Private Behavior Methods
    ////////////////////////////////////////////////////////////////////////////

    private boolean isRightRight() {
        double idealAngle = Angles.getAngle(getCenter(), player.getCenter());
        if(idealAngle > Math.PI) idealAngle -= Math.PI*2;
        else if(idealAngle < -Math.PI) idealAngle += Math.PI*2;
        
        double angle = getAngle();
        
        if(angle > idealAngle && angle < idealAngle + Math.PI)
            return true;
        if(angle < idealAngle && angle > idealAngle - Math.PI)
            return false;
        if(angle < 0 && idealAngle > 0){
            if(angle + Math.PI *3/4 > idealAngle)
                return false;
            return true;
        }else if(angle - Math.PI *3/4 > idealAngle)
            return false; 
        else return true;
    }
    
    private boolean isEatable(Sprite other) {
        if (!(other instanceof EdibleSprite) || isFull) return false;
        Point2D ediblePoint = 
            Angles.getOutPoint(getBounds(), getRadius()/4, getAngle());
        return other.getCenter().distance(ediblePoint) < other.getRadius();
    }

    private void eat(EdibleSprite other) {
        if(eating != null)return;
        setAngle(Angles.getAngle(getCenter(), other.getCenter()));
        other.setEaten(this);
        eating = other;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Overrides
    ////////////////////////////////////////////////////////////////////////////
    
    @Override
    public void stepFoward(){
        if(isSliding() || isFull){
            super.stepFoward();
            return;
        }
        
        turn(isRightRight()? -.004 : .004);
        counter ++;
        if(counter > 1){
            if(getSpeed() < 4) addForwardSpeed(.02);
            else addForwardSpeed(0);
            counter=0;
        }
        super.stepFoward();
    }
    
    @Override
    public void detectOther(Sprite other) {
        if(other.equals(eating))return;
        if(isEatable(other)){
            eat((EdibleSprite)other);
            return;
        }
        super.detectOther(other);
    }
    
    @Override
    public Color getColor() {
        return Color.BLACK;
    }
    
    @Override
    public boolean isLayered(){
        return true;
    }
    
    @Override
    public BufferedImage[] getFaces() {
        if(isFull){
            BufferedImage[] out = Arrays.copyOf(faces, faces.length);
            out[Sprite.TOP] = out[Sprite.HAPPY];
            return out;
        }
        return faces;
    }
}