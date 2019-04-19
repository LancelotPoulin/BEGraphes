package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        
        final int nbNodes = graph.size();
        
        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];
        
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        
        
        ArrayList<Label> TabLab = new ArrayList<Label>(); 
        
        for (int i = 0; i<nbNodes; i++) {
        	TabLab.add(new Label(i, false, Double.POSITIVE_INFINITY, null));  
        }
        TabLab.get(data.getOrigin().getId()).setCost(0);
        tas.insert(TabLab.get(data.getOrigin().getId()));
        ShortestPathSolution solution = null;

        boolean nonMarque = true;
        while (nonMarque) {

    		Node node = graph.get(tas.findMin().getSommetCourant());
    		if (node == data.getDestination()) {
    			nonMarque = false;
    		}
    		else {
    			TabLab.get(node.getId()).setMarque(true);
            	for (Arc arc: node.getSuccessors()) {
            		
                    // Small test to check allowed roads...
                    if (!data.isAllowed(arc)) {
                        continue;
                    }
                    
                    if (TabLab.get(arc.getDestination().getId()).getMarque()==false) {
                    	double oldDist = TabLab.get(arc.getDestination().getId()).getCost();
                    	double newDist = TabLab.get(node.getId()).getCost() + data.getCost(arc);
                        if (Double.isInfinite(oldDist) && Double.isFinite(newDist)) {
                            notifyNodeReached(arc.getDestination());
                        }
                    	if (oldDist > newDist) {
                    		TabLab.get(arc.getDestination().getId()).setCost(newDist); 
                			
                    		predecessorArcs[arc.getDestination().getId()] = arc;
                			
                    		if (TabLab.get(arc.getDestination().getId()).getVisite()) {
                    			tas.remove(TabLab.get(arc.getDestination().getId())); 
                    			tas.insert(TabLab.get(arc.getDestination().getId()));
                    		} else {
                    			tas.insert(TabLab.get(arc.getDestination().getId()));
                    			TabLab.get(arc.getDestination().getId()).setVisite(true);
                    		}
                    		
                    	}
                    }
        		}
        	}
    		tas.deleteMin();
    	}
        
        
        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
        
        return solution;
    }

}
