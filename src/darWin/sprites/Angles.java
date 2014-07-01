package darWin.sprites;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * A utility class that supplies methods for calculating angles and their 
 * effects. Methods convert angles back from traditional to the java Coordinate 
 * plane. 
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class Angles {
    
    /**
     * Returns the (traditional) angle in radians between two points. 
     * @param start The start point.
     * @param end The end point.
     * @return The (traditional) angle in radians between two points. 
     */
    public static double getAngle(
            Point2D.Double start, Point2D.Double end) {
        switch(getQuadrant(start, end)){
        case 1: case 4: 
            return -Math.atan( 
                (start.y - end.y) / 
                (start.x - end.x));
        case 2: case 3: default:
            return -(Math.PI + Math.atan( 
                    (start.y - end.y) / 
                    (start.x - end.x)));
        }
    }

    /**
     * Returns the (traditional) quadrant a point is in with the given origin. 
     * @param origin The origin.
     * @param target The point to locate.
     * @return The (traditional) quadrant a point is in with the given origin. 
     */
    public static int getQuadrant( Point2D.Double origin, 
            Point2D.Double target) {
        if( target.y <= origin.y ){
            if(target.x >= origin.x) return 1;
            else return 2;
        }
        if(target.x >= origin.x) return 4;
        else return 3;
    }

    /**
     * Returns the legs of a triangle from the opposite angle and hypotenuse. 
     * @param angle The angle.
     * @param radius The hypotenuse.
     * @return The legs of a triangle from the opposite angle and hypotenuse.
     */
    public static Point2D.Double getOpXYFromAngRad(double angle, double radius) {
        return new Point2D.Double((-Math.cos(angle) * radius),
                (Math.sin(angle) * radius));
    }
    
    /**
     * Returns the legs of a triangle from the angle and hypotenuse. 
     * @param angle The angle.
     * @param radius The hypotenuse.
     * @return The legs of a triangle from the angle and hypotenuse.
     */
    public static Point2D.Double getXYFromAngRad(double angle, double radius) {
        return new Point2D.Double(Math.cos(angle) * radius,
                -Math.sin(angle) * radius);
    }
    
    /**
     * Returns the Point2D.Double where a angle from the center point intersects 
     * the given ellipse with added radius.
     * @param ellipse The original ellipse.
     * @param added The added radius.
     * @param angle The angle.
     * @return The Point2D.Double where a angle from the center point intersects 
     * the given ellipse with added radius.
     */
    public static Point2D.Double getOutPoint(Ellipse2D.Double ellipse,
            double added, double angle){
        return new Point2D.Double(
                ellipse.getCenterX() + Math.cos(angle) * ((ellipse.width/2 + added)),
                ellipse.getCenterY() - Math.sin(angle) * ((ellipse.height/2 + added)));
    }
    
    /**
     * Returns the Point2D.Double where a angle from the center point intersects 
     * the given ellipse.
     * @param ellipse The ellipse.
     * @param angle The angle.
     * @return The Point2D.Double where a angle from the center point intersects 
     * the given ellipse.
     */
    public static Point2D.Double getOutPoint(Ellipse2D.Double ellipse,
            double angle){
        return new Point2D.Double(
                ellipse.x + Math.cos(angle) * (ellipse.width/2 ),
                ellipse.y - Math.sin(angle) * (ellipse.height/2 ));
    }
}
//public static Point2D.Double getOpOutPoint(Ellipse2D.Double Ellipse1,
//Ellipse2D.Double Ellipse2, double angle){
//angle += Math.PI;
//return new Point2D.Double(
//  Ellipse1.x + Math.cos(angle) * ((Ellipse1.width/2 + Ellipse2.width/2)),
//  Ellipse1.y - Math.sin(angle) * ((Ellipse1.height/2 + Ellipse2.height/2)));
//}