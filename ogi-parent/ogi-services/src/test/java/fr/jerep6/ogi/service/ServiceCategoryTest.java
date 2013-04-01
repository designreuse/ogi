package fr.jerep6.ogi.service;

import java.util.Collection;

import org.dbunit.dataset.DataSetException;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.jerep6.ogi.framework.test.AbstractTransactionalTest;
import fr.jerep6.ogi.persistance.bo.Category;

@ContextConfiguration(locations = { "classpath:META-INF/spring/spring-context.xml", "classpath:tu-context.xml" })
public class ServiceCategoryTest extends AbstractTransactionalTest {

	@Autowired
	private ServiceCategory	serviceCategory;

	@Override
	protected String[] getDatasetFileName() {
		return new String[] { "./src/test/resources/dbunit/TR.xml",
				"./src/test/resources/dbunit/RealPropertyLivable.xml" };
	}

	@Test
	public void listAll() throws DataSetException {
		Collection<Category> categories = serviceCategory.listAll();

		Assert.assertNotNull(categories);
		Assert.assertThat(categories.isEmpty(), Is.is(false));
		Assert.assertEquals(dataSet.getTable("TR_CATEGORY").getRowCount(), categories.size());
	}
}
