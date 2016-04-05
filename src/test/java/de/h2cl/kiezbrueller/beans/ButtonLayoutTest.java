package de.h2cl.kiezbrueller.beans;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Created by martin.junker on 05.04.16.
 */
public class ButtonLayoutTest {

    @Test
    public void coordinatesToPos() throws Exception {
        
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(0, 0), is(1));
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(1, 0), is(2));
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(0, 1), is(3));
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(1, 1), is(4));
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(0, 2), is(5));
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(1, 2), is(6));
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(0, 3), is(7));
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(1, 3), is(8));
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(0, 4), is(9));
        assertThat(ButtonLayout.COL2ROW5.coordinatesToPos(1, 4), is(10));

        assertThat(ButtonLayout.COL3ROW2.coordinatesToPos(0, 0), is(1));
        assertThat(ButtonLayout.COL3ROW2.coordinatesToPos(1, 0), is(2));
        assertThat(ButtonLayout.COL3ROW2.coordinatesToPos(2, 0), is(3));
        assertThat(ButtonLayout.COL3ROW2.coordinatesToPos(0, 1), is(4));
        assertThat(ButtonLayout.COL3ROW2.coordinatesToPos(1, 1), is(5));
        assertThat(ButtonLayout.COL3ROW2.coordinatesToPos(2, 1), is(6));
    }

}