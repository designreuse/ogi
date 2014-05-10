package fr.jerep6.ogi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.Room;
import fr.jerep6.ogi.persistance.dao.DaoRoom;
import fr.jerep6.ogi.service.ServiceRoom;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceRoom")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceRoomImpl extends AbstractTransactionalService<Room, Integer> implements ServiceRoom {
	private static Logger		LOGGER	= LoggerFactory.getLogger(ServiceRoomImpl.class);

	@Autowired
	private DaoRoom				daoRoom;

	@Autowired
	private OrikaMapperService	mapper;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoRoom);
	}

	@Override
	public Set<Room> merge(Set<Room> roomBD, Set<Room> roomModif, Set<Document> documents) {
		// Keep room to reuse it (avoid insert)
		List<Room> roomBDBackup = new ArrayList<>(roomBD);

		// clear rooms in database
		roomBD.clear();

		// Populate set of room.
		roomModif.stream().forEach(aRoomModif -> {
			Room r = aRoomModif;

			int index = roomBDBackup.indexOf(r);
			if (index != -1) { // If room is new => map it
				// Get existant description to avoid sql insert
				r = roomBDBackup.get(index);
				mapper.map(aRoomModif, r);
			}
			// Photo must be an existing photo.
			final Document finalPhotoRoom = r.getPhoto();
			Optional<Document> photo = documents.stream()
						.filter(d -> finalPhotoRoom != null && finalPhotoRoom.getPath().equals(d.getPath()))
					.findFirst();
			r.setPhoto(photo.orElse(null));
			roomBD.add(r);
		});

		// Delete room
		// roomBDBackup.removeAll(new ArrayList<>(roomBD));
		// remove(roomBDBackup);

		return roomBD;
	}
}