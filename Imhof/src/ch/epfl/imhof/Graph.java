package ch.epfl.imhof;

import java.util.Map;
import java.util.*;

/**
 * Classe générique représentant un graphe non orienté
 *
 * @param <N>
 *     Le type des noeuds du graphe
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Graph<N>{

    private final Map<N, Set<N>> neighbors;

    /**
     * Instancie un graphe non orienté avec la table d'adjacence donnée
     *
     * @param neighbors
     *      La table d'adjacence des noeuds et de leurs voisins
     */
    public Graph(Map<N, Set<N>> neighbors){
        this.neighbors = makeImmutable(neighbors);
    }

    private Map<N, Set<N>> makeImmutable(Map<N, Set<N>> map){
        final Map<N, Set<N>> neighborsTemp = new HashMap<>();

        for(Map.Entry<N, Set<N>> e : map.entrySet() ){
            neighborsTemp.put(e.getKey(), Collections.unmodifiableSet(new HashSet<>(e.getValue())));
        }

        return Collections.unmodifiableMap(neighborsTemp);
    }

    /**
     * Le set des noeuds du graphe, correspondant aux clés de la table d'adjacence donnée au constructeur
     *
     * @return
     *      Les nœuds du graphe
     */
    public Set<N> nodes(){
        return neighbors.keySet();
    }

    /**
     * Le set des voisins du noeud donnée
     *
     * @param node
     *      Le noeud duquel on désire obtenir les voisins
     *
     * @return
     *      L'ensemble des nœuds voisins du nœud donné
     *
     * @throws java.lang.IllegalArgumentException
     *      Si le noeud fourni ne fait pas partie du graphe
     */
    public Set<N> neighborsOf(N node) {
        Set<N> neighbors = this.neighbors.get(node);

        if(neighbors == null){
            throw new IllegalArgumentException("Le noeud donné ne fait pas partie du graphe.");
        }
        return neighbors;
    }

    /**
     * Bâtisseur de la classe Graph, permettant de construire un Graph en plusieurs étapes
     *
     * @param <N>
     *     Le type des noeuds du graphe
     */
    public static final class Builder<N> {

        private final Map<N, Set<N>> neighbors = new HashMap<>();

        /**
         * Ajoute le nœud donné au graphe en cours de construction, s'il n'en faisait pas déjà partie
         *
         * @param n
         *      Le noeud à ajouter au graphe
         */
        public void addNode(N n){
            neighbors.putIfAbsent(n, new HashSet<>());
        }

        /**
         * Ajoute une arrête entre les deux nœuds donnés au graphe en cours de construction
         *
         * @param n1
         *      Le premier noeud de l'arrête
         * @param n2
         *      Le deuxième noeud de l'arrête
         */
        public void addEdge(N n1, N n2) {
            if(neighbors.get(n1) == null){
                throw new IllegalArgumentException("Le noeud n1 n'appartient pas au graphe en cours de construction");
            }
            else if(neighbors.get(n2) == null){
                throw new IllegalArgumentException("Le noeud n2 n'appartient pas au graphe en cours de construction");
            }

            neighbors.get(n1).add(n2);
            neighbors.get(n2).add(n1);
        }

        /**
         * Construit le graphe composé des nœuds et arrêtes ajoutés jusqu'à présent au bâtisseur
         *
         * @return
         *      Un graphe orienté de type Graph
         */
        public Graph<N> build() {
            return new Graph<>(this.neighbors);
        }
    }
}
