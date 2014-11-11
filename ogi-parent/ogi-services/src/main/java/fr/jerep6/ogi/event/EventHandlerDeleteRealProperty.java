package fr.jerep6.ogi.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.search.service.ServiceSearch;

@Component
public class EventHandlerDeleteRealProperty implements ApplicationListener<EventDeleteRealProperty> {

	@Autowired
	private ServiceSearch	serviceSearch;

	@Override
	public void onApplicationEvent(EventDeleteRealProperty event) {
		serviceSearch.delete(event.getReference());
	}

}
