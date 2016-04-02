package br.edu.ifpi.ads.deacm.web.rest.dto;

import java.util.List;

import br.edu.ifpi.ads.deacm.domain.DMU;

public class SingleDMUDTO {

	private DMU target;

	private Double ks;

	private Double ke;

	private Double capital;

	private List<DMU> dmus;

	public DMU getTarget() {
		return target;
	}

	public void setTarget( DMU target ) {
		this.target = target;
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

}