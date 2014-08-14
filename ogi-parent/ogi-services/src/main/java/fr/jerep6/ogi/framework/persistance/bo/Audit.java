package fr.jerep6.ogi.framework.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TT_AUDIT")
@Getter
@Setter
@EqualsAndHashCode(of = { "techid" })
public class Audit {
	@Id
	@Column(name = "AUD_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer	techid;

	@Column(name = "AUD_ENTITY_ID", length = 64)
	private String	entityId;

	@Column(name = "AUD_ENTITY_CLASS", length = 255)
	private String	entityClass;

	@Column(name = "AUD_PROPERTY_NAME", length = 255)
	private String	propertyName;

	@Column(name = "AUD_PROPERTY_OLD", length = 2048)
	private String	propertyOldValue;

	@Column(name = "AUD_PROPERTY_NEW", length = 2048)
	private String	propertyNewValue;

}