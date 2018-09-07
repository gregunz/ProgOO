package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.*;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Interface fonctionnelle représentant un peintre
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */

@FunctionalInterface
public interface Painter {

	/**	
	 * Dessine une carte donnée sur une toile donnée
	 * 
	 * @param map
	 * 		la carte à dessiner
	 * @param can
	 * 		la toile où dessiner
	 */
	void drawMap(Map map, Canvas can);
	
	/**
	 * Dessine l'intérieur de tous les polygones de la carte reçue avec la 
	 * couleur donnée en argument
	 * 
	 * @param c
	 * 		couleur des polygones à dessiner
	 * @return
	 * 		peintre dessinant l'intérieur des polygones de la couleur donnée
	 */
	static Painter polygon(Color c){
		return (map, can) -> map.polygons().forEach(attPolygon -> can.drawPolygon(attPolygon.value(), c));
	}
	
	/**
	 * Dessine toutes les lignes de la carte qu'on lui fournit avec le style donné
	 * 
	 * @param l
	 * 		style de ligne à dessiner
	 * @return
	 * 		peintre dessinant les lignes avec avec le style donné
	 */
	static Painter line(LineStyle l){
		return (map, can) -> map.polyLines().forEach(attPolyLine -> can.drawPolyLine(attPolyLine.value(), l));
	}
	
	/**
	 * Dessine toutes les lignes de la carte qu'on lui fournit avec le style défini
	 * par les différents arguments donnée
	 * 
	 * @param width
	 * 		largeur du trait
	 * @param color
	 * 		couleur du trait
	 * @param lineCap
	 * 		terminaison des lignes
	 * @param lineJoin
	 * 		jointure des segments
	 * @param dashingPattern
	 * 		alternance des sections opaques et transparentes
	 * @return
	 * 		peintre dessinant les lignes avec avec le style donné
	 * @throws IllegalArgumentException
     *      Si la largeur du trait est négative ou si l'un des éléments de la séquence d'alternance
     *      des segments est négatif ou nul
     */
	static Painter line(float width, Color color, LineCap lineCap, LineJoin lineJoin, float[] dashingPattern){
		return line(new LineStyle(width, color, lineCap, lineJoin, dashingPattern));
	}
	
	/**
	 * Dessine toutes les lignes de la carte qu'on lui fournit avec le style défini
	 * par les différents arguments donnée
	 * 
	 * @param width
	 * 		largeur du trait
	 * @param color
	 * 		couleur du trait
	 * @return
	 * 		peintre dessinant les lignes avec avec le style donné
	 * @throws IllegalArgumentException
     *      Si la largeur du trait est négative
     */
	static Painter line(float width, Color color){
		return line(new LineStyle(width, color));
	}
	
	/**
	 * Dessine les pourtours de l'enveloppe et des trous des polygones de la carte
	 * qu'on lui fournit avec le style donné
	 * 
	 * @param l
	 * 		style des pourtours à dessiner
	 * @return
	 * 		peintre dessinant les pourtours avec le style donné
	 */
	static Painter outline(LineStyle l){
		return (map, can) ->
                map.polygons().forEach(attPolygon ->
                    {
                        final Polygon polygon = attPolygon.value();
                        can.drawPolyLine(polygon.shell(), l);
                        polygon.holes().forEach(hole -> can.drawPolyLine(hole, l));
                    }
                );
	}
	
	/**
	 * Dessine les pourtours de l'enveloppe et des trous des polygones de la carte
	 * qu'on lui fournit avec le style défini par les différents arguments
	 * 
	 * @param width
	 * 		largeur du trait
	 * @param color
	 * 		couleur du trait
	 * @param lineCap
	 * 		terminaison des lignes
	 * @param lineJoin
	 * 		jointure des segments
	 * @param dashingPattern
	 * 		alternance des sections opaques et transparentes
	 * @return
	 * 		peintre dessinant les pourtours avec le style donné
	 * @throws IllegalArgumentException
     *      Si la largeur du trait est négative ou si l'un des éléments de la séquence d'alternance
     *      des segments est négatif ou nul
     */
	static Painter outline(float width, Color color, LineCap lineCap, LineJoin lineJoin, float[] dashingPattern){
		return outline(new LineStyle(width, color, lineCap, lineJoin, dashingPattern));
	}
	
	/**
	 * Dessine les pourtours de l'enveloppe et des trous des polygones de la carte
	 * qu'on lui fournit avec le style défini par les différents arguments
	 * 
	 * @param width
	 * 		largeur du trait
	 * @param color
	 * 		couleur du trait
	 * @return
	 * 		peintre dessinant les pourtours avec le style donné
	 * @throws IllegalArgumentException
     *      Si la largeur du trait est négative
     */
	static Painter outline(float width, Color color){
		return outline(new LineStyle(width, color));
	}
	
	/**
	 * Retourne un peintre se comportant comme celui auquel on l'applique considérant
	 * uniquement les éléments de la carte satisfaisant le prédicat donné
	 * 
	 * @param pred
	 * 		prédicat que les éléments de la carte doivent respecter
	 * @return
	 * 		peintre considérant uniquement les éléments de la carte satisfaisant le prédicat donné
	 */
	default Painter when(Predicate<Attributed<?>> pred){
        return (map, can) -> {
            final Map.Builder mapBuilder = new Map.Builder();

            map.polygons().stream().filter(pred).forEach(mapBuilder::addPolygon);
            map.polyLines().stream().filter(pred).forEach(mapBuilder::addPolyLine);

            this.drawMap(mapBuilder.build(), can);
        };
    }
		
	/**
	 * Retourne un peintre dessinant la carte dessinée d'abord avec le second peintre
	 * reçue en arguement puis dessinée par dessus par celui auquel on applique la méthode
	 * 
	 * @param other
	 * 		peintre de la carte sur lequel on dessine avec le peintre qui applique
	 * 		la méthode
	 * @return
	 * 		peintre dessinant d'abord à l'aide du peintre reçu en argument puis celui
	 * 		auquel on applique la méthode
	 */
	default Painter above(Painter other){
		return (map, can) -> {
            if(other != null) {
                other.drawMap(map, can);
            }
            this.drawMap(map, can);
        };
	}


    /**
     * Retourne un peintre dessinant la carte dessinée d'abord avec le peintre qui
     * applique cette méthode puis dessinée par dessus par celui reçu en argument
     * 
     * @param other
     * 		peintre de la carte sous lequel on dessine avec peintre qui applique
     * 		la méthode
     * @return
     * 		peintre dessinant d'abord à l'aide du peintre qui applique la méthode
     *  	puis celui reçu en argument
     */
    default Painter under(Painter other){
        if(other != null){
            return other.above(this);
        }
        else{
            return this::drawMap;
        }
    }
		
	/**	
	 * Retourne un peintre dessinant d'abord toutes les entités de la couches -5
	 * puis incrémentalement jusqu'à +5.
	 * 
	 * @return
	 * 		peintre dessinant les entités de la couche la plus profonde à la couche
	 * 		la plus en surface
	 */
	default Painter layered(){
        Painter painterLayerI, painter = when(Filters.onLayer(-5));
        
		for(int i = -4; i <= 5; ++i){
            final Predicate<Attributed<?>> onLayerI = Filters.onLayer(i);
            painterLayerI = when(onLayerI);
            painter = painterLayerI.above(painter);
        }
        return painter;
	}
}