package fr.jerep6.ogi.persistance.bo;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.enumeration.EnumPartnerRequestType;

@Entity
@Table(name = "TA_PARTNER_REQUEST")
// Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "partner", "property" })
public class PartnerRequest {
	@Id
	@Column(name = "REQ_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer					techid;

	@Column(name = "PRO_ID", nullable = false)
	private Integer					property;

	@Column(name = "REQ_PARTNER", nullable = false, length = 16)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumPartner"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumPartner				partner;

	@Column(name = "REQ_TYPE", nullable = false, length = 16)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumPartnerRequestType"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumPartnerRequestType	requestType;

	@Column(name = "REQ_MODIFICATION_DATE", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar				modificationDate;

	public PartnerRequest() {
		super();
	}

	public PartnerRequest(Integer property, EnumPartner partner, EnumPartnerRequestType requestType) {
		super();
		this.property = property;
		this.partner = partner;
		this.requestType = requestType;
		modificationDate = Calendar.getInstance();
	}

}