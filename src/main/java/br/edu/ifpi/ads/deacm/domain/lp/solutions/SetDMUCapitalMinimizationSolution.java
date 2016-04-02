package br.edu.ifpi.ads.deacm.domain.lp.solutions;

import java.util.List;

import com.ampl.DataFrame;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.ModelMode;
import br.edu.ifpi.ads.deacm.domain.lp.Solution;

public final class SetDMUCapitalMinimizationSolution implements Solution {

	private Double td;

	private Double capital;

	private Double kaoDistance;

	private ModelMode mode;

	private List<DMU> dmus;

	private List<DMU> targets;

	private DataFrame inputsOffsets;

	private DataFrame outputsOffsets;

	private boolean executeWithSuperEfficiency;

	public SetDMUCapitalMinimizationSolution( Double td, Double capital, Double kaoDistance, List<DMU> dmus, List<DMU> targets, DataFrame inputsOffsets, DataFrame outputsOffsets, boolean executeWithSuperEfficiency, ModelMode mode ) {
		super();
		this.td = td;
		this.capital = capital;
		this.dmus = dmus;
		this.mode = mode;
		this.targets = targets;
		this.kaoDistance = kaoDistance;
		this.inputsOffsets = inputsOffsets;
		this.outputsOffsets = outputsOffsets;
		this.executeWithSuperEfficiency = executeWithSuperEfficiency;
	}

	public Double getTd() {
		return td;
	}

	public void setTd( Double td ) {
		this.td = td;
	}

	public Double getCapital() {
		return capital;
	}

	public void setCapital( Double capital ) {
		this.capital = capital;
	}

	public List<DMU> getDmus() {
		return dmus;
	}

	public void setDmus( List<DMU> dmus ) {
		this.dmus = dmus;
	}

	public List<DMU> getTargets() {
		return targets;
	}

	public void setTargets( List<DMU> targets ) {
		this.targets = targets;
	}

	public Double getKaoDistance() {
		return kaoDistance;
	}

	public void setKaoDistance( Double kaoDistance ) {
		this.kaoDistance = kaoDistance;
	}

	public DataFrame getInputsOffsets() {
		return inputsOffsets;
	}

	public void setInputsOffsets( DataFrame inputsOffsets ) {
		this.inputsOffsets = inputsOffsets;
	}

	public DataFrame getOutputsOffsets() {
		return outputsOffsets;
	}

	public void setOutputsOffsets( DataFrame outputsOffsets ) {
		this.outputsOffsets = outputsOffsets;
	}

	public boolean isExecuteWithSuperEfficiency() {
		return executeWithSuperEfficiency;
	}

	public void setExecuteWithSuperEfficiency( boolean executeWithSuperEfficiency ) {
		this.executeWithSuperEfficiency = executeWithSuperEfficiency;
	}

	public ModelMode getMode() {
		return mode;
	}

	public void setMode( ModelMode mode ) {
		this.mode = mode;
	}

}