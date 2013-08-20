package fr.jerep6.ogi.rest;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ma.glasnost.orika.metadata.TypeFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Equipment;
import fr.jerep6.ogi.service.ServiceCategory;
import fr.jerep6.ogi.transfert.bean.CategoryTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * Property's categories are read only
 * 
 * @author jerep6
 */
@Controller
@Path("/category")
public class WSCategory extends AbstractJaxRsWS {

	@Autowired
	private ServiceCategory	serviceCategory;

	@Autowired
	private OrikaMapper		mapper;

	/**
	 * @return all categories in the system
	 */
	@GET
	@Produces(APPLICATION_JSON_UTF8)
	public Collection<CategoryTo> listAll() {
		Collection<Category> categoriesBo = serviceCategory.listAll();

		Collection<CategoryTo> categoriesTo = mapper.mapAsList(categoriesBo, CategoryTo.class);
		return categoriesTo;
	}

	/** @return all equipments from a category */
	@GET
	@Path("/{code}/equipments")
	@Produces(APPLICATION_JSON_UTF8)
	public List<String> readByCategory(@PathParam("code") String code) {
		List<Equipment> eqpts = serviceCategory.readEquipments(EnumCategory.valueOfByCode(code));

		// https://groups.google.com/forum/#!topic/orika-discuss/s5tsPHZvFEA
		List<String> mapAsList = mapper.mapAsList(eqpts, TypeFactory.valueOf(Equipment.class),
				TypeFactory.valueOf(String.class));
		return mapAsList;
	}

	/**
	 * @param code
	 *            category code
	 * @return
	 */
	@GET
	@Path("/{code}")
	@Produces(APPLICATION_JSON_UTF8)
	public CategoryTo readbyCode(@PathParam("code") String code) {
		Category categoryBo = serviceCategory.readByCode(EnumCategory.valueOfByCode(code));

		CategoryTo categoryTo = mapper.map(categoryBo, CategoryTo.class);
		return categoryTo;
	}
}