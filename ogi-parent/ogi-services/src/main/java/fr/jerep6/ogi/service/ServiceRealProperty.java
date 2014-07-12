package fr.jerep6.ogi.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.transfert.ListResult;

public interface ServiceRealProperty extends TransactionalService<RealProperty, Integer> {
	/**
	 * Read a property by this business reference
	 * 
	 * @param reference
	 */
	Optional<RealProperty> readByReference(String reference);

	/**
	 * Delete properties
	 * 
	 * @param reference
	 *            properties references to delete
	 */
	void delete(List<String> reference);

	/**
	 * Return property according to criteria into parameters
	 * 
	 * @param pageNumber
	 *            page to display. Between 1 and XXX
	 * @param itemNumberPerPage
	 *            number of element to display in a page
	 * @param sortBy
	 *            sort by criteria. If null or incorrect => no sort by
	 * @param sortDir
	 *            direction of sorting. ASC or DESC. No case sensitive. By default ASC
	 * @return
	 */
	ListResult<RealProperty> list(Integer pageNumber, Integer itemNumberPerPage, String sortBy, String sortDir);

	Set<RealProperty> readByReference(List<String> prpReferences);

	/**
	 * Get techid from a fonctionnal reference
	 * 
	 * @param aRef
	 * @return
	 */
	Integer readTechid(String aRef);

	/**
	 * Create a real property. All the associated entities are read in database from theirs business fields.
	 * 
	 * Write dpe into dpe property directory
	 * 
	 * @param property
	 * @return
	 */
	RealProperty createFromBusinessFields(RealProperty propertyFromJson);

	RealProperty updateFromBusinessFields(String reference, RealProperty propertyFromJson);

}
