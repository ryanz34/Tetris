import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.util.Random;

public class Main extends JFrame implements ActionListener, ComponentListener {

    // Defining the panels
    GamePanel game;
    Menu menu;
    Help help;

    Timer myTimer;

    public static String page = "menu"; // Checking which page we are currently on to switch JPanels
    public static int w = 600; // The current height and width of the JFrame
    public static int h = 630;
    public static int ox = 0; // The offset needed to center the JPanel on the JFrame
    public static int oy = 0;

    public Main() {
        super("Tetris");  // Sets the title of the JFrame to Tetris
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(w, h);  // Setting the size to the proper size
        setMinimumSize(new Dimension(600, 630));  // Set a minimum size so the game wont be cut off
        setLayout(new BorderLayout());

        myTimer = new Timer(50, this);//trigger 20 times per second
        myTimer.start();

        getContentPane().addComponentListener(this);  // Adding a component listener to listen for resizing
        setVisible(true);

        startGraphics();  // Switching to the right page

        Random r = new Random();  // Creating a random to play a random sound each time

        try {
            // Sketchy audio loading
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(r.nextBoolean() ? "data/soviet.wav" : "data/tetris.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.loop(clip.LOOP_CONTINUOUSLY);  // Looping the sound continuously
        } catch (Exception e) {

        }

    }

    public void startGraphics() {
        switch (page) {  // Checks for which page we are currently on
            case "menu":
                if (menu == null) {  // Check to see if that page is currently active. If not, switch to that JPanel
                    menu = new Menu(this);  // Create a new Menu
                    add(menu);  // Adding the JPanel
                    menu.requestFocus();
                    setVisible(true);  // Make the JPanel visible
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

    public void startMenuFromHelp() { // The following 3 functions switches the JPanel
        this.getContentPane().remove(help);  // Remove the current panel from the JFrame
        help = null;  // Setting the page to null to have the GC clean it
        page = "menu";  // Setting the page to the right one
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
        this.getContentPane().remove(game);
        game = null;
        page = "menu";
    }

    public void exit() {  // Exits the game
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));  // Dispatches an event that simulates a user clicking the x button
    }

    public void componentHidden(ComponentEvent ce) {
    }

    ;

    public void componentShown(ComponentEvent ce) {
    }

    ;

    public void componentMoved(ComponentEvent ce) {
    }

    ;

    public void componentResized(ComponentEvent ce) {  // Listens for when the component resize
        w = getWidth();  // Sets the vars to the correct value and calculates the offset
        h = getHeight();
        ox = (w - 600) / 2;
        oy = (h - 630) / 2;

        // Repaints each of the JPanel for seamless resize
        if (help != null) {
            help.repaint();
        }
        if (game != null) {
            game.repaint();
        }
        if (menu != null) {
            menu.repaint();
        }

    }

    public void actionPerformed(ActionEvent evt) {  // Listens for the timer
        startGraphics();  // Check if on the right page
        if (game != null) { // Update each page
            game.move();  // Move the piece
            game.repaint();  // Repaints the game
        }
        if (menu != null) {
            menu.requestFocus();
        }
        if (help != null) {
            help.repaint();
        }
    }

    public static void main(String[] arguments) {
        Main frame = new Main();  // Starts the game
    }
}