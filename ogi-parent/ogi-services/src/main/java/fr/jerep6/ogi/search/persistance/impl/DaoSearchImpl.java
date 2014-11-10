package fr.jerep6.ogi.search.persistance.impl;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
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

	@Autowired
	private Client	client;

	@Value("${elasticsearch.indexName}")
	private String	indexName;

	@Value("${elasticsearch.propertyType}")
	private String	propertyType;

	@Override
	public void index(RealProperty r) {
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

		String prpJson = JSONUtils.toJson(sp);
		IndexResponse response = client.prepareIndex(indexName, propertyType).setSource(prpJson).execute().actionGet();
	}

	@Override
	public void search() {

	}

}
