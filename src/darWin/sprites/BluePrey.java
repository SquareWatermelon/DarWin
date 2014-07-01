package darWin.sprites;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

/**
 * A blue Prey with a behavior where it avoids other EdibleSprites.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class BluePrey extends Prey {

    public BluePrey(Ellipse2D.Double bounds) {
        super(bounds);
    }
    
    @Override
    protected void doBehavior(Sprite other) {
        double distance = getCenter().distance(other.getCenter());
        if(other instanceof Prey && getCenter().distance(other.getCenter()) <= 
            getWidth() + other.getWidth()*.5){
            addSpeed(Angles.getAngle(other.getCenter(), getCenter()),
                    0.01 * (getWidth() / distance));
        }
        evade(other);
    }
    
    @Override
    public Color getColor() {
        return new Color(BASE_COLOR_AMOUNT, 
                BASE_COLOR_AMOUNT, 
                BASE_COLOR_AMOUNT + 60 * level);
    }
}
