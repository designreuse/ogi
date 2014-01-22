package fr.jerep6.ogi.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.exception.business.RealPropertyNotFoundBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyBuilt;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.persistance.bo.State;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.persistance.dao.DaoProperty;
import fr.jerep6.ogi.service.ServiceCategory;
import fr.jerep6.ogi.service.ServiceDPE;
import fr.jerep6.ogi.service.ServiceDescription;
import fr.jerep6.ogi.service.ServiceDiagnosis;
import fr.jerep6.ogi.service.ServiceDocument;
import fr.jerep6.ogi.service.ServiceEquipment;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.ServiceSale;
import fr.jerep6.ogi.service.ServiceState;
import fr.jerep6.ogi.service.ServiceType;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceRealProperty")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceRealPropertyImpl extends AbstractTransactionalService<RealProperty, Integer> implements
		ServiceRealProperty {
	private static Logger		LOGGER	= LoggerFactory.getLogger(ServiceRealPropertyImpl.class);

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

	@Autowired
	private ServiceSale			serviceSale;

	@Autowired
	private ServiceState		serviceState;

	@Autowired
	private ServiceDPE			serviceDPE;

	@Autowired
	private OrikaMapperService	mapper;

	/**
	 * Compute new reference for a property.
	 * 
	 * WARNING : Not thread safe
	 * 
	 * @param categ
	 * @return
	 */
	private String computeReference(Category categ) {
		Preconditions.checkNotNull(categ);
		return categ.getPrefixReference() + (daoProperty.getMax() + 1);
	}

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

		// It's impossible to create new category so only read from database given code
		Category cat = serviceCategory.readByCode(propertyFromJson.getCategory().getCode());

		boolean create;
		// If json reference is not null => try to read property into database
		if (!Strings.isNullOrEmpty(propertyFromJson.getReference())) {
			prp = readByReference(propertyFromJson.getReference());
			// If reference is supply, prp must exist
			if (prp == null) {
				throw new RealPropertyNotFoundBusinessException(propertyFromJson.getReference());
			}

			// Map into object read from BD simples fields
			mapper.map(propertyFromJson, prp);

			// Update
			create = false;
		} else { // No reference (ie create prp) => generate it
			prp = propertyFromJson;
			prp.setReference(computeReference(cat));

			create = true;
		}

		// ###### COMMON ######
		prp.setCategory(cat);

		// Create type if needed
		Type type = null;
		if (propertyFromJson.getType() != null) {
			type = serviceType.readOrInsert(propertyFromJson.getType().getLabel(), cat);
		}
		prp.setType(type);

		// ###### SALE ######
		// Map modification into object.
		Sale saleModif = serviceSale.merge(prp.getSale(), propertyFromJson.getSale());
		prp.setSale(saleModif);
		if (saleModif != null) {
			saleModif.setProperty(prp);
		}

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

		// ###### Descriptions ######
		Set<Description> descriptions = serviceDescription.merge(prp.getDescriptions(),
				propertyFromJson.getDescriptions());
		for (Description description : descriptions) {
			description.setProperty(prp);
		}
		prp.setDescriptions(descriptions);

		// ###### SPECIFIC ######
		if (propertyFromJson instanceof RealPropertyBuilt) {
			RealPropertyBuilt builtFromJson = (RealPropertyBuilt) propertyFromJson;
			RealPropertyBuilt built = (RealPropertyBuilt) prp;

			// State
			State state = null;
			if (builtFromJson.getState() != null) {
				state = serviceState.read(builtFromJson.getState().getOrder());
			}
			built.setState(state);

			// Write images dpe to disk
			serviceDPE.writeDPEFiles(built.getReference(), built.getDpe());
		}

		if (propertyFromJson instanceof RealPropertyLivable) {
			RealPropertyLivable liveable = (RealPropertyLivable) propertyFromJson;
			/*
			 * // Room
			 * if (liveable.getRooms() != null) {
			 * for (Room aRoom : liveable.getRooms()) {
			 * // techid to null to force insert
			 * aRoom.setTechid(null);
			 * aRoom.setProperty(liveable);
			 * }
			 * }
			 */
		}

		// Documents
		Set<Document> documents = serviceDocument.merge(prp.getReference(), prp.getDocuments(),
				propertyFromJson.getDocuments());
		prp.setDocuments(documents);

		// Save real property into database
		if (create) {
			save(prp);
		} else {
			update(prp);
		}
		return prp;
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
