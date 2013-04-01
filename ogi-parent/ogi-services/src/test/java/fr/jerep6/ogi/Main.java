package fr.jerep6.ogi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.service.ServiceRealProperty;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"META-INF/spring/spring-context.xml");
		System.out.println("Cool");

		ServiceRealProperty sp = context.getBean(ServiceRealProperty.class);
		sp.readByReference("R1");

		RealPropertyLivable maison = new RealPropertyLivable("r13", null, null);
		maison.setTechid(13);
		maison.setReference("r13");

		Category c = new Category();
		c.setTechid(1);
		c.setLabel("Maison");
		c.setCode(EnumCategory.HOUSE);
		maison.setCategory(c);

		// sp.save(maison);
		context.close();
	}

}
