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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.google.common.base.Objects;

import fr.jerep6.ogi.enumeration.EnumOrientation;

/**
 * Entities input by users doesn't have code.
 * 
 * @author jerep6 Mar 16, 2013
 */
@Entity
@Table(name = "TA_ROOM")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "techid" })
public class Room {
	@Id
	@Column(name = "ROO_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				techid;

	@Column(name = "ROO_TYPE", nullable = false)
	private String				roomType;

	@Column(name = "ROO_AREA")
	private Integer				area;

	@Column(name = "ROO_CARREZ")
	private Boolean				carrezLaw;

	@Column(name = "ROO_LIVABLE")
	private Boolean				livable;

	@Column(name = "ROO_ORIENTATION")
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumOrientation"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumOrientation		orientation;

	@Column(name = "ROO_FLOOR")
	private String				floor;

	@Column(name = "ROO_WALL")
	private String				wall;

	@Column(name = "ROO_VIEW")
	private String				view;

	@Column(name = "ROO_FLOOR_LEVEL")
	private Integer				floorLevel;

	@Column(name = "ROO_DESCRIPTION")
	private String				description;

	@Column(name = "ROO_NB_WINDOW")
	private Integer				nbWindow;

	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	private Document			photo;

	@ManyToOne
	@JoinColumn(name = "PRO_ID", nullable = false)
	private RealPropertyBuilt	property;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("area", area).toString();
	}
}