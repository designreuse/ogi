package fr.jerep6.ogi.persistance.bo;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TA_VISIT")
// Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "techid" })
public class Visit {
	@Id
	@Column(name = "VIS_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				techid;

	@Column(name = "VIS_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar			date;

	@Column(name = "VIS_CLIENT")
	private String				client;

	@Column(name = "VIS_DESCRIPTION", length = 2048)
	private String				description;


	@ManyToOne
	@JoinColumn(name = "PRO_ID", nullable = false)
	private RealProperty	property;

}
