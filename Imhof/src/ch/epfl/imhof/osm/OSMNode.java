package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * Classe héritant de OSMEntity, représentant un noeud OSM
 * 
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class OSMNode extends OSMEntity {
	
	private final PointGeo position;

	/**
	 * Construit un noeud OSM avec l'identifiant, la position et les attributs donnés
	 *
	 * @param id
	 * 		identifiant du noeud
	 * @param position
	 * 		position du noeud
	 * @param attributes
	 * 		attributs du noeud
	 */
	public OSMNode(long id, PointGeo position, Attributes attributes){
		super(id, attributes);
		this.position = position;
	}
	
	/**
	 * Retourne la position du noeud
	 *
	 * @return
	 * 		position du noeud
	 */
	public PointGeo position(){
		return position;
	}
	
    /**
	 *	Bâtisseur permettant la création d'un noeud OSM en plusieurs étapes
     */
    public static final class Builder extends OSMEntity.Builder {

    	private final PointGeo position;
    	
        /**
         * Construit un bâtisseur pour un noeud ayant l'identifiant et la position donnés
		 *
         * @param id
         * 		identifiant du noeud
         * @param position
         * 		position du noeud
         */
        public Builder(long id, PointGeo position){
            super(id);
            this.position = position;
        }
        
        /**
         * Construit le noeud OSM avec l'identifiant et la position et les éventuels attributs ajoutés
         *
         * @return
         *		Le noeud créé
		 *
		 * @throws java.lang.IllegalStateException
		 * 		Si la construction est incomplète
         */
        public OSMNode build() {
        	if(isIncomplete()){
        		throw new IllegalStateException();
        	}
        	return new OSMNode(id, position, attributesBuilder.build());
        }
    }
	
}
