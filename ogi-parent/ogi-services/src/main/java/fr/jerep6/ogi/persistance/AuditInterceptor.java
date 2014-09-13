package fr.jerep6.ogi.persistance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.core.annotation.AnnotationUtils;

import fr.jerep6.ogi.framework.persistance.bo.Audit;
import fr.jerep6.ogi.framework.persistance.dao.DaoAudit;
import fr.jerep6.ogi.framework.utils.ContextUtils;

public class AuditInterceptor extends EmptyInterceptor {
	private static final long									serialVersionUID	= 1L;

	private DaoAudit											daoAudit;

	/**
	 * Hibernate may trigger several onFlushDirty. In order to log only once, store in an array audits already save.
	 */
	private List<fr.jerep6.ogi.framework.persistance.bo.Audit>	audits				= new ArrayList<>(0);

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
		fr.jerep6.ogi.persistance.annotation.Audit findAnnotation = AnnotationUtils.findAnnotation(entity.getClass(),
				fr.jerep6.ogi.persistance.annotation.Audit.class);

		// Handle only class with @Audit annotation
		if (findAnnotation != null) {
			// Extract data from annotation
			List<String> excludes = Arrays.asList(findAnnotation.excludes());
			List<String> includes = Arrays.asList(findAnnotation.includes());

			for (int i = 0; i < currentState.length; i++) {
				// Determine if log according to state
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
					Audit audit = new Audit((Integer) id, entity.getClass(), previousState[i], currentState[i],
							propertyNames[i]);

					// Find audit with same entity, id, propertyName
					Optional<Audit> findFirst = audits.stream().filter(//
							a -> a.getEntityId().equals(audit.getEntityId()) && //
							a.getEntityClass().equals(audit.getEntityClass()) && //
							a.getPropertyName().equals(a.getPropertyName()))//
							.findFirst();

					if (!findFirst.isPresent()) {
						audits.add(daoAudit.save(audit));
					}
				}
			}
		}
		return false;
	}
}