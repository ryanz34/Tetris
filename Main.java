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

public class Main extends JFrame implements ActionListener {

    GamePanel game;
    Menu menu;
    GameOver gameOver;
    Timer myTimer;
    boolean gameRunning = false;
    public static String page = "menu";

    public Main() {
        super("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        myTimer = new Timer(50, this);//trigger 20 times per second
        myTimer.start();

        setVisible(true);
        setResizable(false);

        startGraphics();

    }

    public void startGraphics() {
        switch (page) {
            case "menu":
                menu = new Menu(this);
                add(menu);
                setVisible(true);
                break;

            case "gameOver":
                gameOver = new GameOver(this);
                add(gameOver);
                setVisible(true);
                break;

            case "game":
                game = new GamePanel();
                add(game);
                setVisible(true);
                break;
        }
    }

    public void startgame() {
        this.getContentPane().remove(menu);
        page = "game";
        startGraphics();
    }

    public void exit() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public void actionPerformed(ActionEvent evt) {
        if (game != null) {
            if (game.gameOver) {
                this.getContentPane().remove(game);
                //page = "gameOver";
                page = "menu";
                startGraphics();
            }
            else {
                game.move();
                game.repaint();
            }
        } else if (menu != null) {
            menu.requestFocus();
        } else if (gameOver != null) {
            gameOver.requestFocus();
        }
    }

    public static void main(String[] arguments) {
        Main frame = new Main();
    }
}