package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.jerep6.ogi.persistance.bo.Photo;
import fr.jerep6.ogi.transfert.bean.PhotoTo;

@Service
// @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConverterPhoto extends CustomConverter<Photo, PhotoTo> {
	@Value("${photos.url}")
	private String	urlBasePhoto;

	@Override
	public PhotoTo convert(Photo source, Type<? extends PhotoTo> destinationType) {
		PhotoTo i = new PhotoTo();
		i.setTechid(source.getTechid());
		i.setOrder(source.getOrder());
		i.setUrl(urlBasePhoto + source.getPath());
		return i;
	}

}
