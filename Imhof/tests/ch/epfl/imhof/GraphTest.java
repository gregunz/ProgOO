/**
 * 
 */
package ch.epfl.imhof;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.imhof.osm.OSMNode;

/**
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public class GraphTest {

	private OSMNode a, b, c, d, e, eCopy;
	private PointGeo p = new PointGeo(1.,1.1), p2 = new PointGeo(0.5,0.6),p3 = new PointGeo(0.2,0.3), p4 = new PointGeo(0.1,0.2), p5 = new PointGeo(-0.2,-0.35);
	private Attributes at;
	private long l = 542052405244556l, l2 = 63635653l, l3 = 4342l, l4 = 435435134, l5=433;
	private Graph<OSMNode> g;
	private ArrayList<OSMNode> al = new ArrayList<OSMNode>();



	@Before
	public void builder() {
		Attributes.Builder ab = new Attributes.Builder();
		ab.put("Ville","Moudon");
		ab.put("Cité","Moudon");

		//ATTRIBUTES
		at = ab.build();

		//NODEs
		a = new OSMNode(l,p,at);
		b = new OSMNode(l2, p2, at);
		c = new OSMNode(l3, p3, at);
		d = new OSMNode(l4, p4, at);
		e = new OSMNode(l5, p5, at);
		eCopy = new OSMNode(l5, p5, at);
		
		al.add(a);
		al.add(b);
		al.add(c);
		al.add(d);
		al.add(e);
		
		Graph.Builder<OSMNode> gb =  new Graph.Builder<OSMNode>();
		gb.addNode(a);
		gb.addNode(b);
		gb.addNode(c);
		gb.addNode(d);
		gb.addNode(e);		
		gb.addEdge(a, b);
		gb.addEdge(a, c);
		gb.addEdge(a, e);
		gb.addEdge(b, e);
		gb.addEdge(b, c);
		g = gb.build();
	}
	
	@Test
	public void nodesTest() {
		for(int i=0; i<g.nodes().size(); ++i){
			assertTrue(g.nodes().contains(al.get(i)));
		}
	}
	
	@Test
	public void neighborsOfTest01() {
		assertTrue(g.neighborsOf(a).contains(b));
		assertTrue(g.neighborsOf(c).contains(b));
		assertTrue(g.neighborsOf(e).contains(a));
		assertTrue(g.neighborsOf(d).isEmpty());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void neighborsOfTest02() {
		g.neighborsOf(eCopy);
	}
	
}
