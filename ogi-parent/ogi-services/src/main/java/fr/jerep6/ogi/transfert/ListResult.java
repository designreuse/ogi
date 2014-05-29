package fr.jerep6.ogi.transfert;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListResult<T> {

	/** Result of query. Item match criteria */
	private Collection<T>	items;

	/** Total of items without criteria. For pagination */
	private Integer			total;

}
