package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente une polyligne
 * Ses sous-classes représentent les polylignes ouvertes et fermées
 * 
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */

public abstract class PolyLine {
    
    private final List<Point> points;

    /**
     * Construis une polyligne à partir des sommets donnés.
     * La polyligne construite est immuable
     * 
     * @param points
     *          Une liste de sommets formant une polyligne
     *          
     * @throws IllegalArgumentException
     *          Si la liste des sommets est vide
     */
    public PolyLine(List<Point> points){
        if(points.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.points = Collections.unmodifiableList( new ArrayList<>(points) );
    }
    
    
    /**
     * @return
     *      <p>true si la polyligne est fermée</p>
     *      <p>false autrement</p>
     */
    public abstract boolean isClosed();
    
    /**
     * @return
     *      Retourne une liste contenant tous les sommets
     */
    public List<Point> points(){
        return points;
    }
    
    /**
     * @return
     *      Retourne le premier point de la polyligne
     */
    public Point firstPoint(){
        return points.get(0);
    }

    /**
     * Permet de construire une polyligne en plusieurs étapes
     */
    public static final class Builder{

        private final List<Point> points = new ArrayList<>();

        /**
         * Ajoute un point à la polyligne en cours de construction
         *
         * @param newPoint
         *      Point à ajouter à la polyligne
         */
        public void addPoint(Point newPoint){
            points.add(newPoint);
        }

        /**
         * Achève la costruction d'une polyligne ouverte à partir des points précédemment ajoutés
         *
         * @return
         *      Une polyligne ouverte
         */
        public OpenPolyLine buildOpen(){
            return new OpenPolyLine(points);
        }

        /**
         * Achève la costruction d'une polyligne fermée à partir des points précédemment ajoutés
         *
         * @return
         *      Une polyligne fermée
         */
        public ClosedPolyLine buildClosed(){
            return new ClosedPolyLine(points);
        }
    }

}
