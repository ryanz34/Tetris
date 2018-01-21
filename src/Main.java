import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

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

    private Integer[][] piece;

    private Integer[][][] pieceStates;

    private Integer[][][] Z_STATES = {{{1, 1, 0},
                                       {0, 1, 1}},
                                      {{0, 1},
                                       {1, 1},
                                       {1, 0}}};

    private Integer[][][] I_Z_STATES = {{{0, 1, 1},
                                         {1, 1, 0}},
                                        {{1, 0},
                                         {1, 1},
                                         {0, 1}}};

    private Integer[][][] L_STATES = {{{1, 0},
                                       {1, 0},
                                       {1, 1}},
                                      {{0, 0, 1},
                                       {1, 1, 1}},
                                      {{1, 1},
                                       {0, 1},
                                       {0, 1}},
                                      {{1, 1, 1},
                                       {1, 0, 0}}};

    private Integer[][][] I_L_STATES = {{{0, 1},
                                         {0, 1},
                                         {1, 1}},
                                        {{1, 1, 1},
                                         {0, 0, 1}},
                                        {{1, 1},
                                         {1, 0},
                                         {1, 0}},
                                        {{1, 0, 0},
                                         {1, 1, 1}}};

    private Integer[][][] T_STATES = {{{1, 1, 1},
                                       {0, 1, 0}},
                                      {{1, 0},
                                       {1, 1},
                                       {1, 0}},
                                      {{0, 1, 0},
                                       {1, 1, 1}},
                                      {{0, 1},
                                       {1, 1},
                                       {0, 1}}};

    private Integer[][][] I_STATES = {{{1},
                                       {1},
                                       {1},
                                       {1}},
                                      {{1, 1, 1, 1}}};

    private Integer[][][] O_STATES = {{{1, 1},
                                       {1, 1}}};


    private ArrayList<Integer[][][]> states = new ArrayList<Integer[][][]>() {{
        add(Z_STATES);
        add(I_Z_STATES);
        add(L_STATES);
        add(I_L_STATES);
        add(T_STATES);
        add(I_STATES);
        add(O_STATES);
    }};

    private int stateNum;

    private boolean placed;

    int x, y;
    private Integer[][] board;

    int score = 0;


    public GamePanel() {
        setSize(400, 790);
        board = new Integer[19][10];

        for (int i = 0; i < 19; i ++) {
            for (int j = 0; j < 10; j++) {
                if (j == 0 || j == 9){
                    board[i][j] = 1;
                } else {
                    board[i][j] = 0;
                }
            }
        }

        x = 4;
        y = 0;

        pieceStates = states.get((int) (Math.random()*7));
        stateNum = (int) (Math.random()*pieceStates.length);
        piece = pieceStates[stateNum];

        addKeyListener(this);
        setFocusable(true);

    }

    private boolean arrayintersect () {
        for (int yy = 0; yy < piece.length; yy++) {
            for (int xx = 0; xx < piece[0].length; xx++) {
                if (xx > 9 || yy > 19) {
                    return true;

                } else if (piece[yy][xx] == 1 && board[yy+y][xx+x] == 1) {
                    return true;
                }
            }
        }

        return false;
    }


    //********************************
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int previous_state = stateNum;

        if (e.getKeyCode() == e.VK_LEFT) {
            x -= 1;
            if (arrayintersect()) {
                x++;
            } else {
                repaint();
            }

        } else if (e.getKeyCode() == e.VK_RIGHT) {
            x += 1;

            if (arrayintersect()) {
                x--;
            } else {
                repaint();
            }

        } else if (e.getKeyCode() == e.VK_UP) {
            stateNum ++;
            if (stateNum == pieceStates.length) {
                stateNum = 0;
            }

            piece = pieceStates[stateNum];

            if (arrayintersect()) {
                stateNum = previous_state;
                piece = pieceStates[stateNum];
            } else {
                repaint();
            }
        } else if (e.getKeyCode() == e.VK_DOWN) {
            placed = false;
            while (!placed) {
                move();
            }

            repaint();
        }
    }

    public void keyReleased(KeyEvent e) {
    }
    //********************************

    private void placePiece () {
        for (int yy = 0; yy < piece.length; yy++) {
            for (int xx = 0; xx < piece[0].length; xx++) {
                if (piece[yy][xx] == 1) {
                    board[y+yy][x+xx] = piece[yy][xx];
                }
            }
        }
        y = 0;
        x = 4;

        pieceStates = states.get((int) (Math.random()*7));
        stateNum = (int) (Math.random()*pieceStates.length);
        piece = pieceStates[stateNum];

        placed = true;

        for (int xx = 1; xx < 9; xx++) {
            if (board[0][xx] == 1) {
                System.out.println("U lose");
            }
        }


        //*******

        ArrayList<Integer[]> currentboard = new ArrayList<>(Arrays.asList(board));

        boolean isfull;

        for (int i = 18; i > -1; i--) {
            isfull = true;
            for (int j = 0; j < 10; j++) {
                if (currentboard.get(i)[j] == 0) {
                    isfull = false;
                }
            }

            if (isfull) {
                score ++;
                System.out.println(score);
                currentboard.remove(i);
                currentboard.add(0, new Integer[]{1,0,0,0,0,0,0,0,0,1});
            }
        }


        //****** reconstruct board

        for (int yy = 0; yy < 19; yy++) {
            board[yy] = currentboard.get(yy);
        }

    }


    public void move() {
        y += 1;

        if (y+piece.length > 19 || arrayintersect()) {
            y--;
            placePiece();
        }

    }


    //all drawing code goes here
    public void paintComponent(Graphics g) {

        //System.out.println("Painting");
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);

        for (int yy = 0; yy < piece.length; yy++) {
            for (int xx = 0; xx < piece[0].length; xx++) {
                if (piece[yy][xx] == 1) {
                    g.setColor(Color.WHITE);
                    g.fillRect((x+xx) * 40, (y+yy) * 40, 40, 40);
                    g.setColor(Color.BLACK);
                    g.fillRect((x+xx) * 40+10, (y+yy) * 40+10, 20, 20);
                }
            }
        }


        for (int yy = 0; yy < 19; yy++) {
            for (int xx = 0; xx < 10; xx++) {
                if (board[yy][xx].equals(1)) {
                    g.setColor(Color.WHITE);
                    g.fillRect(xx*40, yy*40, 40, 40);
                    g.setColor(Color.BLACK);
                    g.fillRect(xx * 40+10, yy * 40+10, 20, 20);
                }
            }
        }



    }
}