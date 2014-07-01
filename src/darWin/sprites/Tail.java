package darWin.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Will contain tail functionality for the player.
 * Not yet implemented!
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class Tail {
    private static final int COUNTER_END = 20, POLY_NUMBER = 10;
    private static int counter = 0;
    private static Player player;
    private static int[] x = new int[POLY_NUMBER], y = new int[POLY_NUMBER];
    private static Point2D.Double end;
    
    public void drawTail(Graphics2D g) {
//        assert(player != null && end != null); 
//        g.setStroke(new BasicStroke(2));
//        g.setColor(Color.BLACK);
//        counter++;
//        if(counter >= COUNTER_END){ 
//            x[0] = (int) (Math.cos()*player.getRadius());
//            for(int i = 0; i < POLY_NUMBER; i++){
//                Angles.getOutsidePoint(getBounds(), Math.random()*Math.PI*2);
//                x[i] = (int) (player.getCenter().x + );
//                y[i] = (int) (player.getCenter().y - Math.sin(Math.random()*Math.PI*2)*player.getRadius());
//            }
//            counter = 0;
//        }
//        g.drawPolygon(x, y, POLY_NUMBER);
    }

    public static void setPlayer(Player player) {
        Tail.player = player;
    }

    public static void setEnd(Point2D.Double end) {
        Tail.end = end;
    }

}
