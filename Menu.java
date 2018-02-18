import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import static java.lang.Math.max;

public class Menu extends JPanel implements KeyListener {

    private Main parent;  // The JFrame which this Menu was created by
    private BufferedImage menuImage; // The images that the menu will use
    private BufferedImage selecter; // A selector
    private BufferedImage background;  // Loading the images
    private Font menuFont;  // The 8-bit font

    private int selecter_option = 0;  // The location where the selector is on

    public Menu(Main parent) {
        setSize(Main.w, Main.h);
        this.parent = parent;

        InputStream is = Help.class.getResourceAsStream("data/PressStart2P.ttf");  // Loads the font using InputStream
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is); // Creating a font
            this.menuFont = font.deriveFont(20f);  // Deriving the font we just created because it has a size of 0
            background = ImageIO.read(new File("data/propaganda.png"));
            menuImage = ImageIO.read(new File("data/menu/menu.png"));  // Loading the images using imageIO
            selecter = ImageIO.read(new File("data/menu/selecter.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        addKeyListener(this); // Adding a keylistener to the JPanel

        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) { // Listens for Keyboard input
        if (e.getKeyCode() == e.VK_UP) {
            if (selecter_option > 0) {  // If the slider can move down then move it
                selecter_option -= 1;
                repaint();
            }
        } else if (e.getKeyCode() == e.VK_DOWN) {
            if (selecter_option < 2) {  // Same as above but for moving up the selector
                selecter_option += 1;
                repaint();
            }
        } else if (e.getKeyCode() == e.VK_ENTER) {  // Select an option
            if (selecter_option == 0) {  // Start game
                removeKeyListener(this);  // Removes the keylistener so we will trigger the right Panel
                parent.startGame();  // Starting the game
            } else if (selecter_option == 1) {
                removeKeyListener(this);
                parent.startHelp();

            } else if (selecter_option == 2) {
                parent.exit();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.setFont(menuFont);  // Setting the current font

        g.drawImage(background, 0, 0, max(Main.w, Main.h), max(Main.w, Main.h), this);  // Draws the background
        g.drawImage(menuImage, Main.ox, Main.oy, 600, 600, this);

        g.setColor(Color.BLACK);

        g.drawString("Start", Main.ox + 152, Main.oy + 344);  // Drawing the shadow for each text using the offset
        g.drawString("Help", Main.ox + 152, Main.oy + 387);
        g.drawString("Quit", Main.ox + 152, Main.oy + 430);

        g.setColor(Color.WHITE);

        g.drawString("Start", Main.ox + 150, Main.oy + 342);
        g.drawString("Help", Main.ox + 150, Main.oy + 385);
        g.drawString("Quit", Main.ox + 150, Main.oy + 428);

        g.drawImage(selecter, Main.ox + 100, Main.oy + 317 + 43 * selecter_option, selecter.getWidth(), selecter.getHeight(), this); // Drawing the selector

    }

}
