package fr.jerep6.ogi.transfert.mapping;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapEntry;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeBuilder;
import ma.glasnost.orika.metadata.TypeFactory;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import fr.jerep6.ogi.batch.bean.BatchReportJobInstance;
import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.transfert.ErrorTo;
import fr.jerep6.ogi.framework.transfert.ExceptionTo;
import fr.jerep6.ogi.framework.utils.ContextUtils;
import fr.jerep6.ogi.framework.utils.ExceptionUtils;
import fr.jerep6.ogi.framework.utils.UrlUtils;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.DPE;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.Label;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyDiagnosis;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.RealPropertyPlot;
import fr.jerep6.ogi.persistance.bo.Rent;
import fr.jerep6.ogi.persistance.bo.Room;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.persistance.bo.State;
import fr.jerep6.ogi.rest.batch.transfert.BatchReportInstanceTo;
import fr.jerep6.ogi.rest.batch.transfert.BatchReportJobExecutionTo;
import fr.jerep6.ogi.rest.batch.transfert.BatchReportStepExecutionTo;
import fr.jerep6.ogi.rest.search.transfert.SearchResultTo;
import fr.jerep6.ogi.search.model.SearchResult;
import fr.jerep6.ogi.service.ServiceUrl;
import fr.jerep6.ogi.transfert.FileUpload;
import fr.jerep6.ogi.transfert.ListResult;
import fr.jerep6.ogi.transfert.PartnerPropertyCount;
import fr.jerep6.ogi.transfert.bean.AddressTo;
import fr.jerep6.ogi.transfert.bean.CategoryTo;
import fr.jerep6.ogi.transfert.bean.DPETo;
import fr.jerep6.ogi.transfert.bean.DescriptionTo;
import fr.jerep6.ogi.transfert.bean.DocumentTo;
import fr.jerep6.ogi.transfert.bean.FileUploadTo;
import fr.jerep6.ogi.transfert.bean.LabelTo;
import fr.jerep6.ogi.transfert.bean.OwnerTo;
import fr.jerep6.ogi.transfert.bean.PartnerPropertyCountTo;
import fr.jerep6.ogi.transfert.bean.PartnerRequestTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyDiagnosisTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyLinkTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyLivableTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyPlotTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyTo;
import fr.jerep6.ogi.transfert.bean.RentTo;
import fr.jerep6.ogi.transfert.bean.RoomTo;
import fr.jerep6.ogi.transfert.bean.SaleTo;
import fr.jerep6.ogi.transfert.bean.StateTo;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumBatchExitStatus;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumBatchStatus;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumCategory;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumDescriptionType;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumLabelType;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumMandateType;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumOrientation;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumPartner;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumPartnerRequestType;
import fr.jerep6.ogi.utils.DocumentUtils;

@Component("orikaMapper")
public class OrikaMapper extends ConfigurableMapper {
	@Autowired
	private ServiceUrl					serviceUrl;

	@Resource(name = "mapPartnersMaxAllowed")
	private Map<EnumPartner, String>	partnerMaxAllowed;

	private MapperFactory				factory;

	private void batchMapping() {

		factory.classMap(BatchReportJobInstance.class, BatchReportInstanceTo.class)//
		.field("jobInstance.jobName", "jobName")//
		.field("jobExecutions", "jobExecutions")//
		.field("success", "isSuccess")//
		.register();

		factory.classMap(JobExecution.class, BatchReportJobExecutionTo.class)//
		.field("startTime", "startTime")//
		.field("endTime", "endTime")//
		.field("status", "status")//
		.field("exitStatus", "exitStatus")//
		.field("stepExecutions", "steps")//
		.register();

		factory.classMap(StepExecution.class, BatchReportStepExecutionTo.class)//
		.field("stepName", "stepName")//
		.field("status", "status")//
		.field("exitStatus", "exitStatus")//
		.field("readCount", "readCount")//
		.field("writeCount", "writeCount")//
		.field("commitCount", "commitCount")//
		.field("rollbackCount", "rollbackCount")//
		.field("readSkipCount", "readSkipCount")//
		.field("processSkipCount", "processSkipCount")//
		.field("writeSkipCount", "writeSkipCount")//
		.register();
	}

	@Override
	protected void configure(MapperFactory factory) {
		super.configure(factory);
		this.factory = factory;

		// Init converter here because from 1.4.5 orika throw exception when this code is not in configure method
		// Caused by: java.lang.IllegalStateException: Cannot register converters after MapperFacade has been
		// initialized at
		// ma.glasnost.orika.converter.DefaultConverterFactory.registerConverter(DefaultConverterFactory.java:192)
		ConverterFactory converterFactory = factory.getConverterFactory();

		// Specifics converter (instantiate and copy properties)
		converterFactory.registerConverter(new ConverterEnumCategory());
		converterFactory.registerConverter(new ConverterEnumDescriptionType());
		converterFactory.registerConverter(new ConverterEnumOrientation());
		converterFactory.registerConverter(new ConverterEnumMandateType());
		converterFactory.registerConverter(new ConverterEnumLabelType());
		converterFactory.registerConverter(new ConverterEnumPartner());
		converterFactory.registerConverter(new ConverterEnumPartnerRequestType());

		converterFactory.registerConverter(new ConverterEnumBatchStatus());
		converterFactory.registerConverter(new ConverterEnumBatchExitStatus());
	}

	/** Mapping for exception */
	private void exceptionMapping() {
		factory.classMap(BusinessException.class, ErrorTo.class)//
		.customize(new CustomMapper<BusinessException, ErrorTo>() {
			@Override
			public void mapAtoB(BusinessException a, ErrorTo b, MappingContext context) {
				String msg = a.getMessage() == null ? "" : ExceptionUtils.i18n(a);
				b.setMessage(msg);
			}

			@Override
			public void mapBtoA(ErrorTo b, BusinessException a, MappingContext context) {}
		})//
		.byDefault().register();

		factory.classMap(Exception.class, ErrorTo.class)//
		.customize(new CustomMapper<Exception, ErrorTo>() {
			@Override
			public void mapAtoB(Exception a, ErrorTo b, MappingContext context) {
				String msg = a.getMessage() == null ? "" : ExceptionUtils.i18n(a);
				b.setMessage(msg);

				// Exception other than business exception don't have code
				b.setCode("TECH");
			}

			@Override
			public void mapBtoA(ErrorTo b, Exception a, MappingContext context) {}
		})//
		.byDefault().register();

		factory.classMap(Exception.class, ExceptionTo.class)//
		.customize(new CustomMapper<Exception, ExceptionTo>() {

			@Override
			public void mapAtoB(Exception a, ExceptionTo b, MappingContext context) {
				b.add(map(a, ErrorTo.class));
			}

			@Override
			public void mapBtoA(ExceptionTo b, Exception a, MappingContext context) {}
		})//
		.register();

		factory.classMap(MultipleBusinessException.class, ExceptionTo.class)//
		.field("exceptions", "errors")//
		.register();
	}

	/**
	 * La méthode configure est appelée lors de la construction de l'objet et les injections spring ne sont pas encore
	 * réalisées
	 */
	@PostConstruct
	private void postConstruct() {

		exceptionMapping();
		batchMapping();

		// Specifics factory (create object)
		factory.registerObjectFactory(new FactoryRealPropertyTo(), TypeFactory.valueOf(RealPropertyTo.class));
		factory.registerObjectFactory(new FactoryRealProperty(), TypeFactory.valueOf(RealProperty.class));

		// Mapping definition
		factory.classMap(ListResult.class, ListResult.class).byDefault().register();

		factory.classMap(Category.class, CategoryTo.class).byDefault().register();
		factory.classMap(Address.class, AddressTo.class).byDefault().register();
		factory.classMap(Description.class, DescriptionTo.class).byDefault().register();
		factory.classMap(RealPropertyDiagnosis.class, RealPropertyDiagnosisTo.class)
				.field("diagnosis.label", "diagnosis").byDefault().register();
		factory.classMap(Room.class, RoomTo.class).byDefault().register();
		factory.classMap(Sale.class, SaleTo.class).byDefault().register();
		factory.classMap(Rent.class, RentTo.class).byDefault().register();
		factory.classMap(Label.class, LabelTo.class).byDefault().register();
		factory.classMap(State.class, StateTo.class).byDefault().register();
		factory.classMap(Owner.class, OwnerTo.class).byDefault().register();
		factory.classMap(DPE.class, DPETo.class).byDefault().register();
		factory.classMap(SearchResult.class, SearchResultTo.class).byDefault().register();
		factory.classMap(RealProperty.class, RealPropertyLinkTo.class)//
				.customize(new CustomMapper<RealProperty, RealPropertyLinkTo>() {
					@Override
					public void mapAtoB(RealProperty a, RealPropertyLinkTo b, MappingContext context) {
						b.setUrl(serviceUrl.urlProperty(a.getReference()));
					}

					@Override
					public void mapBtoA(RealPropertyLinkTo b, RealProperty a, MappingContext context) {}
				})//
				.fieldAToB("reference", "reference").register();

		factory.classMap(Document.class, DocumentTo.class)//
				.customize(new CustomMapper<Document, DocumentTo>() {
					@Override
					public void mapAtoB(Document a, DocumentTo b, MappingContext context) {
						b.setUrl(serviceUrl.urlDocument(a.getPath()));
					}

					@Override
					public void mapBtoA(DocumentTo b, Document a, MappingContext context) {
						String urlWithoutParam = UrlUtils.deleteQueryParams(b.getUrl());
						a.setPath(DocumentUtils.relativePathFromUrl(urlWithoutParam).toString());
					}
				})//
				.byDefault().register();
		factory.classMap(FileUpload.class, FileUploadTo.class).byDefault().register();

		factory.classMap(RealProperty.class, RealPropertyTo.class)//
				.field("type.label", "type") //
				// .field("equipments{label}", "equipments{}")//
				.field("diagnosisProperty", "diagnosis")//
				// must exclude the field else classcast exception. It doesn't detected that description is mapped as
				// map bellow
				.exclude("descriptions")//
				.field("descriptions{type}", "descriptions{key}")//
				.field("descriptions{}", "descriptions{value}")//
				.fieldAToB("photos", "photos")//
				.fieldBToA("photos", "documents")//
				.field("owners{techid}", "owners{}")//
				.byDefault()//
				.register();

		factory.classMap(RealPropertyLivable.class, RealPropertyLivableTo.class) //
				.use(RealProperty.class, RealPropertyTo.class) //
				.exclude("descriptions")//
				.exclude("type")// Need to exclude type because "byDefault" override parent behaviour
				.exclude("owners")//
				.byDefault()//
				.register();

		factory.classMap(RealPropertyPlot.class, RealPropertyPlotTo.class) //
				.use(RealProperty.class, RealPropertyTo.class) //
				.exclude("descriptions")//
				.exclude("type") //
				.exclude("owners")//
				.byDefault()//
				.register();

		Type<Map<String, List<PartnerRequest>>> mapType = new TypeBuilder<Map<String, List<PartnerRequest>>>() {}
				.build();
		Type<MapEntry<String, List<PartnerRequest>>> entryType = MapEntry.concreteEntryType(mapType);

		Type<Map<String, List<PartnerRequestTo>>> mapTypeDest = new TypeBuilder<Map<String, List<PartnerRequestTo>>>() {}
				.build();
		Type<MapEntry<String, List<PartnerRequestTo>>> entryTypeDest = MapEntry.concreteEntryType(mapTypeDest);

		factory.classMap(entryType, entryTypeDest)
		//
				.customize(
						new CustomMapper<MapEntry<String, List<PartnerRequest>>, MapEntry<String, List<PartnerRequestTo>>>() {

							@Override
							public void mapAtoB(MapEntry<String, List<PartnerRequest>> a,
									MapEntry<String, List<PartnerRequestTo>> b, MappingContext context) {
								// TODO Auto-generated method stub
								super.mapAtoB(a, b, context);
							}

							@Override
							public void mapBtoA(MapEntry<String, List<PartnerRequestTo>> b,
									MapEntry<String, List<PartnerRequest>> a, MappingContext context) {
								// TODO Auto-generated method stub
								super.mapBtoA(b, a, context);
							}

						})//
				.byDefault().register();

		factory.classMap(PartnerRequest.class, PartnerRequestTo.class)//
		.customize(new CustomMapper<PartnerRequest, PartnerRequestTo>() {
			@Override
			public void mapAtoB(PartnerRequest a, PartnerRequestTo b, MappingContext context) {
				b.setLabel(ContextUtils.getMessage("partner.request." + a.getRequestType().getCode()));
			}

			@Override
			public void mapBtoA(PartnerRequestTo b, PartnerRequest a, MappingContext context) {}
		})//
		.byDefault().register();

		factory.classMap(PartnerPropertyCount.class, PartnerPropertyCountTo.class)//
				.byDefault()//
				.customize(new CustomMapper<PartnerPropertyCount, PartnerPropertyCountTo>() {
					@Override
					public void mapAtoB(PartnerPropertyCount a, PartnerPropertyCountTo b, MappingContext context) {
						String smax = partnerMaxAllowed.get(a.getPartner());
						if (!Strings.isNullOrEmpty(smax)) {
							b.setMaxAllowed(Long.valueOf(smax));
						}
					}

					@Override
					public void mapBtoA(PartnerPropertyCountTo b, PartnerPropertyCount a, MappingContext context) {}
				})//
				.register();
	}
}
