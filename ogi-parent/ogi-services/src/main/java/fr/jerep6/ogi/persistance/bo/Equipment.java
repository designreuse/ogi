package fr.jerep6.ogi.persistance.bo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

@Entity
@Table(name = "TR_EQUIPMENT")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "category", "label" })
public class Equipment {
	@Id
	@Column(name = "EQP_ID", unique = true, nullable = false)
	private Integer				techid;

	@Column(name = "EQP_LABEL", nullable = false)
	private String				label;

	@ManyToOne
	@JoinColumn(name = "CAT_ID", nullable = false)
	private Category			category;

	@ManyToMany(mappedBy = "equipments")
	private Set<RealProperty>	properties;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("label", label).toString();
	}

}