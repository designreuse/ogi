package fr.jerep6.ogi.persistance.bo;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import fr.jerep6.ogi.enumeration.EnumMandateType;
import fr.jerep6.ogi.utils.VenteUtils;

@Entity
@Table(name = "TA_RENT")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "techid" })
public class Rent {
	@Id
	@Column(name = "REN_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer			techid;

	@Column(name = "REN_MAND_REFERENCE", length = 64)
	private String			mandateReference;

	@Column(name = "REN_FREE_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar		freeDate;

	@Column(name = "REN_MAND_TYPE", length = 2)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumMandateType"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumMandateType	mandateType;

	@Column(name = "REN_PRICE")
	private Float			price;

	@Column(name = "REN_COMMISSION")
	private Float			commission;

	@Column(name = "REN_COMMISSION_MANAGEMENT")
	private Float			commissionManagement;

	@Column(name = "REN_DEPOSIT")
	private Float			deposit;

	@Column(name = "REN_SERVICE_CHARGE")
	private Float			serviceCharge;

	@Column(name = "REN_SERVICE_CHARGE_INCLUDED")
	private Boolean			serviceChargeIncluded;

	@Column(name = "REN_FUNISHED", nullable = false)
	private Boolean			furnished;

	@ManyToOne
	@JoinColumn(name = "PRO_ID", nullable = false)
	private RealProperty	property;

	public Float getCommissionPercent() {
		if (commission != null && price != null) {
			return VenteUtils.roundPrice(commission / price * 100);
		}
		return null;
	}

	public boolean getExclusive() {
		return mandateType == EnumMandateType.EXCLUSIF;
	}

	public Float getPriceFinal() {
		Float finalPrice = 0F;
		if (serviceChargeIncluded != null && !serviceChargeIncluded && serviceCharge != null) {
			finalPrice += serviceCharge;
		}
		if (price != null) {
			finalPrice += price;
		}

		return finalPrice;
	}

	@PreUpdate
	@PrePersist
	private void setDefaultValue() {
		if (furnished == null) {
			furnished = false;
		}
	}
}