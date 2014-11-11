package fr.jerep6.ogi.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

public class EventDeleteRealProperty extends ApplicationEvent {
	private static final long	serialVersionUID	= 2009962288411824102L;
	private List<String>		reference;

	public EventDeleteRealProperty(Object source, List<String> reference) {
		super(source);
		this.reference = reference;
	}

	public List<String> getReference() {
		return reference;
	}

}
