package fr.jerep6.ogi.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.search.persistance.DaoSearch;

@Component
public class EventHandlerUpdateRealProperty implements ApplicationListener<EventUpdateRealProperty> {

	@Autowired
	private DaoSearch	daoSearch;

	@Override
	public void onApplicationEvent(EventUpdateRealProperty event) {
		daoSearch.index(event.getProperty());
	}

}
