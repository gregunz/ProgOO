/**
 * 
 */
package ch.epfl.imhof.painting;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public class ColorTest {

	private final double DELTA = 10e-10;
	private Color c, c1;
	private java.awt.Color c0;
	private double a1, a2, a3;
	
	@Before
	public void before(){
		a1 = .43532;
		a2 = .54354;
		a3 = .12345;
		c = Color.rgb(a1, a2, a3);
		
		//useless (see with to-do method)
		c0 = new java.awt.Color((float)a1, (float)a2, (float)a3);
		c1 = Color.rgb( c0.getRGB() );

	}
	
	//to-do !
	public void testConstructor02(){
		assertEquals(a1, c1.r(), DELTA);
		assertEquals(a2, c1.g(), DELTA);
		assertEquals(a2, c1.b(), DELTA);
	}
	
	@Test
	public void testWhite(){
		c = Color.rgb(1, 1, 1);
		assertEquals(Color.WHITE.r(), c.r(), DELTA);
		assertEquals(Color.WHITE.g(), c.g(), DELTA);
		assertEquals(Color.WHITE.b(), c.b(), DELTA);
	}
	
	@Test
	public void testConstructor01(){
		assertEquals(a1, c.r(), DELTA);
		assertEquals(a2, c.g(), DELTA);
		assertEquals(a3, c.b(), DELTA);
	}

	@Test
	public void testMultiplyWith(){
		assertEquals(a1*a1, c.multiplyWith(c).r(), DELTA);
		assertEquals(a2*a2, c.multiplyWith(c).g(), DELTA);
		assertEquals(a3*a3, c.multiplyWith(c).b(), DELTA);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorException01(){
		Color c2 = Color.rgb(1.00001 , a2, a3);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorException02(){
		Color c2 = Color.rgb(-.01 , a2, a3);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorException03(){
		Color c2 = Color.rgb(a1 , -9876567, a3);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorException04(){
		Color c2 = Color.rgb(a1 , a2, 4353);
	}

}
