import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

public class Help extends JPanel implements KeyListener {

    private Main parent;
    private BufferedImage helpImage;
    private BufferedImage selecter;
    private Font menuFont;

    public Help(Main parent){
        setSize(Main.w, Main.h);
        this.parent = parent;

        InputStream is = Help.class.getResourceAsStream("data/PressStart2P.ttf");
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            this.menuFont = font.deriveFont(20f);

            helpImage = ImageIO.read(new File("data/kremlin.png"));
            selecter = ImageIO.read(new File("data/menu/selecter.png"));
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

        if (e.getKeyCode() == e.VK_ENTER) {
            parent.startMenuFromHelp();
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.setFont(menuFont);

        g.setColor(Color.WHITE);
        g.fillRect(Main.ox, Main.oy, 600, 600);

        g.drawImage(helpImage, Main.ox, Main.oy, 600, 600,null);

        g.setColor(Color.BLACK);

        g.drawString("Back", Main.ox + 152, Main.oy + 430);

        g.setColor(Color.WHITE);

        g.drawString("Back", Main.ox + 150, Main.oy + 428);

        g.drawImage(selecter, Main.ox + 100, Main.oy + 403, selecter.getWidth(), selecter.getHeight(), null);

    }

}
