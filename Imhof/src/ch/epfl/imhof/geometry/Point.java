package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * Représente un point dans le plan en coordonnées cartésiennes
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Point {
    
	private final double x, y;

    /**
     * Construit un point dans le plan, en coordonnées cartésiennes
     * 
     * @param x
     * 		coordonnée selon l'axe x
     * @param y
     * 		coordonnée selon l'axe y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
	
    /**
     * @return
     *  	La coordonnée x du point
     */
    public double x(){
        return x;
    }
    
    /**
     * @return
     * 		La coordonnée y du point
     */
    public double y(){
        return y;
    }

    /**
     * Retourne une fonction calculant un changement de repère entre deux repères alignés.
     * Les deux repères sont définis en terme de deux points, chacun exprimé dans les deux repères.
     * Il faut donc fournir un total de quatre points à la fonction.
     *
     * @param p1From
     *      Le premier point exprimé dans le repère de départ
     * @param p1To
     *      Le premier point exprimé dans le repère d'arrivée
     * @param p2From
     *      Le deuxième point exprimé dans le repère de départ
     * @param p2To
     *      Le deuxième point exprimé dans le repère d'arrivée
     *
     * @return
     *      Une fonction définissant permettant d'obtenir les coordonnés d'un point dans le repère d'arrivée,
     *      en fonction de ses coordonnées dans le repère de départ
     *
     * @throws IllegalArgumentException
     *      Si un des couples est aligné horizontalement ou verticalement, rendant le calcul du changement
     *      de repère impossible
     */
    public static Function<Point, Point> alignedCoordinateChange(Point p1From, Point p1To, Point p2From, Point p2To){
        if(p1From.x == p1To.x || p1From.y == p1To.y || p2From.x == p2To.x || p2From.y == p2To.y){
            StringBuilder errorMessage =
                    new StringBuilder("Impossible de calculer le changement de repère: les points ");

            if(p1From.x == p1To.x){
                errorMessage.append("P1 sont alignés verticalement");
            }
            else if(p1From.y == p1To.y){
                errorMessage.append("P1 sont alignés horizontalement");
            }
            else if(p2From.x == p2To.x){
                errorMessage.append("P2 sont alignés verticalement");
            }
            else if(p2From.y == p1To.y){
                errorMessage.append("P2 sont alignés horizontalement");
            }

            throw new IllegalArgumentException(errorMessage.toString());
        }

        return point -> {
            double ax, bx, ay, by;

            ax = ( p2To.x - p1To.x ) / ( p2From.x - p1From.x );
            bx = p1To.x - ax * p1From.x;

            ay = ( p2To.y - p1To.y ) / ( p2From.y - p1From.y );
            by = p1To.y - ay * p1From.y;

            return new Point(ax * point.x + bx, ay * point.y + by);
        };

    }

    /**
     * @return
     *      Une représentation textuelle du point (x, y)
     */
    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }

}
