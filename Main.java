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
    Timer myTimer;
    boolean gameRunning = false;
    String page = "game";

    public Main() {
        super("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        setVisible(true);
        setResizable(false);

        startGraphics();

    }

    public void startGraphics() {
        myTimer = new Timer(50, this);//trigger 20 times per second
        myTimer.start();

        switch (page) {
            case "menu":
                menu = new Menu();
                add(menu);
                break;

            case "game":
                game = new GamePanel();
                add(game);
                break;
        }
    }

    public void actionPerformed(ActionEvent evt) {
        if (game != null) {
                game.move();
                game.repaint();
        }
    }

    public static void main(String[] arguments) {
        Main frame = new Main();
    }
}