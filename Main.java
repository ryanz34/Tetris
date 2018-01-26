import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.applet.*;
import java.net.URL;

public class Main extends JFrame implements ActionListener {

    GamePanel game;
    Menu menu;
    Timer myTimer;
    boolean gameRunning = false;

    public Main() {
        super("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        setVisible(true);
        startGame();
        setResizable(false);
    }

    public void startGame() {

        /*
        URL urlSound = Main.class.getResouce("soviet.wav");
        sound = Applet.newAudioClip(urlSound);
        sound.play(); */

        myTimer = new Timer(50, this);//trigger 20 times per second
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

class piece{
    // Raw blocks
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

    // Blocks
    private ArrayList<Integer[][][]> states = new ArrayList<Integer[][][]>() {{
        add(Z_STATES);
        add(I_Z_STATES);
        add(L_STATES);
        add(I_L_STATES);
        add(T_STATES);
        add(I_STATES);
        add(O_STATES);
    }};

    public int x, y;

    public Integer[][] piece;
    public int stateNum;
    public Integer[][][] pieceStates;
    public int blockType;

    public piece() {
        blockType = (int) (Math.random() * 7) + 1;
        pieceStates = states.get((int) (Math.random()*7));
        stateNum = (int) (Math.random()*pieceStates.length);
        piece = pieceStates[stateNum];

        x = 4;
        y = 0;
    }

    public int height() {
        return piece.length;
    }

    public int width() {
        return piece[0].length;
    }

}

class GamePanel extends JPanel implements KeyListener {

    // Clock stuff
    private boolean fastdrop = false;
    private int tick = 0;

    private HashMap<Integer, BufferedImage> blocks = new HashMap<>();

    private boolean placed;

    private int yp;
    private Integer[][] board;

    private int score = 0;

    private piece currentPiece, nextPiece;


    public GamePanel() {
        setSize(600, 600);

        // Load images
        try {
            blocks.put(1, ImageIO.read(new File("data/bluebrick.png")));
            blocks.put(2, ImageIO.read(new File("data/greenbrick.png")));
            blocks.put(3, ImageIO.read(new File("data/orangebrick.png")));
            blocks.put(4, ImageIO.read(new File("data/purplebrick.png")));
            blocks.put(5, ImageIO.read(new File("data/redbrick.png")));
            blocks.put(6, ImageIO.read(new File("data/tealbrick.png")));
            blocks.put(7, ImageIO.read(new File("data/yellowbrick.png")));
            blocks.put(100, ImageIO.read(new File("data/graybrick.png")));

            // Sketchy audio loading
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("soviet.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

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

        currentPiece = new piece();
        nextPiece = new piece();

        addKeyListener(this);
        setFocusable(true);
        previewUpdate();

    }

    private void previewUpdate() {
        for (int ypp = currentPiece.y; ypp < 20; ypp++) {
            if (ypp + currentPiece.height() < 20 && !arrayintersect(currentPiece.x, ypp, currentPiece, board)) {
                yp = ypp;
            }
            else {
                break;
            }
        }
    }

    private boolean arrayintersect(int x, int y, piece checkPiece, Integer[][] board) {
        for (int yy = 0; yy < checkPiece.height(); yy++) {
            for (int xx = 0; xx < checkPiece.width(); xx++) {
                if (xx > 9 || yy > 19) {
                    return true;

                } else if (checkPiece.piece[yy][xx] != 0 && board[yy+y][xx+x] != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int previous_state = currentPiece.stateNum;

        //System.out.println(e.getKeyCode());

        if (e.getKeyCode() == e.VK_LEFT) {
            currentPiece.x -= 1;
            if (arrayintersect(currentPiece.x, currentPiece.y, currentPiece, board)) {
                currentPiece.x++;
            } else {
                repaint();
            }

            previewUpdate();

        } else if (e.getKeyCode() == e.VK_RIGHT) {
            currentPiece.x+= 1;

            if (arrayintersect(currentPiece.x, currentPiece.y, currentPiece, board)) {
                currentPiece.x--;
            } else {
                repaint();
            }

            previewUpdate();

        } else if (e.getKeyCode() == e.VK_UP) {
            currentPiece.stateNum ++;
            if (currentPiece.stateNum == currentPiece.pieceStates.length) {
                currentPiece.stateNum = 0;
            }

            currentPiece.piece = currentPiece.pieceStates[currentPiece.stateNum];

            if (arrayintersect(currentPiece.x, currentPiece.y, currentPiece, board)) {
                currentPiece.stateNum = previous_state;
                currentPiece.piece = currentPiece.pieceStates[currentPiece.stateNum];
            } else {
                repaint();
            }

            previewUpdate();

        } else if (e.getKeyCode() == e.VK_SPACE) {
            placed = false;
            while (!placed) {
                move();
            }

            previewUpdate();

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

    private void placePiece () {
        for (int yy = 0; yy < currentPiece.height(); yy++) {
            for (int xx = 0; xx < currentPiece.width(); xx++) {
                if (currentPiece.piece[yy][xx] == 1) {
                    board[currentPiece.y+yy][currentPiece.x+xx] = currentPiece.blockType;
                }
            }
        }

        currentPiece = nextPiece;

        nextPiece = new piece();

        placed = true;

        for (int xx = 1; xx < 9; xx++) {
            if (board[0][xx] != 0) {
                System.out.println("U lose nigger");
            }
        }

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


        for (int yy = 0; yy < 19; yy++) {
            board[yy] = currentboard.get(yy);
        }

        previewUpdate();

    }


    public void move() {
        tick++;

        requestFocus();

        if (tick >= 8 || fastdrop) {
            tick = Math.max(0, tick - 8);
            currentPiece.y += 1;

            if (currentPiece.y + currentPiece.height() > 19 || arrayintersect(currentPiece.x, currentPiece.y, currentPiece, board)) {
                currentPiece.y--;
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

        // Block Preview

        g.drawRoundRect(300, 0, 200, 200, 10, 10);
        g.drawString("Next Piece", 320, 10);

        g.drawString("Score:", 300, 220);
        g.drawString(Integer.toString(score), 300, 230);


        int nextPieceX = 300 + 100 - nextPiece.width()*15;
        int nextPieceY = 100 - nextPiece.height()*15;

        for (int yy = 0; yy < nextPiece.height(); yy++) {
            for (int xx = 0; xx < nextPiece.width(); xx++) {
                if (nextPiece.piece[yy][xx] == 1) {
                    g.drawImage(blocks.get(nextPiece.blockType), xx*30 + nextPieceX, yy*30 + nextPieceY, 30, 30, null);
                }
            }
        }

        // Active block
        for (int yy = 0; yy < currentPiece.height(); yy++) {
            for (int xx = 0; xx < currentPiece.width(); xx++) {
                if (currentPiece.piece[yy][xx] != 0) {
                    g.fillRect((currentPiece.x+xx)*30, (yp+yy)*30, 30, 30);
                    g.drawImage(blocks.get(currentPiece.blockType), (currentPiece.x+xx)*30, (currentPiece.y+yy)*30, 30, 30, null);
                }
            }
        }

        // Fixed board
        for (int yy = 0; yy < 19; yy++) {
            for (int xx = 0; xx < 10; xx++) {
                if (!board[yy][xx].equals(0)) {
                    g.drawImage(blocks.get(board[yy][xx]), xx*30, yy*30, 30, 30, null);
                }
            }
        }
    }
}