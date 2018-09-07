/**
 * 
 */
package ch.epfl.imhof.painting;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public class LineStyleTest {
	
	private double DELTA = 10e-10;	
	private double d1=.1, d2=.34, d3=.98;
	private float f1=0.0322f, f2=2.1f, f3=12;

	private LineStyle l;
	private Color c = Color.rgb(d1, d2, d3);
	private float[] dP = {f1, f2, f3};
	

	@Before
	public void before(){
		l = new LineStyle(f1, c, LineCap.BUTT, LineJoin.BEVEL, dP);
	}
	
	@Test
	public void testConstructor01(){
		assertEquals(f1, l.width(), DELTA);
		assertEquals(d1, l.color().r(), DELTA);
		assertEquals(d2, l.color().g(), DELTA);
		assertEquals(d3, l.color().b(), DELTA);
		assertEquals(LineCap.BUTT, l.lineCap());
		assertEquals(LineJoin.BEVEL, l.lineJoin());
		assertEquals(f1, l.dashingPattern()[0], DELTA);
		assertEquals(f2, l.dashingPattern()[1], DELTA);
		assertEquals(f3, l.dashingPattern()[2], DELTA);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructor02(){
		LineStyle test = new LineStyle(-0.00001f, c);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructor03(){
		float[] dP1 = {2.45f, 1, -2.4535f};
		LineStyle test = new LineStyle(0.00001f, c, LineCap.BUTT, LineJoin.BEVEL, dP1);
	}

}
