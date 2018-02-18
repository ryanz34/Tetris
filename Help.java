import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

/**
 * Help class
 *
 * A copy of the Menu class that display the help screen
 */
public class Help extends JPanel implements KeyListener {

    private Main parent;
    private Font menuFont;

    public Help(Main parent){
        setSize(Main.w, Main.h);
        this.parent = parent;

        InputStream is = Help.class.getResourceAsStream("data/PressStart2P.ttf");  // Getting and creating the font
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);  // Creating the font from an inputstream
            this.menuFont = font.deriveFont(15f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addKeyListener(this);

        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        parent.startMenuFromHelp();  // If anything is pressed then switch back the menu
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.setFont(menuFont);

        g.setColor(Color.BLACK);
        g.fillRect(Main.ox, Main.oy, 600, 600);

        g.setColor(Color.WHITE);
        g.drawString("~How 2 play tetris~", Main.ox + 30, Main.oy + 90); // Displaying the texts
        g.drawString("K so basically u have to get the bar", Main.ox + 30, Main.oy + 120);
        g.drawString("things 2 like line up and wreck 360", Main.ox + 30, Main.oy + 150);
        g.drawString("no scope each other and lik the", Main.ox + 30, Main.oy + 180);
        g.drawString("beats drop xxponentially ya", Main.ox + 30, Main.oy + 210);
        g.drawString("u can press anything to continue", Main.ox + 30, Main.oy + 270);
        g.drawString("RASTERA DEV | rastera.xyz", Main.ox + 30, Main.oy + 370);
        g.drawString("(C) 2018 under WTFPL", Main.ox + 30, Main.oy + 400);
        g.drawString("Notice: This project was basically", Main.ox + 30, Main.oy + 460);
        g.drawString("        made in 15 mins because we", Main.ox + 30, Main.oy + 490);
        g.drawString("        worked on MasseyHacks IV", Main.ox + 30, Main.oy + 520);
        g.drawString("        stuff in class :^)", Main.ox + 30, Main.oy + 550);
    }

}
