package de.h2cl.kiezbrueller.beans;

/**
 * ButtonLayout defines a relation between the position of a button and the order of mp3 files.
 *
 * Created by martin.junker on 05.04.16.
 */
public enum ButtonLayout {

    COL2ROW5(2, 5),

    COL3ROW2(3, 2);

    private final Integer cols;
    private final Integer rows;

    /**
     * Constructor
     *
     * 
     * @param cols
     * @param rows
     */
    private ButtonLayout(Integer cols, Integer rows) {
        this.cols = cols;
        this.rows = rows;
    }

    /**
     * Turns the grid layout into array index. Only valid for 2 * col?
     *
     * @param col
     * @param row
     * @return position
     */
    public Integer coordinatesToPos(final Integer col, final Integer row) {

        switch (this) {
            case COL2ROW5:
                return row * 2 + col + 1;

            case COL3ROW2:
                return row * 3 + col + 1;

            default:
                throw new IllegalArgumentException("unknown ButtonLayout " + name());

        }
    }
}
