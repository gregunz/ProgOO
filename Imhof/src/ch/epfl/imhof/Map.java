package ch.epfl.imhof;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente une carte projetée, composée d'entités géométriques attribuées.
 * Cette classe est finale et immuable
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Map {

    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;

    /**
     * Construit une carte à partir des listes de polylignes et polygones attribués donnés
     *
     * @param polyLines
     *      Une liste de polylignes attribuées
     * @param polygons
     *      Une liste de polygones attribués
     */
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons){
        this.polyLines = Collections.unmodifiableList(new ArrayList<>(polyLines) );
        this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons) );
    }

    /**
     * @return
     *      La liste des polylignes attribuées de la carte
     */
    public List<Attributed<PolyLine>> polyLines(){
        return polyLines;
    }

    /**
     * @return
     *      La liste des polygones attribués de la carte
     */
    public List<Attributed<Polygon>> polygons(){
        return polygons;
    }

    /**
     * Sert de bâtisseur à la carte projetée Map, permettant de la construire en plusieurs étapes
     */
    public static final class Builder {

        private final List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private final List<Attributed<Polygon>> polygons = new ArrayList<>();

        /**
         * Ajoute une polyligne attribuée à la carte en cours de construction
         *
         * @param newPolyLine
         *      La poyligne attribuée à ajouter
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine){
            polyLines.add(newPolyLine);
        }

        /**
         * Ajoute un polygone attribué à la carte en cours de construction
         *
         * @param newPolygon
         *      Le polygone attribué à ajouter
         */
        public void addPolygon(Attributed<Polygon> newPolygon){
            polygons.add(newPolygon);
        }

        /**
         * Construit une carte avec les polylignes et polygones ajoutés jusqu'à présent au bâtisseur
         *
         * @return
         *      Une carte projetée Map
         */
        public Map build(){
            return new Map(polyLines, polygons);
        }
    }
}
