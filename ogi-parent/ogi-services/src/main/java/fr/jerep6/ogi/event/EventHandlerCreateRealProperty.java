package fr.jerep6.ogi.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandlerCreateRealProperty implements ApplicationListener<EventCreateRealProperty> {

	@Override
	public void onApplicationEvent(EventCreateRealProperty event) {
		// System.out.println("CREATE");
	}

}
