package fr.jerep6.ogi.service;

import java.util.Set;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.Room;

public interface ServiceRoom extends TransactionalService<Room, Integer> {
	Set<Room> merge(Set<Room> roomBD, Set<Room> roomModif, Set<Document> documents);
}
