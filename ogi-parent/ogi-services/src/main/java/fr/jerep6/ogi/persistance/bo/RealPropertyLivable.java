package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TA_PROPERTY_LIVABLE")
@PrimaryKeyJoinColumn(name = "PRO_ID")
// Lombok
@Getter
@Setter
public class RealPropertyLivable extends RealPropertyBuilt {

	@Column(name = "PRL_NB_ROOM")
	private Integer	nbRoom;

	@Column(name = "PRL_NB_BEDROOM")
	private Integer	nbBedRoom;

	@Column(name = "PRL_NB_BATHROOM")
	private Integer	nbBathRoom;

	@Column(name = "PRL_NB_SHOWERROOM")
	private Integer	nbShowerRoom;

	@Column(name = "PRL_NB_WC")
	private Integer	nbWC;

	@Column(name = "PRL_HEATING")
	private String	heating;

	@Column(name = "PRL_HOT_WATER")
	private String	hotWater;

	public RealPropertyLivable() {
		super(null, null, null);
	}

	public RealPropertyLivable(String reference, Category category, Type type) {
		super(reference, category, type);
	}

}
