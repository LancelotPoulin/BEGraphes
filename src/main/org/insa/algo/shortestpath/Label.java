package org.insa.algo.shortestpath;

import org.insa.graph.Arc;
import org.insa.graph.Node;

public class Label implements Comparable<Label> {
	private int sommetCourant;
	private boolean marque;
	private boolean visite;
	private double cost;
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
		
		/*int cmp = 0;
		
		if (this.cost<autrelabel.getCost()) {
			cmp = -1;
		} else if (this.cost>autrelabel.getCost()){
			cmp = 1;
		} else if (this.cost==autrelabel.getCost()) {
			cmp = 0;
		}*/
		return ((Double)(this.cost)).compareTo((Double)(autrelabel.getCost()));
	}
}
