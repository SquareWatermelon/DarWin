package darWin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import darWin.sprites.Player;
import darWin.sprites.Predator;
import darWin.sprites.Prey;

/**
 * The Main Controller for the game DarWin.
 * @author Robert "Square Watermelon" Tolda
 * 
 */
public class MainController {
    public static final int DELAY = 8;
    private static PlayField gameField;
    private static Timer t;
    
    /**
     * Starts and runs the game DarWin.
     * @param args Not used.
     */
    public static void main(String[] args) {
        Map<SpriteTypes, BufferedImage> imageMap;
        try {
            imageMap = createImageMap();
            mapImages(imageMap);
            PlayField.create(imageMap,
                    Controls.createMouse(),
                    Controls.createMouseMotion());
            t = new Timer(DELAY, createStep());
            t.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "A necessary file was not found.",
                    "File Not Found!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void mapImages(Map<SpriteTypes, BufferedImage> imageMap) {
        Player.setFaces(new BufferedImage[]{
                imageMap.get(SpriteTypes.PLAYER), 
                imageMap.get(SpriteTypes.PLAYER)});
        Prey.setFaces(new BufferedImage[]{
                imageMap.get(SpriteTypes.PREY), 
                imageMap.get(SpriteTypes.PREY_SLIDE)});
        Predator.setFaces(new BufferedImage[]{ 
                imageMap.get(SpriteTypes.PREDATOR), 
                imageMap.get(SpriteTypes.PREDATOR_SLIDE),
                imageMap.get(SpriteTypes.PREDATOR_HAPPY),
                imageMap.get(SpriteTypes.PREDATOR_TOP)});
    }

    private static Map<SpriteTypes, BufferedImage> createImageMap() throws IOException {
        Map<SpriteTypes, BufferedImage> imageMap = 
            new HashMap<SpriteTypes, BufferedImage>();
        imageMap.put(SpriteTypes.PLAYER, ImageIO.read(new File("sprites/Player.png")));
        imageMap.put(SpriteTypes.PREY, ImageIO.read(new File("sprites/PreyHappy.png")));
        imageMap.put(SpriteTypes.PREY_SLIDE, ImageIO.read(new File("sprites/PreySad.png")));
        imageMap.put(SpriteTypes.PREDATOR, ImageIO.read(new File("sprites/PredatorBottom.png")));
        imageMap.put(SpriteTypes.PREDATOR_TOP, ImageIO.read(new File("sprites/PredatorTop.png")));
        imageMap.put(SpriteTypes.PREDATOR_SLIDE, ImageIO.read(new File("sprites/PredatorSlide.png")));
        imageMap.put(SpriteTypes.PREDATOR_HAPPY, ImageIO.read(new File("sprites/PredatorHappy.png")));
        imageMap.put(SpriteTypes.GRASS, ImageIO.read(new File("textures/grass1.jpg")));
        imageMap.put(SpriteTypes.DIRT, ImageIO.read(new File("textures/grass1.jpg")));
        return imageMap;
    }
    
    private static ActionListener createStep() {
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                PlayField.stepFoward();
            }
        };
    }

}
