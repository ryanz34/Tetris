import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameOver extends JPanel implements KeyListener {

    private Main parent;
    private BufferedImage menuImage;
    private BufferedImage selecter;
    private Font menuFont;

    private int selecter_option = 0;

    public GameOver (Main parent){
        setSize(600, 600);
        this.parent = parent;

        InputStream is = GameOver.class.getResourceAsStream("data/PressStart2P.ttf");
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            this.menuFont = font.deriveFont(20f);

            menuImage = ImageIO.read(new File("data/menu/menu.png"));
            selecter = ImageIO.read(new File("data/menu/selecter.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        addKeyListener(this);

        repaint();
    }

    public void focus () {
        requestFocus();
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_UP) {
            if (selecter_option > 0) {
                selecter_option -= 1;
            }
        } else if (e.getKeyCode() == e.VK_DOWN) {
            if (selecter_option < 2) {
                selecter_option += 1;
            }
        } else if (e.getKeyCode() == e.VK_ENTER) {
            if (selecter_option == 0) {
                parent.startgame();
            } else if (selecter_option == 2) {
                parent.exit();
            }
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.setFont(menuFont);

        g.drawImage(menuImage, 0, 0, 600, 600,null);

        g.setColor(Color.BLACK);

        g.drawString("Start", 152, 344);
        g.drawString("Help", 152, 387);
        g.drawString("Quit", 152, 430);

        System.out.println("U lose");

        g.setColor(Color.WHITE);

        g.drawString("Start", 150, 342);
        g.drawString("Help", 150, 385);
        g.drawString("Quit", 150, 428);

        g.drawImage(selecter, 100, 317 + 43 * selecter_option, selecter.getWidth(), selecter.getHeight(), null);

    }

}
