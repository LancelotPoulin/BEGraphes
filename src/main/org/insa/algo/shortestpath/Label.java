package org.insa.algo.shortestpath;

import org.insa.graph.Arc;


public class Label implements Comparable<Label> {
	private int sommetCourant;
	private boolean marque;
	private boolean visite;
	protected double cost;
	private Arc pere;

	
	public Label (int s, boolean m, double c, Arc p) {
		this.sommetCourant = s;
		this.marque = m;
		this.cost = c;
		this.pere = p;
	}	
	
	/*
	 * setter
	 */
	
	protected void setSommetCourant(int s) {
		this.sommetCourant = s;
	}
	protected void setMarque(boolean b) {
		this.marque = b;
	}
	protected void setCost(double c) {
		this.cost = c;
	}
	protected void setPere(Arc p) {
		this.pere = p;
	}
	protected void setVisite(boolean v) {
		this.visite = v;
	}
	
	/*
	 * getter
	 */
	protected double getCost() {
		return this.cost;
	}
	
	protected double getTotalCost() {
		return this.cost;
	}
	
	protected Arc getPere() {
		return this.pere;
	}

	protected boolean getMarque() {
		return this.marque;
	}	
	
	protected int getSommetCourant() {
		return this.sommetCourant;
	}
	
	protected boolean getVisite() {
		return this.visite;
	}
	
	public int compareTo(Label autrelabel) {
		return ((Double)(this.getTotalCost())).compareTo((Double)(autrelabel.getTotalCost()));
	}
}
