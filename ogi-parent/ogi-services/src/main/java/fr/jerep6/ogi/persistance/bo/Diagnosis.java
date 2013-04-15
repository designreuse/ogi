package fr.jerep6.ogi.persistance.bo;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

@Entity
@Table(name = "TR_DIAGNOSIS")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "label" })
public class Diagnosis {
	@Id
	@Column(name = "DIA_ID", unique = true, nullable = false)
	private Integer						techid;

	@Column(name = "DIA_LABEL", nullable = false)
	private String						label;

	@OneToMany(mappedBy = "pk.diagnosis", cascade = CascadeType.ALL)
	private Set<DiagnosisRealProperty>	diagnosisProperty;

	@ManyToMany
	@JoinTable(name = "TJ_DIA_CAT", //
	joinColumns = @JoinColumn(name = "DIA_ID"), //
	inverseJoinColumns = @JoinColumn(name = "CAT_ID")//
	)
	private Set<Category>				categories;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("label", label).toString();
	}

}