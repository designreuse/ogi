package fr.jerep6.ogi.persistance.bo.id;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import fr.jerep6.ogi.persistance.bo.Diagnosis;
import fr.jerep6.ogi.persistance.bo.RealProperty;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisRealPropertyId implements Serializable {
	@ManyToOne
	@JoinColumn(name = "PRP_ID")
	private RealProperty	property;

	@ManyToOne
	@JoinColumn(name = "DIA_ID")
	private Diagnosis		diagnosis;
}