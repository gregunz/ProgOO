package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Représente une toile abstraite permettant de dessiner des polylignes et polygones
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public interface Canvas {

	/**
	 * Permet de dessiner sur la toile une polyligne donnée avec un style de ligne donné
	 *
	 * @param polyline
	 * 		La polyligne à dessiner
	 * @param lineStyle
	 * 		Le style de ligne à appliquer au dessin de la polyligne
	 */
	void drawPolyLine(PolyLine polyline, LineStyle lineStyle);

	/**
	 * Permet de dessiner sur la toile un polygone donné avec une couleur donnée
	 *
	 * @param polygon
	 * 		Le polygone à dessiner
	 * @param color
	 * 		Le couleur de remplissage à appliquer au dessin du polygone
	 */
	void drawPolygon(Polygon polygon, Color color);
	
}
