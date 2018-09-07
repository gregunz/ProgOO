package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.*;
import ch.epfl.imhof.projection.Projection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Cette classe représente un convertisseur de données OSM en carte Map.
 * Cette classe est finale et immuable
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class OSMToGeoTransformer {

    private final Projection projection;

    private static final Set<String>
            ATT_DESCRIBES_SURFACE = new HashSet<>( Arrays.asList(
                    "aeroway", "amenity", "building", "harbour", "historic",
                    "landuse", "leisure", "man_made", "military", "natural",
                    "office", "place", "power", "public_transport", "shop",
                    "sport", "tourism", "water", "waterway", "wetland"
            )),
            POLYLINES_ATT_TO_KEEP = new HashSet<>( Arrays.asList(
                    "bridge", "highway", "layer", "man_made",
                    "railway", "tunnel", "waterway"
            )),
            POLYGONS_ATT_TO_KEEP = new HashSet<>( Arrays.asList(
                    "building", "landuse", "layer",
                    "leisure", "natural", "waterway"
            ));

    private static final int OPEN_POLYLINE = 0, CLOSED_POLYLINE = 1, POLYGON = 2;

    /**
     * Construit un convertisseur OSM en géométrie qui utilise la projection donnée
     *
     * @param projection
     *      La projection utilisée pour la conversion
     */
    public OSMToGeoTransformer(Projection projection){
        this.projection = Objects.requireNonNull(projection);
    }

    /**
     * Convertit une « carte » OSM en une carte géométrique projetée
     *
     * @param map
     *      La carte OSM à convertir
     *
     * @return
     *      La carte convertie
     */
    public Map transform(OSMMap map){
        final Map.Builder mapBuilder = new Map.Builder();

        processWays(map.ways(), mapBuilder);
        map.relations()
                .stream()
                .filter(r -> r.attributes().get("type","default").equals("multipolygon") )
                .forEach(r -> assemblePolygon(r, r.attributes() ).forEach(mapBuilder::addPolygon) );

        return mapBuilder.build();
    }

    /**
     * Extrait de la map OSM donnée les polylignes et polygones et les ajoute à la Map en cours de construction
     *
     * @param ways
     *      Une liste de chemins
     * @param mapBuilder
     *      Le Builder de la carte à laquelle on veut ajouter les chemins
     */
    @SuppressWarnings("unchecked")
    private void processWays(List<OSMWay> ways, Map.Builder mapBuilder){
        final Predicate<String> attAreaValid = s-> s != null && (s.equals("yes") || s.equals("1") || s.equals("true") );
        final Predicate<OSMWay>
                containsAttArea     = w -> attAreaValid.test(w.attributeValue("area") ),
                attDescribesArea    = w -> !w.attributes().keepOnlyKeys(ATT_DESCRIBES_SURFACE).isEmpty(),
                toPolygon           = w -> containsAttArea.or(attDescribesArea).test(w),
                isClosed            = OSMWay::isClosed;

        buildAttributed(ways, isClosed.negate(),                OPEN_POLYLINE   ).forEach(mapBuilder::addPolyLine);
        buildAttributed(ways, isClosed.and(toPolygon.negate()), CLOSED_POLYLINE ).forEach(mapBuilder::addPolyLine);
        buildAttributed(ways, isClosed.and(toPolygon),          POLYGON         ).forEach(mapBuilder::addPolygon);
    }

    /**
     * Retourne une liste d'entités attribuées à partir de la liste de chemins fournie. La liste des chemins est
     * tout d'abord filtrée avec le filtre donné.
     *
     * @param ways
     *      Les chemins desquels on veut extraire les entités attribuées
     * @param filter
     *      Le filtre à appliquer à la liste des chemins
     * @param type
     *      Le type d'entité attribuée voulu: OpenPolyLine, ClosedPolyLine ou Polygon
     * @return
     *      Une liste d'entités attribuées du type désiré
     */
    @SuppressWarnings("rawtypes")
    private List<Attributed> buildAttributed(List<OSMWay> ways, Predicate<OSMWay> filter, int type){
        final Set<String> SET_ATT_TO_KEEP =
                type == POLYGON ? POLYGONS_ATT_TO_KEEP : POLYLINES_ATT_TO_KEEP;
        final Predicate<OSMWay> attToKeepNotEmpty =
                w -> !w.attributes().keepOnlyKeys(SET_ATT_TO_KEEP).isEmpty();

        return ways.stream()
                    .filter(way -> filter.and(attToKeepNotEmpty).test(way))
                    .map(way -> type == OPEN_POLYLINE ?
                                    new Attributed<>(
                                            new OpenPolyLine(wayToProjectedPoints(way)),
                                            way.attributes().keepOnlyKeys(POLYLINES_ATT_TO_KEEP)
                                    ) :
                                    type == CLOSED_POLYLINE ?
                                    new Attributed<>(
                                            new ClosedPolyLine(wayToProjectedPoints(way)),
                                            way.attributes().keepOnlyKeys(POLYLINES_ATT_TO_KEEP)
                                    ) :
                                    new Attributed<>(
                                            new Polygon(new ClosedPolyLine(wayToProjectedPoints(way))),
                                            way.attributes().keepOnlyKeys(POLYGONS_ATT_TO_KEEP)
                                    )
                    )
                    .collect(Collectors.toList());
    }

    /**
     * Projette tous les noeuds d'un chemin
     *
     * @param way
     *      Le chemin à "projeter"
     * @return
     *      Une liste de Point correspondant aux noeuds projetés du chemin
     */
    private List<Point> wayToProjectedPoints(OSMWay way){
        return way.nonRepeatingNodes()
                  .stream()
                  .map(node -> projection.project( node.position() ) )
                  .collect(Collectors.toList());
    }

	/**
	 * Calcule et retourne l'ensemble des anneaux de la relation donnée ayant le rôle spécifié.
	 * Cette méthode retourne une liste vide si le calcul des anneaux échoue
	 * 
	 * @param relation
	 * 		La relation dont on veut extraire les anneaux
	 * @param role
	 * 		Le rôle spécifié à filtrer
	 * @return
	 * 		La liste des anneaux ayant le rôle spécifié
	 */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role){
    	final Graph.Builder<OSMNode> graphBuilder =  new Graph.Builder<>();

    	relation.members()
                .stream()
                .filter(member -> member.role().equals(role) && member.type() == OSMRelation.Member.Type.WAY)
    			.forEach(member ->
                    {
                        final OSMWay way = ((OSMWay) member.member());
                        OSMNode n = way.firstNode();
                        graphBuilder.addNode( n );
                        for(int i=1; i<way.nonRepeatingNodes().size(); ++i){
                            OSMNode tmp = n;
                            n = way.nonRepeatingNodes().get(i);
                            graphBuilder.addNode(n);
                            graphBuilder.addEdge(n, tmp);
                        }
                        if(way.isClosed()){
                            graphBuilder.addEdge( way.firstNode(), n );
                        }
                    }
                );
    	
    	return ringsFromGraph(graphBuilder.build());
    }

    /**
     * Calcule et retourne l'ensemble des anneaux du graph donné
     * Cette méthode retourne une liste vide si le calcul des anneaux échoue
     */
    private List<ClosedPolyLine> ringsFromGraph(Graph<OSMNode> graph){
    	final List<ClosedPolyLine> rings = new ArrayList<>();
    	final Set<OSMNode> nodesAdded = new HashSet<>();
    	
    	if(!graph.nodes().isEmpty() && !graph.nodes().stream().anyMatch(n -> graph.neighborsOf(n).size() != 2) )
    	{   		
    		graph.nodes()
                    .stream()
                    .forEach(n ->
                        {
                            if(!nodesAdded.contains(n)) {
                                final PolyLine.Builder ring = new PolyLine.Builder();
                                final Function<OSMNode, Optional<OSMNode>> getNextNeighbor = node ->
                                        graph.neighborsOf(node)
                                                .stream()
                                                .filter(node1 -> !nodesAdded.contains(node1) )
                                                .findAny();
                                Optional<OSMNode> node = getNextNeighbor.apply(n);

                                while( node.isPresent() ){
                                    nodesAdded.add(node.get());
                                    ring.addPoint( projection.project( node.get().position() ));
                                    node = getNextNeighbor.apply( node.get() );
                                }
                                rings.add( ring.buildClosed() );
                            }
                        }
                    );
    	}
    	return rings;
    }


    /**
     * Calcule et retourne la liste des polygones attribués de la relation donnée, en leur attachant
     * les attributs donnés		
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes){
    	final List<Attributed<Polygon>> polygons = new ArrayList<>();
    	final Attributes attToKeep = attributes.keepOnlyKeys(POLYGONS_ATT_TO_KEEP);
    	final List<ClosedPolyLine> outers = ringsForRole(relation, "outer");
    			
    	if(!attToKeep.isEmpty() && !outers.isEmpty()) {
    		final List<ClosedPolyLine> inners = ringsForRole(relation, "inner");
    		
    		if(inners.isEmpty()){
    			outers.forEach(outer -> 
    				polygons.add( new Attributed<>(new Polygon(outer) , attToKeep) )
    			);
    		}
    		else{
    			final java.util.Map<ClosedPolyLine, List<ClosedPolyLine>> ringWithHoles = new HashMap<>();

    			outers.sort((cpl1, cpl2) ->
                                cpl1.area() < cpl2.area() ? -1 :
                                cpl1.area() > cpl2.area() ? 1 :
                                0
                );
    			outers.forEach(outer -> ringWithHoles.put(outer, new ArrayList<>()));
    			inners.forEach(inner ->
                        {
                            Optional<ClosedPolyLine> outer =
                                    outers.stream()
                                            .filter(cpl -> cpl.containsPoint(inner.firstPoint()))
                                            .findFirst();

                            if(outer.isPresent()){
                                ringWithHoles.get(outer.get()).add(inner);
                            }
                        }
                );
    			ringWithHoles.forEach((ring, holes) ->
                                polygons.add(new Attributed<>(new Polygon(ring, holes), attToKeep))
                );
    		}
    	}

    	return polygons;
    }
}
