/**
 * 
 */
package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import ch.epfl.imhof.Graph;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.EquirectangularProjection;

/**
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public class OSMToGeoTransformerTest {

    private static final double DELTA = 0.000001;
	//private OSMNode a, b, c, d, e, f, g;
	private Point p1 = new Point(1,1),
				  p2 = new Point(1,2),
				  p3 = new Point(2,2),
				  p4 = new Point(2,1),
				  p5 = new Point(10,10),
				  p6 = new Point(10,15),
				  p7 = new Point(15,15);
	
	private Graph<Point> g;
	private EquirectangularProjection e = new EquirectangularProjection();
	private OSMToGeoTransformer o;
	private List<ClosedPolyLine> rings, rings2, rings3;
	private OSMMap m;
	private String url1,url2,url3,url4;

	@Before
	public void builder(){
		Graph.Builder<Point> gb = new Graph.Builder<Point>();
		gb.addNode(p1);
		gb.addNode(p2);
		gb.addNode(p3);
		gb.addNode(p4);
		gb.addNode(p5);
		gb.addNode(p6);
		gb.addNode(p7);
		
		gb.addEdge(p1, p2);
		gb.addEdge(p2, p3);
		gb.addEdge(p3, p4);
		gb.addEdge(p4, p1);
		gb.addEdge(p5, p6);
		gb.addEdge(p6, p7);
		gb.addEdge(p7, p5);
		
		g = gb.build();
		
		o = new OSMToGeoTransformer(e);

        url1 = getClass().getResource("/lc.osm").getFile();
        url2 = getClass().getResource("/berne.osm.gz").getFile();
        url3 = getClass().getResource("/lausanne.osm.gz").getFile();
        url4 = getClass().getResource("/interlaken.osm.gz").getFile();
	}

    public void transformTest(String url){
    	
        url = url.replace("%c3%a9", "é");
        boolean bool = url.contains(".gz");
		try {
			m = OSMMapReader.readOSMFile(url, bool);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		Map map = new OSMToGeoTransformer(e).transform(m);

		System.out.print("Données pour ");
		if(url.contains("lc.osm"))
			System.out.println("LEARNING CENTER");
		if(url.contains("berne.osm"))
			System.out.println("BERNE");
		if(url.contains("lausanne.osm"))
			System.out.println("LAUSANNE");
		if(url.contains("interlaken.osm"))
			System.out.println("INTERLAKEN");
		System.out.println(
				"Polygons: " + map.polygons().size()+"\n"+
				"Polylines: " + map.polyLines().size()+"\n"+
    			"Relations: "+ m.relations().size()+"\n"+
    			"Ways: "+m.ways().size()
    			);
		System.out.println();
    }
    
    public Map getMap(String city){
    	String url = "";
    	switch(city){
    		case "lc" :
    			url = url1;
    			break;
    		case "berne" :
    			url = url2;
    			break;
    		case "lausanne" :
    			url = url3;
    			break;
    		case "interlaken" :
    			url = url4;
    			break;
    	}

        boolean bool = url.contains(".gz");
    	try {
			m = OSMMapReader.readOSMFile(url, bool);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
    	return new OSMToGeoTransformer(e).transform(m);
    }
    
    @Test
    public void transformTest(){
    	transformTest(url1);
    	transformTest(url2);
    	transformTest(url3);
    	transformTest(url4);
    }
    

    
    
        /*
    	final Set<String>
                ATT_DESCRIBES_SURFACE = new HashSet<>( Arrays.asList(
                "aeroway", "amenity", "building", "harbour", "historic",
                "landuse", "leisure", "man_made", "military", "natural",
                "office", "place", "power", "public_transport", "shop",
                "sport", "tourism", "water", "waterway", "wetland"));
        Predicate<String> containsAttArea = s -> s != null && (s.equals("yes") || s.equals("1") || s.equals("true"));
        Predicate<Attributes> attDescribesArea = s -> !s.keepOnlyKeys(ATT_DESCRIBES_SURFACE).isEmpty();
        BiPredicate<String, Attributes> notToPolygon = (s, a) -> !(containsAttArea.test(s) && attDescribesArea.test(a) );

        System.out.println(
                m.ways().stream().filter(way -> way.isClosed() && notToPolygon.negate().test(way.attributes().get("area"),way.attributes())).count()
        );
                Map map = new OSMToGeoTransformer(e).transform(m);
        System.out.println(map.polygons().size());
    */

    /*
	public void ringsForRoleTest() {
		assertNotNull(m.relations().get(0));
		//rings2 = o.ringsForRole(m.relations().get(0), "inner");
		//rings3 = o.ringsForRole(m.relations().get(0), "outer");
		
		assertEquals(14, rings2.size());
		assertEquals(1, rings3.size());
	}
    
	//
    public void assemblePolygonTest(){
    	//System.out.println(o.assemblePolygon(m.relations().get(0), m.relations().get(0).attributes()).get(0).value().shell().points().get(0).x());
    	//System.out.println(o.ringsForRole(m.relations().get(0), "outer").get(0).points().get(0).x());
    	
    	assertEquals(
    			14,
    			o.assemblePolygon(m.relations().get(0), m.relations().get(0).attributes()).get(0).value().holes().size()
    	);
    }
    */

}
