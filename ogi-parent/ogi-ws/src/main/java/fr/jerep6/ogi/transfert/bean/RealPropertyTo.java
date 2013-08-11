package fr.jerep6.ogi.transfert.bean;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

// Jackson
// Pour l'héritage, indique que lors de la serialisation, le flux json contiendra une propriété @type indiquant le type
// de l'objet. @JsonSubTypes fait la correspondance entre les classes et les valeurs dans le json
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({ @Type(name = "HSE", value = RealPropertyLivableTo.class),
		@Type(name = "APT", value = RealPropertyLivableTo.class) })
// lombok
@Getter
@Setter
@ToString
public class RealPropertyTo {
	private String							reference;
	private Set<String>						equipments;
	private Integer							landArea;
	private Float							cos;
	private Boolean							housingEstate;
	private AddressTo						address;
	private Set<DescriptionTo>				descriptions;
	private Set<RealPropertyDiagnosisTo>	diagnosis;
	private CategoryTo						category;
	private String							type;
	private List<PhotoTo>					photos;
}