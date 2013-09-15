package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

/**
 * Examples for an apartment : Duplex, studio, T1 --> T6 <br />
 * maison : bastide, immeuble, maison, contemporaine, architecte, plein pied, village, indépendance, jumelée,
 * provençale, masse , propriété, villa <br />
 * Garage : parking, garage <br />
 * Terrain : boissé
 * 
 * 
 * @author jerep6 Mar 16, 2013
 */
@Entity
@Table(name = "TR_TYPE")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "category", "label" })
public class Type {
	@Id
	@Column(name = "TYP_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer		techid;

	@Column(name = "TYP_LABEL", nullable = false)
	private String		label;

	@ManyToOne
	@JoinColumn(name = "CAT_ID", nullable = false)
	private Category	category;

	public Type() {
		super();
	}

	public Type(String label, Category category) {
		super();
		this.label = label;
		this.category = category;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("label", label).toString();
	}

}