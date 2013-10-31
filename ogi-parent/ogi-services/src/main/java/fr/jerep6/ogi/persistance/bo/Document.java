package fr.jerep6.ogi.persistance.bo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import fr.jerep6.ogi.enumeration.EnumDocumentType;

@Entity
@Table(name = "TA_DOCUMENT")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "path" })
@ToString
public class Document {
	@Id
	@Column(name = "DOC_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				techid;

	@Column(name = "DOC_PATH", nullable = false, length = 1024)
	private String				path;

	@Column(name = "DOC_NAME", nullable = false, length = 255)
	private String				name;

	@Column(name = "DOC_ORDER", nullable = true)
	private Integer				order;

	@Column(name = "DOC_TYPE", nullable = false, length = 24)
	@Type(type = "fr.jerep6.ogi.framework.persistance.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "fr.jerep6.ogi.enumeration.EnumDocumentType"),
			@Parameter(name = "identifierMethod", value = "getCode"),
			@Parameter(name = "valueOfMethod", value = "valueOfByCode") })
	private EnumDocumentType	type;

	@ManyToMany(mappedBy = "documents")
	private Set<RealProperty>	property;

	public Document() {
		super();
	}

	public Document(String path, String name, Integer order, EnumDocumentType type) {
		super();
		this.path = path;
		this.name = name;
		this.order = order;
		this.type = type;
	}

}