package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

/**
 * Classe permettant de dessiner un relief ombré coloré
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class ReliefShader {

    final private Projection proj;
    final private File dem;
    final private Vector3 light;

    /**
     * Prend en arguments la projection à utiliser, le modèle numérique du terrain
     * et le vecteur pointant dans la direction de la source lumineuse
     *
     * @param proj
     *      projection à utiliser pour cartographie le relief
     * @param dem
     *      chemin du modèle numérique du terrain
     * @param light
     *      vecteur point dans la direction de la source lumineuse
     */
    public ReliefShader(Projection proj, String dem, Vector3 light) {
        this.proj = proj;
        this.dem = new File(dem);
        this.light = light;
    }

    /**
     * Dessine l'image du relief ombré coloré à l'aide des coins bas-gauche et haut-droite,
     * de la largeur et hauteur du l'image à dessiner ainsi que son rayon de floutage
     *
     * @param bottomLeft
     *      le coin bas-gauche du relief à dessiner
     * @param topRight
     *      le coin haut-droite du relief à dessiner
     * @param width
     *      la largeur en pixel de l'image à dessiner
     * @param height
     *      la hauteur en pixel de l'image à dessiner
     * @param radius
     *      le rayon de floutage
     * @return
     *      l'image du relief flouté selon un certain rayon de floutage aux coordonnées désirées
     */
    public BufferedImage shadedRelief(Point bottomLeft, Point topRight, int width,
            int height, double radius) throws IOException {
        if(radius < 0){
            throw new IllegalArgumentException("Le rayon de floutage doit être supérieur ou égal à 0");
        }
        Kernel kernel = createKernel(radius);
        int edge = kernel.getWidth() / 2;
        final Point
                coordinateChangedBl = new Point(edge, height + edge ),
                coordinateChangedTr = new Point(width + edge, edge );
        final Function<Point, Point> f =
                Point.alignedCoordinateChange(coordinateChangedBl, bottomLeft, coordinateChangedTr, topRight);
        if(edge == 0){
            return notBlurredImage(f, width, height);
        }
        return blurredImage(notBlurredImage(f, width + 2*edge, height + 2*edge), kernel )
                  .getSubimage(edge, edge, width, height);
    }

    //crée l'image du relief pas encore flouté
    private BufferedImage notBlurredImage(Function<Point, Point> f, int width, int height) throws IOException {
        try(final HGTDigitalElevationModel hgt = new HGTDigitalElevationModel(dem) ) {
            final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Vector3 normal;
            Color c;
            for (int i = 0; i < img.getWidth(); ++i) {
                for (int j = 0; j < img.getHeight(); ++j) {
                    PointGeo p = proj.inverse(f.apply(new Point(i, j)));
                    normal = hgt.normalAt(p).normalized();
                    double cosine = normal.scalarProduct(light) / (normal.norm() * light.norm()),
                           rg = 0.5 * (cosine + 1) ,
                           b = 0.5 * (0.7 * cosine + 1);
                    c = Color.rgb(rg, rg, b);
                    img.setRGB(i, j, c.toAWTColor().getRGB());
                }
            }
            return img;
        }
    }

    //crée un Kernel unidimensionel horizonzal à selon un rayon de floutage
    private Kernel createKernel(double radius) {
        final double o = radius / 3f;
        final int n = (int) Math.ceil(radius) * 2 + 1;
        final float[] ker = new float[n];

        float sum = 0;
        for (int i = -n/2; i < n/2+1; ++i) {
            float val = (float) Math.exp( - Math.pow(i, 2) / (2 * Math.pow(o, 2)) );
            ker[i + n/2] = val;
            sum += val;
        }
        for (int i = 0; i < ker.length; ++i) {
            ker[i] /= sum;
        }
        return new Kernel(n, 1, ker);
    }

    //Floute une image en à l'aide d'un kernel à une dimension, horizontalement puis verticalement
    private BufferedImage blurredImage(BufferedImage img, Kernel kernel) {
        ConvolveOp op = new ConvolveOp(kernel,  ConvolveOp.EDGE_NO_OP, null);
        BufferedImage filteredImg = op.filter(img, null);
        kernel = new Kernel( 1, kernel.getWidth(), kernel.getKernelData(null) );
        op = new ConvolveOp(kernel,  ConvolveOp.EDGE_NO_OP, null);
        filteredImg = op.filter(filteredImg, null);
        return filteredImg;
    }
}
