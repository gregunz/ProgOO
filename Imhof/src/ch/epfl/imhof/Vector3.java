package ch.epfl.imhof;

/**
 * Représente un vecteur tridimensionnel
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Vector3 {

    private final double x, y, z;

    /**
     * Initialise un vecteur tridimensionnel avec ses trois coordonnées dans l'espace
     *
     * @param x
     *      Coordonnée x
     * @param y
     *      Coordonnée y
     * @param z
     *      Coordonnée z
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return
     *      Composante x du vecteur
     */
    public double x(){
        return x;
    }

    /**
     * @return
     *      Composante y du vecteur
     */
    public double y(){
        return y;
    }

    /**
     * @return
     *      Composante z du vecteur
     */
    public double z(){
        return z;
    }

    /**
     * @return
     *      La norme du vecteur
     */
    public double norm(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Permet d'obtenir la version normalisée du vecteur (c-à-d un vecteur parallèle à celui-ci,
     * de même direction mais de longueur unitaire)
     *
     * @return
     *      Ce vecteur normalisé
     */
    public Vector3 normalized(){
        final double norm = norm();
        return new Vector3(x/norm, y/norm, z/norm);
    }

    /**
     * Retourne le produit scalaire entre le récepteur et un second vecteur passé en argument
     *
     * @param other
     *      Le deuxième vecteur impliqué dans le produit scalaire
     *
     * @return
     *      Le produit scalaire entre le vecteur récepteur et le vecteur passé en paramètre
     */
    public double scalarProduct(Vector3 other){
        return x*other.x + y*other.y + z*other.z;
    }

    /**
     * Retourne le produit vectoriel entre le récepteur et un second vecteur passé en argument
     *
     * @param other
     *      Le deuxième vecteur impliqué dans le produit vectoriel
     *
     * @return
     *      Le produit vectoriel entre le vecteur récepteur (premier élément du produit) et le vecteur passé
     *      en paramètre
     */
    public Vector3 crossProduct(Vector3 other){
        return new Vector3(
                y * other.z - z * other.y,
                - (x * other.z - z * other.x),
                x * other.y - y * other.x
        );
    }

    /**
     * Retourne le vecteur correspondant à l'addition entre le récepteur et un second vecteur passé en argument
     *
     * @param other
     *      Le deuxième vecteur impliqué dans l'addition
     *
     * @return
     *      L'addition des deux vecteurs
     */
    public Vector3 add(Vector3 other){
        return new Vector3(
                x + other.x,
                y + other.y,
                z + other.z
        );
    }

    /**
     * Multiplie ce vecteur par un scalaire
     *
     * @param scalar
     *      Un nombre
     *
     * @return
     *      Le vecteur correspondant à la multiplication de ce vecteur par le scalaire
     */
    public Vector3 multiplyWithScalar(double scalar){
        return new Vector3(
                x * scalar,
                y * scalar,
                z * scalar
        );
    }
}
