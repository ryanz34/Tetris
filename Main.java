import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main extends JFrame implements ActionListener, ComponentListener {

    GamePanel game;
    Menu menu;
    GameOver gameOver;
    Timer myTimer;
    boolean gameRunning = false;
    public static String page = "menu";
    public static int w = 600;
    public static int h = 600;

    public Main() {
        super("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(w, h);
        setMinimumSize(new Dimension(600, 600));
        setLayout(new BorderLayout());

        myTimer = new Timer(50, this);//trigger 20 times per second
        myTimer.start();

        getContentPane().addComponentListener(this);
        setVisible(true);
        //setResizable(false);

        startGraphics();

    }

    public void startGraphics() {
        switch (page) {
            case "menu":
                if (menu == null) {
                    menu = new Menu(this);
                    add(menu);
                    menu.requestFocus();
                    setVisible(true);
                }
                break;

            case "gameOver":
                if (gameOver == null) {
                    gameOver = new GameOver(this);
                    add(gameOver);
                    gameOver.requestFocus();
                    setVisible(true);
                }
                break;

            case "game":
                if (game == null) {
                    game = new GamePanel();
                    add(game);
                    game.requestFocus();
                    setVisible(true);
                }
                break;
        }
    }

    public void startgame() {
        this.getContentPane().remove(menu);
        menu = null;
        page = "game";
    }

    public void exit() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public void componentHidden(ComponentEvent ce) {};
    public void componentShown(ComponentEvent ce) {};
    public void componentMoved(ComponentEvent ce) {};
    public void componentResized(ComponentEvent ce) {
        w = getWidth();
        h = getHeight();
        System.out.println(w + " " + h);
    }

    public void actionPerformed(ActionEvent evt) {
        startGraphics();
        if (game != null) {
            if (game.gameOver) {
                this.getContentPane().remove(game);
                page = "gameOver";
            }
            else {
                game.move();
                game.repaint();
            }
        }
        if (menu != null) {
            menu.requestFocus();
        }
        if (gameOver != null) {
            gameOver.requestFocus();
        }
    }

    public static void main(String[] arguments) {
        Main frame = new Main();
    }
}