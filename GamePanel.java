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

import static java.lang.Math.max;

class GamePanel extends JPanel implements KeyListener {

    // Counting the number of ticks
    private int tick = 0;

    private int totalTick = 0;  // The total number of ticks used per piece used to calculate ppm
    private int ppm = 0; // Pieces per minute, gives the user some feedback if they are doing a race or something
    private int gameOverTick = 0;

    private boolean gameOver = false;  // Game over state
    private boolean fastDrop = false;  // Whether to drop the piece faster
    private boolean pause = false; // Flag for whether the game is currently paused

    private BufferedImage background;  // Loading the images
    private HashMap<Integer, BufferedImage> blocks = new HashMap<>();  // HashMap is used to store blocks according to their id
    private HashMap<Integer, BufferedImage> blocksPreview = new HashMap<>();

    private boolean placed; // If the piece has been placed or not

    private double speed = 8;  // The number of ticks per move
    private int yPreview;  // The y position of the preview piece
    private Integer[][] board;  // The game board

    private int score = 0;

    private Piece currentPiece, nextPiece; // The current piece and the future piece

    private Font gameFont, gameOverFont, contFont;  // The different fonts that the game will use
    private Main parent;

    public GamePanel(Main parent) {
        setSize(Main.w, Main.h);
        this.parent = parent;

        // Load images
        try {
            InputStream is = GamePanel.class.getResourceAsStream("data/PressStart2P.ttf");

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            gameFont = font.deriveFont(18f);  // Deriving different font sizes
            contFont = font.deriveFont(12f);
            gameOverFont = font.deriveFont(30f);

            background = ImageIO.read(new File("data/propaganda.png"));
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

        board = new Integer[18][10];  // Creating a blank board

        for (int i = 0; i < 18; i++) {  // Setting the entire board to zeros and create the side gray blocks
            for (int j = 0; j < 10; j++) {
                if (j == 0 || j == 9) {
                    board[i][j] = 100;
                } else {
                    board[i][j] = 0;
                }
            }
        }

        currentPiece = new Piece();  // Create a new piece
        nextPiece = new Piece();  // Create the next piece

        addKeyListener(this);
        setFocusable(true);  // Allows the window to be focused
        previewUpdate();  // Get the location for the preview

    }

    /**
     * Update the preview
     */
    private void previewUpdate() {
        for (int ypp = currentPiece.y; ypp < 19; ypp++) {  // Increase the y until it intersects
            if (ypp + currentPiece.height() < 19 && !arrayIntersect(currentPiece.x, ypp, currentPiece, board)) { // Testing if it intersects
                yPreview = ypp; // Setting the preview y to the right height
            } else {
                break;
            }
        }
    }

    /**
     * Check for collision using two arrays
     *
     * @param x          int the x of the current piece
     * @param y          int the y of the current piece
     * @param checkPiece Piece the current piece or the piece u want to check
     * @param board      Integer[][] The current board
     * @return boolean of whether it intersects
     */
    private boolean arrayIntersect(int x, int y, Piece checkPiece, Integer[][] board) {
        for (int yy = 0; yy < checkPiece.height(); yy++) {
            for (int xx = 0; xx < checkPiece.width(); xx++) {
                if ((xx > 9 || yy > 18) && checkPiece.piece[yy][xx] == 1) {
                    return true;
                } else if (checkPiece.piece[yy][xx] != 0 && board[yy + y][xx + x] != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) { // Gets keyboard input
        if (!pause && !gameOver) {  // If the game is running
            if (e.getKeyCode() == e.VK_LEFT) { // If left is pressed
                currentPiece.x -= 1; // Move the piece left
                if (arrayIntersect(currentPiece.x, currentPiece.y, currentPiece, board)) {  // If it intersects, move back
                    currentPiece.x++;
                } else {
                    repaint();
                }

                previewUpdate();  // Update the preview

            } else if (e.getKeyCode() == e.VK_RIGHT) {  // Same as above but moves right
                currentPiece.x += 1;

                if (arrayIntersect(currentPiece.x, currentPiece.y, currentPiece, board)) {
                    currentPiece.x--;
                } else {
                    repaint();
                }

                previewUpdate();

            } else if (e.getKeyCode() == e.VK_UP) {  // When up is pressed
                Integer[][] previous_state = currentPiece.piece;  // Makes a backup of the current state
                currentPiece.get_new_state();

                if (currentPiece.y + currentPiece.newState.length <= 18) {  // Check the rotated piece fits on the board
                    currentPiece.piece = currentPiece.newState;  // Advance the state/rotate

                    if (arrayIntersect(currentPiece.x, currentPiece.y, currentPiece, board)) {  // Check if intersect
                        currentPiece.piece = previous_state;
                    } else {
                        repaint();
                    }
                }

                previewUpdate();

            } else if (e.getKeyCode() == e.VK_SPACE) {  // Instant drop
                placed = false;  // Reset the placed var
                int currentTick = totalTick;  // Get the total tick. Used to calculate the ppm
                while (!placed) { // Continue moving the block until its placed
                    move();
                }

                ppm = 60000 / (currentTick * 15);  // Calculates ppm

                previewUpdate();
                repaint();

            } else if (e.getKeyCode() == e.VK_DOWN) {  // Fast drop
                fastDrop = true; // Set the fast drop flag so the move method knows
            }
        }
        if (e.getKeyCode() == e.VK_ESCAPE) {
            if (gameOver) {  // If its game over then pressing any button will reset the game
                removeKeyListener(this);  // Remove KeyListener to prevent conflicts
                parent.gameOver();  // Call game over in the JFrame to change the page
            }

            pause = !pause;  // Pausing and un-pausing the game
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_DOWN) {  // When fast drop key is released, set flag to false
            fastDrop = false;
        }
    }

    private void placePiece() {
        ppm = 60000 / (totalTick * 15);  // Calculate ppm
        totalTick = 0;  // Reset total tick

        for (int yy = 0; yy < currentPiece.height(); yy++) {  // Placing the block by looping through and setting setting the piece on the board
            for (int xx = 0; xx < currentPiece.width(); xx++) {
                if (currentPiece.piece[yy][xx] == 1) {
                    board[currentPiece.y + yy][currentPiece.x + xx] = currentPiece.blockType;
                }
            }
        }

        currentPiece = nextPiece;  // Get a new piece
        nextPiece = new Piece();
        placed = true;

        for (int xx = 1; xx < 9; xx++) {  // If the it reaches the top, then its game over
            if (board[0][xx] != 0) {
                gameOver = true;
                break;
            }
        }

        ArrayList<Integer[]> currentBoard = new ArrayList<>(Arrays.asList(board)); // Making an array of the board for easier processing

        boolean isFull;  // A boolean that marks if the board is full or not

        for (int i = 17; i > -1; i--) {  // Loops through the board
            isFull = true;  // is full starts with being true, when an empty space is detected, it is set to false
            for (int j = 0; j < 10; j++) {
                if (currentBoard.get(i)[j] == 0) {
                    isFull = false;
                }
            }

            if (isFull) {  // if it is full
                speed = 8 * Math.pow(0.99, score);  // Adjust speed
                score++;  // Increase score
                currentBoard.remove(i);  // Remove the row of blocks
            }
        }

        while (currentBoard.size() != 18) {  // Add new rows to the front of the arrayList
            currentBoard.add(0, new Integer[]{100, 0, 0, 0, 0, 0, 0, 0, 0, 100});
        }

        for (int yy = 0; yy < 18; yy++) {  // Putting the board back
            board[yy] = currentBoard.get(yy);
        }

        previewUpdate();

    }

    public void move() {
        requestFocus();  // Request focus to we can get keyboard input

        if (!pause && !gameOver) {  // If the game is running
            tick++;  // Increase the ticks
            totalTick++;

            if (tick >= (int) speed || fastDrop) {  // If the tick is the same as speed or fastdrop
                tick = Math.max(0, tick - (int) speed);  // Reset tick
                currentPiece.y += 1;  // Increase the y

                if (currentPiece.y + currentPiece.height() > 18 || arrayIntersect(currentPiece.x, currentPiece.y, currentPiece, board)) { // Check for collision
                    currentPiece.y--;  // If collide then move back one
                    placePiece();  // And place the piece
                }
            }
        } else if (gameOver) {  // else increase game over tick to create flashing effect
            gameOverTick++;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, max(Main.w, Main.h), max(Main.w, Main.h), this);  // Draws the background

        g.setFont(gameFont);  // Setting the font

        g.setColor(Color.WHITE);
        g.fillRect(Main.ox, Main.oy, 600, 600);

        g.setColor(Color.BLACK);
        g.fillRect(Main.ox + 4, Main.oy + 4, 592, 592);

        g.setColor(Color.WHITE);

        g.fillRect(Main.ox + 300, Main.oy, 4, 600);

        // Block Preview

        g.drawRoundRect(Main.ox + 330, Main.oy + 60, 240, 200, 10, 10);  // Drawing the texts
        g.drawString("Next Piece", Main.ox + 340, Main.oy + 90);

        g.drawString("DROP RATE: ", Main.ox + 330, Main.oy + 405);
        g.drawString(Double.toString(Math.round((20 / speed) * 1000d) / 1000d) + " blks/s", Main.ox + 330, Main.oy + 430);

        g.drawString("PPM: ", Main.ox + 330, Main.oy + 350);
        g.drawString(Integer.toString(ppm), Main.ox + 330, Main.oy + 375);

        g.drawString("Score:", Main.ox + 330, Main.oy + 295);
        g.drawString(Integer.toString(score), Main.ox + 330, Main.oy + 320);

        int nextPieceX = 330 + 120 - nextPiece.width() * 15;
        int nextPieceY = 175 - nextPiece.height() * 15;

        for (int yy = 0; yy < nextPiece.height(); yy++) {  // Drawing the next piece by looping through the piece array
            for (int xx = 0; xx < nextPiece.width(); xx++) {
                if (nextPiece.piece[yy][xx] == 1) {
                    g.drawImage(blocks.get(nextPiece.blockType), Main.ox + xx * 30 + nextPieceX, Main.oy + yy * 30 + nextPieceY, 30, 30, null);
                }
            }
        }

        // Active block
        for (int yy = 0; yy < currentPiece.height(); yy++) { // Draws the current dropping block using the same method as above
            for (int xx = 0; xx < currentPiece.width(); xx++) {
                if (currentPiece.piece[yy][xx] != 0) {
                    g.drawImage(blocksPreview.get(currentPiece.blockType), Main.ox + (currentPiece.x + xx) * 30, Main.oy + 30 + (yPreview + yy) * 30, 30, 30, null);
                    g.drawImage(blocks.get(currentPiece.blockType), Main.ox + (currentPiece.x + xx) * 30, Main.oy + 30 + (currentPiece.y + yy) * 30, 30, 30, null);
                }
            }
        }

        // Fixed board
        for (int yy = 0; yy < 18; yy++) {  // board
            for (int xx = 0; xx < 10; xx++) {
                if (!board[yy][xx].equals(0)) {
                    g.drawImage(blocks.get(board[yy][xx]), Main.ox + xx * 30, Main.oy + 30 + yy * 30, 30, 30, null);
                }
            }
        }

        for (int xx = 0; xx < 10; xx++) {  // Drawing some border blocks
            g.drawImage(blocks.get(100), Main.ox + xx * 30, Main.oy + 19 * 30, 30, 30, null);
            g.drawImage(blocks.get(100), Main.ox + xx * 30, Main.oy, 30, 30, null);
        }

        if (pause) { // Display paused if its paused
            g.drawString("PAUSED", Main.ox + 90, Main.oy + 300);
        }

        if (gameOver) {  // If its game over
            if (gameOverTick >= 30) {  // Flashing effect
                g.setFont(contFont);
                g.drawString("Press ESC to continue!", Main.ox + 5, Main.oy + 500);
            }

            if (gameOverTick >= 60) {
                gameOverTick = 0;
            }
            g.setFont(gameOverFont);  // set the right font and draw the large game over
            g.drawString("GAME OVER", Main.ox + 15, Main.oy + 300);
        }
    }
}