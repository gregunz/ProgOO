package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.util.Objects.requireNonNull;


/**
 * Représente un polygone à trous.
 * Cette classe est final et immuable
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */


public final class Polygon {

    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;

    /**
     * Construit un polygone avec l'enveloppe et les trous donnés
     *
     * @param shell
     *      L'enveloppe du polygone représentée par une polyligne fermée
     * @param holes
     *      Les trous contenus entièrement dans le polygone, représentés par des polylignes fermées
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes){
        this.shell = requireNonNull(shell, "L'enveloppe ne peut pas être un objet null");
        this.holes = holes.isEmpty() ?
                Collections.<ClosedPolyLine>emptyList() :
                Collections.unmodifiableList( new ArrayList<>(holes) );
    }

    /**
     * Construit un polygone sans trous
     *
     * @param shell
     *      L'enveloppe du polygone représentée par une polyligne fermée
     */
    public Polygon(ClosedPolyLine shell){
        this(shell, Collections.<ClosedPolyLine>emptyList() );
    }

    /**
     * @return
     *      La polyligne fermée représentant l'enveloppe du polygone
     */
    public ClosedPolyLine shell(){
        return shell;
    }

    /**
     * @return
     *      La liste des polylignes fermées représentant les trous du polygone
     */
    public List<ClosedPolyLine> holes(){
        return holes;
    }
}
