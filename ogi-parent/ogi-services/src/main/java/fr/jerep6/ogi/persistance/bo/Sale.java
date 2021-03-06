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
@Table(name = "TA_SALE")
// Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "techid" })
public class Sale {
	@Id
	@Column(name = "SAL_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer			techid;

	// Many properties can have same mandate reference
	@Column(name = "SAL_MAND_REFERENCE", length = 64)
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

	@Column(name = "SAL_COMMISSION_SELLER")
	private Boolean			commissionSeller;

	@Column(name = "SAL_ESTI_PRICE")
	private Float			estimationPrice;

	@Column(name = "SAL_ESTI_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar		estimationDate;

	@Column(name = "SAL_PROPERTY_TAX")
	private Float			propertyTax;

	@Column(name = "SAL_SOLD", columnDefinition = "BIT(1) DEFAULT 0")
	private Boolean			sold	= false;

	@Column(name = "SAL_SOLD_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar		soldDate;

	@Column(name = "SAL_SOLD_PRICE")
	private Float			soldPrice;

	@ManyToOne
	@JoinColumn(name = "PRO_ID", nullable = false)
	private RealProperty	property;

	@PreUpdate
	@PrePersist
	private void beforePersist() {
		if (sold == null) {
			sold = false;
		}
		if (commissionSeller == null) {
			commissionSeller = false;
		}
	}

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
		if (commission != null || price != null) {
			Float c = commission == null ? 0 : commission;
			Float p = price == null ? 0 : price;
			return p + c;
		} else {
			return null;
		}
	}
}