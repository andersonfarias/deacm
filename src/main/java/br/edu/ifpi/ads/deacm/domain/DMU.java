package br.edu.ifpi.ads.deacm.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A DMU.
 */
@Entity
@Table( name = "dmu" )
@Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
@Document( indexName = "dmu" )
public class DMU implements Serializable {

	private static final long serialVersionUID = 4799252889937048945L;

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private Long id;

	@NotNull
	@Size( min = 1, max = 80 )
	@Column( name = "name", length = 80, nullable = false )
	private String name;

	// @Transient
	private Double relativeArea;

	// @Transient
	private Double relativeSize;

	// @Transient
	private Double newRelativeSize;

	// @Transient
	private boolean oversized;

	// @Transient
	@Min( value = 0 )
	@Max( value = 1 )
	private Double deaEfficiency;

	// @Transient
	private Double deaSuperEfficiency;

	// @Transient
	private Double l2Efficiency;

	// @Transient
	private Double nEfficiency;

	// @Transient
	private Double newL2Efficiency;

	// @Transient
	private boolean superEfficient;

	// @Transient
	private Double kaoVirtualInput;

	// @Transient
	private Double kaoNormalizedVirtualInput;

	// @Transient
	private Double kaoVirtualOutput;

	// @Transient
	private Double kaoNormalizedVirtualOutput;

	// @Transient
	private Double deaVirtualInput;

	// @Transient
	private Double deaVirtualOutput;

	// @Transient
	private Double newVirtualInput;

	// @Transient
	private Double newVirtualOutput;

	// @Transient
	private Double coefficientSize;

	// @Transient
	private Double coefficientEfficiency;

	// @Transient
	private Double capital;

	@Valid
	@Size( min = 1 )
	@OneToMany( cascade = { CascadeType.ALL } )
	@Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
	private List<Output> outputs = new ArrayList<>();

	@Valid
	@Size( min = 1 )
	@OneToMany( cascade = { CascadeType.ALL } )
	@Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
	private List<Input> inputs = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Double getRelativeArea() {
		return relativeArea;
	}

	public void setRelativeArea( Double relativeArea ) {
		this.relativeArea = relativeArea;
	}

	public Double getRelativeSize() {
		return relativeSize;
	}

	public void setRelativeSize( Double relativeSize ) {
		this.relativeSize = relativeSize;
	}

	public Double getDeaEfficiency() {
		return deaEfficiency;
	}

	public void setDeaEfficiency( Double deaEfficiency ) {
		this.deaEfficiency = deaEfficiency;
	}

	public Double getL2Efficiency() {
		return l2Efficiency;
	}

	public void setL2Efficiency( Double l2Efficiency ) {
		this.l2Efficiency = l2Efficiency;
	}

	public Double getKaoVirtualInput() {
		return kaoVirtualInput;
	}

	public void setKaoVirtualInput( Double kaoVirtualInput ) {
		this.kaoVirtualInput = kaoVirtualInput;
	}

	public Double getKaoVirtualOutput() {
		return kaoVirtualOutput;
	}

	public void setKaoVirtualOutput( Double kaoVirtualOutput ) {
		this.kaoVirtualOutput = kaoVirtualOutput;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public void setOutputs( List<Output> outputs ) {
		this.outputs = outputs;
	}

	public List<Input> getInputs() {
		return inputs;
	}

	public void setInputs( List<Input> inputs ) {
		this.inputs = inputs;
	}

	public Double getCoefficientSize() {
		return coefficientSize;
	}

	public void setCoefficientSize( Double coefficientSize ) {
		this.coefficientSize = coefficientSize;
	}

	public Double getDeaVirtualInput() {
		return deaVirtualInput;
	}

	public void setDeaVirtualInput( Double deaVirtualInput ) {
		this.deaVirtualInput = deaVirtualInput;
	}

	public Double getDeaVirtualOutput() {
		return deaVirtualOutput;
	}

	public void setDeaVirtualOutput( Double deaVirtualOutput ) {
		this.deaVirtualOutput = deaVirtualOutput;
	}

	public Double getNewRelativeSize() {
		return newRelativeSize;
	}

	public void setNewRelativeSize( Double newRelativeSize ) {
		this.newRelativeSize = newRelativeSize;
	}

	public Double getNewL2Efficiency() {
		return newL2Efficiency;
	}

	public void setNewL2Efficiency( Double newL2Efficiency ) {
		this.newL2Efficiency = newL2Efficiency;
	}

	public boolean isOversized() {
		return oversized;
	}

	public void setOversized( boolean oversized ) {
		this.oversized = oversized;
	}

	public boolean isSuperEfficient() {
		return superEfficient;
	}

	public void setSuperEfficient( boolean superEfficient ) {
		this.superEfficient = superEfficient;
	}

	public Double getCapital() {
		return capital;
	}

	public void setCapital( Double capital ) {
		this.capital = capital;
	}

	public Double getNewVirtualInput() {
		return newVirtualInput;
	}

	public void setNewVirtualInput( Double newVirtualInput ) {
		this.newVirtualInput = newVirtualInput;
	}

	public Double getNewVirtualOutput() {
		return newVirtualOutput;
	}

	public void setNewVirtualOutput( Double newVirtualOutput ) {
		this.newVirtualOutput = newVirtualOutput;
	}

	public Double getKaoNormalizedVirtualInput() {
		return kaoNormalizedVirtualInput;
	}

	public void setKaoNormalizedVirtualInput( Double kaoNormalizedVirtualInput ) {
		this.kaoNormalizedVirtualInput = kaoNormalizedVirtualInput;
	}

	public Double getKaoNormalizedVirtualOutput() {
		return kaoNormalizedVirtualOutput;
	}

	public void setKaoNormalizedVirtualOutput( Double kaoNormalizedVirtualOutput ) {
		this.kaoNormalizedVirtualOutput = kaoNormalizedVirtualOutput;
	}

	public Double getDeaSuperEfficiency() {
		return deaSuperEfficiency;
	}

	public void setDeaSuperEfficiency( Double deaSuperEfficiency ) {
		this.deaSuperEfficiency = deaSuperEfficiency;
	}

	public Double getCoefficientEfficiency() {
		return coefficientEfficiency;
	}

	public void setCoefficientEfficiency( Double coefficientEfficiency ) {
		this.coefficientEfficiency = coefficientEfficiency;
	}

	public Double getnEfficiency() {
		return nEfficiency;
	}

	public void setnEfficiency( Double nEfficiency ) {
		this.nEfficiency = nEfficiency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( inputs == null ) ? 0 : inputs.hashCode() );
		result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
		result = prime * result + ( ( outputs == null ) ? 0 : outputs.hashCode() );
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
		DMU other = (DMU) obj;
		if ( inputs == null ) {
			if ( other.inputs != null )
				return false;
		} else if ( !inputs.equals( other.inputs ) )
			return false;
		if ( name == null ) {
			if ( other.name != null )
				return false;
		} else if ( !name.equals( other.name ) )
			return false;
		if ( outputs == null ) {
			if ( other.outputs != null )
				return false;
		} else if ( !outputs.equals( other.outputs ) )
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DMU [name=" + name + ", outputs=" + Arrays.deepToString( outputs.toArray() ) + ", inputs=" + Arrays.deepToString( inputs.toArray() ) + "]";
	}

}