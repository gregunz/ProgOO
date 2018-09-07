package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;

import static org.junit.Assert.*;

public class OSMNodeTest {
    private OSMNode osmn;
    private PointGeo p = new PointGeo(1,1.1);
    private Attributes a;
    private long l = 542052405244556l;

    @Before
    public void builder(){
        Attributes.Builder ab = new Attributes.Builder();
        ab.put("Ville","Moudon");
        ab.put("Cité","Moudon");
        ab.put("Pays","Suisse");
        ab.put("ValueNull",null);
        ab.put(null,"KeyNull");
        ab.put(null,null);
        a = ab.build();
        osmn = new OSMNode(l,p,a);
    }

    @Test
    public void testPosition() throws Exception {
        assertEquals(p,osmn.position());
    }

    @Test
    public void testId() throws Exception {
        assertEquals(l,osmn.id());
    }

    @Test
    public void testAttributes() throws Exception {
        assertEquals(a,osmn.attributes());
    }

    @Test
    public void testHasAttribute() throws Exception {
        assertTrue(osmn.hasAttribute("Ville"));
        assertTrue(osmn.hasAttribute("ValueNull"));
        assertFalse(osmn.hasAttribute("Moudon"));
    }

    @Test
    public void testAttributeValue() throws Exception {
        assertEquals("Moudon",osmn.attributeValue("Ville"));
        assertEquals("Moudon",osmn.attributeValue("Cité"));
        assertEquals(null,osmn.attributeValue("ValueNull"));
    }


    @Test(expected = IllegalStateException.class)
    public void testBuild() throws Exception {
        OSMNode.Builder ob = new OSMNode.Builder(l, p);
        ob.setIncomplete();
        ob.build();
    }
}