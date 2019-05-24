package org.insa.algo.shortestpath;

public class stat {
	protected String valSolution;
	protected static long tps_calc;
	protected static int nbSommetVisites;
	protected static int nbSommetMarques;
	protected static int TailleTasMax;
	
	public stat() {
        nbSommetVisites = 0;
        nbSommetMarques = 0;
        TailleTasMax = 0; 
	}
	
	public void setValSolution(ShortestPathSolution sol) {
		this.valSolution=sol.toString();
	}
	public void startChrono(long tps) {
		tps_calc = tps;
	}
	public void endChrono(long tps) {
		tps_calc = tps-tps_calc;
	}
	public void IncrNbSommetVisites() {
		nbSommetVisites ++;
	}
	public void IncrNbSommetMarques() {
		nbSommetMarques ++;
		System.out.println(nbSommetMarques);
	}
	public void setTailleTas(int taille) {
		if (TailleTasMax < taille) 
			TailleTasMax = taille;
	}
	
	/*getter*/
	public String getValSolution() {
		return this.valSolution;
	}
	public long getCPUTime() {
		return tps_calc;
	}
	public long getNbSommetVisites() {
		return tps_calc;
	}
	public long getNbSommetMarques() {
		return tps_calc;
	}
	public int getTailleTasMax() {
		return TailleTasMax;
	}

}
