package fr.jerep6.ogi.service;

import java.util.List;
import java.util.Set;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.Room;

public interface ServiceRoom extends TransactionalService<Room, Integer> {
	List<Room> merge(List<Room> roomBD, List<Room> roomModif, Set<Document> documents);
}
