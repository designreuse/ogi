package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.google.common.base.Objects;

import fr.jerep6.ogi.enumeration.EnumLabelType;

@Entity
@Table(name = "TA_DESCRIPTION")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "type" })
public class Description {
	@Id
	@Column(name = "DSC_ID", unique = true, nullable = false)
	private Integer			techid;

	@Column(name = "DSC_TYPE", nullable = false, length = 16)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumDescriptionType"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumLabelType	type;

	@Column(name = "DSC_LABEL", nullable = false)
	private String			label;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("type", type).add("label", label).toString();
	}

}