package fr.jerep6.ogi.transfert.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

// Jackson
// Pour l'héritage, indique que lors de la serialisation, le flux json contiendra une propriété mappingType indiquant le
// type de l'objet. @JsonSubTypes fait la correspondance entre les classes et les valeurs dans le json
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mappingType")
@JsonSubTypes({ @Type(name = "HSE", value = RealPropertyLivableTo.class),
		@Type(name = "APT", value = RealPropertyLivableTo.class) })
// lombok
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Classe non abstraite pour orika. Sinon, il faudrait déclarer un mapping pour chaque classe concrete
 * @author jerep6
 */
public class RealPropertyTo {
	private String							reference;
	private Set<String>						equipments		= new HashSet<>(0);
	private Integer							landArea;
	private Float							cos;
	private Boolean							housingEstate;
	private AddressTo						address;
	private Map<String, DescriptionTo>		descriptions	= new HashMap<>(0);
	private Set<RealPropertyDiagnosisTo>	diagnosis		= new HashSet<>(0);
	private CategoryTo						category;
	private String							type;
	private List<DocumentTo>				photos			= new ArrayList<>(0);
	private SaleTo							sale;

}