package br.edu.ifpi.ads.deacm.web.rest.dto;

import java.util.List;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.ModelMode;

public class SetDMUDTO {

	private Double td;

	private ModelMode mode;

	private Double capital;

	private List<DMU> dmus;

	private List<DMU> targets;

	private Double kaoDistance;

	private Boolean executeWithSuperEfficiency;

	public Double getTd() {
		return td;
	}

	public void setTd( Double td ) {
		this.td = td;
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

	public Double getCapital() {
		return capital;
	}

	public void setCapital( Double capital ) {
		this.capital = capital;
	}

	public Boolean getExecuteWithSuperEfficiency() {
		return executeWithSuperEfficiency;
	}

	public void setExecuteWithSuperEfficiency( Boolean executeWithSuperEfficiency ) {
		this.executeWithSuperEfficiency = executeWithSuperEfficiency;
	}

	public ModelMode getMode() {
		return mode;
	}

	public void setMode( ModelMode mode ) {
		this.mode = mode;
	}

}