package fr.jerep6.ogi.rest;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import junitx.framework.ListAssert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.test.AbstractTest;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.transfert.bean.DescriptionTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyTo;
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
	private WSRealProperty		wsRealProperty;

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Test
	public void readPropertyLiveable() {
		RealPropertyTo read = wsRealProperty.read("ref1");
		Assert.assertNotNull(read);

		Assert.assertEquals(EnumCategory.HOUSE.getCode(), read.getCategory().getCode());
		ListAssert.assertEquals(Arrays.asList("Cheminée", "Interphone"), new ArrayList<>(read.getEquipments()));

		Description d = Data.getFarm().getDescriptions().iterator().next();
		DescriptionTo d1 = new DescriptionTo();
		d1.setType(d.getType().getCode());
		d1.setLabel(d.getLabel());
		ListAssert.assertContains(new ArrayList<>(read.getDescriptions()), d1);

		// Address
		Address a = Data.getAddressTyrosse();
		Assert.assertEquals(a.getNumber(), read.getAddress().getNumber());
		Assert.assertEquals(a.getCity(), read.getAddress().getCity());
		Assert.assertEquals(a.getLatitude(), read.getAddress().getLatitude());

	}

	@Before
	public void setup() {
		when(serviceRealProperty.readByReference("ref1")).thenReturn(Data.getFarm());
	}
}
