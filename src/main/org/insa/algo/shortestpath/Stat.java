package org.insa.algo.shortestpath;

public class Stat {
	protected float lengthSolution;
	protected double timeSolution;
	protected long tps_calc;
	protected int nbSommetVisites;
	protected int nbSommetMarques;
	protected int TailleTasMax;
	
	public Stat() {
		this.nbSommetVisites = 0;
        this.nbSommetMarques = 0;
        this.TailleTasMax = 0; 
        this.tps_calc = 0;
        this.lengthSolution = 0;
        this.timeSolution = 0;
	}
	
	public void setLengthSolution(float sol) {
		this.lengthSolution= sol;
	}
	public void setTimeSolution(double sol) {
		this.timeSolution= sol;
	}
	public void startChrono(long tps) {
		this.tps_calc = tps;
	}
	public void endChrono(long tps) {
		this.tps_calc = tps-tps_calc;
	}
	public void IncrNbSommetVisites() {
		this.nbSommetVisites ++;
	}
	public void IncrNbSommetMarques() {
		this.nbSommetMarques ++;
	}
	public void setTailleTas(int taille) {
		if (this.TailleTasMax < taille) 
			this.TailleTasMax = taille;
	}
	
	/*getter*/
	public float getLengthSolution() {
		return this.lengthSolution;
	}
	public double getTimeSolution() {
		return this.timeSolution;
	}
	public long getCPUTime() {
		return this.tps_calc;
	}
	public int getNbSommetVisites() {
		return this.nbSommetVisites;
	}
	public int getNbSommetMarques() {
		return this.nbSommetMarques;
	}
	public int getTailleTasMax() {
		return this.TailleTasMax;
	}
	

}
