package fr.jerep6.ogi.framework.persistance.bo;

import java.util.Date;
import java.util.Objects;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jerep6.ogi.framework.utils.StringUtils;

@Entity
@Table(name = "TT_AUDIT")
@Getter
@Setter
@EqualsAndHashCode(of = { "date", "entityId", "propertyName" })
public class Audit {
	private static Logger	LOGGER	= LoggerFactory.getLogger(Audit.class);

	@Id
	@Column(name = "AUD_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer			techid;

	@Column(name = "AUD_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date			date;

	@Column(name = "AUD_ENTITY_ID", length = 64)
	private String			entityId;

	@Column(name = "AUD_ENTITY_CLASS", length = 255)
	private String			entityClass;

	@Column(name = "AUD_PROPERTY_NAME", length = 255)
	private String			propertyName;

	@Column(name = "AUD_PROPERTY_OLD", length = 2048)
	private String			propertyOldValue;

	@Column(name = "AUD_PROPERTY_NEW", length = 2048)
	private String			propertyNewValue;

	public Audit(Integer parentId, Class objClass, Object oldValue, Object newValue, String propertyName) {
		this.entityId = parentId.toString();
		this.date = new Date();
		this.entityClass = objClass.getName();
		this.propertyName = propertyName;
		// Truncate silently string
		try {
			int oldValueSize = this.getClass().getDeclaredField("propertyOldValue").getAnnotation(Column.class)
					.length();
			int newValueSize = this.getClass().getDeclaredField("propertyNewValue").getAnnotation(Column.class)
					.length();
			this.propertyOldValue = StringUtils.truncate(Objects.toString(oldValue, null), oldValueSize);
			this.propertyNewValue = StringUtils.truncate(Objects.toString(newValue, null), newValueSize);

		} catch (NoSuchFieldException | SecurityException e) {
			LOGGER.error("Error while reading field size", e);
		}
	}

}