package ch.epfl.imhof.painting;

import java.util.function.BiConsumer;

/**
 * Cette classe représente une couleur par ses trois composantes RGB
 * Cette classe est finale et immuable
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Color {

    private final double r, g, b;

    /**
     * La couleur « rouge »
     */
    public final static Color RED = new Color(1, 0, 0);

    /**
     * La couleur « vert »
     */
    public final static Color GREEN = new Color(0, 1, 0);

    /**
     * La couleur « bleu »
     */
    public final static Color BLUE = new Color(0, 0, 1);

    /**
     * La couleur « noir ».
     */
    public final static Color BLACK = new Color(0, 0, 0);

    /**
     * La couleur « blanc ».
     */
    public final static Color WHITE = new Color(1, 1, 1);

    private Color(double r, double g, double b){
        final BiConsumer<Double, String> validRGB = (d, s) -> {
            if (!(0.0 <= d && d <= 1.0)) {
                throw new IllegalArgumentException("Composante " + s + " invalide: " + d);
            }
        };

        validRGB.accept(r, "rouge");
    	validRGB.accept(g, "verte");
    	validRGB.accept(b, "bleue");

        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Prends en argument une seule valeur comprise entre 0 et 1 et construit la couleur grise dont les trois
     * composantes sont égales à cette valeur
     *
     * @param value
     *      La valeur des trois composantes de la couleur grise
     *
     * @return
     *      Une couleur grise
     *
     * @throws IllegalArgumentException
     *      Si la composante de la couleur est en dehors de la plage 0-1 (compris)
     */
    public static Color gray(double value){
        return new Color(value, value, value);
    }

    /**
     * Construit une couleur définie par ces trois composantes rouge, verte et bleue (r, g, b)
     *
     * @param r
     *      Rouge
     * @param g
     *      Vert
     * @param b
     *      Bleu
     *
     * @return
     *      Une couleur de type Color
     *
     * @throws IllegalArgumentException
     *      Si une des composantes de la couleur est en dehors de la plage 0-1 (compris)
     */
    public static Color rgb(double r, double g, double b){
        return new Color(r, g, b);
    }

    /**
     * Construit une couleur en « déballant » les trois composantes RGB stockées
     * chacune sur 8 bits. La composante R est supposée occuper les bits 23 à
     * 16, la composante G les bits 15 à 8 et la composante B les bits 7 à 0.
     * Les composantes sont de plus supposées gamma-encodées selon le standard
     * sRGB.
     *
     * @param packedRGB
     *      La couleur encodée, au format RGB
     */
    public static Color rgb(int packedRGB){
        return new Color(
                ((packedRGB >> 16) & 0xFF) / 255d,
                ((packedRGB >>  8) & 0xFF) / 255d,
                ((packedRGB >>  0) & 0xFF) / 255d
        );
    }

    /**
     * Retourne la composante rouge de la couleur, comprise entre 0 et 1.
     *
     * @return
     *      La composante rouge de la couleur.
     */
    public double r(){
        return r;
    }

    /**
     * Retourne la composante verte de la couleur, comprise entre 0 et 1.
     *
     * @return
     *      La composante verte de la couleur.
     */
    public double g(){
        return g;
    }

    /**
     * Retourne la composante bleue de la couleur, comprise entre 0 et 1.
     *
     * @return
     *      La composante bleue de la couleur.
     */
    public double b() {
        return b;
    }

    /**
     * Convertit la couleur en une couleur AWT, couleur de l'API Java.
     *
     * @return
     *      La couleur AWT correspondant à la couleur réceptrice.
     */
    public java.awt.Color toAWTColor() {
        return new java.awt.Color((int)(r*255.9999), (int)(g*255.9999), (int)(b*255.9999));
    }

    /**
     * Retourne une nouvelle couleur dont les composantes sont la multiplication
     * de la couleur réceptrice et d'une autre couleur
     *
     * @param other
     *      L'autre couleur
     *
     * @return
     *      Une nouvelle couleur issue de la multiplication
     */
    public Color multiplyWith(Color other){
        return new Color(r * other.r, g * other.g, b * other.b);
    }

    /**
     * @return
     *      Représentation textuelle de la couleur
     */
    @Override
    public String toString(){
        return "(" + r + ", " + g + ", " + b + ")";
    }

}
