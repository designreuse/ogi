package fr.jerep6.ogi.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumLabelType;
import fr.jerep6.ogi.exception.business.EntityAlreadyExist;
import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorLabel;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Label;
import fr.jerep6.ogi.persistance.dao.DaoLabel;
import fr.jerep6.ogi.service.ServiceLabel;

@Service("serviceLabel")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceLabelImpl extends AbstractTransactionalService<Label, Integer> implements ServiceLabel {
	private final Logger	LOGGER	= LoggerFactory.getLogger(ServiceLabelImpl.class);

	@Autowired
	private DaoLabel		daoLabel;

	@Override
	public Label add(Label label) {
		Preconditions.checkNotNull(label);

		// Check if label is correct
		validate(label);

		if (label.getTechid() != null) {
			LOGGER.warn("Receive techid into label {}", label);
			label.setTechid(null);
		}

		Label existantLabel = read(label.getType(), label.getLabel());
		if (existantLabel != null) {
			throw new EntityAlreadyExist(existantLabel, existantLabel.getType().getCode(), existantLabel.getLabel());
		}

		return daoLabel.save(label);

	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoLabel);
	}

	@Override
	public Label read(EnumLabelType type, String label) {
		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(label);

		return daoLabel.read(type, label);
	}

	@Override
	public List<Label> readByType(EnumLabelType type) {
		Preconditions.checkNotNull(type);
		return daoLabel.readByType(type);
	}

	@Override
	public void validate(Label bo) throws BusinessException {
		if (bo == null) {
			return;
		}
		MultipleBusinessException mbe = new MultipleBusinessException();

		if (Strings.isNullOrEmpty(bo.getLabel())) {
			mbe.add(EnumBusinessErrorLabel.NO_LABEL, bo.getType());
		} else {
			if (bo.getLabel().length() > 255) {
				mbe.add(EnumBusinessErrorLabel.LABEL_SIZE, bo.getLabel(), bo.getLabel().length(), 255);
			}
		}

		mbe.checkErrors();
	}

}
