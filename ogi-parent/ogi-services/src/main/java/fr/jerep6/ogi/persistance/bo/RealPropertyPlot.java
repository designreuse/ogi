package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TA_PROPERTY_PLOT")
@PrimaryKeyJoinColumn(name = "PRO_ID")
// Lombok
@Getter
@Setter
public class RealPropertyPlot extends RealProperty {

	@Column(name = "PRP_BUILDING")
	private Boolean	building;

	// Viabilisé
	@Column(name = "PRP_SERVICED")
	private Boolean	serviced;

	@Column(name = "PRP_ZONE", length = 16)
	private String	zone;

	@Column(name = "PRP_FLOOR_AREA")
	private Float	floorArea;

	@Column(name = "PRP_FOOT_PRINT")
	private Float	footPrint;

	public RealPropertyPlot() {
		super(null, null, null);
	}

	public RealPropertyPlot(String reference, Category category, Type type) {
		super(reference, category, type);
	}

}
