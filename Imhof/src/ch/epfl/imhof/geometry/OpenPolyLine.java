package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Représente une polyligne ouverte
 * 
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */

public final class OpenPolyLine extends PolyLine {

    /**
     * Construis une polyligne ouverte à partir des sommets donnés
     * 
     * @param points
     *          Une liste de sommets formant une polyligne
     *          
     * @throws IllegalArgumentException
     *          Si la liste des sommets est vide
     */
    public OpenPolyLine(List<Point> points){
        super(points);
    }

    /**
     * Indique si la polyligne est fermée, ce qui n'est pas le cas
     * 
     * @return
     *      false
     */
    @Override
    public boolean isClosed() {
        return false;
    }

}
