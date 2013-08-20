package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.persistance.bo.Photo;
import fr.jerep6.ogi.transfert.bean.PhotoTo;

public class ConverterPhoto extends CustomConverter<Photo, PhotoTo> {
	private final String	urlBasePhoto;

	public ConverterPhoto(String urlBasePhoto) {
		super();
		this.urlBasePhoto = urlBasePhoto;
	}

	@Override
	public PhotoTo convert(Photo source, Type<? extends PhotoTo> destinationType) {
		PhotoTo i = new PhotoTo();
		i.setTechid(source.getTechid());
		i.setOrder(source.getOrder());
		i.setUrl(urlBasePhoto + source.getPath());
		return i;
	}

}
