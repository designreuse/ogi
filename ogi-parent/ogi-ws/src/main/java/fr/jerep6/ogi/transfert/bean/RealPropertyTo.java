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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Jackson
// Pour l'héritage, indique que lors de la serialisation, le flux json contiendra une propriété mappingType indiquant le
// type de l'objet. @JsonSubTypes fait la correspondance entre les classes et les valeurs dans le json
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mappingType")
@JsonSubTypes({ @Type(name = "liveable", value = RealPropertyLivableTo.class),
	@Type(name = "plot", value = RealPropertyPlotTo.class) })
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
	private Float							landArea;
	private Float							dependencyArea;
	private Float							cos;
	private Boolean							housingEstate;
	private AddressTo						address;
	private Map<String, DescriptionTo>		descriptions	= new HashMap<>(0);
	private Set<RealPropertyDiagnosisTo>	diagnosis		= new HashSet<>(0);
	private CategoryTo						category;
	private String							type;
	private List<DocumentTo>				photos			= new ArrayList<>(0);
	private SaleTo							sale;
	private RentTo							rent;
	private String							sanitation;

	// Owners. Only set by IHM to associate it with prp
	private List<Integer>					owners;
}