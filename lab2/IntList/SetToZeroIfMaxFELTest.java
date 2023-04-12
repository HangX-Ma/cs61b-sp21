package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SetToZeroIfMaxFELTest {

    @Test
    public void testZeroOutFELMaxes1() {
        IntList L = IntList.of(1, 22, 15);
        IntListExercises.setToZeroIfMaxFEL(L);
        assert L != null;
        assertEquals("0 -> 0 -> 15", L.toString());
    }

    @Test
    public void testZeroOutFELMaxes2() {
        IntList L = IntList.of(55, 22, 45, 44, 5);
        IntListExercises.setToZeroIfMaxFEL(L);
        assert L != null;
        assertEquals("0 -> 22 -> 45 -> 0 -> 0", L.toString());
    }

    @Test
    public void testZeroOutFELMaxes3() {
        IntList L = IntList.of(5, 535, 35, 11, 10, 0);
        IntListExercises.setToZeroIfMaxFEL(L);
        assert L != null;
        assertEquals("0 -> 0 -> 35 -> 0 -> 10 -> 0", L.toString());
    }

    @Test
    public void testZeroOutFELMaxesAll() {
        IntList L = IntList.of(5, 5, 5);
        IntListExercises.setToZeroIfMaxFEL(L);
        assert L != null;
        assertEquals("0 -> 0 -> 0", L.toString());
    }

    @Test
    public void testZeroOutFELAllZeros() {
        IntList L = IntList.of(0, 0, 0);
        IntListExercises.setToZeroIfMaxFEL(L);
        assert L != null;
        assertEquals("0 -> 0 -> 0", L.toString());
    }


    @Test
    public void testZeroOutFELWithNegativeNumbers() {
        IntList L = IntList.of(-1, -22, -22);
        IntListExercises.setToZeroIfMaxFEL(L);
        assert L != null;
        assertEquals("0 -> 0 -> 0", L.toString());
    }

}
