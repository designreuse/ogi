package fr.jerep6.ogi.persistance.bo;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

// Hibernate use the name of then entity to determine the table name. But the purpose of this name is to use in JPQL
@Entity
@Table(name = "TA_PROPERTY")
@Inheritance(strategy = InheritanceType.JOINED)
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "reference" })
public abstract class RealProperty {
	@Id
	@Column(name = "PRO_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer						techid;

	@Column(name = "PRO_REFERENCE", nullable = false, unique = true, length = 64)
	private String						reference;

	@Column(name = "PRO_LAND_AREA")
	private Integer						landArea;

	@Column(name = "PRO_COS")
	private Float						cos;

	/** Lotissement */
	@Column(name = "PRO_HOUSING_ESTATE")
	private Boolean						housingEstate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ADD_ID")
	private Address						address;

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
	private Set<Description>			descriptions;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "TJ_PRP_EQP", //
	joinColumns = @JoinColumn(name = "PRO_ID"), //
	inverseJoinColumns = @JoinColumn(name = "EQP_ID")//
	)
	private Set<Equipment>				equipments;

	@ManyToOne
	@JoinColumn(name = "CAT_ID", nullable = false)
	private Category					category;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "TYP_ID", nullable = false)
	private Type						type;

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
	@OrderBy("order ASC")
	private Set<Photo>					photos;

	@OneToMany(mappedBy = "pk.property", cascade = CascadeType.ALL)
	private Set<RealPropertyDiagnosis>	diagnosisProperty;

	// ##### Technical field #####
	// Il faut obligatoirement spécifier l'attribut columnDefinition sinon mysql crée un champ date time
	@Column(name = "PRO_MODIFICATION_DATE", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar					modificationDate;

	@Version
	@Column(name = "PRO_VERSION", nullable = false)
	private Integer						version;

	protected RealProperty(String reference, Category category, Type type) {
		super();
		this.reference = reference;
		this.category = category;
		this.type = type;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("reference", reference).toString();
	}

}

// JPA will use all properties of the class, unless you specifically mark them with @Transient. If a field is not
// annotated it will be still mapped
// The @Column annotation is purely optional, and is there to let you override the auto-generated column name.
// Furthermore, the length attribute of @Column is only used when auto-generating table definitions, it has no effect on
// the runtime.
