package fr.jerep6.ogi.batch.elasticsearch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.search.service.ServiceSearch;

public class WriterElasticsearch implements ItemWriter<RealProperty> {

	private ServiceSearch	serviceSearch;

	public void setServiceSearch(ServiceSearch serviceSearch) {
		this.serviceSearch = serviceSearch;
	}

	@Override
	public void write(List<? extends RealProperty> items) throws Exception {
		serviceSearch.index((List<RealProperty>) items);
	}

}
