package ch.epfl.imhof.dem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.EquirectangularProjection;
import ch.epfl.imhof.projection.Projection;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class HGTDigitalElevationModelTest {

    private static final String FILE_NAME = "N46E006.hgt";

    /*
    @Test
    public void constructorTest(){
        HGTDigitalElevationModel h = new HGTDigitalElevationModel(getFile(FILE_NAME));
    }

    private File getFile(String fileName){
        String url = getClass().getResource("/" + fileName).getFile();
        return new File(url);
    }

    @Test
    public void testNormalAt(){

        HGTDigitalElevationModel
                h1 = new HGTDigitalElevationModel(getFile(FILE_NAME)),
                h2 = new HGTDigitalElevationModel(getFile("N46E007.hgt"));

        final BufferedImage img = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);

        final Projection prj = new CH1903Projection();

        final PointGeo
                bl = new PointGeo(Math.toRadians(7.2), Math.toRadians(46.2)),
                tr = new PointGeo(Math.toRadians(7.8), Math.toRadians(46.8));

        final Point
                projectedBl = prj.project(bl), projectedTr = prj.project(tr),
                coordinateChangedBl = new Point(0., 799.), coordinateChangedTr = new Point(799., 0.);

        final Function<Point, Point> f =
                Point.alignedCoordinateChange(coordinateChangedBl, projectedBl, coordinateChangedTr, projectedTr);

        Vector3 normal;
        Color c;
        for(int i = 0; i < img.getWidth(); ++i){
            for(int j = 0; j < img.getHeight(); ++j){
                PointGeo p = prj.inverse(f.apply(new Point(i, j)));
                try{
                    normal = h1.normalAt(p).normalized();
                }
                catch (Exception e){
                    normal = h2.normalAt(p).normalized();
                }
                c = Color.gray(0.5*(normal.y()+1.));
                img.setRGB(i, j, c.toAWTColor().getRGB());
            }
        }

        try {
            ImageIO.write(img, "png", new File("images\\hgt.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
