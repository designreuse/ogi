package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.google.common.base.Objects;

import fr.jerep6.ogi.enumeration.EnumLabelType;

@Entity
@Table(name = "TA_LABEL", //
uniqueConstraints = { @UniqueConstraint(columnNames = { "LAB_TYPE", "LAB_LABEL" }) }//
		)
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "type", "label" })
public class Label {
	@Id
	@Column(name = "LAB_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer			techid;

	@Column(name = "LAB_TYPE", nullable = false, length = 16)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumLabelType"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumLabelType	type;

	@Column(name = "LAB_LABEL", nullable = false, length = 255)
	private String			label;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("type", type).add("label", label).toString();
	}

}