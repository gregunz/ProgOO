/**
 * 
 */
package ch.epfl.imhof.painting;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.geometry.ClosedPolyLine;

/**
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public class FiltersTest {
	
	private Map<String,String> attributes, attributes2;
	private Attributes at, at2;
	private Attributed<ClosedPolyLine> a, a2;
	
	@Before
	public void before(){
		attributes = new HashMap<>();
		attributes.put("building", "yes");
		attributes2 = new HashMap<>(attributes);
		attributes.put("layer", "1");
		attributes2.put("layer", "bonjour");
		
		at = new Attributes(attributes); 
		at2 = new Attributes(attributes2); 
		
		a = new Attributed<>(null, at);
		a2 = new Attributed<>(null, at2);
	}
	
	@Test
	public void taggedTest() {		
		final String[] s = {"a","b","1","yes"};
		
		assertTrue( Filters.tagged("building").test(a) );
		assertFalse( Filters.tagged("layerS").test(a) );
		assertTrue( Filters.tagged("building","yes").test(a) );
		assertTrue( Filters.tagged("building", s ).test(a) );
		assertTrue( Filters.tagged("layer", s ).test(a) );
	}
	
	@Test
	public void onLayerTest(){
		assertTrue( Filters.onLayer(1).test(a) );
		assertFalse( Filters.onLayer(-1).test(a) );
		assertTrue( Filters.onLayer(0).test(a2) );
		assertFalse( Filters.onLayer(1).test(a2) );
	}

}
