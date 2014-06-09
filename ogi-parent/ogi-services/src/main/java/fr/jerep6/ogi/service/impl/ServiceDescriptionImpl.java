package fr.jerep6.ogi.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;

import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorDescription;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.dao.DaoDescription;
import fr.jerep6.ogi.service.ServiceDescription;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceDescription")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceDescriptionImpl extends AbstractTransactionalService<Description, Integer> implements
		ServiceDescription {
	private static Logger		LOGGER	= LoggerFactory.getLogger(ServiceDescriptionImpl.class);

	@Autowired
	private DaoDescription		daoDescription;

	@Autowired
	private OrikaMapperService	mapper;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoDescription);
	}

	@Override
	public Set<Description> merge(Set<Description> descriptionsBD, Set<Description> descriptionsModif) {
		// If label is empty => ignore it
		// New descriptions
		Collection<Description> descriptionsNew = Collections2.filter(descriptionsModif, new Predicate<Description>() {
			@Override
			public boolean apply(Description p) {
				return !Strings.isNullOrEmpty(p.getLabel()) && p.getType() != null;
			}
		});

		// Keep description to reuse it (avoid insert)
		List<Description> descriptionsBDBackup = new ArrayList<>(descriptionsBD);

		// descriptions in database
		descriptionsBD.clear();

		// Add descriptions to manage object
		for (Description aDescriptionModif : descriptionsNew) {
			Description d = aDescriptionModif;

			int indexDesc = descriptionsBDBackup.indexOf(d);
			if (indexDesc != -1) {
				// Get existant description to avoid sql insert
				d = descriptionsBDBackup.get(indexDesc);
				mapper.map(aDescriptionModif, d);
			}
			validate(d);
			descriptionsBD.add(d);
		}

		// Delete descriptions : description BD - new descriptions (new descriptions must contains all descriptions)
		descriptionsBDBackup.removeAll(new ArrayList<>(descriptionsNew));
		remove(descriptionsBDBackup);

		return descriptionsBD;
	}

	@Override
	public void validate(Description bo) throws BusinessException {
		if (bo == null) {
			return;
		}
		MultipleBusinessException mbe = new MultipleBusinessException();

		if (Strings.isNullOrEmpty(bo.getLabel())) {
			mbe.add(EnumBusinessErrorDescription.NO_LABEL, bo.getType());
		} else {
			if (bo.getLabel().length() > 2048) {
				mbe.add(EnumBusinessErrorDescription.LABEL_SIZE, bo.getType(), bo.getLabel().length(), 2048);
			}
		}

		mbe.checkErrors();
	}

}