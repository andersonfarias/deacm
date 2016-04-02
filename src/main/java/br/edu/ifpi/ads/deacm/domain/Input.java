package br.edu.ifpi.ads.deacm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A Input.
 */
@Entity
@Table( name = "input" )
@Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
@Document( indexName = "input" )
public class Input implements Serializable {

	private static final long serialVersionUID = 6616952481920050767L;

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private Long id;

	@NotNull
	@Size( min = 1, max = 80 )
	@Column( name = "name", length = 80, nullable = false )
	private String name;

	@NotNull
	@Column( name = "value", nullable = false )
	private Double value;

	// @Transient
	private Double newValue;

	// @Transient
	private Double offset;

	// @Transient
	private Double deaWeight;

	// @Transient
	private Double kaoNormalizedWeight;

	// @Transient
	private Double kaoWeight;

	// @Transient
	private Double newWeight;

	// @Transient
	private Double cost;

	// @Transient
	private Double bound;

	public Input() {}

	public Input( String name, Double value ) {
		super();
		this.name = name;
		this.value = value;
	}

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

	public Double getValue() {
		return value;
	}

	public void setValue( Double value ) {
		this.value = value;
	}

	public Double getKaoNormalizedWeight() {
		return kaoNormalizedWeight;
	}

	public void setKaoNormalizedWeight( Double kaoNormalizedWeight ) {
		this.kaoNormalizedWeight = kaoNormalizedWeight;
	}

	public Double getKaoWeight() {
		return kaoWeight;
	}

	public void setKaoWeight( Double kaoWeight ) {
		this.kaoWeight = kaoWeight;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost( Double cost ) {
		this.cost = cost;
	}

	public Double getBound() {
		return bound;
	}

	public void setBound( Double bound ) {
		this.bound = bound;
	}

	public Double getDeaWeight() {
		return deaWeight;
	}

	public void setDeaWeight( Double deaWeight ) {
		this.deaWeight = deaWeight;
	}

	public Double getNewWeight() {
		return newWeight;
	}

	public void setNewWeight( Double newWeight ) {
		this.newWeight = newWeight;
	}

	public Double getNewValue() {
		return newValue;
	}

	public void setNewValue( Double newValue ) {
		this.newValue = newValue;
	}

	public Double getOffset() {
		return offset;
	}

	public void setOffset( Double offset ) {
		this.offset = offset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
		result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
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
		Input other = (Input) obj;
		if ( name == null ) {
			if ( other.name != null )
				return false;
		} else if ( !name.equals( other.name ) )
			return false;
		if ( value == null ) {
			if ( other.value != null )
				return false;
		} else if ( !value.equals( other.value ) )
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Input [name=" + name + ", value=" + value + ", newValue=" + newValue + ", offset=" + offset + ", cost=" + cost + ", bound=" + bound + "]";
	}

}