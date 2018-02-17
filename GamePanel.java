import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class GamePanel extends JPanel implements KeyListener {

    // Clock stuff
    public boolean gameOver = false;
    private boolean fastDrop = false;
    private int tick = 0;

    private BufferedImage background;
    private HashMap<Integer, BufferedImage> blocks = new HashMap<>();
    private HashMap<Integer, BufferedImage> blocksPreview = new HashMap<>();
    private int totalTick = 0;
    private int ppm = 0;

    private boolean placed;

    private double speed = 8;
    private int yPreview;
    private Integer[][] board;

    private int score = 0;

    private Piece currentPiece, nextPiece;
    private Font gameFont, gameOverFont;
    private boolean pause = false;
    private Main parent;

    public GamePanel(Main parent) {
        setSize(Main.w, Main.h);
        this.parent = parent;

        // Load images
        try {
            InputStream is = GamePanel.class.getResourceAsStream("data/PressStart2P.ttf");

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            gameFont = font.deriveFont(18f);
            gameOverFont = font.deriveFont(30f);

            background = ImageIO.read(new File("data/kremlin.png"));
            blocks.put(1, ImageIO.read(new File("data/bluebrick.png")));
            blocks.put(2, ImageIO.read(new File("data/greenbrick.png")));
            blocks.put(3, ImageIO.read(new File("data/orangebrick.png")));
            blocks.put(4, ImageIO.read(new File("data/purplebrick.png")));
            blocks.put(5, ImageIO.read(new File("data/redbrick.png")));
            blocks.put(6, ImageIO.read(new File("data/tealbrick.png")));
            blocks.put(7, ImageIO.read(new File("data/yellowbrick.png")));
            blocks.put(100, ImageIO.read(new File("data/graybrick.png")));

            blocksPreview.put(1, ImageIO.read(new File("data/bluebrickpreview.png")));
            blocksPreview.put(2, ImageIO.read(new File("data/greenbrickpreview.png")));
            blocksPreview.put(3, ImageIO.read(new File("data/orangebrickpreview.png")));
            blocksPreview.put(4, ImageIO.read(new File("data/purplebrickpreview.png")));
            blocksPreview.put(5, ImageIO.read(new File("data/redbrickpreview.png")));
            blocksPreview.put(6, ImageIO.read(new File("data/tealbrickpreview.png")));
            blocksPreview.put(7, ImageIO.read(new File("data/yellowbrickpreview.png")));
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

        currentPiece = new Piece();
        nextPiece = new Piece();

        addKeyListener(this);
        setFocusable(true);
        previewUpdate();

    }

    public int getScore() {
        return score;
    }

    private void previewUpdate() {
        for (int ypp = currentPiece.y; ypp < 20; ypp++) {
            if (ypp + currentPiece.height() < 20 && !arrayIntersect(currentPiece.x, ypp, currentPiece, board)) {
                yPreview = ypp;
            }
            else {
                break;
            }
        }
    }

    private boolean arrayIntersect(int x, int y, Piece checkPiece, Integer[][] board) {
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
        System.out.println(e.getKeyCode());
        if (!pause && !gameOver) {
            if (e.getKeyCode() == e.VK_LEFT) {
                currentPiece.x -= 1;
                if (arrayIntersect(currentPiece.x, currentPiece.y, currentPiece, board)) {
                    currentPiece.x++;
                } else {
                    repaint();
                }

                previewUpdate();

            } else if (e.getKeyCode() == e.VK_RIGHT) {
                currentPiece.x += 1;

                if (arrayIntersect(currentPiece.x, currentPiece.y, currentPiece, board)) {
                    currentPiece.x--;
                } else {
                    repaint();
                }

                previewUpdate();

            } else if (e.getKeyCode() == e.VK_UP) {
                int previous_state = currentPiece.stateNum;
                currentPiece.stateNum++;
                if (currentPiece.stateNum == currentPiece.pieceStates.length) {
                    currentPiece.stateNum = 0;
                }

                currentPiece.piece = currentPiece.pieceStates[currentPiece.stateNum];

                if (arrayIntersect(currentPiece.x, currentPiece.y, currentPiece, board) || currentPiece.y + currentPiece.height() >= 18) {
                    currentPiece.stateNum = previous_state;
                    currentPiece.piece = currentPiece.pieceStates[currentPiece.stateNum];
                } else {
                    repaint();
                }

                previewUpdate();

            } else if (e.getKeyCode() == e.VK_SPACE) {
                placed = false;
                int currenttick = totalTick;
                while (!placed) {
                    move();
                }

                ppm = 60000 / (currenttick * 15);

                previewUpdate();

                repaint();
            } else if (e.getKeyCode() == e.VK_DOWN) {
                fastDrop = true;
            }
        }
        if (e.getKeyCode() == e.VK_ESCAPE) {
            if (gameOver) {
                removeKeyListener(this);
                parent.gameOver();
                System.out.println("ga");
            } else {
                pause = !pause;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_DOWN) {
            fastDrop = false;
        }
    }

    private void placePiece () {
        ppm = 60000 / (totalTick * 15);
        totalTick = 0;
        for (int yy = 0; yy < currentPiece.height(); yy++) {
            for (int xx = 0; xx < currentPiece.width(); xx++) {
                if (currentPiece.piece[yy][xx] == 1) {
                    board[currentPiece.y+yy][currentPiece.x+xx] = currentPiece.blockType;
                }
            }
        }

        currentPiece = nextPiece;
        nextPiece = new Piece();
        placed = true;

        for (int xx = 1; xx < 9; xx++) {
            if (board[0][xx] != 0) {
                gameOver = true;
                break;
            }
        }

        ArrayList<Integer[]> currentBoard = new ArrayList<>(Arrays.asList(board));

        boolean isFull;

        for (int i = 18; i > -1; i--) {
            isFull = true;
            for (int j = 0; j < 10; j++) {
                if (currentBoard.get(i)[j] == 0) {
                    isFull = false;
                }
            }

            if (isFull) {
                speed = 8 * Math.pow(0.99, score);
                score ++;
                System.out.println(speed + " // " + score);
                currentBoard.remove(i);
            }
        }

        while (currentBoard.size() != 19) {
            currentBoard.add(0, new Integer[]{100,0,0,0,0,0,0,0,0,100});
        }

        for (int yy = 0; yy < 19; yy++) {
            board[yy] = currentBoard.get(yy);
        }

        previewUpdate();

    }

    public void move() {
        requestFocus();

        if (!pause && !gameOver) {
            tick++;
            totalTick++;

            if (tick >= (int) speed || fastDrop) {
                tick = Math.max(0, tick - (int) speed);
                currentPiece.y += 1;

                if (currentPiece.y + currentPiece.height() > 19 || arrayIntersect(currentPiece.x, currentPiece.y, currentPiece, board)) {
                    currentPiece.y--;
                    placePiece();
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(gameFont);

        g.setColor(Color.BLACK);
        g.fillRect(Main.ox, Main.oy, 600, 600);
        g.setColor(Color.WHITE);

        //g.drawImage(background, 0, 0, null);

        // Block Preview

        g.drawRoundRect(Main.ox + 300, Main.oy, 200, 200, 10, 10);
        g.drawString("Next Piece", Main.ox + 310, Main.oy + 30);

        g.drawString("PPM: ",Main.ox + 300, Main.oy + 275);
        g.drawString(Integer.toString(ppm), 300, 295);

        g.drawString("Score:", Main.ox + 300, Main.oy + 225);
        g.drawString(Integer.toString(score), Main.ox + 300, Main.oy + 250);

        int nextPieceX = 300 + 100 - nextPiece.width()*15;
        int nextPieceY = 100 - nextPiece.height()*15;

        for (int yy = 0; yy < nextPiece.height(); yy++) {
            for (int xx = 0; xx < nextPiece.width(); xx++) {
                if (nextPiece.piece[yy][xx] == 1) {
                    g.drawImage(blocks.get(nextPiece.blockType), Main.ox + xx*30 + nextPieceX, Main.oy + yy*30 + nextPieceY, 30, 30, null);
                }
            }
        }

        // Active block
        for (int yy = 0; yy < currentPiece.height(); yy++) {
            for (int xx = 0; xx < currentPiece.width(); xx++) {
                if (currentPiece.piece[yy][xx] != 0) {
                    g.drawImage(blocksPreview.get(currentPiece.blockType),Main.ox + (currentPiece.x+xx)*30, Main.oy + (yPreview +yy)*30, 30, 30, null);
                    g.drawImage(blocks.get(currentPiece.blockType), Main.ox + (currentPiece.x+xx)*30, Main.oy + (currentPiece.y+yy)*30, 30, 30, null);
                }
            }
        }

        // Fixed board
        for (int yy = 0; yy < 19; yy++) {
            for (int xx = 0; xx < 10; xx++) {
                if (!board[yy][xx].equals(0)) {
                    g.drawImage(blocks.get(board[yy][xx]), Main.ox + xx*30, Main.oy + yy*30, 30, 30, null);
                }
            }
        }

        for (int xx = 0; xx < 10; xx++) {
            g.drawImage(blocks.get(100), Main.ox + xx*30, Main.oy + 19*30, 30, 30, null);
        }

        if (pause) {
            g.drawString("PAUSED", Main.ox + 90, Main.oy + 300);
        }

        if (gameOver) {
            g.setFont(gameOverFont);
            g.drawString("GAME OVER", Main.ox + 20, Main.oy + 300);
        }
    }
}