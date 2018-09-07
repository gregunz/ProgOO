package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Classe représentant la projection équirectangulaire
 * 
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class EquirectangularProjection implements Projection{
	
	/**
	  * {@inheritDoc}
	  */
    @Override
    public Point project(PointGeo point) {
        return new Point( point.longitude(), point.latitude() );
    }

	/**
	  * {@inheritDoc}
	  */
    @Override
    public PointGeo inverse(Point point) {
        return new PointGeo( point.x(), point.y() );
    }
    
}
