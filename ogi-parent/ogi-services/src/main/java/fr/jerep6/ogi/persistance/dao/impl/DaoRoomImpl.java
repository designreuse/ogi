package fr.jerep6.ogi.persistance.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Room;
import fr.jerep6.ogi.persistance.dao.DaoRoom;

@Repository("daoRoom")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoRoomImpl extends AbstractDao<Room, Integer> implements DaoRoom {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoRoomImpl.class);
}
