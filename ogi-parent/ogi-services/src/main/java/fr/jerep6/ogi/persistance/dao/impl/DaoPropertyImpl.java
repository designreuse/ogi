package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Book;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.dao.DaoProperty;
import fr.jerep6.ogi.transfert.database.CustomObj;

@Repository("daoProperty")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoPropertyImpl extends AbstractDao<RealProperty, Integer> implements DaoProperty {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoPropertyImpl.class);

	@Override
	public void readByReference(String reference) {
		LOGGER.info("READ");
		RealProperty rp = read(1);

		System.out.println(rp);

		// RealPropertyLivable maison = new RealPropertyLivable();
		// maison.setReference("r1");
		// entityManager.persist(maison);
		// entityManager.flush();

		// System.out.println("DAO : " + entityManager);
		// RealProperty p = new RealProperty();
		// p.setCreateDate(Calendar.getInstance());
		// p.setReference("r1");
		//
		// RealProperty p1 = read(1);
		// System.out.println(p1);
		// p1.setReference("r1");

	}

	@Override
	public void test() {
		StringBuilder q = new StringBuilder();
		q.append("SELECT new " + CustomObj.class.getName() + "(b.techid, b.authors)");
		q.append(" FROM Book b");
		// q.append(" JOIN b.authors as aut");
		q.append(" WHERE b.techid=1");

		Query query = entityManager.createQuery(q.toString());
		List<Book> results = query.getResultList();
		System.out.println(results);

	}
}
