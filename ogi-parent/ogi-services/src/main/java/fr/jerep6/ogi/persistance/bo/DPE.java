package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

// The @Embeddable annotation allows to specify a class whose instances are stored as intrinsic part of the owning
// entity.
@Embeddable
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "kwh", "ges" })
public class DPE {
	private Integer	kwh;
	private String	classificationKWh;
	private Integer	ges;
	private String	classificationGes;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("kWh", kwh).add("classificationKWh", classificationKWh).add("ges", ges)
				.add("classificationGes", classificationGes).toString();
	}

}