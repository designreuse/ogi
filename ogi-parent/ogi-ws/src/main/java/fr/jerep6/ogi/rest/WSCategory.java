package fr.jerep6.ogi.rest;

import java.util.Collection;
import java.util.List;

import ma.glasnost.orika.metadata.TypeFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Equipment;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.service.ServiceCategory;
import fr.jerep6.ogi.service.ServiceType;
import fr.jerep6.ogi.transfert.bean.CategoryTo;
import fr.jerep6.ogi.transfert.bean.TypeTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * Property's categories are read only
 *
 * @author jerep6
 */
@RestController
@RequestMapping(value = "/category", produces = "application/json;charset=UTF-8")
public class WSCategory extends AbtractWS {

	@Autowired
	private ServiceCategory	serviceCategory;
	@Autowired
	private ServiceType		serviceType;

	@Autowired
	private OrikaMapper		mapper;

	/**
	 * @return all categories in the system
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Collection<CategoryTo> listAll() {
		Collection<Category> categoriesBo = serviceCategory.listAll();

		Collection<CategoryTo> categoriesTo = mapper.mapAsList(categoriesBo, CategoryTo.class);
		return categoriesTo;
	}

	/**
	 * @param code
	 *            category code
	 * @return
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public CategoryTo readbyCode(@PathVariable("code") String code) {
		Category categoryBo = serviceCategory.readByCode(EnumCategory.valueOfByCode(code));

		CategoryTo categoryTo = mapper.map(categoryBo, CategoryTo.class);
		return categoryTo;
	}

	/** @return all equipments from a category */
	@RequestMapping(value = "/{code}/equipments", method = RequestMethod.GET)
	public List<String> readEquipements(@PathVariable("code") String code) {
		List<Equipment> eqpts = serviceCategory.readEquipments(EnumCategory.valueOfByCode(code));

		// https://groups.google.com/forum/#!topic/orika-discuss/s5tsPHZvFEA
		List<String> mapAsList = mapper.mapAsList(eqpts, TypeFactory.valueOf(Equipment.class),
				TypeFactory.valueOf(String.class));
		return mapAsList;
	}

	/** @return add types from a category */
	@RequestMapping(value = "/{code}/types/{label}", method = RequestMethod.PUT)
	public TypeTo typeAdd( //
			@PathVariable("code") String categoryCode, //
			@PathVariable("label") String typeLabel) {

		// Insert
		Category category = serviceCategory.readByCode(EnumCategory.valueOfByCode(categoryCode));
		Type type = serviceType.readOrInsert(typeLabel, category);

		return mapper.map(type, TypeTo.class);
	}

	/** @return all types from a category */
	@RequestMapping(value = "/{code}/types", method = RequestMethod.GET)
	public List<TypeTo> typesRead(@PathVariable("code") String code) {
		List<Type> types = serviceType.readByCategory(EnumCategory.valueOfByCode(code));

		// https://groups.google.com/forum/#!topic/orika-discuss/s5tsPHZvFEA
		List<TypeTo> mapAsList = mapper.mapAsList(types, TypeTo.class);
		return mapAsList;
	}
}