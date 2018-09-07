package ch.epfl.imhof.osm;

import java.util.*;

/**
 * Représente une carte OpenStreetMap, c'est-à-dire un ensemble de chemins et de relations
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class OSMMap {

    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    /**
     * Construit une carte OSM avec les chemins et les relations donnés
     *
     * @param ways
     *      Une collection de chemins OSM
     * @param relations
     *      Une collection de relations OSM
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations){
        this.ways = Collections.unmodifiableList(new ArrayList<>(ways) );
        this.relations = Collections.unmodifiableList(new ArrayList<>(relations) );
    }

    /**
     * @return
     *      La liste des chemins de la carte
     */
    public List<OSMWay> ways(){
        return ways;
    }

    /**
     * @return
     *      La liste des relations de la carte
     */
    public List<OSMRelation> relations(){
        return relations;
    }

    /**
     * Sert de bâtisseur à la classe OSMMap.
     * Le bâtisseur peut stocker les noeuds, contrairement à la carte elle-même
     */
    public static final class Builder{

        private final Map<Long, OSMNode> nodes = new HashMap<>();
        private final Map<Long, OSMWay> ways = new HashMap<>();
        private final Map<Long, OSMRelation> relations = new HashMap<>();

        /**
         * Ajoute le nœud donné au bâtisseur
         *
         * @param newNode
         *      Le noeud OSMNode à ajouter au bâtisseur
         */
        public void addNode(OSMNode newNode){
            nodes.put(newNode.id(), newNode);
        }

        /**
         * Retourne le nœud dont l'identifiant unique est égal à celui donné
         *
         * @param id
         *      L'id unique du noeud
         * @return
         *      Le noeud dont l'id est donné ou
         *      null si ce nœud n'a pas été ajouté précédemment au bâtisseur
         */
        public OSMNode nodeForId(long id){
            return nodes.get(id);
        }

        /**
         * Ajoute le chemin donné au bâtisseur
         *
         * @param newWay
         *      Le chemin OSMWay à ajouter au bâtisseur
         */
        public void addWay(OSMWay newWay){
            ways.put(newWay.id(), newWay);
        }

        /**
         * Retourne le chemin dont l'identifiant unique est égal à celui donné
         *
         * @param id
         *      L'id unique du chemin
         * @return
         *      Le chemin dont l'id est donné ou
         *      null si ce nœud n'a pas été ajouté précédemment au bâtisseur
         */
        public OSMWay wayForId(long id){
            return ways.get(id);
        }

        /**
         * Ajoute la relation donnée au bâtisseur
         *
         * @param newRelation
         *      La relation OSMRelation à ajouter au bâtisseur
         */
        public void addRelation(OSMRelation newRelation){
            relations.put(newRelation.id(), newRelation);
        }

        /**
         * Retourne la relation dont l'identifiant unique est égal à celui donné
         *
         * @param id
         *      L'id unique de la relation
         * @return
         *      La relation dont l'id est donné ou
         *      null si cette relation n'a pas été ajoutée précédemment au bâtisseur
         */
        public OSMRelation relationForId(long id){
            return relations.get(id);
        }

        /**
         * Construit une carte OSM avec les chemins et les relations ajoutés jusqu'à présent
         *
         * @return
         *      Une carte OSMMap
         */
        public OSMMap build(){
            return new OSMMap(ways.values(), relations.values() );
        }
    }
}
