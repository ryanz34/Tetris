import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class Main extends JFrame implements ActionListener {

    GamePanel game;
    Menu menu;
    Timer myTimer;
    boolean gameRunning = false;

    public Main() {
        super("Pong game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        setVisible(true);
        startGame();
        setResizable(false);
    }

    public void startGame() {
        myTimer = new Timer(125, this);//trigger 20 times per second
        myTimer.start();

        game = new GamePanel();//creating the panel
        //adding the panel
        add(game);

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

    private boolean fastdrop = false;

    private int tick = 0;

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

    private HashMap<Integer, BufferedImage> blocks = new HashMap<>();

    private int stateNum;

    private boolean placed;

    private int x, y;
    private Integer[][] board;

    private int score = 0;

    private int blockType;

    private int nextBlockType, nextPieceStates, nextStateNum;
    private Integer[][] nextSelectedPiece;


    public GamePanel() {
        setSize(600, 600);

        try {
            blocks.put(1, ImageIO.read(new File("data/bluebrick.png")));
            blocks.put(2, ImageIO.read(new File("data/greenbrick.png")));
            blocks.put(3, ImageIO.read(new File("data/orangebrick.png")));
            blocks.put(4, ImageIO.read(new File("data/purplebrick.png")));
            blocks.put(5, ImageIO.read(new File("data/redbrick.png")));
            blocks.put(6, ImageIO.read(new File("data/tealbrick.png")));
            blocks.put(7, ImageIO.read(new File("data/yellowbrick.png")));
            blocks.put(100, ImageIO.read(new File("data/graybrick.png")));

        } catch (Exception e) {
            e.printStackTrace();
        }

        board = new Integer[19][10];

        for (int i = 0; i < 19; i ++) {
            for (int j = 0; j < 10; j++) {
                if (j == 0 || j == 9){
                    board[i][j] = 100;
                } else {
                    board[i][j] = 0;
                }
            }
        }

        nextBlockType = (int) (Math.random() * 7) + 1;
        nextPieceStates = (int) (Math.random()*7);
        nextStateNum = (int) (Math.random()*states.get(nextPieceStates).length);
        nextSelectedPiece = states.get(nextPieceStates)[nextStateNum];

        blockType = (int) (Math.random() * 7) + 1;
        pieceStates = states.get((int) (Math.random()*7));
        stateNum = (int) (Math.random()*pieceStates.length);
        piece = pieceStates[stateNum];

        x = 4;
        y = 0;

        addKeyListener(this);
        setFocusable(true);

    }

    private boolean arrayintersect () {
        for (int yy = 0; yy < piece.length; yy++) {
            for (int xx = 0; xx < piece[0].length; xx++) {
                if (xx > 9 || yy > 19) {
                    return true;

                } else if (piece[yy][xx] != 0 && board[yy+y][xx+x] != 0) {
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

        System.out.println(e.getKeyCode());

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
        } else if (e.getKeyCode() == e.VK_SPACE) {
            placed = false;
            while (!placed) {
                move();
            }

            repaint();
        } else if (e.getKeyCode() == e.VK_DOWN) {
            fastdrop = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_DOWN) {
            fastdrop = false;
        }
    }
    //********************************

    private void placePiece () {
        for (int yy = 0; yy < piece.length; yy++) {
            for (int xx = 0; xx < piece[0].length; xx++) {
                if (piece[yy][xx] == 1) {
                    board[y+yy][x+xx] = blockType;
                }
            }
        }
        y = 0;
        x = 4;


        blockType = nextBlockType;
        pieceStates = states.get(nextPieceStates);
        stateNum = nextStateNum;
        piece = pieceStates[stateNum];

        nextBlockType = (int) (Math.random() * 7) + 1;
        nextPieceStates = (int) (Math.random()*7);
        nextStateNum = (int) (Math.random()*states.get(nextPieceStates).length);
        nextSelectedPiece = states.get(nextPieceStates)[nextStateNum];

        placed = true;

        for (int xx = 1; xx < 9; xx++) {
            if (board[0][xx] != 0) {
                System.out.println("U lose nigger");
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
            }
        }

        while (currentboard.size() != 19) {
            currentboard.add(0, new Integer[]{100,0,0,0,0,0,0,0,0,100});
        }


        //****** reconstruct board

        for (int yy = 0; yy < 19; yy++) {
            board[yy] = currentboard.get(yy);
        }

    }


    public void move() {
        tick++;

        requestFocus();

        if (tick >= 8 || fastdrop) {
            tick = Math.max(0, tick - 8);
            y += 1;

            if (y + piece.length > 19 || arrayintersect()) {
                y--;
                placePiece();
            }
        }
    }


    //all drawing code goes here
    public void paintComponent(Graphics g) {

        //System.out.println("Painting");
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);


        g.drawRoundRect(300, 0, 200, 200, 10, 10);
        g.drawString("Next Piece", 320, 10);

        g.drawString("Score:", 300, 220);
        g.drawString(Integer.toString(score), 300, 230);


        int nextPieceX = 300 + 100 - nextSelectedPiece[0].length*15;
        int nextPieceY = 100 - nextSelectedPiece.length*15;

        for (int yy = 0; yy < nextSelectedPiece.length; yy++) {
            for (int xx = 0; xx < nextSelectedPiece[0].length; xx++) {
                if (nextSelectedPiece[yy][xx] == 1) {
                    g.drawImage(blocks.get(nextBlockType), xx*30 + nextPieceX, yy*30 + nextPieceY, 30, 30, null);
                }
            }
        }

        for (int yy = 0; yy < piece.length; yy++) {
            for (int xx = 0; xx < piece[0].length; xx++) {
                if (piece[yy][xx] != 0) {
                    g.drawImage(blocks.get(blockType), (x+xx)*30, (y+yy)*30, 30, 30, null);
                }
            }
        }

        for (int yy = 0; yy < 19; yy++) {
            for (int xx = 0; xx < 10; xx++) {
                if (!board[yy][xx].equals(0)) {
                    g.drawImage(blocks.get(board[yy][xx]), xx*30, yy*30, 30, 30, null);
                }
            }
        }
    }
}