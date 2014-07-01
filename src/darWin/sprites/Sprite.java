package darWin.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Sprites have a step forward behavior, are round, and simulate simple physics. 
 * @author Robert "Square Watermelon" Tolda
 *
 */
public abstract class Sprite  {
    public static final double RADIAN=Math.PI/180;
    public static final int BASE_COLOR_AMOUNT = 160, MAX_SPEED = 20;
    public static final int FACE = 0, SLIDING = 1, HAPPY=2, TOP=3;
    private static boolean hasBouncyWalls = true;
    private static Rectangle playBounds;
    private double angle, friction, weight;
    private boolean isSliding = false, isRemovable = false;
    private Sprite detected = null;
    private Point2D.Double xySpeed;
    private Ellipse2D.Double bounds;

    public Sprite(Ellipse2D.Double bounds) {
        this.bounds = bounds;
        this.angle = 0;
        friction = .97; 
        weight = 50;
        xySpeed = new Point2D.Double(0, 0);
    }
    
    public Sprite(Ellipse2D.Double bounds, int weight, double friction) {
        this.angle = Math.PI/2;
        this.weight = weight;
        this.friction = friction;
        this.bounds = bounds;
        xySpeed = new Point2D.Double(0, 0);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Getters
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * Returns true if the sprite should be removed.
     * @return True if the sprite should be removed.
     */
    public boolean isRemovable() { return isRemovable; }
    
    /**
     * Returns true if the sprite bounds contain the point.
     * @param point The point to test for containment.
     * @return True if the sprite bounds contain the point.
     */
    public boolean contains(Point point) {
        return bounds.contains(point.x, point.y);
    }
    
    protected boolean isSliding() { return isSliding; }
    
    protected double getWeight() { return weight; }
    
    protected double getXSpeed() { return xySpeed.x; }
    
    protected double getYSpeed() { return xySpeed.y; }
    
    protected Point2D.Double getXYSpeed() { return xySpeed; }
    
    protected double getAngle() { return angle; }
    
    protected Ellipse2D.Double getBounds() { return bounds; }
    
    protected double getWidth() { return bounds.width; }
    
    protected double getRadius() { return bounds.width/2; }
    
    protected double getMotionAngle() {
        int modifier = (int) Math.copySign(1, xySpeed.y);
        return modifier*Math.acos(xySpeed.x/getSpeed());
    }

    protected Point2D.Double getCenter() {
        return new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
    }
    
    protected double getSpeed() {
        return Math.hypot(xySpeed.x, xySpeed.y);
    }
    
    protected static Rectangle getPlayBounds() { return playBounds; }
    
    /**
     * Returns the color of the drawn part of the Sprite.
     * @return The color of the drawn part of the Sprite.
     */
    public abstract Color getColor();
    
    /**
     * Returns true if the sprite has parts which must be drawn higher or lower.
     * @return True if the sprite has parts which must be drawn higher or lower.
     */
    public abstract boolean isLayered();
    
    ////////////////////////////////////////////////////////////////////////////
    //Setters
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Sets the walls of the play area for all Sprites.
     */
    public static void setPlayBounds(Rectangle playBounds) {
        Sprite.playBounds = playBounds;
    }

    /**
     * Sets whether Sprites bounce off the walls or warp to the other side.
     */
    public static void setBouncyWalls(boolean bouncyWalls) {
        Sprite.hasBouncyWalls = bouncyWalls;
    }

    protected void remove() { 
        bounds = new Ellipse2D.Double( -300,-300,0,0);
        stop();
        weight = 1;
        isRemovable = true; 
    }
    
    protected void slide() { isSliding = true; }
    
    protected void setAngle(double angle) { this.angle = angle; }

    protected void turn(double turn){
        angle += turn;
        if(angle > Math.PI) angle -= Math.PI*2;
        else if(angle < -Math.PI) angle += Math.PI*2;
    }
    
    protected void stop(){
        xySpeed.x = 0;
        xySpeed.y = 0;
    }
    
    protected void addForwardSpeed(double speed){
        addSpeed(angle,speed);
    }
    
    protected void addSpeed(double collisionAngle , double collisionSpeed) {
        Point2D.Double addSpeed = 
            Angles.getXYFromAngRad(collisionAngle, collisionSpeed);
        xySpeed.x += addSpeed.x;
        xySpeed.y += addSpeed.y;
    }
    
    /**
     * Adds speed to the sprite as determined by weightings.
     * @param p
     * @param otherWeight
     */
    public void addSpeed(Point2D.Double p, double otherWeight){
        isSliding=true;
        xySpeed.x += p.x * (otherWeight / weight);
        xySpeed.y += p.y * (otherWeight / weight);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Behavior
    ////////////////////////////////////////////////////////////////////////////
    
    public void stepFoward(){
        if(isRemovable)return;
        xySpeed.y *= friction;
        bounds.y += xySpeed.y;
        xySpeed.x *= friction ;
        bounds.x += xySpeed.x;
        if(getSpeed() > MAX_SPEED){
            double tempAngle = getMotionAngle();
            xySpeed.x = Math.cos(tempAngle) * MAX_SPEED;
            xySpeed.y = Math.sin(tempAngle) * MAX_SPEED;
        }
        if(xySpeed.x < .5 && xySpeed.y < .5 && xySpeed.x > -.5 && xySpeed.y > -.5)
            isSliding = false;
        detectWalls();
    }
    
    protected void collide(Sprite other){
        if(other instanceof AirShot){
            other.collide(this);
            return;
        }
        double hitAngle =  Angles.getAngle(getCenter(), other.getCenter());
        Point2D.Double impact = 
            Angles.getOpXYFromAngRad(hitAngle, other.getSpeed());
        bounds.x = 
            other.getCenter().x + -Math.cos(hitAngle) 
            * (getWidth()/2 + other.getWidth()/2) - getWidth()/2;
        bounds.y = 
            other.getCenter().y + Math.sin(hitAngle) 
            * (getWidth()/2 + other.getWidth()/2) - getWidth()/2;
        xySpeed.x += impact.x;
        xySpeed.y += impact.y;
        impact.x *= -1;
        impact.y *= -1;
        other.addSpeed(impact, weight);
        isSliding=true;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    //Edge Detection
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Detects other Sprites for behavior or collisions.
     * @param other The Sprite to be detected.
     */
    public void detectOther(Sprite other) {
        if(isRemovable) return;
        if( other == detected) return;
        detected = other;
        if(getCenter().distance(other.getCenter()) <= 
            getWidth() / 2 + other.getWidth()/2){
            collide(other);
            other.collide(this);
        }else{
            if(other instanceof Prey)other = (Prey)other;
            if(other instanceof Predator)other = (Predator)other;
            other.detectOther(this);
        }
    }
    
    /**
     * Detects collisions with the walls and reacts accord to hasBouncyWalls.
     */
    public void detectWalls() {
        if(isRemovable) return;
        if(hasBouncyWalls) detectBouncyWalls();
        else detectWrapWalls();
    }
    
    private void detectBouncyWalls() {
        if(bounds.x > playBounds.getMaxX() - bounds.width ){
            bounds.x = playBounds.getMaxX() - bounds.width;
            xySpeed.x = -xySpeed.x;
        }
        if(bounds.x < playBounds.getMinX()){
            bounds.x = playBounds.getMinX();
            xySpeed.x = -xySpeed.x;
        }
        if(bounds.y > playBounds.getMaxY() - bounds.height){
            bounds.y = playBounds.getMaxY() - bounds.height;
            xySpeed.y = -xySpeed.y;
        }
        if(bounds.y < playBounds.getMinY()){
            bounds.y = playBounds.getMinY();
            xySpeed.y = -xySpeed.y;
        }
    }

    private void detectWrapWalls() {
        if(bounds.x > playBounds.getMaxX())
            bounds.x = -bounds.width;
        if(bounds.x < playBounds.getMinX() - bounds.width)
            bounds.x = playBounds.getMaxX();
        if(bounds.y > playBounds.getMaxY())
            bounds.y = -bounds.width;
        if(bounds.y < playBounds.getMinY() - bounds.width)
            bounds.y = playBounds.getMaxY();
    }

    ///////////////////////////////////////////////////////////////////////////
    //Graphics
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Draws the sprite, or in the case of a layered sprite, the bottom layer.
     * @param g The Graphics to draw with.
     */
    public void draw(Graphics2D g){
        if(isRemovable)return;
        g.setStroke(new BasicStroke(3));
        g.setColor(getColor());
        g.fill(getBounds());
        g.setColor(Color.BLACK);
        g.draw(getBounds());
        if(!(this instanceof Faced))return;
        drawFace((Faced) this, FACE, g);
    }

    /**
     * Draws the top layer of a layered sprite.
     * @param g The Graphics to draw with.
     */
    public void drawTopLayer(Graphics2D g) {
        if(isLayered() == false || !(this instanceof Faced))return;
        drawFace((Faced) this, TOP, g);
    }
    
    private void drawFace(Faced f, int layer, Graphics2D g) {
        if(angle!=0)
            g.rotate(-angle + Math.PI/2, getCenter().x, getCenter().y);
        g.drawImage((isSliding) ? f.getFaces()[SLIDING] : f.getFaces()[layer], 
                (int)bounds.x, (int)bounds.y, 
                (int)bounds.width, (int)bounds.height, null);
        if(angle!=0)
            g.rotate(angle - Math.PI/2, getCenter().x, getCenter().y);
    }

}
