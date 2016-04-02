package br.edu.ifpi.ads.deacm.domain.lp.solutions;

import java.util.List;

import org.apache.commons.math3.optim.PointValuePair;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;

public final class DEASolution implements Solution {

	private final DMU dmu0;

	private final List<DMU> dmus;

	private final PointValuePair solution;

	public DEASolution( DMU dmu0, List<DMU> dmus, PointValuePair solution ) {
		super();
		this.dmu0 = dmu0;
		this.dmus = dmus;
		this.solution = solution;
	}

	public DMU getDmu0() {
		return dmu0;
	}

	public List<DMU> getDmus() {
		return dmus;
	}

	public PointValuePair getSolution() {
		return solution;
	}

}