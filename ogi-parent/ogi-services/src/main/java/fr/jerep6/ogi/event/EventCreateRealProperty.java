package fr.jerep6.ogi.event;

import org.springframework.context.ApplicationEvent;

import fr.jerep6.ogi.persistance.bo.RealProperty;

public class EventCreateRealProperty extends ApplicationEvent {
	private static final long	serialVersionUID	= 2009962288411824102L;
	private RealProperty		property;

	public EventCreateRealProperty(Object source, RealProperty p) {
		super(source);
		property = p;
	}

	public RealProperty getProperty() {
		return property;
	}

}
