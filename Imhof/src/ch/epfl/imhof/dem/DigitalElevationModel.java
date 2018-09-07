package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Représente un modèle numérique du terrain
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public interface DigitalElevationModel extends AutoCloseable {

    /**
     * Prend un point en coordonnées WGS 84 en argument et retourne le vecteur normal à la Terre en ce point.
     *
     * @param p
     *      Point en coordonnées WGS 84 en lequel on aimerait obtenir le vecteur normal à la Terre
     *
     * @return
     *      Vecteur normal à la terre en le point donné
     *
     * @throws IllegalArgumentException
     *      Si le point pour lequel la normale est demandé ne fait pas partie de la zone couverte par le MNT
     */
	Vector3 normalAt(PointGeo p);
	
}
