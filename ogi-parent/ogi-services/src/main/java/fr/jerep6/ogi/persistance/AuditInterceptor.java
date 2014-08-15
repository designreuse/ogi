package fr.jerep6.ogi.persistance;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.framework.persistance.dao.DaoAudit;
import fr.jerep6.ogi.framework.utils.ContextUtils;
import fr.jerep6.ogi.persistance.annotation.Audit;

@Component
public class AuditInterceptor extends EmptyInterceptor {
	private static final long	serialVersionUID	= 1L;

	private DaoAudit			daoAudit;

	public AuditInterceptor() {
		super();
		daoAudit = ContextUtils.getBean(DaoAudit.class);
	}

	/**
	 * Excludes take precedence over includes
	 * If include is empty then all properties are managed
	 *
	 * @param propertyName
	 *            name of property to test
	 * @param includes
	 *            list of properties to audit
	 * @param excludes
	 *            list of properties to NOT audit
	 * @return
	 */
	private boolean isPropertyAuditable(String propertyName, List<String> includes, List<String> excludes) {
		if (excludes.contains(propertyName)) {
			return false;
		}
		if (includes.isEmpty() || includes.contains(propertyName)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {

		// Read @Audit annotation
		Audit findAnnotation = AnnotationUtils.findAnnotation(entity.getClass(), Audit.class);

		// Handle only class with @Audit annotation
		if (findAnnotation != null) {
			// Extract data from annotation
			List<String> excludes = Arrays.asList(findAnnotation.excludes());
			List<String> includes = Arrays.asList(findAnnotation.includes());

			for (int i = 0; i < currentState.length; i++) {
				boolean log = false;
				if (currentState[i] == null) {
					if (previousState[i] != null) {
						log = true;
					}
				} else if (!currentState[i].equals(previousState[i])) {
					log = true;
				}
				// Log only if field is modify
				if (log && isPropertyAuditable(propertyNames[i], includes, excludes)) {
					daoAudit.log((Integer) id, entity.getClass(), previousState[i], currentState[i], propertyNames[i]);
				}
			}
		}
		return false;
	}
}