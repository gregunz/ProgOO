/**
 * 
 */
package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;


/**
 * Classe héritant de OSMEntity représentant un chemin OSM
 * 
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public final class OSMWay extends OSMEntity {
	
	private final List<OSMNode> nodes;
	
	/**
	 * Construit un chemin étant donnés son identifiant unique, ses noeuds et ses attributs
     *
	 * @param id
	 * 		identifiant unique du chemin
	 * @param nodes
	 * 		noeuds du chemin
	 * @param attributs
	 * 		attributs du chemin
     *
     * @throws java.lang.IllegalArgumentException
     *      S'il n'y pas au minimum 2 noeuds
	 */
	public OSMWay(long id, List<OSMNode> nodes, Attributes attributs){
		super(id, attributs);
		if(nodes.size() < 2){
			throw new IllegalArgumentException();
		}
		this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
	}
	
	/**
	 * @return
	 * 		Le nombre de noeuds du chemin
	 */
	public int nodesCount(){
		return nodes.size();
	}
	
	/**
	 * @return
	 * 		La liste des noeuds du chemin
	 */
	public List<OSMNode> nodes(){
		return nodes;
	}
	
	/**
	 * @return
	 * 		Le premier noeud du chemin
	 */
	public OSMNode firstNode(){
		return nodes.get(0);
	}
	
	/**
	 * @return
	 * 		Le dernier noeud du chemin
	 */
	public OSMNode lastNode(){
		return nodes.get(nodes.size()-1);
	}
	
	/**
	 * Retourne vrai si le chemin est fermé
     *
	 * @return
	 * 		<p>true si le chemin est fermé</p>
     *      <p>false si le chemin est ouvert</p>
	 */
	public boolean isClosed(){
		return firstNode().equals(lastNode());
	}

    /**
     * Retourne la liste des noeuds non-répétés, c'est à dire en excluant le dernier (identique au premier) si le
     * chemin est fermé
     *
     * @return
     * 		<p>L'ensemble des noeuds du chemin, dernier exclu, si le chemin est fermé</p>
     * 	    <p>L'ensemble des noeuds du chemin autrement</p>
     */
    public List<OSMNode> nonRepeatingNodes(){
        if(isClosed()){
            return nodes.subList(0, nodes.size()-1);
        }
        else{
            return nodes;
        }
    }
	
    /**
	 * Bâtisseur permettant la création d'un chemin OSM en plusieurs étapes
     */
    public static final class Builder extends OSMEntity.Builder {

    	private final List<OSMNode> nodes;
    	
        /**
         * Initialise un bâtisseur pour un chemin ayant l'identifiant donné
         *
         * @param id
         * 		L'identifiant du futur chemin
         */
        public Builder(long id){
            super(id);
            nodes = new ArrayList<>();
        }
        
        /**
         * Ajoute un noeud à la fin des noeuds du chemin en construction
         *
         * @param newNode
         * 		Nouveau noeud à ajouter au chemin
         */
        public void addNode(OSMNode newNode){
        	nodes.add(newNode);
        }
        
        /**
         * {@inheritDoc}
         * Retourne également vrai si le nombre de noeuds du chemin est inférieur à 2
         *
         * @return
         * 		vrai si chemin incomplet, faux sinon
         */
        @Override
        public boolean isIncomplete(){
        	return super.isIncomplete() || nodes.size()<2;
        }
        
        /**
         * Construit et retourne le chemin ayant les noeuds et les attributs ajoutés jusqu'à présent
         *
         * @return
         * 		Le chemin créé
         *
         * @throws java.lang.IllegalStateException
         *      Si la construction est incomplète
         */
        public OSMWay build(){
        	if(isIncomplete()){
        		throw new IllegalStateException();
        	}
        	return new OSMWay(id, nodes, attributesBuilder.build());
        }
    }

}
