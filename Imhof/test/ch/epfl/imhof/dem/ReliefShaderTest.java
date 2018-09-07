package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.projection.CH1903Projection;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ReliefShaderTest {

    PointGeo bl = new PointGeo(Math.toRadians(7.8122), Math.toRadians(46.6645)),
                  tr = new PointGeo(Math.toRadians(7.9049), Math.toRadians(46.7061));

    @Test
    public void shadedReliefTest() {
        /*
        ReliefShader rs = new ReliefShader(new CH1903Projection(), getFile("N46E007.hgt"), new Vector3(-1, 1 , 1));
        /*
        try {
            ImageIO.write(rs.shadedRelief(bl, tr, 3316, 2188, 5.834646f), "png", new File("images\\test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    private File getFile(String fileName) {
        String url = getClass().getResource("/" + fileName).getFile();
        return new File(url);
    }

}
