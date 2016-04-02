package br.edu.ifpi.ads.deacm.domain.lp.solutions;

import java.util.List;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;

public class KAOSolution implements Solution {

	private final Double[] u;

	private final Double[] v;

	private final List<DMU> dmus;

	private final Double kaoDistance;

	public KAOSolution( Double kaoDistance, Double[] u, Double[] v, List<DMU> dmus ) {
		super();
		this.u = u;
		this.v = v;
		this.dmus = dmus;
		this.kaoDistance = kaoDistance;
	}

	public Double[] getU() {
		return u;
	}

	public Double[] getV() {
		return v;
	}

	public List<DMU> getDmus() {
		return dmus;
	}

	public Double getKaoDistance() {
		return kaoDistance;
	}

}