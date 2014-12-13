package fr.jerep6.ogi.search.persistance.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.MatchQueryBuilder.ZeroTermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.InternalNumericMetricsAggregation.SingleValue;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import fr.jerep6.ogi.framework.utils.JSONUtils;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyBuilt;
import fr.jerep6.ogi.search.model.SearchAddress;
import fr.jerep6.ogi.search.model.SearchImage;
import fr.jerep6.ogi.search.model.SearchOwner;
import fr.jerep6.ogi.search.model.SearchRealProperty;
import fr.jerep6.ogi.search.model.SearchRent;
import fr.jerep6.ogi.search.model.SearchSale;
import fr.jerep6.ogi.search.obj.SearchCriteria;
import fr.jerep6.ogi.search.obj.SearchCriteriaFilter;
import fr.jerep6.ogi.search.obj.SearchCriteriaFilterRange;
import fr.jerep6.ogi.search.obj.SearchCriteriaFilterTerm;
import fr.jerep6.ogi.search.obj.SearchResult;
import fr.jerep6.ogi.search.obj.SearchResultAggregation;
import fr.jerep6.ogi.search.persistance.DaoSearch;
import fr.jerep6.ogi.utils.MyUrlUtils;

@Repository("daoSearch")
public class DaoSearchImpl implements DaoSearch {
	private static Map<String, List<String>>	sortFields		= new HashMap<>();
	static {
		sortFields.put("price", Arrays.asList("rent.price", "sale.price"));
		sortFields.put("city", Arrays.asList("address.city.raw"));
		sortFields.put("owner", Arrays.asList("owners.name.raw"));
	}

	private final Logger						LOGGER			= LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Client								client;

	@Value("${elasticsearch.indexName}")
	private String								indexName;

	@Value("${elasticsearch.propertyType}")
	private String								propertyType;

	@Value("${elasticsearch.minimumShoudMatch}")
	private String								minimumShouldMatch;

	private static String[]						searchFields	= new String[] { //
		"reference", //
			"category", //
			"address.postalCode",//
			"address.city", //
			"sale.mandateReference",//
			"rent.mandateReference", //
			"owners.name"										};

	private void addAggregations(SearchCriteria criteria, SearchRequestBuilder requestBuilder) {
		// Category (maison, appartement, terrain)
		requestBuilder.addAggregation(//
				AggregationBuilders.terms(//
						"categories")//
						.field("category.raw")//
						.size(0)//
						.order(Terms.Order.term(true))// Tri alphabétique
				);
		// City
		requestBuilder.addAggregation(//
				AggregationBuilders.terms(//
						"cities")//
						.field("address.city.raw")//
						.size(0)//
						.order(Terms.Order.term(true))// Tri alphabétique
				);

		// Mode
		requestBuilder.addAggregation(//
				AggregationBuilders.terms(//
						"modes")//
						.field("modes")//
						.size(0)//
						.order(Terms.Order.term(true))// Tri alphabétique
				);

		// Min sale price
		requestBuilder.addAggregation(//
				AggregationBuilders.min(//
						"salePriceMin")//
						.field("sale.price")//
				);
		// Max sale price
		requestBuilder.addAggregation(//
				AggregationBuilders.max(//
						"salePriceMax")//
						.field("sale.price")//
				);
		// Min rent price
		requestBuilder.addAggregation(//
				AggregationBuilders.min(//
						"rentPriceMin")//
						.field("rent.price")//
				);
		// Max rent price
		requestBuilder.addAggregation(//
				AggregationBuilders.max(//
						"rentPriceMax")//
						.field("rent.price")//
				);
	}

	private BoolFilterBuilder computeFilters(SearchCriteria criteria) {
		BoolFilterBuilder boolFilter = FilterBuilders.boolFilter();

		Map<String, List<SearchCriteriaFilter>> orFilters = criteria.getFiltres().stream()
				.filter(f -> !Strings.isNullOrEmpty(f.getOrIdentifiant()))
				.collect(Collectors.groupingBy(SearchCriteriaFilter::getOrIdentifiant));

		List<SearchCriteriaFilter> mustFilters = criteria.getFiltres().stream()
				.filter(f -> Strings.isNullOrEmpty(f.getOrIdentifiant())).collect(Collectors.toList());

		for (Entry<String, List<SearchCriteriaFilter>> e : orFilters.entrySet()) {
			boolFilter.must(createFilterShould(e.getValue()));
		}

		// Filtre à inclure (MUST)
		for (SearchCriteriaFilter aFilter : mustFilters) {
			FilterBuilder fb = createFilter(aFilter);

			// ET entre les filtres
			if (fb != null) {
				boolFilter.must(fb);
			}
		}

		return boolFilter.hasClauses() ? boolFilter : null;
	}

	private QueryBuilder computeRequest(SearchCriteria criteria) {
		QueryBuilder queryMatch;
		// Si pas de critères de recherche => match_all
		if (criteria.getKeywords() == null || criteria.getKeywords().isEmpty() || "*".equals(criteria.getKeywords())) {
			queryMatch = QueryBuilders.matchAllQuery();
		} else {
			queryMatch = QueryBuilders.multiMatchQuery(criteria.getKeywords(), searchFields)//
					.operator(Operator.OR)//
					.minimumShouldMatch(minimumShouldMatch)//
					.zeroTermsQuery(ZeroTermsQuery.ALL)//
					.type("cross_fields");

		}

		return queryMatch;
	}

	private FilterBuilder createFilter(SearchCriteriaFilter unFiltre) {
		FilterBuilder fb = null;

		// Terms filter (cities, modes, categories)
		if (unFiltre instanceof SearchCriteriaFilterTerm) {
			SearchCriteriaFilterTerm f = (SearchCriteriaFilterTerm) unFiltre;
			fb = FilterBuilders.termsFilter(f.getMappingField(), f.getValeur());
		}
		// Range filter (prices)
		else if (unFiltre instanceof SearchCriteriaFilterRange) {
			SearchCriteriaFilterRange f = (SearchCriteriaFilterRange) unFiltre;
			fb = FilterBuilders.rangeFilter(f.getMappingField())//
					.from(f.getMin())//
					.to(f.getMax());
		}
		return fb;
	}

	private FilterBuilder createFilterShould(List<SearchCriteriaFilter> filters) {
		BoolFilterBuilder boolFilter = FilterBuilders.boolFilter();

		for (SearchCriteriaFilter aFilter : filters) {
			FilterBuilder fb = createFilter(aFilter);

			// OR between filters
			if (fb != null) {
				boolFilter.should(fb);
			}
		}

		return boolFilter;
	}

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

	private Map<String, List<SearchResultAggregation>> extractAggregations(Aggregations aggregations) {
		Map<String, List<SearchResultAggregation>> aggreationResult = new HashMap<>();
		for (Aggregation agg : aggregations) {
			// Converti le nom de l'aggréation en énumération

			List<SearchResultAggregation> buckets = new ArrayList<SearchResultAggregation>(0);

			// Aggregation de type term => extraction de toutes les valeurs
			if (agg instanceof StringTerms) {
				StringTerms st = (StringTerms) agg;
				// Iterate over terms values
				for (Terms.Bucket bucket : st.getBuckets()) {
					buckets.add(new SearchResultAggregation(bucket.getKey(), bucket.getDocCount()));
				}
			}
			// Aggregation de type Simple valeur (prix min et max par exemple)
			else if (agg instanceof SingleValue) {
				SingleValue im = (SingleValue) agg;
				buckets.add(new SearchResultAggregation(String.valueOf(im.value()), -1L));
			}
			aggreationResult.put(agg.getName(), buckets);
		}
		return aggreationResult;
	}

	@Override
	public void index(List<RealProperty> realProperty) {
		LOGGER.info("Indexing property {}", realProperty.stream().map(r -> r.getReference()));

		BulkRequestBuilder bulkRequest = client.prepareBulk();

		// Add each property to bulk request
		for (RealProperty r : realProperty) {
			List<String> modes = new ArrayList<>(2);

			SearchRealProperty sp = new SearchRealProperty();
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

				modes.add("Vente");
			}

			if (r.getRent() != null) {
				SearchRent searchRent = new SearchRent();
				searchRent.setMandateReference(r.getRent().getMandateReference());
				searchRent.setPrice(r.getRent().getPriceFinal());
				sp.setRent(searchRent);

				modes.add("Location");
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

			for (Owner owner : r.getOwners()) {
				List<String> l = new ArrayList<>(2);
				l.add(Strings.nullToEmpty(owner.getSurname()));
				l.add(Strings.nullToEmpty(owner.getFirstname()));
				String name = Joiner.on(" ").join(l);
				if (!Strings.isNullOrEmpty(name)) {
					SearchOwner o = new SearchOwner();
					o.setTechid(owner.getTechid());
					o.setName(name);
					sp.getOwners().add(o);
				}
			}

			sp.setModes(modes);

			bulkRequest.add(client.prepareIndex(indexName, propertyType).setSource(JSONUtils.toJson(sp)));
		}

		// Execute bulk
		BulkResponse response = bulkRequest.execute().actionGet();

		if (response.hasFailures()) {
			LOGGER.error("Indexing error");
			for (BulkItemResponse bulkItemResponse : response) {
				LOGGER.error(bulkItemResponse.getFailureMessage());
			}
		}
	}

	@Override
	public SearchResult search(SearchCriteria criteria) {

		// Create query
		QueryBuilder query = computeRequest(criteria);

		// Create filter
		BoolFilterBuilder boolFilter = computeFilters(criteria);

		SearchRequestBuilder requestBuilder = client.prepareSearch(indexName).setTypes(propertyType) //
				.setQuery(QueryBuilders.filteredQuery(query, boolFilter)) // Query
				.setFrom(criteria.getFrom()).setSize(criteria.getNbreResultatPage());

		// Add sort to request
		List<String> sortField = sortFields.get(criteria.getChampTri());
		if (sortField != null && !sortField.isEmpty()) {
			for (String s : sortField) {
				requestBuilder.addSort(s, SortOrder.valueOf(criteria.getOrder()));
			}
		}

		addAggregations(criteria, requestBuilder);

		LOGGER.debug(requestBuilder.toString());
		SearchResponse response = requestBuilder.execute().actionGet();
		LOGGER.debug(response.toString());

		// Extract results source
		List<SearchRealProperty> sources = new ArrayList<>();
		for (SearchHit hit : response.getHits()) {
			SearchRealProperty prp = JSONUtils.toObject(hit.getSourceAsString(), SearchRealProperty.class);
			// Update url according to caller
			SearchImage img = prp.getImage();
			if (img != null) {
				img.setUrl(MyUrlUtils.urlDocument(img.getUrl()));
			}
			sources.add(prp);
		}

		Map<String, List<SearchResultAggregation>> aggs;
		// Extract aggregation
		if (response.getAggregations() != null) {
			aggs = extractAggregations(response.getAggregations());
		} else {
			aggs = new HashMap<>(0);
		}

		return new SearchResult(response.getHits().totalHits(), sources, aggs);
	}
}
