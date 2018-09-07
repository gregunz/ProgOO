package ch.epfl.imhof.osm;

import ch.epfl.imhof.Map;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Date;

public class OSMToGeoTransformerSpeedTest {
    @Ignore
    public void mapTest() throws IOException, SAXException {
        Date debut = new Date();
        Projection p = new CH1903Projection();
        String file = getClass().getResource("/lausanne.osm.gz").getFile();
        file = file.replace("%c3%a9", "é");
        OSMMap osm = OSMMapReader.readOSMFile(file, true);
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(p);
        Map map = transformer.transform(osm);
        Date fin = new Date();
        System.out.println("Time to complete map transformation: " + (fin.getTime() - debut.getTime()) / 1000);
    }
}
