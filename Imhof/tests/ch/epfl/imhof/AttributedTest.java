/**
 * 
 */
package ch.epfl.imhof;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.Polygon;

/**
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public class AttributedTest {

	@Test
	public void test01() {
		Attributes.Builder b = new Attributes.Builder();
		b.put("natural", "water");
		b.put("name", "Lac Léman");
		b.put("ele", "372");
		Attributes a = b.build();
		
		ClosedPolyLine s = new ClosedPolyLine(
                Arrays.asList(
                        new Point(36, 54),
                        new Point(193, -61),
                        new Point(-70, -231),
                        new Point(-634, 96),
                        new Point(36, 54)));
        Polygon p = new Polygon(s);
        Attributed<Polygon> ap = new Attributed<>(p, a);
        assertEquals(ap.attributeValue("ele", 0), 372);
	}

}
