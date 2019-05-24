package org.insa.algo.shortestpath;

import org.insa.algo.AbstractInputData;
import org.insa.graph.Arc;

public class LabelStar extends Label implements Comparable<Label> {
	private double dist_oiseau;
	
	public LabelStar (int s, boolean m, double c, Arc p, ShortestPathData data) {
		super(s,m,c,p);
		/*calcul distance oiseau*/
		if(data.getMode() == AbstractInputData.Mode.LENGTH) {			
			/*si c'est en mode distance, alors on calcule une distance:*/
			this.dist_oiseau = (double)data.getGraph().get(s).getPoint().distanceTo(data.getDestination().getPoint()); 
		} else {
			/*si c'est en mode temps, alors on calcule : delta_t=d/vitesse*/
			double vitesse = Math.max(data.getMaximumSpeed(),data.getGraph().getGraphInformation().getMaximumSpeed());
			this.dist_oiseau = (double)data.getGraph().get(s).getPoint().distanceTo(data.getDestination().getPoint())/(vitesse*1000/3600);
		}
	}	
	/*redefinition de getTotalCost*/
	protected double getTotalCost() {
		return this.cost+this.dist_oiseau;
	}
	
}