import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

public class Main extends JFrame implements ActionListener {

    GamePanel game;

    Timer myTimer;

    public Main() {
        super("Pong game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 790);
        setLayout(new BorderLayout());


        game = new GamePanel();//creating the panel
        //adding the panel
        add(game);

        setVisible(true);

        myTimer = new Timer(1000, this);//trigger 20 times per second
        myTimer.start();
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
}//*************************************************************

class GamePanel extends JPanel implements KeyListener {

    private int[][] piece = {{1}};
    private int piecex, piecey;

    int x, y;
    private boolean[] keys;
    private int[][] board;


    public GamePanel() {
        setSize(400, 790);
        keys = new boolean[KeyEvent.KEY_LAST + 1];
        board =new int[10][19];

        for (int i = 0; i < 19; i ++) {
            board[0][i] = 1;
            board[9][i] = 1;
        }

        x = 4;
        y = 0;


        addKeyListener(this);

    }


    //********************************
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_LEFT) {
            x -= 1;
            repaint();
        } else if (e.getKeyCode() == e.VK_RIGHT) {
            x += 1;
            repaint();
        }
    }

    public void keyReleased(KeyEvent e) {
    }
    //********************************


    public void move() {

        requestFocus();//without this line the program
        //wouldn't be able to "listen" to the key events
        y += 1;

        if (y == 18) {
            for (int xx = 0; xx < piece.length; xx++) {
                for (int yy = 0; yy < piece[0].length; yy++) {
                    board[x + xx][y + yy] = piece[xx][yy];
                }
            }
            y = 0;
            x = 4;
        }

    }


    //all drawing code goes here
    public void paintComponent(Graphics g) {

        //System.out.println("Painting");
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);

        for (int xx = 0; xx < piece.length; xx++) {
            for (int yy = 0; yy < piece[0].length; yy++) {
                if (piece[xx][yy] == 1) {
                    g.fillRect((x+xx) * 40, (y+yy) * 40, 40, 40);
                }
            }
        }


        for (int xx = 0; xx < 10; xx++) {
            for (int yy = 0; yy < 19; yy++) {
                if (board[xx][yy] == 1) {
                    g.setColor(Color.WHITE);
                    g.fillRect(xx*40, yy*40, 40, 40);
                }
            }
        }



    }
}