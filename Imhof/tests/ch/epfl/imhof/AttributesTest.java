/**
 * 
 */
package ch.epfl.imhof;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public class AttributesTest {

	@Test
	public void isEmptyTest() {
		Map<String,String> b = new HashMap<String, String>();
		Attributes a = new Attributes(b);
		b.put("la", "do"); //ne doit rien changer si immuable (copie)
		assertTrue(a.isEmpty());
	}
	
	@Test
	public void isEmptyTest02() {
		Map<String,String> b = new HashMap<String, String>();
		b.put("la","do");
		Attributes a = new Attributes(b);	
		assertFalse(a.isEmpty());
	}
	
	@Test
	public void containsTest() {
		Map<String,String> b = new HashMap<String, String>();
		b.put("la","do");
		Attributes a = new Attributes(b);	
		assertTrue(a.contains("la"));
	}
	
	@Test
	public void getTest00() {
		Map<String,String> b = new HashMap<String, String>();
		b.put("la","do");
		Attributes a = new Attributes(b);	
		assertTrue(a.get("la").equals("do"));
	}
	
	@Test
	public void getTest01() {
		Map<String,String> b = new HashMap<String, String>();
		b.put("la","do");
		Attributes a = new Attributes(b);
		assertTrue(a.get("si")==null);
	}


	@Test
	public void getTestDefault00() {
		Map<String,String> b = new HashMap<String, String>();
		b.put("la","do");
		Attributes a = new Attributes(b);	
		assertTrue(a.get("la","caca").equals("do"));
	}
	
	@Test
	public void getTestDefault01() {
		Map<String,String> b = new HashMap<String, String>();
		b.put("la","do");
		Attributes a = new Attributes(b);	
		assertTrue(a.get("si","do").equals("do"));
	}

    @Test
    public void getTestDefault02() {
        Map<String,String> b = new HashMap<String, String>();
        b.put("la",null);
        Attributes a = new Attributes(b);
        assertTrue(a.get("si","do").equals("do"));
    }
	
	@Test
	public void getIntTest00() {
		Map<String,String> b = new HashMap<String, String>();
		b.put("la","1994");
		Attributes a = new Attributes(b);
		assertEquals(a.get("la",94),1994);
	}
	
	@Test
	public void getIntTest01() {
		Map<String,String> b = new HashMap<String, String>();
		b.put("la","1994");
		Attributes a = new Attributes(b);
		assertEquals(a.get("si",94),94);
	}
	
	@Test
	public void keepOnlyKeysTest() {
		Map<String,String> b = new HashMap<String, String>();
		b.put("la","1994");
		b.put("do","sirop");
		b.put("ré","numeroUno");
		Attributes a = new Attributes(b);
		Set<String> s = new HashSet<String>();
		s.add("la");
		s.add("ré");

		Attributes a2 = a.keepOnlyKeys(s);
		assertTrue(a2.contains("la"));
		assertTrue(a2.contains("ré"));
		assertFalse(a2.contains("do"));
	}
}
