package ch.epfl.imhof.painting;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Représente une toile permettant de dessiner dans une image discrète des polylignes et polygones
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Java2DCanvas implements Canvas {

    private static final float MITER_LIMIT = 10, DASH_PHASE = 0;

	private final Function<Point,Point> coordinateChanged;
	private final BufferedImage image;
	private final Graphics2D ctx;

    /**
     * Initialise une toile avec ses caractéristiques
     *
     * @param bLeft
     *      Point en coordonnées du plan correspondant au coins bas-gauche de la toile
     * @param tRight
     *      Point en coordonnées du plan correspondant au coin haut-droite de la toile
     * @param width
     *      Largeur de la toile en pixels
     * @param height
     *      Hauteur de la toile en pixels
     * @param dpi
     *      La résolution de l'image de la toile, en points par pouce
     * @param color
     *      La couleur de fond de la toile
     */
	public Java2DCanvas(Point bLeft, Point tRight, int width, int height, double dpi, Color color){
		final double scale = dpi/72d;
		this.coordinateChanged =
                Point.alignedCoordinateChange(bLeft, new Point(0, height/scale), tRight, new Point(width/scale, 0));
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.ctx = image.createGraphics();
		ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		ctx.setColor(color.toAWTColor());
        ctx.fillRect(0, 0, width, height);
		ctx.scale(scale, scale);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void drawPolyLine(PolyLine polyline, LineStyle lineStyle) {
        ctx.setStroke(lineStyle.toBasicStroke(MITER_LIMIT, DASH_PHASE));
        ctx.setColor(lineStyle.color().toAWTColor());
		ctx.draw(makeShape(polyline));
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void drawPolygon(Polygon polygon, Color color) {
		final Area area = new Area( makeShape( polygon.shell() ) );
		for(PolyLine p : polygon.holes()){
			area.subtract( new Area(makeShape(p)) );
		}
		ctx.setColor(color.toAWTColor());
		ctx.fill( area );
	}

    /**
     * "Convertit" une polyligne en Shape de l'API Java
     */
	private Shape makeShape(PolyLine polyline){
		final Path2D line = new Path2D.Double();
		boolean first = true;

		for(Point p : polyline.points()){
			Point newP = coordinateChanged.apply(p);
			if(first){
				first = false;
				line.moveTo( newP.x(), newP.y() );
			}
            else{
				line.lineTo( newP.x(), newP.y() );
			}			
		}
		if( polyline.isClosed() ){
            line.closePath();
        }
		return line;
	}
	
	/**
	 * Retourne l'image de la toile
	 * 
	 * @return
	 * 		image de la toile
	 */
	public BufferedImage image(){
		return image;
	}

}
