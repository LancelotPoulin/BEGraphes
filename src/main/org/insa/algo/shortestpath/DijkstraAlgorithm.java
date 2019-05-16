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
    
    protected Label LabelIdoine(int s, boolean m, double c, Arc p, ShortestPathData data) {
    	return new Label(s, m, c, p);
    }
    
    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        
        final int nbNodes = graph.size();
        int compteurIteration = 0; 
        int compteurArcSolution = 0;
        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];
        
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        
        
        ArrayList<Label> TabLab = new ArrayList<Label>(); 
        
        for (int i = 0; i<nbNodes; i++) {
        	TabLab.add(LabelIdoine(i, false, Double.POSITIVE_INFINITY, null,data));  
        }
        TabLab.get(data.getOrigin().getId()).setCost(0);
        tas.insert(TabLab.get(data.getOrigin().getId()));
        ShortestPathSolution solution = null;

        boolean nonMarque = true;
        while (nonMarque) {
        	compteurIteration++;
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
                    	double oldDist = TabLab.get(arc.getDestination().getId()).getTotalCost();
                    	double newDist = TabLab.get(node.getId()).getTotalCost() + data.getCost(arc);
                        if (Double.isInfinite(oldDist) && Double.isFinite(newDist)) {
                            notifyNodeReached(arc.getDestination());
                        }
                    	/*if (oldDist > newDist) {*/
                        if (TabLab.get(arc.getDestination().getId()).compareTo(TabLab.get(node.getId()))>0) {
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
        System.out.println("nb iteration: "+compteurIteration);

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
                compteurArcSolution ++;
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
        System.out.println("nb arc solution: "+compteurArcSolution);
        return solution;
    }

}
