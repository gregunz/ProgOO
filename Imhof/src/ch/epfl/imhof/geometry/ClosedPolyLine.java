package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Représente une polyligne fermée
 * 
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */

public final class ClosedPolyLine extends PolyLine {

    /**
     * Construis une polyligne fermée à partir des sommets donnés
     * 
     * @param points
     *          Une liste de sommets formant une polyligne
     *          
     * @throws IllegalArgumentException
     *          Si la liste des sommets est vide
     */
    public ClosedPolyLine(List<Point> points){
        super(points);
    }

    /**
     * Indique si la polyligne est fermée, ce qui est le cas
     * 
     * @return
     *      true
     */
    @Override
    public boolean isClosed() {
        return true;
    }
    
    /**
     * L'aire de la polyligne fermée
     * 
     * @return
     *      L'aire (positive) de la polyligne
     */
    public double area(){
        double area = 0.;
        final int size = this.points().size();
        
        for(int i = 0; i < size; ++i){
            area += this.points().get(i).x() *
                    ( this.points().get( (i+1) % size ).y() - this.points().get( Math.floorMod(i-1, size) ).y() );
        }
        
        return Math.abs(area)/2.;
    }

    /**
     * Indique si un point est contenu dans la polyligne fermée
     * 
     * @param p
     *      Un point quelconque
     *
     * @return
     *      <p>true si le point est à l'intérieur de la polyligne fermée</p>
     *      <p>false autrement</p>
     */
    public boolean containsPoint(Point p){
		int index=0;
		final int size = this.points().size();
		Point p1, p2;
		
	    for(int i = 0; i < size; ++i){
	    	p1 = this.points().get( i );
	    	p2 = this.points().get( (i+1) % size);
	    	
	    	if(p1.y() <= p.y()){
	    		if(p2.y() > p.y() && this.isLeft(p,p1,p2)){
	    			++index;
	    		}
	    	}
            else{
	    		if(p2.y() <= p.y() && this.isLeft(p,p2,p1)){
	    			--index;
	    		}
	    	}
	    }

	    return (index != 0);
    }
    
    /**
     * Indique si le point est à gauche d'une droite ou non
     * 
     * @param p
     * 		Un point quelconque
     * @param p1
     * 		Premier des 2 points formant la droite
     * @param p2
     * 		Deuxième point formant la droite
     *
     * @return
     * 		true si le point est effectivement à gauche de la droite et false sinon
     */
    private boolean isLeft(Point p, Point p1, Point p2){
    	return (p1.x() - p.x() )*(p2.y() - p.y() ) > (p2.x() - p.x() )*(p1.y() - p.y() );
    }
}