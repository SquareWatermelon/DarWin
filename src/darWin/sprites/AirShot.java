package darWin.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * A shot of "air" which knocks the player back and knocks a sprite hit with it 
 * forward.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class AirShot extends Sprite {
    public static final int POLY_NUMBER = 20, COUNTER_END = 8;
    private static Sprite player;
    private static double expansion;
    private int counter = COUNTER_END;
    private int[] x = new int[POLY_NUMBER], y = new int[POLY_NUMBER];
    
    private AirShot(Ellipse2D.Double bounds, int weight, double friction) {
        super(bounds, weight, friction);
    }
    
    /**
     * Sets the player the shots will come from.
     * @param player the player the shots will come from.
     */
    public static void setPlayer(Sprite player) {
        AirShot.player = player;
    }
    
    /**
     * Creates a new AirShot, with the given firing target, from the player.
     * @param target The point towards which the shot was fired.
     * @return a new Airshot, with the given firing target, from the player.
     */
    public static AirShot Create(Point2D.Double target) {
        assert(player != null);
        double angle = Angles.getAngle(player.getCenter(), target);
        double width = player.getWidth()/2;
        expansion = width / 40;
        Point2D.Double center = Angles.getOutPoint(player.getBounds(), 
                player.getRadius(), angle);
        AirShot shot = new AirShot(
                new Ellipse2D.Double( center.x - width/2, center.y - width/2,
                        width, width),
                1, .92);
        shot.addSpeed(angle, 7);
        if(player.getSpeed()>.3)
            shot.addSpeed(player.getXYSpeed(), 1);
        player.addSpeed(new Point2D.Double(-shot.getXSpeed(), -shot.getYSpeed()), 10);
        return shot;
    }

    private void expand() {
        getBounds().height += expansion;
        getBounds().width += expansion;
        getBounds().x -= expansion / 2;
        getBounds().y -= expansion / 2;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Overrides
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void stepFoward(){
        expand();
        if(getSpeed() < .2) remove();
        super.stepFoward();
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    @Override
    public boolean isLayered() {
        return false;
    }
    
    @Override
    public void draw(Graphics2D g){
        if(isRemovable())return;
        g.setStroke(new BasicStroke(2));
        g.setColor(getColor());
        counter++;
        if(counter >= COUNTER_END){
            Point2D.Double temp;
            for(int i = 0; i < POLY_NUMBER; i++){
                temp = Angles.getOutPoint(getBounds(), Math.random()*Math.PI*2);
                x[i] = (int) temp.x;
                y[i] = (int) temp.y;
            }
            counter = 0;
        }
        g.drawPolygon(x, y, POLY_NUMBER);
    }
    
    @Override
    protected void collide(Sprite other){
        if(other instanceof Player) return;
        other.addSpeed(getXYSpeed(), 30);
        remove();
    }
}
