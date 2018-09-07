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
public class OSMMapReaderTest02 {

	private static final double DELTA = 1e-15;
	private OSMMap m;
	
	@Before
	public void build() {
		
		
		String nom = getClass().getResource("/lc.osm").getFile();
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
	public void relationTest() {
		assertEquals(m.relations().size(),1);
	}
	
	@Test
	public void relationTest02() {
		assertEquals(m.relations().get(0).members().size(),49);
	}
	
	@Test
	public void relationTest03() {
		assertEquals(m.relations().get(0).members().get(0).type(),OSMRelation.Member.Type.WAY);
	}
	
	@Test
	public void relationTest04() {
		assertEquals(m.relations().get(0).members().get(0).role(),"inner");
	}

	@Test
	public void relationTest05() {
		assertEquals(m.relations().get(0).members().get(0).member().id(),44675205);
	}
	
	@Test
	public void relationTest06() {
		assertEquals(m.relations().get(0).attributes().get("name"),"Rolex Learning Center");
		assertEquals(m.relations().get(0).attributes().get("type"),"multipolygon");
	}
	
}

