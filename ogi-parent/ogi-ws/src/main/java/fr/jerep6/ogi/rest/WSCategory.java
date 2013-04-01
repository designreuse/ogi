package fr.jerep6.ogi.rest;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.service.ServiceCategory;
import fr.jerep6.ogi.transfert.bean.CategoryTo;
import fr.jerep6.ogi.transfert.converter.OrikaMapper;

@Controller
@Path("/category")
public class WSCategory {

	@Autowired
	private ServiceCategory	serviceCategory;

	@Autowired
	private OrikaMapper		mapper;

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<CategoryTo> listAll() {
		Collection<Category> categoriesBo = serviceCategory.listAll();
		Collection<CategoryTo> categoriesTo = mapper.mapAsList(categoriesBo, CategoryTo.class);

		return categoriesTo;
	}
}