package fr.jerep6.ogi.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
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

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorProperty;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
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
	public List<Room> merge(List<Room> roomBD, List<Room> roomModif, Set<Document> documents) {
		// Keep room to reuse it (avoid insert)
		List<Room> roomBDBackup = new ArrayList<>(roomBD);

		// clear rooms in database
		roomBD.clear();

		// Populate set of room.
		roomModif.forEach(aRoomModif -> {
			Room r = aRoomModif;

			int index = roomBDBackup.indexOf(r);
			if (index != -1) { // Room exist => modify existent
					// Get existant description to avoid sql insert
					r = roomBDBackup.get(index);
					mapper.map(aRoomModif, r);
				}
				// Photo must be an existing photo.
				final Document roomPhoto = r.getPhoto();
				Optional<Document> photo = documents.stream().filter(d -> roomPhoto != null && roomPhoto.equals(d))
						.findFirst();
				r.setPhoto(photo.orElse(null));

			validate(r);
				roomBD.add(r);
			});

		// Delete rooms
		SetView<Room> difference = Sets.difference(new HashSet<>(roomBDBackup), new HashSet<>(roomBD));
		remove(difference);

		return roomBD;
	}

	@Override
	public void validate(Room bo) throws BusinessException {
		if (bo == null) {
			return;
		}
		MultipleBusinessException mbe = new MultipleBusinessException();

		if (Strings.isNullOrEmpty(bo.getRoomType())) {
			mbe.add(EnumBusinessErrorProperty.NO_ROOM_TYPE);
		}

		mbe.checkErrors();
	}

}