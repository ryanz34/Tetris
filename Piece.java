import java.util.ArrayList;

class Piece {
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

    public Piece() {
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