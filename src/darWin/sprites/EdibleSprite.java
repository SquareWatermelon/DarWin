package darWin.sprites;

import java.awt.geom.Ellipse2D.Double;

/**
 * A sprite that can be eaten.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public abstract class EdibleSprite extends Sprite {
    private Predator eatenBy = null;
    
    public EdibleSprite(Double bounds) {
        super(bounds);
    }

    public EdibleSprite(Double bounds, int weight, double friction) {
        super(bounds, weight, friction);
    }
    
    @Override
    public void stepFoward(){
        if(eatenBy != null) beEaten();
        super.stepFoward();
    }
    
    protected void setEaten(Predator eater) { eatenBy = eater; }
    
    private void beEaten() {
        if(getCenter().distance(eatenBy.getCenter()) < 4){
            remove();
            eatenBy.setFull();
        }else if(!eatenBy.isFull()){
            double angle = Angles.getAngle(getCenter(),eatenBy.getCenter());
            stop();
            eatenBy.stop();
            slide();
            eatenBy.addSpeed(angle + Math.PI, 5);
            addSpeed(angle, 1);
        }
    }

}
