package ch.epfl.imhof.painting;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.SwissPainter;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

public class DrawLausanne {

    @Test
    public void draw(){

        final Painter painter = SwissPainter.painter();

        final Map map = readFile("lausanne.osm.gz");

        // La toile
        Point bl = new Point(532510, 150590);
        Point tr = new Point(539570, 155260);
        Java2DCanvas canvas =
                new Java2DCanvas(bl, tr, 1600, 1060, 150, Color.WHITE);

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(map, canvas);

        try {
            ImageIO.write(canvas.image(), "png", new File("images\\loz.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map readFile(String fileName){
        OSMMap m = null;
        String url = getClass().getResource("/" + fileName).getFile();
        final Projection p = new CH1903Projection();

        url = url.replace("%c3%a9", "é");
        boolean bool = url.contains(".gz");

        try {
            m = OSMMapReader.readOSMFile(url, bool);
        }
        catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return new OSMToGeoTransformer(p).transform(m);
    }
}
