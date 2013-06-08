package fr.jerep6.ogi.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Diagnosis;
import fr.jerep6.ogi.persistance.dao.DaoDiagnosis;
import fr.jerep6.ogi.service.ServiceDiagnosis;

@Service("serviceDiagnosis")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceDiagnosisImpl extends AbstractTransactionalService<Diagnosis, Integer> implements ServiceDiagnosis {

	@Autowired
	private DaoDiagnosis	daoDiagnosis;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoDiagnosis);
	}

	@Override
	public Diagnosis readByLabel(String label) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(label));

		return daoDiagnosis.readByLabel(label);
	}

}
