package darWin;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import darWin.sprites.Player;

/**
 * The listeners for controlling the player character mid-game.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class Controls {
    private static final int CURSOR_WEIGHT = 50;
    private static List<Point> mice = new LinkedList<Point>();
    
    /**
     * Creates a MouseMotionListener which gets average drag speed on the 
     * PlayField and transfers it to the player upon collision.
     * @return A MouseMotionListener which gets average drag speed on the 
     * PlayField and transfers it to the player upon collision.
     */
    public static MouseMotionListener createMouseMotion() {
        return new MouseMotionAdapter(){
            
            @Override
            public void mouseDragged(MouseEvent e) {
                PlayField source = (PlayField) e.getSource();
                if(source.getPlayer().contains(e.getPoint())){
                    Point2D.Double xySpeed = getAverageSpeed();
                    source.getPlayer().addSpeed(xySpeed, CURSOR_WEIGHT);
                    mice.clear();
                    return;
                }
                if(mice.size() == 10) mice.remove(0);
                mice.add(e.getPoint());
            }

            private Point2D.Double getAverageSpeed() {
                Point2D.Double out = new Point2D.Double( 0, 0);
//                out.x = mice.get(0).x;
//                out.y = mice.get(0).y;
                for(int i = 1; i < mice.size() - 1; i++){
                    out.x += mice.get(i + 1).x - mice.get(i).x;
                    out.x /= 2;
                    out.y += mice.get(i + 1).y - mice.get(i).y;
                    out.y /= 2;
                }
                return out;
            }
        };
    }
    
    /**
     * Creates a MouseListener which activates player abilities and helps the
     * MouseMotionListener.
     * @return A MouseListener which activates player abilities and helps the
     * MouseMotionListener.
     */
    public static MouseListener createMouse() {
        return new MouseAdapter(){
            
            @Override
            public void mousePressed(MouseEvent e) {
                PlayField source = (PlayField) e.getSource();
                Player player = (Player) source.getPlayer();
                if(Player.hasHandle() && player.contains(e.getPoint())){
                    player.stop();
                }
//                if(player.hasTail() && player.getSpeed() < .01){
//                    player.startTail();
//                }
                mice.add(e.getPoint());
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                PlayField source = (PlayField) e.getSource();
                Player player = (Player) source.getPlayer();
                if(Player.hasAirShot() && !player.contains(e.getPoint())){
                    player.shootAir(new Point2D.Double(e.getX(),e.getY()));
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                mice.clear();
            }
        };   
    }
}
