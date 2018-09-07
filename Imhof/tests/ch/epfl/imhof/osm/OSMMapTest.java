/**
 * 
 */
package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public class OSMMapTest {

	private static final double DELTA = 1e-15;
	
	private OSMWay osmwclosed, osmw;
    private OSMNode osmn, osmn1, osmn2;
    private ArrayList<OSMNode> al1 = new ArrayList<>(), al2 = new ArrayList<>();
    private PointGeo p = new PointGeo(1.,1.1), p2 = new PointGeo(0.5,0.6),p3 = new PointGeo(0.2,0.3);
    private Attributes a;
    private long l = 542052405244556l, l2 = 63635653l, l3 = 4342l, l4 = 435435134;
    private OSMRelation r1;
    private OSMMap m1;
    private OSMMap.Builder mb = new OSMMap.Builder();
    
    @Before
    public void builder(){
        Attributes.Builder ab = new Attributes.Builder();
        ab.put("Ville","Moudon");
        ab.put("Cité","Moudon");
        
        //ATTRIBUTES
        a = ab.build();
        
        //NODEs
        osmn = new OSMNode(l,p,a);
        osmn1 = new OSMNode(l2, p2, a);
        osmn2 = new OSMNode(l3, p3, a);
        
        al1.add(osmn);
        al1.add(osmn1);
        al1.add(osmn2);
        al2.add(osmn);
        al2.add(osmn1);
        al2.add(osmn);
        
        //WAYs
        osmw = new OSMWay(l2,al1, a);
        osmwclosed = new OSMWay(l3, al2, a);
        
        OSMRelation.Builder rb = new OSMRelation.Builder(l4);
        rb.addMember(OSMRelation.Member.Type.WAY, "no idea", osmw);
        rb.addMember(OSMRelation.Member.Type.WAY, "no idea too", osmwclosed);
        
        //RELATIONs
        r1 = rb.build();
        
        ArrayList<OSMWay> ways = new ArrayList<>();
        ways.add(osmw); ways.add(osmwclosed);
        ArrayList<OSMRelation> relations = new ArrayList<>();
        relations.add(r1);
        
        //MAPs
        m1 = new OSMMap(ways, relations);
    }
    
	@Test
	public void waysTest() {
		assertEquals(m1.ways().get(0).id(), l2);
	}
	
	@Test
	public void relationsTest() {
		assertEquals(m1.relations().get(0).id(), l4);
	}
	
	@Test
	public void addNodeAndNodeForIdTest() {
        mb.addNode(osmn);
        mb.addNode(osmn1);
        mb.addNode(osmn2);
        assertEquals(mb.nodeForId(l2).position().longitude(),0.5,DELTA);
	}
	
	@Test
	public void addNodeAndNodeForIdTest01() {
        mb.addNode(osmn);
        mb.addNode(osmn1);
        mb.addNode(osmn2);
        assertEquals(mb.nodeForId(l).position().latitude(),1.1,DELTA);  
	}
	
	@Test
	public void addWayAndWayForIdTest() {
        mb.addWay(osmw);
        mb.addWay(osmwclosed);
        assertEquals(mb.wayForId(l2).nodes().get(1).position().longitude(),0.5,DELTA); 
	}
	
	@Test
	public void addWayAndWayForITestd02() {
        mb.addWay(osmw);
        mb.addWay(osmwclosed);
        assertEquals(mb.wayForId(l3).nodes().get(0).position().latitude(),1.1,DELTA); 
	}
	
	@Test
	public void addWayAndWayForIdTest03() {
        mb.addWay(osmw);
        mb.addWay(osmwclosed);
        assertEquals(mb.wayForId(l),null); 
	}
    
	@Test
	public void buildTest() {
        mb.addWay(osmw);
        mb.addWay(osmwclosed);
        mb.addRelation(r1);
        OSMMap m = mb.build();
        assertEquals(m.relations().get(0).id(), l4);
	}

}
