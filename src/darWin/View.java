package darWin;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * The JFrame for a game. Will be more sophisticated later on.
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class View extends JFrame {
    private static View v;
    private static PlayField p;

    private View() throws HeadlessException {
    }

    /**
     * Creates a new JFrame for the game.
     * @param gameField the PlayField in which the game is played.
     * @return a new JFrame for the game.
     */
    public static View create(PlayField gameField){
        p = gameField;
        v = new View();
        v.setLayout(new BorderLayout());
        v.add(p, BorderLayout.CENTER);
        v.setVisible(true);
        v.paintAll(v.getGraphics());
        v.pack();
        v.setDefaultCloseOperation(EXIT_ON_CLOSE);
        return v;
    }

    public void replace(PlayField newField) {
        v.remove(p);
        p = newField;
        v.add(p, BorderLayout.CENTER);
    }
    
}
