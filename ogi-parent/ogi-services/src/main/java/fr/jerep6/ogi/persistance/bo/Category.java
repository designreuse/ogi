package fr.jerep6.ogi.persistance.bo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.google.common.base.Objects;

import fr.jerep6.ogi.enumeration.EnumCategory;

/**
 * Examples : house, apartment, plot, garage, business
 * 
 * @author jerep6 Mar 16, 2013
 */
@Entity
@Table(name = "TR_CATEGORY")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "code" })
public class Category {
	@Id
	@Column(name = "CAT_ID", unique = true, nullable = false)
	private Integer			techid;

	@Column(name = "CAT_CODE", nullable = false, unique = true, length = 8)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumCategory"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumCategory	code;

	@Column(name = "CAT_LABEL", unique = true, nullable = false)
	private String			label;

	@ManyToMany(mappedBy = "categories")
	private Set<Diagnosis>	diagnosis;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("code", code).toString();
	}

}