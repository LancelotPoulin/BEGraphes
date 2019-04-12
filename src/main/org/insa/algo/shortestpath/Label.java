package org.insa.algo.shortestpath;

import org.insa.graph.Arc;
import org.insa.graph.Node;

public class Label {
	private Node sommetCourant;
	private boolean marque;
	private double cost;
	private Arc pere;
	
	protected double getCost() {
		return this.cost;
	}
	
	public Label (Node s, boolean m, double c, Arc p) {
		this.sommetCourant = s;
		this.marque = m;
		this.cost = c;
		this.pere = p;
	}
}
