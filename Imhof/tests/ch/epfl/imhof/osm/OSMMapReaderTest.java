/**
 * 
 */
package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public class OSMMapReaderTest {

	private static final double DELTA = 1e-15;
	private OSMMap m;
	
	@Before
	public void build() {
		
		
		String nom = getClass().getResource("/bc.osm").getFile();
        nom = nom.replace("%c3%a9", "é");
		
		try {
			m = OSMMapReader.readOSMFile(nom, false);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void waysTest() {
		assertEquals(m.ways().get(0).id(),5);
	}
	
	@Test
	public void nodesTest() {
		assertEquals(m.ways().get(0).nonRepeatingNodes().size(),4);
	}
	
	@Test
	public void nodesTest02() {
		double val = m.ways().get(0).nonRepeatingNodes().get(2).position().latitude();
		//System.out.println(val);
		assertEquals(val, Math.toRadians(46.5188294), DELTA);
	}
	
	public void nodesTest03() {
		double val = m.ways().get(0).nonRepeatingNodes().get(0).position().longitude();
		//System.out.println(val);
		assertEquals(val, Math.toRadians(6.5616727), DELTA);
	}
	
	@Test
	public void waysAttributesTest() {
		String val = m.ways().get(0).attributes().get("building");
		assertEquals(val, "yes");
	}
	
	@Test
	public void waysAttributesTest02() {
		String val = m.ways().get(0).attributes().get("name");
		assertEquals(val, "BC");
	}

}
