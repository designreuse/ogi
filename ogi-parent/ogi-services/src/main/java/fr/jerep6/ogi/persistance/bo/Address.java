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
import lombok.ToString;

@Entity
@Table(name = "TA_ADDRESS")
// Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "number", "street", "postalCode", "city", "additional" })
public class Address {
	@Id
	@Column(name = "ADD_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer			techid;

	@Column(name = "ADD_NUMBER")
	private String			number;

	@Column(name = "ADD_STREET")
	private String			street;

	@Column(name = "ADD_ADDITIONAL")
	private String			additional;

	@Column(name = "ADD_POSTALCODE", nullable = false)
	private String			postalCode;

	@Column(name = "ADD_CITY", nullable = false)
	private String			city;

	@Column(name = "ADD_CEDEX")
	private String			cedex;

	@Column(name = "ADD_LATITUDE")
	private String			latitude;

	@Column(name = "ADD_LONGITUDE")
	private String			longitude;

	@ManyToOne
	@JoinColumn(name = "PRO_ID", nullable = false)
	private RealProperty	property;
}
