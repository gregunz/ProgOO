package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Classe représentant la "projection" CH1903
 * 
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public final class CH1903Projection implements Projection{
	
	/**
	  * {@inheritDoc}
	  */
    @Override
    public Point project(PointGeo point) {
        double 
            lng1 = ( Math.toDegrees( point.longitude() ) * 3600. - 26782.5 ) / 10000.,
            lat1 = ( Math.toDegrees( point.latitude() ) * 3600. - 169028.66 ) / 10000.;
        
        return new Point(
                600072.37 + 211455.93*lng1 - 10938.51*lng1*lat1 - 0.36*lng1*Math.pow(lat1, 2) - 44.54*Math.pow(lng1, 3),
                200147.07 + 308807.95*lat1 + 3745.25*Math.pow(lng1, 2) + 76.63*Math.pow(lat1, 2) -
                        194.56*Math.pow(lng1, 2)*lat1 + 119.79*Math.pow(lat1, 3)
        		);
    }
    
    /**
	  * {@inheritDoc}
	  */
    @Override
    public PointGeo inverse(Point point) {
        double 
            x1 = (point.x() - 600000.)/1000000.,
            y1 = (point.y() - 200000.)/1000000.,
            lng0 = 2.6779094 + 4.728982*x1 + 0.791484*x1*y1 + 0.1306*x1*Math.pow(y1, 2) - 0.0436*Math.pow(x1, 3),
            lat0 = 16.9023892 + 3.238272*y1 - 0.270978*Math.pow(x1, 2) - 0.002528*Math.pow(y1, 2) -
                    0.0447*Math.pow(x1, 2)*y1 - 0.014*Math.pow(y1, 3);
        
        return new PointGeo(
                Math.toRadians(lng0 * 100. / 36.),
                Math.toRadians(lat0 * 100. / 36.)
                );
        
    }

    

}