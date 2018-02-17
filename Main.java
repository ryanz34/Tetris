import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame implements ActionListener, ComponentListener {

    GamePanel game;
    Menu menu;
    Help help;
    GameOver gameOver;
    Timer myTimer;
    boolean gameRunning = false;
    public static String page = "menu";
    public static int w = 600;
    public static int h = 600;
    public static int ox = 0;
    public static int oy = 0;

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

            case "help":
                if (help == null) {
                    help = new Help(this);
                    add(help);
                    help.requestFocus();
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
                    game = new GamePanel(this);
                    add(game);
                    game.requestFocus();
                    setVisible(true);
                }
                break;
        }
    }

    public void startMenuFromHelp() {
        this.getContentPane().remove(help);
        help = null;
        page = "menu";
    }

    public void startHelp() {
        this.getContentPane().remove(menu);
        menu = null;
        page = "help";
    }

    public void startGame() {
        this.getContentPane().remove(menu);
        menu = null;
        page = "game";
    }

    public void gameOver() {
        System.out.println("a");
        this.getContentPane().remove(game);
        game = null;
        page = "menu";
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
        ox = (w - 600) / 2;
        oy = (h - 600) / 2;
        System.out.println(w + " " + h);

        if (help != null) {
            help.repaint();
        }
        if (game != null) {
            game.repaint();
        }
        if (menu != null) {
            menu.repaint();
        }
        if (gameOver != null) {
            gameOver.repaint();
        }

    }

    public void actionPerformed(ActionEvent evt) {
        startGraphics();
        if (game != null) {
            game.move();
            game.repaint();
        }
        if (menu != null) {
            menu.requestFocus();
        }
        if (gameOver != null) {
            gameOver.requestFocus();
        } if (help != null) {
            help.repaint();
        }
    }

    public static void main(String[] arguments) {
        Main frame = new Main();
    }
}