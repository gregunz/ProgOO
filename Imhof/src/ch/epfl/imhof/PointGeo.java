package ch.epfl.imhof;

import static java.lang.Math.PI;

/**
 * Un point à la surface de la terre, en coordonnées sphériques
 * 
 * @author Ali Hosseiny (sciper)
 * @author Grégoire Clément (sciper)
 */
public final class PointGeo {

	private final double longitude, latitude;

	/**
	 * Construit un point à la surface de la terre à l'aide de sa longitude et latitude
	 * 
	 * @param longitude
	 *            la longitude en radians
	 * @param latitude
	 *            la latitude en radians
	 * @throws IllegalArgumentException
	 *             si la longitude n'est pas entre -pi et pi
	 * @throws IllegalArgumentException
	 *             si la latitude n'est pas entre -pi/2 et pi/2
	 */
	public PointGeo(double longitude, double latitude){
		if (longitude < -PI || longitude > PI) {
			throw new IllegalArgumentException("La longitude doit se trouver dans l'intervalle [-pi,pi]");
		}
        else if(latitude < -PI / 2. || latitude > PI / 2.){
            throw new IllegalArgumentException("La latitude doit se trouver dans l'intervalle [-pi/2,pi/2]");
        }

		this.longitude = longitude;
		this.latitude = latitude;
	}

	/**
	 * @return
	 * 		La longitude du point
	 */
	public double longitude() {
		return longitude;
	}

	/**
	 * @return
	 * 		La latitude du point
	 */
	public double latitude() {
		return latitude;
	}

    /**
     * @return
     *      Une représentation textuelle du point (longitude, latitude)
     */
    @Override
    public String toString(){
        return "(" + longitude + ", " + latitude + ")";
    }

}
