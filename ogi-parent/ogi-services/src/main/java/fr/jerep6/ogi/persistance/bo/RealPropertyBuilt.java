package fr.jerep6.ogi.persistance.bo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import fr.jerep6.ogi.enumeration.EnumDPE;
import fr.jerep6.ogi.enumeration.EnumOrientation;
import fr.jerep6.ogi.utils.DocumentUtils;

@Entity
@Table(name = "TA_PROPERTY_BUILT")
@PrimaryKeyJoinColumn(name = "PRO_ID")
@Inheritance(strategy = InheritanceType.JOINED)
// Lombok
@Getter
@Setter
public abstract class RealPropertyBuilt extends RealProperty {
	@Column(name = "PRB_AREA")
	private Integer			area;

	@Column(name = "PRB_BUILD_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar		buildDate;

	/** Number of floor of the property */
	@Column(name = "PRB_NB_FLOOR")
	private Integer			nbFloor;

	/** Current floor of the property (apartment for example) */
	@Column(name = "PRB_FLOOR_LEVEL")
	private Integer			floorLevel;

	@Column(name = "PRB_ORIENTATION", length = 2)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumOrientation"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumOrientation	orientation;

	@Column(name = "PRB_PARKING", length = 255)
	private String			parking;

	@Column(name = "PRL_NB_GARAGE")
	private Integer			nbGarage;

	@Column(name = "PRB_INSULATION", length = 255)
	private String			insulation;

	// The mappedBy attribute of @OneToMany annotation behaves the same as inverse = true in the xml file. I.E the many
	// side is the owner; the one side is the inverse
	// Pour que RealPropertyBuilt soit maitre de la relation, il ne faut pas définir le mappedBy et donc indiquer la
	// joincolumn
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "PRO_ID")
	private Set<Room>		rooms;

	// The @Embedded annotation is used to specify a persistent field or property of an entity whose value is an
	// instance of an embeddable class. By default, column definitions specified in the @Embeddable class apply to the
	// table of the owning entity but you can override them using@AttributeOverride:
	@Embedded
	@AttributeOverrides({ //
	@AttributeOverride(name = "kWh", column = @Column(name = "PRB_DPE_KWH")), //
			@AttributeOverride(name = "classificationKWh", column = @Column(name = "PRB_DPE_CLASS_KWH")), //
			@AttributeOverride(name = "ges", column = @Column(name = "PRB_DPE_GES")), //
			@AttributeOverride(name = "classificationGes", column = @Column(name = "PRB_DPE_CLASS_GES")), //
	})
	private DPE				dpe;

	@ManyToOne
	@JoinColumn(name = "STA_ORDER", nullable = true)
	private State			state;

	protected RealPropertyBuilt(String reference, Category category, fr.jerep6.ogi.persistance.bo.Type type) {
		super(reference, category, type);
	}

	public Map<EnumDPE, Path> getDpeFile() {
		Map<EnumDPE, Path> m = new HashMap<>(4);

		for (EnumDPE dpe : EnumDPE.values()) {
			Path p = DocumentUtils.absolutize(Paths.get(getReference() + "/dpe/" + EnumDPE.KWH_180.getFileName()));
			if (Files.exists(p)) {
				m.put(dpe, p);
			}
		}

		return m;
	}
}
