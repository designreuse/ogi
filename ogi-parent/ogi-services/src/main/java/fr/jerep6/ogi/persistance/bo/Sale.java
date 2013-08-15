package fr.jerep6.ogi.persistance.bo;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import fr.jerep6.ogi.enumeration.EnumMandateType;

@Entity
@Table(name = "TA_SALE")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "mandateReference" })
public class Sale {
	@Id
	@Column(name = "SAL_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer			techid;

	@Column(name = "SAL_MAND_REFERENCE", nullable = false, unique = true, length = 64)
	private String			mandateReference;

	@Column(name = "SAL_MAND_START")
	@Temporal(TemporalType.DATE)
	private Calendar		mandateStartDate;

	@Column(name = "SAL_MAND_END")
	@Temporal(TemporalType.DATE)
	private Calendar		mandateEndDate;

	@Column(name = "SAL_MAND_TYPE", length = 2)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumMandateType"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumMandateType	mandateType;

	@Column(name = "SAL_PRICE")
	private Float			price;

	@Column(name = "SAL_COMMISSION")
	private Float			commission;

	@Column(name = "SAL_ESTI_PRICE")
	private Float			estimationPrice;

	@Column(name = "SAL_ESTI_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar		estimationDate;

	@ManyToOne
	@JoinColumn(name = "PRO_ID", nullable = false)
	private RealProperty	property;

}