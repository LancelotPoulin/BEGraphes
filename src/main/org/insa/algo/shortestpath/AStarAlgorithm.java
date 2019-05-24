package org.insa.algo.shortestpath;

import org.insa.graph.Arc;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        this.perfo = new stat();
    }
    
    /*redefinition of the method newLabel*/
    protected Label LabelIdoine(int s, boolean m, double c, Arc p, ShortestPathData data) {
    	return new LabelStar(s, m, c, p, data);
    }
}
