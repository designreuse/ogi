package fr.jerep6.ogi.service;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.test.AbstractTransactionalTest;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.Type;

@ContextConfiguration(locations = { "classpath:spring-tu-context.xml" })
public class ServiceRealPropertyTest extends AbstractTransactionalTest {

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Autowired
	private ServiceCategory		serviceCategory;

	@Autowired
	private ServiceType			serviceType;

	@Test
	public void createHouse() {

		// Category
		Category catHouse = serviceCategory.readByCode(EnumCategory.HOUSE);
		Assert.assertNotNull(catHouse);

		// Type
		Type typeFerme = serviceType.readOrInsert("Fermee", catHouse);
		Assert.assertNotNull(typeFerme);

		// Address
		Address addr = new Address();
		addr.setCity("Mérignac");
		addr.setPostalCode("33700");

		RealPropertyLivable rl = new RealPropertyLivable("ref1", catHouse, typeFerme);
		Calendar buildDate = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
		buildDate.set(1948, Calendar.AUGUST, 18);
		rl.setBuildDate(buildDate);
		rl.setAddress(addr);

		RealPropertyLivable rsave = (RealPropertyLivable) serviceRealProperty.save(rl);
		Assert.assertNotNull(rsave.getTechid());
		Assert.assertNotNull(rsave.getAddress().getTechid());

	}

	@Override
	protected String[] getDatasetFileName() {
		return new String[] { "./src/test/resources/dbunit/TR.xml",
				"./src/test/resources/dbunit/RealPropertyLivable.xml" };
	}

}
