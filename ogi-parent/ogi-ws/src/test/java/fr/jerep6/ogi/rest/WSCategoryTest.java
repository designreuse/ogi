package fr.jerep6.ogi.rest;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.test.AbstractTest;
import fr.jerep6.ogi.service.ServiceCategory;
import fr.jerep6.ogi.transfert.bean.CategoryTo;
import fr.jerep6.ogi.utils.Data;

/**
 * Aucun accès à la BD pour les TU du projet ws
 * 
 * @author jerep6
 */
@ContextConfiguration(locations = { "classpath:META-INF/spring/tu-web-context.xml",
		"classpath:META-INF/spring/web-context.xml" })
public class WSCategoryTest extends AbstractTest {

	@Autowired
	private WSCategory		wsCategory;

	@Autowired
	private ServiceCategory	serviceCategory;

	@Test
	public void readCategoryHouse() {
		CategoryTo read = wsCategory.readbyCode(EnumCategory.HOUSE.getCode());
		Assert.assertNotNull(read);

		Assert.assertEquals(EnumCategory.HOUSE.getCode(), read.getCode());
		Assert.assertEquals(Data.getCategoryHouse().getLabel(), read.getLabel());

	}

	@Before
	public void setup() {
		when(serviceCategory.readByCode(EnumCategory.HOUSE)).thenReturn(Data.getCategoryHouse());
	}
}
