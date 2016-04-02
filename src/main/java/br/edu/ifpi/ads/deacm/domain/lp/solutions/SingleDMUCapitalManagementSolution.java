package br.edu.ifpi.ads.deacm.domain.lp.solutions;

import java.util.List;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;

public final class SingleDMUCapitalManagementSolution implements Solution {

	private DMU dmu0;

	private Double ks;

	private Double ke;

	private Double minCapital;

	private List<DMU> dmus;

	private List<Double> inputsOffset;

	private List<Double> outputsOffset;

	public SingleDMUCapitalManagementSolution( DMU dmu0, Double minCapital, Double ks, Double ke, List<Double> inputsOffset, List<Double> outputsOffset ) {
		super();
		this.dmu0 = dmu0;
		this.ks = ks;
		this.ke = ke;
		this.minCapital = minCapital;
		this.inputsOffset = inputsOffset;
		this.outputsOffset = outputsOffset;
	}

	public DMU getDmu0() {
		return dmu0;
	}

	public void setDmu0( DMU dmu0 ) {
		this.dmu0 = dmu0;
	}

	public Double getMinCapital() {
		return minCapital;
	}

	public void setMinCapital( Double minCapital ) {
		this.minCapital = minCapital;
	}

	public Double getKs() {
		return ks;
	}

	public void setKs( Double ks ) {
		this.ks = ks;
	}

	public Double getKe() {
		return ke;
	}

	public void setKe( Double ke ) {
		this.ke = ke;
	}

	public List<DMU> getDmus() {
		return dmus;
	}

	public void setDmus( List<DMU> dmus ) {
		this.dmus = dmus;
	}

	public List<Double> getInputsOffset() {
		return inputsOffset;
	}

	public void setInputsOffset( List<Double> inputsOffset ) {
		this.inputsOffset = inputsOffset;
	}

	public List<Double> getOutputsOffset() {
		return outputsOffset;
	}

	public void setOutputsOffset( List<Double> outputsOffset ) {
		this.outputsOffset = outputsOffset;
	}

}