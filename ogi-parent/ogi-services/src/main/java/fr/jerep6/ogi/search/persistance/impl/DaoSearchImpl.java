package fr.jerep6.ogi.search.persistance.impl;

import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import fr.jerep6.ogi.framework.utils.JSONUtils;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyBuilt;
import fr.jerep6.ogi.search.obj.SearchAddress;
import fr.jerep6.ogi.search.obj.SearchImage;
import fr.jerep6.ogi.search.obj.SearchProperty;
import fr.jerep6.ogi.search.obj.SearchRent;
import fr.jerep6.ogi.search.obj.SearchSale;
import fr.jerep6.ogi.search.persistance.DaoSearch;

@Repository("daoSearch")
public class DaoSearchImpl implements DaoSearch {
	private final Logger	LOGGER	= LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Client			client;

	@Value("${elasticsearch.indexName}")
	private String			indexName;

	@Value("${elasticsearch.propertyType}")
	private String			propertyType;

	@Override
	public void delete(List<String> references) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		for (String ref : references) {
			bulkRequest.add(client.prepareDelete(indexName, propertyType, ref));
		}

		// Execute bulk
		BulkResponse response = bulkRequest.execute().actionGet();

		if (response.hasFailures()) {
			LOGGER.error("Delete error");
		}

	}

	@Override
	public void index(List<RealProperty> realProperty) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		// Add each property to bulk request
		for (RealProperty r : realProperty) {
			SearchProperty sp = new SearchProperty();
			sp.setReference(r.getReference());
			sp.setCategory(r.getCategory().getLabel());
			sp.setLandArea(r.getLandArea());
			sp.setHousingEstate(r.getHousingEstate());

			if (r.getAddress() != null) {
				SearchAddress addr = new SearchAddress();
				addr.setPostalCode(r.getAddress().getPostalCode());
				addr.setCity(r.getAddress().getCity());
				sp.setAddress(addr);
			}

			if (r.getSale() != null) {
				SearchSale searchSale = new SearchSale();
				searchSale.setMandateReference(r.getSale().getMandateReference());
				searchSale.setPrice(r.getSale().getPriceFinal());
				sp.setSale(searchSale);
			}

			if (r.getRent() != null) {
				SearchRent searchRent = new SearchRent();
				searchRent.setMandateReference(r.getRent().getMandateReference());
				searchRent.setPrice(r.getRent().getPriceFinal());
				sp.setRent(searchRent);
			}

			if (!r.getPhotos().isEmpty()) {
				SearchImage img = new SearchImage();
				Document d = r.getPhotos().get(0);
				img.setUrl(d.getPath());
				img.setTitle(r.getReference());
				sp.setImage(img);
			}

			if (r instanceof RealPropertyBuilt) {
				sp.setArea(((RealPropertyBuilt) r).getArea());
			}

			bulkRequest.add(client.prepareIndex(indexName, propertyType).setSource(JSONUtils.toJson(sp)));
		}

		// Execute bulk
		BulkResponse response = bulkRequest.execute().actionGet();

		if (response.hasFailures()) {
			LOGGER.error("Indexing error");
		}
	}

	@Override
	public void search() {

	}

}
