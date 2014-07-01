package darWin.sprites;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * A Prey Sprite. Can be eaten by Predators. Behavior varies by color.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public abstract class Prey extends EdibleSprite implements Faced {
    private static final int RED = 1, BLUE = 0, GREEN = 2;
    public static final int BASE_COLOR_AMOUNT = 120;
    private static int number = 1;
    private static BufferedImage[] faces;
    protected static final int level = 1;
    
    public Prey(Ellipse2D.Double bounds) {
        super(bounds);
    }
    
    /**
     * Creates a new red, green or blue Prey.
     * @return a new red, green or blue Prey.
     */
    public static Prey create(){
        assert(faces != null && Sprite.getPlayBounds() != null);
        Rectangle p = Sprite.getPlayBounds();
        number+=1;
        switch(number % 3){
        case RED: return new RedPrey(
                new Ellipse2D.Double(p.getWidth()*number/4, 
                        p.getHeight()*((number-1) / 3)/8,
                        ((double)p.getWidth())/10, ((double)p.getWidth())/10));
        case BLUE: return new BluePrey(
                new Ellipse2D.Double(p.getWidth()*number/8,
                        p.getHeight()*((number-1) / 3)/8,
                        ((double)p.getWidth())/10, ((double)p.getWidth())/10)); 
        case GREEN: default: return new GreenPrey(
                new Ellipse2D.Double(p.getWidth()*number/8,
                        p.getHeight()*((number-1) / 3)/8,
                        ((double)p.getWidth())/10, ((double)p.getWidth())/10));
        }
    }
    
    protected void evade(Sprite other){
        double distance = getCenter().distance(other.getCenter());
        if(other instanceof Predator
                && !((Predator) other).isFull() 
                && distance <= getWidth() + other.getWidth()*2)
            addSpeed(Angles.getAngle(other.getCenter(), getCenter()),
                    0.03 * ( distance /getWidth()));
    }
    
    /**
     * Sets the faces for the Prey.
     * @param faces The faces for the Prey.
     */
    public static void setFaces(BufferedImage[] faces){
        Prey.faces = faces;
    }
    
    /**
     * Resets the numbering of the Prey for a new level.
     */
    public static void resetNumber(){
        number = 1;
    }
    
    protected abstract void doBehavior( Sprite other );
    
    @Override
    public void detectOther(Sprite other) {
        if(isRemovable()) return;
        if(!isSliding()){ 
            doBehavior( other );
        }
        super.detectOther(other);
    }
    
    @Override
    public boolean isLayered(){
        return false;
    }

    @Override
    public BufferedImage[] getFaces() {
        return faces;
    }
    
}
