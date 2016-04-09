package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TA_PROPERTY_BUSINESS")
@PrimaryKeyJoinColumn(name = "PRO_ID")
// Lombok
@Getter
@Setter
public class RealPropertyBusiness extends RealPropertyBuilt {

	@Column(name = "PRB_WATER")
	private Boolean		water;

	@Column(name = "PRB_ELECTRICITY")
	private Boolean		electricity;

	

	public RealPropertyBusiness() {
		super(null, null, null);
	}
	
	public RealPropertyBusiness(String reference, Category category, fr.jerep6.ogi.persistance.bo.Type type) {
		super(reference, category, type);
	}

}
