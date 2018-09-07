package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OSMWayTest {
    private OSMWay osmwclosed, osmw;
    private OSMNode osmn, osmn1, osmn2;
    ArrayList<OSMNode> al1 = new ArrayList<>(), al2 = new ArrayList<>();
    private PointGeo p = new PointGeo(1.,1.1), p2 = new PointGeo(0.5,0.6),p3 = new PointGeo(0.2,0.3);
    private Attributes a;
    private long l = 542052405244556l, l2 = 63635653l, l3 = 4342l;

    @Before
    public void builder(){
        Attributes.Builder ab = new Attributes.Builder();
        ab.put("Ville","Moudon");
        ab.put("Cité","Moudon");
        a = ab.build();
        osmn = new OSMNode(l,p,a);
        osmn1 = new OSMNode(l2, p2, a);
        osmn2 = new OSMNode(l3, p3, a);
        al1.add(osmn);
        al1.add(osmn1);
        al1.add(osmn2);
        osmw = new OSMWay(l2,al1, a);
        al1.remove(2);

        al2.add(osmn);
        al2.add(osmn1);
        al2.add(osmn);
        osmwclosed = new OSMWay(l3, al2, a);
    }

    @Test
    public void testNodesCount() throws Exception {
        assertEquals(3, osmw.nodesCount());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNodes() throws Exception {
        osmw.nodes().add(osmn);
    }

    @Test
    public void testFirstNode() throws Exception {
        assertEquals(osmn, osmwclosed.firstNode());
    }

    @Test
    public void testLastNode() throws Exception {
        assertEquals(osmn2, osmw.lastNode());
        assertEquals(osmn, osmwclosed.lastNode());
    }

    @Test
    public void testIsClosedClosed() throws Exception {
        assertTrue(osmwclosed.isClosed());
    }

    @Test
    public void testIsClosedNotClosed() throws Exception {
        assertFalse(osmw.isClosed());
    }

    @Test
    public void testNonRepeatingNodes() throws Exception {
        assertFalse(osmwclosed.lastNode().equals(osmwclosed.nonRepeatingNodes().get((osmwclosed.nonRepeatingNodes().size()-1))));
        assertEquals(al2.size()-1, osmwclosed.nonRepeatingNodes().size());

        assertEquals(osmn2, osmw.nonRepeatingNodes().get(osmw.nonRepeatingNodes().size()-1));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuild() throws Exception {
        OSMNode.Builder ob = new OSMNode.Builder(l, p);
        ob.setIncomplete();
        ob.build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuild2() throws Exception {
        OSMWay.Builder ob = new OSMWay.Builder(l2);
        ob.addNode(osmn);
        ob.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorTest(){
        al1.remove(1);
        OSMWay o = new OSMWay(l2,al1, a);
    }

    @Test
    public void constructorTest1(){
        OSMWay o = new OSMWay(l2,al2, a);
    }
}