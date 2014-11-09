package fr.jerep6.ogi.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandlerUpdateRealProperty implements ApplicationListener<EventUpdateRealProperty> {

	@Override
	public void onApplicationEvent(EventUpdateRealProperty event) {
		// System.out.println("UPDATE");
	}

}
