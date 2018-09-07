package ch.epfl.imhof.painting;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Générateur de peintre de réseau routier. Cette classe n'est pas instanciable
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class RoadPainterGenerator {

    private static final LineCap
            BRIDGES_CAP = LineCap.ROUND,
            BRIDGES_BORDER_CAP = LineCap.BUTT,
            ROADS_BORDER_CAP = LineCap.ROUND,
            TUNNEL_CAP = LineCap.BUTT;
    private static final LineJoin
            BRIDGES_JOIN = LineJoin.ROUND,
            BRIDGES_BORDER_JOIN = LineJoin.ROUND,
            TUNNEL_JOIN = LineJoin.ROUND;

    private static final Predicate<Attributed<?>>
            IS_BRIDGE = Filters.tagged("bridge"),
            IS_TUNNEL = Filters.tagged("tunnel"),
            IS_ROAD = IS_BRIDGE.negate().and(IS_TUNNEL.negate());


    /**
     * Prend un nombre variable de spécifications de routes (concept décrit ci-après) en arguments.
     * Elle retourne le peintre pour le réseau routier correspondant. les peintres de chacun des cinq types donnés
     * sont empilés de manière à ce que celui correspondant à la première spécification de route soit au sommet,
     * celui correspondant à la seconde juste en dessous, et ainsi de suite.
     *
     * @param roadSpecs
     *      Un nombre arbitraire de spécifications de routes décrivant le dessin de ces derniers
     *
     * @return
     *      Un peintre dessinant le réseau routier en utilisant les spécifications données
     */
    public static Painter painterForRoads(RoadSpec... roadSpecs){
        Painter bridges = null, bridgesBorder = null, roads = null, roadsBorder = null, tunnels = null;

        for (RoadSpec roadSpec : roadSpecs) {
            bridges =
                    Painter
                            .line(roadSpec.lineStyleForBridge())
                            .when(roadSpec.filter().and(IS_BRIDGE))
                            .under(bridges);
            bridgesBorder =
                    Painter
                            .line(roadSpec.lineStyleForBridgeBorder())
                            .when(roadSpec.filter().and(IS_BRIDGE))
                            .under(bridgesBorder);
            roads =
                    Painter
                            .line(roadSpec.lineStyleForRoad())
                            .when(roadSpec.filter().and(IS_ROAD))
                            .under(roads);
            roadsBorder =
                    Painter
                            .line(roadSpec.lineStyleForRoadBorder())
                            .when(roadSpec.filter().and(IS_ROAD))
                            .under(roadsBorder);
            tunnels =
                    Painter
                            .line(roadSpec.lineStyleForTunnel())
                            .when(roadSpec.filter().and(IS_TUNNEL))
                            .under(tunnels);
        }

        assert bridges != null;
        return bridges
                .above(bridgesBorder)
                .above(roads)
                .above(roadsBorder)
                .above(tunnels);
    }

    /**
     * Une spécification de route décrit le dessin d'un type de route donné. Elle est composée d'une part d'un filtre
     * permettant de sélectionner ce type de route, et d'autre part des quatre paramètres de style à partir desquels
     * tous les styles des peintres sont déterminés
     */
    public static final class RoadSpec{

        private final Predicate<Attributed<?>> filter;
        private final float innerWidth, outerWidth;
        private final Color innerColor, outerColor;

        /**
         * Initialise une spécification de route
         *
         * @param filter
         *      Le filtre permettant de déterminer quelles entités seront ciblées par cette spécification
         * @param innerWidth
         *      Largeur du trait intérieur
         * @param innerColor
         *      Couleur du trait intérieur
         * @param outerWidth
         *      Largeur du trait de la bordure
         * @param outerColor
         *      Couleur du trait de la bordure
         */
        public RoadSpec(Predicate<Attributed<?>> filter,
                        float innerWidth, Color innerColor, float outerWidth, Color outerColor){

            Objects.requireNonNull(filter);
            Objects.requireNonNull(innerColor);
            Objects.requireNonNull(outerColor);

            this.filter = filter;
            this.innerWidth = innerWidth;
            this.innerColor = innerColor;
            this.outerWidth = outerWidth;
            this.outerColor = outerColor;
        }

        /**
         * @return
         *      Filtre de la spécification
         */
        public Predicate<Attributed<?>> filter() {
            return filter;
        }

        /**
         * Style de ligne adapté au trait intérieur des ponts à partir des paramètres de cette spécifications
         *
         * @return
         *      Style de ligne avec comme largeur innerWidth, la couleur innerColor, la terminaison ROUND,
         *      la jointure ROUND et le trait plein
         */
        public LineStyle lineStyleForBridge(){
            return new LineStyle(innerWidth, innerColor, BRIDGES_CAP, BRIDGES_JOIN);
        }

        /**
         * Style de ligne adapté à la bordure des ponts à partir des paramètres de cette spécifications
         *
         * @return
         *      Style de ligne avec comme largeur innerWidth+2*outerWidth, la couleur outerColor, la terminaison butt,
         *      la jointure round et le trait plein
         */
        public LineStyle lineStyleForBridgeBorder(){
            return new LineStyle(innerWidth + 2*outerWidth, outerColor, BRIDGES_BORDER_CAP, BRIDGES_BORDER_JOIN);
        }

        /**
         * Style de ligne adapté au trait intérieur des routes à partir des paramètres de cette spécifications
         *
         * @return
         *      Style de ligne semblable à celui retourné par lineStyleForBridge
         */
        public LineStyle lineStyleForRoad(){
            return lineStyleForBridge();
        }

        /**
         * Style de ligne adapté à la bordure des routes à partir des paramètres de cette spécifications
         *
         * @return
         *      Style de ligne semblable à celui retourné par lineStyleForBridgeBorder, sauf la terminaison
         *      qui est round
         */
        public LineStyle lineStyleForRoadBorder(){
            return lineStyleForBridgeBorder().withLineCap(ROADS_BORDER_CAP);
        }

        /**
         * Style de ligne adapté aux tunnels à partir des paramètres de cette spécifications
         *
         * @return
         *      Style de ligne avec a largeur innerWidth/2, la couleur outerColor, la terminaison BUTT,
         *      la jointure ROUND et une séquence d'alternance des sections opaques et transparentes de
         *      [2*innerWidth,2*innerWidth]
         */
        public LineStyle lineStyleForTunnel(){
            float[] dashingPattern = new float[]{2*innerWidth, 2*innerWidth};
            return new LineStyle(innerWidth/2, outerColor, TUNNEL_CAP, TUNNEL_JOIN, dashingPattern);
        }
    }
}
