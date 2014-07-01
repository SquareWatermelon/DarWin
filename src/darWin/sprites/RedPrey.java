package darWin.sprites;

import java.awt.Color;
import java.awt.geom.Ellipse2D.Double;

/**
 * A red Prey with a behavior where it rams other EdibleSprites.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class RedPrey extends Prey {

    public RedPrey(Double bounds) {
        super(bounds);
    }

    @Override
    public Color getColor() {
        return new Color(
                BASE_COLOR_AMOUNT + 60 * level, 
                BASE_COLOR_AMOUNT, 
                BASE_COLOR_AMOUNT);
    }
    
    @Override
    protected void doBehavior(Sprite other){
        double distance = getCenter().distance(other.getCenter());
        if((other instanceof EdibleSprite) 
                && distance <= getWidth() + other.getWidth()*4){
            addSpeed(Angles.getAngle(getCenter(), other.getCenter())
                    , 0.04 * (getWidth() / distance));
        }
        evade(other);
    }

}
