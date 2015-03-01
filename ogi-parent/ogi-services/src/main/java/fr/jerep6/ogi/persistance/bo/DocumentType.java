package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import fr.jerep6.ogi.enumeration.EnumDocumentZoneList;

@Entity
@Table(name = "TR_DOCUMENT_TYPE")
@Getter
@Setter
@EqualsAndHashCode(of = { "zoneList", "label" })
@ToString(of = { "techid", "label", "zoneList" })
/**
 * @author jerep6 1 mars. 2015
 */
public class DocumentType {
	@Id
	@Column(name = "DOT_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer					techid;

	@Column(name = "DOT_LABEL", nullable = false, length = 1024)
	private String					label;

	/** Where to display document type. For example : ADMINISTRATIF_RENT, ADMINISTRATIF_SALE */
	@Column(name = "DOT_ZONELIST", nullable = false, length = 24)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumDocumentZoneList"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumDocumentZoneList	zoneList;

	public DocumentType() {
		super();
	}

}