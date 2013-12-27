package fr.jerep6.ogi.persistance.bo;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TA_OWNER")
// Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "techid" })
public class Owner {
	@Id
	@Column(name = "OWN_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				techid;

	@Column(name = "OWB_GENDER")
	private String				gender;

	@Column(name = "OWB_FIRSTNAME")
	private String				firstname;

	@Column(name = "OWN_SURNAME")
	private String				surname;

	@Column(name = "OWN_BIRTHDATE")
	@Temporal(TemporalType.DATE)
	private Calendar			birthDate;

	@Column(name = "OWN_PHONE_HOME")
	private String				phoneHome;

	@Column(name = "OWN_PHONE_WORK")
	private String				phoneWork;

	@Column(name = "OWN_PHONE_MOBILE")
	private String				phoneMobile;

	@Column(name = "OWN_PHONE_CONJOINT")
	private String				phoneConjoint;

	@Column(name = "OWN_FAX")
	private String				fax;

	@Column(name = "OWN_MAIL")
	private String				mail;

	@ManyToMany(mappedBy = "owners")
	private Set<RealProperty>	properties;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "TJ_OWN_ADD", //
	joinColumns = @JoinColumn(name = "OWN_ID"), //
	inverseJoinColumns = @JoinColumn(name = "ADD_ID")//
	)
	private Set<Address>		addresses;
}
