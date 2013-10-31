package fr.jerep6.ogi.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;

import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.Room;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.persistance.dao.DaoProperty;
import fr.jerep6.ogi.service.ServiceCategory;
import fr.jerep6.ogi.service.ServiceDescription;
import fr.jerep6.ogi.service.ServiceDiagnosis;
import fr.jerep6.ogi.service.ServiceDocument;
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

	@Autowired
	private ServiceDocument		serviceDocument;

	@Autowired
	private ServiceDescription	serviceDescription;

	@Value("${search.result.max}")
	private Integer				searchMaxResult;

	/**
	 * <ul>
	 * <li>Si la référence du bien est renseignée => lecture du bien et modification</li>
	 * <li>Si pas de référence (ie nouveau bien) => enregistrement</li>
	 * </ul>
	 */
	@Override
	public RealProperty createOrUpdateFromBusinessFields(RealProperty propertyFromJson) {
		Preconditions.checkNotNull(propertyFromJson);

		RealProperty prp;

		boolean create;
		if (!Strings.isNullOrEmpty(propertyFromJson.getReference())) {
			prp = readByReference(propertyFromJson.getReference());
			// If reference is supply, prp must exist
			if (prp == null) {
				// TODO : change exception
				throw new BusinessException();
			}
			// Update
			create = false;
		}
		// No reference (ie create prp) => generate it
		else {
			prp = propertyFromJson;
			prp.setReference("pouet1");

			create = true;
		}

		// ###### COMMON ######
		// It's impossible to create new category so only read from database given code
		Category cat = serviceCategory.readByCode(propertyFromJson.getCategory().getCode());
		prp.setCategory(cat);

		// Create type if needed
		Type type = null;
		if (propertyFromJson.getType() != null) {
			type = serviceType.readOrInsert(propertyFromJson.getType().getLabel(), cat);
		}
		prp.setType(type);

		// ###### Equipment ######
		/*
		 * Set<Equipment> eqpts = new HashSet<>(propertyFromJson.getEquipments().size());
		 * for (Equipment anEqpt : propertyFromJson.getEquipments()) {
		 * // Read equipment from DB
		 * Equipment eqptFull = serviceEquipment.readByLabel(anEqpt.getLabel(), cat.getCode());
		 * 
		 * // if not exist create it
		 * if (eqptFull == null) {
		 * eqptFull = new Equipment(anEqpt.getLabel(), cat);
		 * }
		 * 
		 * // don't supply property because property is relation owner
		 * eqpts.add(eqptFull);
		 * }
		 * prp.setEquipments(eqpts);
		 */

		// ###### Diagnosis ######
		/*
		 * for (RealPropertyDiagnosis dia : prp.getDiagnosisProperty()) {
		 * // Read diagnosis corresponding to label
		 * dia.setPk(new DiagnosisRealPropertyId(prp, serviceDiagnosis.readByLabel(dia.getDiagnosis().getLabel())));
		 * }
		 */

		// ###### Description ######
		// If label is empty => ignore it
		// New descriptions
		Collection<Description> descriptionsNew = Collections2.filter(propertyFromJson.getDescriptions(),
				new Predicate<Description>() {
					@Override
					public boolean apply(Description p) {
						return !Strings.isNullOrEmpty(p.getLabel()) && p.getType() != null;
					}
				});

		// Keep description to reuse it (avoid insert)
		List<Description> descriptionsBDBackup = new ArrayList<>(prp.getDescriptions());

		// descriptions in database
		Collection<Description> descriptionsPresents = prp.getDescriptions();
		descriptionsPresents.clear();

		// Add descriptions to manage object
		for (Description aDescription : descriptionsNew) {
			Description d = aDescription;

			int indexDesc = descriptionsBDBackup.indexOf(d);
			if (indexDesc != -1) {
				// Get existant description to avoid sql insert
				d = descriptionsBDBackup.get(indexDesc);
			} else {
				d.setProperty(prp);
			}
			descriptionsPresents.add(d);
		}

		// Delete descriptions
		descriptionsBDBackup.removeAll(new ArrayList<>(descriptionsNew));
		serviceDescription.remove(descriptionsBDBackup);

		// ###### SPECIFIC ######
		if (RealPropertyLivable.class.equals(prp.getClass())) {
			RealPropertyLivable liveable = (RealPropertyLivable) prp;
			// Room
			if (liveable.getRooms() != null) {
				for (Room aRoom : liveable.getRooms()) {
					// techid to null to force insert
					aRoom.setTechid(null);
					aRoom.setProperty(liveable);
				}
			}
		}
		// Nothing to do for address

		// Documents
		Set<Document> documentsSuccess = serviceDocument.copyTempToDirectory(prp.getDocuments(), prp.getReference());
		prp.setDocuments(documentsSuccess);

		// Save real property into database
		if (create) {
			save(prp);
		} else {
			update(prp);
		}
		return propertyFromJson;
	}

	@Override
	public void delete(List<String> reference) {
		for (String aReference : reference) {
			RealProperty rp = readByReference(aReference);
			daoProperty.remove(rp);
		}
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoProperty);
	}

	@Override
	public RealProperty readByReference(String reference) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reference), "Reference is null or empty");

		RealProperty r = daoProperty.readByReference(reference);
		return r;
	}

}
