package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Interface décrivant une projection
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public interface Projection {
    
    /**
     * <p>Projette un point sur la surface de la terre (long. et lat.) en un point sur un plan</p>
     * <p>(en coordonnées cartésiennes)</p>
     * 
     * @param point
     * 		point sur la surface de la terre que l'on veut projeter
     * @return 
     * 		point projeté sur le plan (coordonnées cartésiennes)
     */
    Point project(PointGeo point);
    
    /**
     * Dé-projette un point du plan reçu en coordonnées cartésiennes en un point sur la surface
     * de la terre (long. et lat.)
     * 
     * @param point
     * 		point du plan que l'on veut dé-projeter
     * @return
     * 		point dé-projeté (long. lat.)
     */
    PointGeo inverse(Point point);
    
}
