package ch.epfl.imhof.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.function.Function;

public class PointTest {
    private static final double DELTA = 0.000001;

    @Test
    public void xGetterReturnsX() {
        for (double x = -100; x <= 100; x += 12.32)
            assertEquals(x, new Point(x, 0).x(), DELTA);
    }

    @Test
    public void yGetterReturnsY() {
        for (double y = -100; y <= 100; y += 12.32)
            assertEquals(y, new Point(0, y).y(), DELTA);
    }

    @Test
    public void alignedCoordinateChangeTest(){
        Point p1From = new Point(1, -1),
                p1To = new Point(5, 4),
                p2From = new Point(-1.5, 1),
                p2To = new Point(0, 0);
        Function<Point, Point> blueToRed =
                Point.alignedCoordinateChange(p1From, p1To, p2From, p2To);
        final Point
                newP = blueToRed.apply(new Point(0, 0)),
                p1projected = blueToRed.apply(p1From),
                p2projected = blueToRed.apply(p2From);

        assertEquals(3, newP.x(), DELTA);
        assertEquals(2, newP.y(), DELTA);
        assertEquals(p1To.x(), p1projected.x(), DELTA);
        assertEquals(p1To.y(), p1projected.y(), DELTA);
        assertEquals(p2To.x(), p2projected.x(), DELTA);
        assertEquals(p2To.y(), p2projected.y(), DELTA);
    }
}
