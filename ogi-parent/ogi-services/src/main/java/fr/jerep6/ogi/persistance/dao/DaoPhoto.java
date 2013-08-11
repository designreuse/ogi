package fr.jerep6.ogi.persistance.dao;

import java.util.List;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Photo;

public interface DaoPhoto extends DaoCRUD<Photo, Integer> {

	List<Photo> getPhotos(String reference);

}
