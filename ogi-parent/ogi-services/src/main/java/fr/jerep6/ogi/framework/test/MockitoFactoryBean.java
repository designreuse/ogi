package fr.jerep6.ogi.framework.test;

import org.mockito.Mockito;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} for creating mocked beans based on Mockito so that they
 * can be {@link @Autowired} into Spring test configurations.
 * 
 * @author Mattias Severson, Jayway
 * 
 * @see FactoryBean
 * @see org.mockito.Mockito
 * @see http://www.jayway.com/2011/11/30/spring-integration-tests-part-i-creating-mock-objects/
 */
public class MockitoFactoryBean<T> implements FactoryBean<T> {

	private final Class<T>	classToBeMocked;

	/**
	 * Creates a Mockito mock instance of the provided class.
	 * 
	 * @param classToBeMocked
	 *            The class to be mocked.
	 */
	public MockitoFactoryBean(Class<T> classToBeMocked) {
		this.classToBeMocked = classToBeMocked;
	}

	@Override
	public T getObject() throws Exception {
		return Mockito.mock(classToBeMocked);
	}

	@Override
	public Class<?> getObjectType() {
		return classToBeMocked;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}