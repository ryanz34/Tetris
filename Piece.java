import java.util.ArrayList;
import java.util.Random;

/**
 * Piece class
 * <p>
 * Contains the position of each piece and the rotation of the block.
 */

class Piece {
    // The different rotations of each block
    private Integer[][] Z;

    //The X and Y of the piece on the game board
    public int x, y;

    // The properties of the piece
    public Integer[][] piece;  // The array representation of the current piece including rotation
    public Integer[][] newState;
    public int stateNum;  // The num of the current state
    public int blockType;  // The color of sprite to use for this piece
    public Random rd = new Random();

    /**
     * Constructor for the piece
     * <p>
     * Chooses a random piece and set the x and y to default
     */
    public Piece() {
        piece = new Integer[3][3];
        blockType = (int) (Math.random() * 7) + 1;

        for (int x = 0; x < 3; x ++) {
            for (int y = 0; y < 3; y ++){
                if (rd.nextInt(2) == 1) {
                    piece[x][y] = 1;
                } else {
                    piece[x][y] = 0;
                }
            }
        }

        x = 4;
        y = 0;
    }

    public void get_new_state() {
        newState = new Integer[3][3];
        for (int x = 0; x < 3; x ++) {
            for (int y = 0; y < 3; y ++){
                if (rd.nextInt(2) == 1) {
                    newState[x][y] = 1;
                } else {
                    newState[x][y] = 0;
                }
            }
        }
    }

    /**
     * Gets the height of the piece
     *
     * @return int of height
     */
    public int height() {
        return piece.length;
    }

    /**
     * Gets the width of the object
     *
     * @return int of width
     */
    public int width() {
        return piece[0].length;
    }

}