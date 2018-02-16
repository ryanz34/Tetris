import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

public class Menu extends JPanel implements KeyListener {

    private Main parent;
    private BufferedImage menuImage;
    private BufferedImage selecter;
    private Font menuFont;

    private int selecter_option = 0;

    public Menu (Main parent){
        setSize(Main.w, Main.h);
        this.parent = parent;

        InputStream is = Help.class.getResourceAsStream("data/PressStart2P.ttf");
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(selecter_option);
        if (e.getKeyCode() == e.VK_UP) {
            if (selecter_option > 0) {
                selecter_option -= 1;
                repaint();
            }
        } else if (e.getKeyCode() == e.VK_DOWN) {
            if (selecter_option < 2) {
                selecter_option += 1;
                repaint();
            }
        } else if (e.getKeyCode() == e.VK_ENTER) {
            if (selecter_option == 0) {
                removeKeyListener(this);
                parent.startGame();
            }
            else if (selecter_option == 1) {
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
        g.setFont(menuFont);

        g.drawImage(menuImage, Main.ox, Main.oy, 600, 600,null);

        g.setColor(Color.BLACK);

        g.drawString("Start", Main.ox + 152, Main.oy + 344);
        g.drawString("Help", Main.ox + 152, Main.oy + 387);
        g.drawString("Quit", Main.ox + 152, Main.oy + 430);

        g.setColor(Color.WHITE);

        g.drawString("Start", Main.ox + 150, Main.oy + 342);
        g.drawString("Help", Main.ox + 150, Main.oy + 385);
        g.drawString("Quit", Main.ox + 150, Main.oy + 428);

        g.drawImage(selecter, Main.ox + 100, Main.oy + 317 + 43 * selecter_option, selecter.getWidth(), selecter.getHeight(), null);

    }

}
