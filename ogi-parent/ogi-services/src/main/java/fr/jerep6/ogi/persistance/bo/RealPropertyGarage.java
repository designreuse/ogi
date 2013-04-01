package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import fr.jerep6.ogi.enumeration.EnumDoor;

@Entity
@Table(name = "TA_PROPERTY_GARAGE")
@PrimaryKeyJoinColumn(name = "PRO_ID")
// Lombok
@Getter
@Setter
public class RealPropertyGarage extends RealPropertyBuilt {

	@Column(name = "PRG_HEIGHT")
	private Integer		height;

	@Column(name = "PRG_WIDTH")
	private Integer		width;

	@Column(name = "PRG_LENGTH")
	private Integer		length;

	@Column(name = "PRG_DOOR", length = 2)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumDoor"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumDoor	door;

	@Column(name = "PRL_NB_PLACE")
	private Integer		nbPlace;

	public RealPropertyGarage(String reference, Category category, fr.jerep6.ogi.persistance.bo.Type type) {
		super(reference, category, type);
	}

}
