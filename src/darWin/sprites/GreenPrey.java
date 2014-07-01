package darWin.sprites;

import java.awt.Color;
import java.awt.geom.Ellipse2D.Double;

/**
 * A green Prey with a behavior where it circles Predators.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class GreenPrey extends Prey {

    public GreenPrey(Double bounds) {
        super(bounds);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void doBehavior(Sprite other) {
        if(other instanceof Predator && getCenter().distance(other.getCenter()) <= 
            getWidth() + other.getWidth()*2){
            addSpeed(Angles.getAngle(getCenter(), other.getCenter()) + Math.PI/2
                    , 0.03);
        }
    }
    
    @Override
    public Color getColor() {
        return new Color(BASE_COLOR_AMOUNT, 
                BASE_COLOR_AMOUNT + 60 * level, 
                BASE_COLOR_AMOUNT);
    }

}
