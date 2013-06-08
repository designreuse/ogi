package fr.jerep6.ogi.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Equipment;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyDiagnosis;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.Room;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.persistance.bo.id.DiagnosisRealPropertyId;
import fr.jerep6.ogi.persistance.dao.DaoProperty;
import fr.jerep6.ogi.service.ServiceCategory;
import fr.jerep6.ogi.service.ServiceDiagnosis;
import fr.jerep6.ogi.service.ServiceEquipment;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.ServiceType;

@Service("serviceRealProperty")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceRealPropertyImpl extends AbstractTransactionalService<RealProperty, Integer> implements
		ServiceRealProperty {

	@Autowired
	private DaoProperty			daoProperty;

	@Autowired
	private ServiceCategory		serviceCategory;

	@Autowired
	private ServiceType			serviceType;

	@Autowired
	private ServiceEquipment	serviceEquipment;

	@Autowired
	private ServiceDiagnosis	serviceDiagnosis;

	@Value("${search.result.max}")
	private Integer				searchMaxResult;

	@Override
	public RealProperty createFromBusinessFields(RealProperty property) {
		Preconditions.checkNotNull(property);
		RealProperty prp = property;

		// If property already exist return it
		if (!Strings.isNullOrEmpty(prp.getReference())) {
			RealProperty propertyFromDB = readByReference(prp.getReference());
			if (propertyFromDB != null) { return propertyFromDB; }
		}

		// ###### COMMON ######
		// It's impossible to create new category so only read from database given code
		Category cat = serviceCategory.readByCode(prp.getCategory().getCode());
		prp.setCategory(cat);

		// Create type if needed
		Type t = serviceType.readOrInsert(prp.getType().getLabel(), cat);
		prp.setType(t);

		Set<Equipment> eqpts = new HashSet<>(prp.getEquipments().size());
		for (Equipment anEqpt : prp.getEquipments()) {
			// Read equipment from DB
			Equipment eqptFull = serviceEquipment.readByLabel(anEqpt.getLabel(), cat.getCode());

			// if not exist create it
			if (eqptFull == null) {
				eqptFull = new Equipment(anEqpt.getLabel(), cat);
			}

			// don't supply property because property is relation owner
			eqpts.add(eqptFull);
		}
		prp.setEquipments(eqpts);

		for (RealPropertyDiagnosis dia : prp.getDiagnosisProperty()) {
			// Read diagnosis corresponding to label
			dia.setPk(new DiagnosisRealPropertyId(prp, serviceDiagnosis.readByLabel(dia.getDiagnosis().getLabel())));
		}

		// ###### SPECIFIC ######
		if (RealPropertyLivable.class.equals(prp.getClass())) {
			RealPropertyLivable liveable = (RealPropertyLivable) prp;
			// Room
			for (Room aRoom : liveable.getRooms()) {
				// techid to null to force insert
				aRoom.setTechid(null);
				aRoom.setProperty(liveable);
			}
		}

		// Description
		for (Description aDescription : prp.getDescriptions()) {
			aDescription.setProperty(prp);
		}

		// Nothing to do for address

		// Save real property into database
		save(property);
		return property;
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoProperty);
	}

	@Override
	public RealProperty readByReference(String reference) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reference), "Reference is null or empty");

		return daoProperty.readByReference(reference);
	}

}
