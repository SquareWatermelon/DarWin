package darWin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import darWin.sprites.AirShot;
import darWin.sprites.Player;
import darWin.sprites.Predator;
import darWin.sprites.Prey;
import darWin.sprites.Sprite;

/**
 * The field of play for the game of DarWin. 
 * @author Robert "Square Watermelon" Tolda
 *
 */
public class PlayField extends JComponent {
    private static enum Status{ WON, LOST, PLAYING };
    public static final boolean BOUNCYWALLS = true;
    public static final double TILE_PERCENT = 0.125; 
    public static final int START_WIDTH = 600, PLAYER = 0;
    private static final int TIME_AT_END = 300; 
    private static int level = 1, endTimer = 0;
    private List<Sprite> sprites = new ArrayList<Sprite>();
    private BufferedImage bGImage;
    private Status gameEnding = Status.PLAYING;
    private static boolean isPlaying = true;
    private static PlayField p;
    private static View view;
    
    private PlayField(BufferedImage bGImage) {
        this.bGImage = bGImage;
        gameEnding = Status.PLAYING;
    }
    
    private PlayField(List<Sprite> sprites, BufferedImage bGImage) {
        this.sprites = sprites;
        this.bGImage = bGImage;
    }
    
    /**
     * Creates a new PlayField and all the Sprites on it.
     * @precondition imageMap must contain all the images necessary for the game
     * to run.
     * @param imageMap All the images the game needs to run.
     * @param mouseMotion the MouseMotionListener for Controlling the game.
     * @param mouse the MouseListener for Controlling the game.
     * @return A new PlayField containing all the Sprites necessary.
     */
    public static void create(Map<SpriteTypes, BufferedImage> imageMap, 
            MouseListener mouse, 
            MouseMotionListener mouseMotion) {
        p = new PlayField(imageMap.get(SpriteTypes.GRASS));
        p.setSize(START_WIDTH, START_WIDTH);
        p.setPreferredSize(new Dimension(START_WIDTH,START_WIDTH));
        Sprite.setPlayBounds(p.getBounds());
        Player.setPlayField(p);
        p.addSprite(Player.create());
        Predator.setTarget(p.getPlayer());
        AirShot.setPlayer(p.getPlayer());
        p.addSprite(Prey.create());
        for(int i = 1; i <= level; i++){
            p.addSprite(Prey.create());
            p.addSprite(Predator.create());
        }
        p.addMouseListener(mouse);
        p.addMouseMotionListener(mouseMotion);
        view = View.create(p);
    }
    
    private PlayField reCreate() {
        PlayField temp = new PlayField(bGImage);
        temp.setSize(getSize());
        temp.setPreferredSize(this.getPreferredSize());
        Sprite.setPlayBounds(temp.getBounds());
        Player.setPlayField(temp);
        temp.addSprite(Player.create());
        Predator.setTarget(temp.getPlayer());
        AirShot.setPlayer(temp.getPlayer());
        Predator.resetNumber();
        Prey.resetNumber();
        temp.addSprite(Prey.create());
        for(int i = 1; i <= level; i++){
            temp.addSprite(Prey.create());
            temp.addSprite(Predator.create());
        }
        temp.addMouseListener(getMouseListeners()[0]);
        temp.addMouseMotionListener(getMouseMotionListeners()[0]);
        view.replace(temp);
        isPlaying = true;
        return temp;
    }
    
    /**
     * Returns the Player.
     * @return the Player.
     */
    public Player getPlayer() {
        return (Player) sprites.get(PLAYER);
    }
    
    /**
     * Tells the current PlayField to increment all Sprites one step forward and
     * redraw.
     * Also removes all removable sprites and checks for the end of the round.
     */
    public static void  stepFoward(){
        p.step();
    }
    
    /**
     * Tells the PlayField to increment all Sprites one step forward and redraw.
     * Also removes all removable sprites and checks for the end of the round.
     */
    private void step(){
        for(int i = 0; i < sprites.size(); i++){
            sprites.get(i).stepFoward();
            for(int j = i + 1; j < sprites.size(); j++){
                if( sprites.get(j) instanceof Predator 
                        ||  sprites.get(j) instanceof AirShot){
                    //Insure that a Predator or Shot will always determine a 
                    //collision. Shots achieve highest priority since they are
                    //always last
                    sprites.get(j).detectOther(sprites.get(i));
                }else 
                    sprites.get(i).detectOther(sprites.get(j));
            }
            if(sprites.get(i).isRemovable() && i != PLAYER )sprites.remove(i);
        }
        if(sprites.get(PLAYER).isRemovable() || Predator.areAllFull())endGame();
        if(gameEnding != Status.PLAYING)levelEnding();
        view.repaint();
    }

    private void levelEnding() {
        endTimer++;
        if(endTimer == TIME_AT_END){
            setPlayfield(reCreate());
            endTimer = 0;
            gameEnding = Status.PLAYING;
        }
    }

    private static void setPlayfield(PlayField p) {
        PlayField.p = p;
    }

    private void endGame() {
        if(!isPlaying)return;
        if(!sprites.get(PLAYER).isRemovable())
            win();
        else lose();
        isPlaying = false;
    }

    private void win() {
        gameEnding = Status.WON;
        Player.gainLives();
        for(Sprite s : sprites)
            Player.evolve(s);
        level++;

    }

    private void lose() {
        gameEnding = Status.LOST;
        Player.loseLife();
        if(Player.getLives() <= 0){
            level--;
            Player.gainLives();
        }
    }

    //Fix encapsulation later
    /**
     * Adds a sprite to the PlayField.
     * @param newSprite The sprite to be added.
     */
    public void addSprite(Sprite newSprite) {
        sprites.add(newSprite);
    }

    ////////////////////////////////////////////////////////////////////////////
    //Overrides
    ////////////////////////////////////////////////////////////////////////////
        
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        drawBackground(g2);
        for(Sprite sprite : sprites)
            if(sprite.isLayered()) sprite.draw(g2);
        for(Sprite sprite : sprites)
            if(!sprite.isLayered())sprite.draw(g2);
        for(Sprite sprite : sprites)
            if(sprite.isLayered()) sprite.drawTopLayer(g2);
        if(gameEnding != Status.PLAYING) drawEnding(g2);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //Additional Draws
    ////////////////////////////////////////////////////////////////////////////

    private void drawBackground(Graphics2D g2) {
        for(int x = 0; x <= getWidth(); x += getWidth() * TILE_PERCENT)
            for(int y = 0; y <= getWidth(); y += getWidth() * TILE_PERCENT)
                g2.drawImage(bGImage, x, y, 
                        (int) ( getWidth() *TILE_PERCENT), 
                        (int) (getHeight()*TILE_PERCENT), null);
    }
    
    private void drawEnding(Graphics2D g) {
        g.setFont(new Font("Verdana", Font.BOLD, getWidth()/10));
        g.setColor(new Color(200, 50, 50));
        switch(gameEnding){
        case WON:
            g.drawString("You Won!", getWidth()/4, getHeight()*9/16);
            break;
        case LOST:
            g.drawString("You Lose", getWidth()/4, getHeight()*9/16);
            break;
        default:
            g.drawString("Error", getWidth()/4, getHeight()*9/16);
        }
        g.setFont(new Font("Verdana", Font.BOLD, getWidth()/20));
        g.drawString("You have "+ Player.getLives() +" lives left.", getWidth()/4, 
                getHeight()*10/16);
        g.drawString("Round "+ level +" is next.", getWidth()/4, 
                getHeight()*11/16);
    }
}

//@Override
//public PlayField clone(){
//  PlayField p = new PlayField(this.sprites, this.bGImage);
//  p.setSize(this.getSize());
//  p.setPreferredSize(this.getPreferredSize());
//  p.setBackground(Color.BLACK);
//  p.setVisible(true);
//  p.addMouseListener(getMouseListeners()[0]);
//  p.addMouseMotionListener(getMouseMotionListeners()[0]);
//  return p;
//}
