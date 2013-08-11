package fr.jerep6.ogi.persistance.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

@Entity
@Table(name = "TA_PHOTO")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "path" })
public class Photo {
	@Id
	@Column(name = "PHO_ID", unique = true, nullable = false)
	private Integer			techid;

	@Column(name = "PHO_PATH", nullable = false)
	private String			path;

	@Column(name = "PHO_ORDER", nullable = false)
	private Integer			order;

	@ManyToOne
	@JoinColumn(name = "PRO_ID", nullable = false)
	private RealProperty	property;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("order", order).toString();
	}

}