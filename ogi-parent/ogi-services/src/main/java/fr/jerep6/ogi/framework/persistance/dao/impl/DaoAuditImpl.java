package fr.jerep6.ogi.framework.persistance.dao.impl;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.bo.Audit;
import fr.jerep6.ogi.framework.persistance.dao.DaoAudit;
import fr.jerep6.ogi.framework.utils.StringUtils;

@Repository("daoAudit")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoAuditImpl extends AbstractDao<Audit, Integer> implements DaoAudit {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoAuditImpl.class);

	@Override
	public void log(Integer parentId, Class parentClass, Object oldValue, Object newValue, String propertyName) {
		Audit a = new Audit();
		a.setEntityId(parentId.toString());
		a.setDate(new Date());
		a.setEntityClass(parentClass.getName());
		a.setPropertyName(propertyName);
		// Truncate silently string
		try {
			int oldValueSize = Audit.class.getDeclaredField("propertyOldValue").getAnnotation(Column.class).length();
			int newValueSize = Audit.class.getDeclaredField("propertyNewValue").getAnnotation(Column.class).length();
			a.setPropertyOldValue(StringUtils.truncate(Objects.toString(oldValue, null), oldValueSize));
			a.setPropertyNewValue(StringUtils.truncate(Objects.toString(newValue, null), newValueSize));

		} catch (NoSuchFieldException | SecurityException e) {
			LOGGER.error("Error while reading field size", e);
		}

		save(a);
	}
}
