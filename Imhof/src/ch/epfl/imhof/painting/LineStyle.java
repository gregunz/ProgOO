package ch.epfl.imhof.painting;

import java.awt.*;

/**
 * Classe regroupant tous les paramètres de style utiles au dessin d'une ligne
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class LineStyle {

    /**
     * Enumération représentant la terminaison de ligne. Les paramètres possibles sont les suivants:
     * <p>- de manière abrupte (BUTT)</p>
     * <p>- au moyen d'un demi-cercle (ROUND)</p>
     * <p>- au moyen d'un demi-carré (SQUARE)</p>
     */
    public enum LineCap {
        BUTT(BasicStroke.CAP_BUTT), ROUND(BasicStroke.CAP_ROUND), SQUARE(BasicStroke.CAP_SQUARE);
        private final int basicStrokeCap;
        LineCap(int basicStrokeCap){
            this.basicStrokeCap = basicStrokeCap;
        }

        /**
         * @return
         *      La constante dans la classe native BasicStroke correspondant à cette terminaison de ligne
         */
        public int basicStrokeCap(){
            return basicStrokeCap;
        }
    }

    /**
     * Enumération représentant la jointure des segments. Les paramètres possibles sont les suivants:
     * <p>- par un trait (BEVEL)</p>
     * <p>- par prologation des côtés (MITER)</p>
     * <p>- par un arc de cercle (ROUND)</p>
     */
    public enum LineJoin {

        BEVEL(BasicStroke.JOIN_BEVEL), MITER(BasicStroke.JOIN_MITER), ROUND(BasicStroke.JOIN_ROUND);

        private final int basicStrokeJoin;

        LineJoin(int basicStrokejoin){
            this.basicStrokeJoin = basicStrokejoin;
        }

        /**
         * @return
         *      La constante dans la classe native BasicStroke correspondant à cette jointure de ligne
         */
        public int basicStrokeJoin(){
            return basicStrokeJoin;
        }
    }

    private final LineCap lineCap;
    private final LineJoin lineJoin;

    private final float width;
    private final float[] dashingPattern;

    private final Color color;

    /**
     * Initialise le style de ligne avec tous ses paramètres utiles
     *
     * @param width
     *      Largeur du trait
     * @param color
     *      Couleur du trait
     * @param lineCap
     *      Terminaison des lignes
     * @param lineJoin
     *      Jointure des segments
     * @param dashingPattern
     *      L'alternance des sections opaques et transparentes, pour le dessin en traitillés
     *
     * @throws IllegalArgumentException
     *      Si la largeur du trait est négative ou si l'un des éléments de la séquence d'alternance
     *      des segments est négatif ou nul
     */
    public LineStyle(float width, Color color, LineCap lineCap, LineJoin lineJoin, float[] dashingPattern) {
        this.dashingPattern = dashingPattern.clone();

        if (width < 0) {
            throw new IllegalArgumentException("La largeur de la ligne doit être positive. Largeur: " + width);
        }
        for (int i = 0; i < this.dashingPattern.length; ++i) {
            if (!(dashingPattern[i] > 0)) {
                throw new IllegalArgumentException(
                        "Elément de la séquence d'alternance des segments négatif ou nul.\n" +
                                "Indice: " + i + ", valeur: " + this.dashingPattern[i]
                );
            }
        }

        this.width = width;
        this.color = color;
        this.lineCap = lineCap;
        this.lineJoin = lineJoin;
    }

    /**
     * Initialise le style de ligne avec tous ses paramètres utiles. Le style de ligne est déduit plein
     * (séquence vide pour l'alternance des segments opaques et transparents).
     *
     * @param width
     *      Largeur du trait
     * @param color
     *      Couleur du trait
     * @param lineCap
     *      Terminaison des lignes
     * @param lineJoin
     *      Jointure des segments
     *
     * @throws IllegalArgumentException
     *      Si la largeur du trait est négative
     */
    public LineStyle(float width, Color color, LineCap lineCap, LineJoin lineJoin) {
        this(width, color, lineCap, lineJoin, new float[]{});
    }

    /**
     * Initialise le style de ligne en ne lui passant qu'un nombre minimal d'arguments. Déduit les valeurs par défaut
     * suivantes: butt pour la terminaison des lignes, miter pour la jointure des segments, et la séquence vide pour
     * l'alternance des segments opaques et transparents
     *
     * @param width
     *      Largeur de ligne
     * @param color
     *      Couleur de ligne
     */
    public LineStyle(float width, Color color) {
        this(width, color, LineCap.BUTT, LineJoin.MITER, new float[0]);
    }

    /**
     * @return
     *     La terminaison de ligne
     */
    public LineCap lineCap() {
        return lineCap;
    }

    /**
     * @return
     *      La jointure des segments de ligne
     */
    public LineJoin lineJoin() {
        return lineJoin;
    }

    /**
     * @return
     *      La largeur de la ligne
     */
    public float width() {
        return width;
    }

    /**
     * @return
     *      La séquence d'alternance des segments
     */
    public float[] dashingPattern() {
        return dashingPattern.clone();
    }

    /**
     * @return
     *      La couleur de ligne
     */
    public Color color() {
        return color;
    }

    /**
     * Permet d'obtenir un style identique à celui auquel on l'applique, sauf pour le paramètre dont la valeur
     * est passée en argument
     *
     * @param lineCap
     *      La terminaison de ligne de la nouvelle ligne
     * @return
     *      Un nouveau style de ligne ne différant du récepteur que par le paramètre fourni
     */
    public LineStyle withLineCap(LineCap lineCap) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * Permet d'obtenir un style identique à celui auquel on l'applique, sauf pour le paramètre dont la valeur
     * est passée en argument
     *
     * @param lineJoin
     *      La jointure des segments de ligne de la nouvelle ligne
     * @return
     *      Un nouveau style de ligne ne différant du récepteur que par le paramètre fourni
     */
    public LineStyle withLineJoin(LineJoin lineJoin) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * Permet d'obtenir un style identique à celui auquel on l'applique, sauf pour le paramètre dont la valeur
     * est passée en argument
     *
     * @param width
     *      La largeur de ligne de la nouvelle ligne
     * @return
     *      Un nouveau style de ligne ne différant du récepteur que par le paramètre fourni
     */
    public LineStyle withWidth(float width) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * Permet d'obtenir un style identique à celui auquel on l'applique, sauf pour le paramètre dont la valeur
     * est passée en argument
     *
     * @param dashingPattern
     *      La séquence d'alternance des segments de ligne de la nouvelle ligne
     * @return
     *      Un nouveau style de ligne ne différant du récepteur que par le paramètre fourni
     */
    public LineStyle withDashingPattern(float[] dashingPattern) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * Permet d'obtenir un style identique à celui auquel on l'applique, sauf pour le paramètre dont la valeur
     * est passée en argument
     *
     * @param color
     *      La couleur de ligne de la nouvelle ligne
     * @return
     *      Un nouveau style de ligne ne différant du récepteur que par le paramètre fourni
     */
    public LineStyle withColor(Color color) {
        return new LineStyle(width, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * Permet d'obtenir une représentation de ce style de ligne sous forme du type natif Stroke de l'API Java.
     * Utilise l'implémentation native BasicStroke de Stroke.
     *
     * @param miterLimit
     *      Le paramètre miterLimit de la classe native BasicStroke
     * @param dashPhase
     *      Le paramètre dashPhase de la classe native BasicStroke
     *
     * @return
     *      Le style de ligne de type Stroke correspondant au style de ligne récepteur
     */
    public Stroke toBasicStroke(float miterLimit, float dashPhase){
        float[] dashingPattern = this.dashingPattern.length > 0 ? this.dashingPattern : null;
        return new BasicStroke(
                width, lineCap.basicStrokeCap, lineJoin.basicStrokeJoin, miterLimit, dashingPattern, dashPhase
        );
    }

}