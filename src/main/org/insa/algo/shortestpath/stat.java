package org.insa.algo.shortestpath;

public class stat {
	protected String valSolution;
	protected long tps_calc;
	protected int nbSommetVisites;
	protected int nbSommetMarques;
	protected int TailleTasMax;
	
	public stat() {
		this.nbSommetVisites = 0;
        this.nbSommetMarques = 0;
        this.TailleTasMax = 0; 
	}
	
	public void setValSolution(ShortestPathSolution sol) {
		this.valSolution=sol.toString();
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
	public String getValSolution() {
		return this.valSolution;
	}
	public long getCPUTime() {
		return this.tps_calc;
	}
	public long getNbSommetVisites() {
		return this.nbSommetVisites;
	}
	public long getNbSommetMarques() {
		return this.nbSommetMarques;
	}
	public int getTailleTasMax() {
		return this.TailleTasMax;
	}
	

}
