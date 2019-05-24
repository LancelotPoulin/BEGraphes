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
    protected ShortestPathSolution doRun()
    {
    	ShortestPathSolution solution;
        ShortestPathData data = getInputData();
        
        if (data.getGraph() == null || data.getDestination() == null || data.getOrigin() == null) // missing arguments
        {
        	solution = new ShortestPathSolution(data, Status.UNKNOWN);
        }
        else if (data.getDestination() == data.getOrigin()) // single node path, valid and feasible
        {
        	solution = new ShortestPathSolution(data, Status.FEASIBLE, new Path(data.getGraph(), data.getOrigin()));
        }
        else
        {
        	
        	Graph graph = data.getGraph();
            final int nbNodes = graph.size();
            
            
            // Initialize array of predecessors.
            Arc[] predecessorArcs = new Arc[nbNodes];
            BinaryHeap<Label> tas = new BinaryHeap<Label>();
            ArrayList<Label> TabLab = new ArrayList<Label>(); 
            
            for (int i = 0; i<nbNodes; i++)
            {
            	TabLab.add(LabelIdoine(i, false, Double.POSITIVE_INFINITY, null, data));  
            }
            
            TabLab.get(data.getOrigin().getId()).setCost(0);
            tas.insert(TabLab.get(data.getOrigin().getId()));

            boolean fin = false;
            this.perfo.startChrono(System.currentTimeMillis());
            while (!fin)
            {
            	if (tas.isEmpty()) // Empty -> infeasible -> Origin can't reach Destination
            	{
            		fin = true;
            	}
            	else
            	{
            		Node node = graph.get(tas.findMin().getSommetCourant());
            		if (TabLab.get(data.getDestination().getId()).getMarque() || node == data.getDestination()) // Si on est a destination
            		{
            			fin = true;
            		}
            		else
            		{
            			TabLab.get(node.getId()).setMarque(true);
            			this.perfo.IncrNbSommetMarques();
                    	for (Arc arc: node.getSuccessors())
                    	{
                            // Small test to check allowed roads...
                            if (!data.isAllowed(arc))
                            {
                                continue;
                            }
                            if (TabLab.get(arc.getDestination().getId()).getMarque() == false)
                            {
                            	double oldTotalDist = TabLab.get(arc.getDestination().getId()).getTotalCost();
                            	double oldDist      = TabLab.get(arc.getDestination().getId()).getCost();
                            	double newTotalDist = TabLab.get(node.getId()).getCost() + data.getCost(arc);
                                if (Double.isInfinite(oldTotalDist) && Double.isFinite(newTotalDist))
                                {
                                    notifyNodeReached(arc.getDestination());
                                }
                            	if ((Double.isInfinite(oldDist)) /*premier noeud (oldTotalDist-oldDist) non defini*/
                            			|| (oldTotalDist > (newTotalDist+(oldTotalDist-oldDist))))                          		
                            	{
                            		TabLab.get(arc.getDestination().getId()).setCost(newTotalDist); 
                            		predecessorArcs[arc.getDestination().getId()] = arc;
                            		this.perfo.setTailleTas(TabLab.size());
                            		if (TabLab.get(arc.getDestination().getId()).getVisite())
                            		{
                            			tas.remove(TabLab.get(arc.getDestination().getId())); 
                            			tas.insert(TabLab.get(arc.getDestination().getId()));
                            		}
                            		else
                            		{
                            			tas.insert(TabLab.get(arc.getDestination().getId()));
                            			TabLab.get(arc.getDestination().getId()).setVisite(true);
                            			this.perfo.IncrNbSommetVisites();
                            		}
                            	}
                            }
                		}
                	}
            		tas.deleteMin();
            	}
        	}
            this.perfo.endChrono(System.currentTimeMillis());
            // Destination has no predecessor, the solution is infeasible... no path
            if (predecessorArcs[data.getDestination().getId()] == null)
            {
                solution = new ShortestPathSolution(data, Status.INFEASIBLE);
            }
            else
            {
                // The destination has been found, notify the observers.
                notifyDestinationReached(data.getDestination());

                // Create the path from the array of predecessors...
                ArrayList<Arc> arcs = new ArrayList<>();
                Arc arc = predecessorArcs[data.getDestination().getId()];
                while (arc != null)
                {
                    arcs.add(arc);
                    arc = predecessorArcs[arc.getOrigin().getId()];
                }

                // Reverse the path...
                Collections.reverse(arcs);

                // Create the final solution.
                solution = new ShortestPathSolution(data, Status.FEASIBLE, new Path(graph, arcs));
                this.perfo.setLengthSolution(solution.getPath().getLength());
                this.perfo.setTimeSolution(solution.getPath().getMinimumTravelTime());
            }
        	/*System.out.println("val_solution    "+this.perfo.getValSolution());
        	System.out.println("temps CPU       "+this.perfo.getCPUTime());
        	System.out.println("nbSommetVisites "+this.perfo.getNbSommetVisites());
        	System.out.println("nbSommetMarques "+this.perfo.getNbSommetMarques());
        	System.out.println("taille maxi tas "+this.perfo.getTailleTasMax());*/
        }
        
        return solution;
    }

}
