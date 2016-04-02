package br.edu.ifpi.ads.deacm.web.rest.dto;

import java.util.List;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.KAOSolution;

public class UploadFileDTO {

	private String modelCode;

	private KAOSolution solution;

	private Double td;

	private Double ks;

	private Double ke;

	private Double capital;

	private List<DMU> targets;

	public List<DMU> getTargets() {
		return targets;
	}

	public void setTargets( List<DMU> targets ) {
		this.targets = targets;
	}

	public KAOSolution getSolution() {
		return solution;
	}

	public void setSolution( KAOSolution solution ) {
		this.solution = solution;
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

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode( String modelCode ) {
		this.modelCode = modelCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( capital == null ) ? 0 : capital.hashCode() );
		result = prime * result + ( ( ke == null ) ? 0 : ke.hashCode() );
		result = prime * result + ( ( ks == null ) ? 0 : ks.hashCode() );
		result = prime * result + ( ( modelCode == null ) ? 0 : modelCode.hashCode() );
		result = prime * result + ( ( solution == null ) ? 0 : solution.hashCode() );
		result = prime * result + ( ( td == null ) ? 0 : td.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		UploadFileDTO other = (UploadFileDTO) obj;
		if ( capital == null ) {
			if ( other.capital != null )
				return false;
		} else if ( !capital.equals( other.capital ) )
			return false;
		if ( ke == null ) {
			if ( other.ke != null )
				return false;
		} else if ( !ke.equals( other.ke ) )
			return false;
		if ( ks == null ) {
			if ( other.ks != null )
				return false;
		} else if ( !ks.equals( other.ks ) )
			return false;
		if ( modelCode == null ) {
			if ( other.modelCode != null )
				return false;
		} else if ( !modelCode.equals( other.modelCode ) )
			return false;
		if ( solution == null ) {
			if ( other.solution != null )
				return false;
		} else if ( !solution.equals( other.solution ) )
			return false;
		if ( td == null ) {
			if ( other.td != null )
				return false;
		} else if ( !td.equals( other.td ) )
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UploadFileDTO [modelCode=" + modelCode + ", solution=" + solution + ", td=" + td + ", ks=" + ks + ", ke=" + ke + ", capital=" + capital + "]";
	}

}