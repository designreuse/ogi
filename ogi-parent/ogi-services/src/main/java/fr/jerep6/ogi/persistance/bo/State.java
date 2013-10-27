package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TR_STATE")
// Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "order" })
public class State {
	@Id
	@Column(name = "STA_ORDER", nullable = false, unique = true)
	private Integer	order;

	@Column(name = "STA_LABEL", nullable = false, unique = true, length = 255)
	private String	label;
}