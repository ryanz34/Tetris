import java.util.ArrayList;

/**
 * Piece class
 * <p>
 * Contains the position of each piece and the rotation of the block.
 */

class Piece {
    // The different rotations of each block
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

    // Adding all the states into an ArrayList to easier processing later on
    private ArrayList<Integer[][][]> states = new ArrayList<Integer[][][]>() {{
        add(Z_STATES);
        add(I_Z_STATES);
        add(L_STATES);
        add(I_L_STATES);
        add(T_STATES);
        add(I_STATES);
        add(O_STATES);
    }};

    //The X and Y of the piece on the game board
    public int x, y;

    // The properties of the piece
    public Integer[][] piece;  // The array representation of the current piece including rotation
    public int stateNum;  // The num of the current state
    public Integer[][][] pieceStates;  // All the states which this piece can be
    public int blockType;  // The color of sprite to use for this piece

    /**
     * Constructor for the piece
     * <p>
     * Chooses a random piece and set the x and y to default
     */
    public Piece() {
        blockType = (int) (Math.random() * 7) + 1;
        pieceStates = states.get((int) (Math.random() * 7));
        stateNum = (int) (Math.random() * pieceStates.length);
        piece = pieceStates[stateNum];

        x = 4;
        y = 0;
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