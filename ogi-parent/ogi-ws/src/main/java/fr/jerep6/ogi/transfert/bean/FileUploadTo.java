package fr.jerep6.ogi.transfert.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Response for jQuery-File-Upload
 * 
 * @author jerep6
 */
@Getter
@Setter
@ToString
public class FileUploadTo {
	private String		name;
	private Long		size;
	/** Mime type */
	private String		type;
	private String		url;
	private String		thumbnailUrl;

	private String		deleteUrl;

	/** Type of HTTP method to delete file (DELETE, GET, POST ....) */
	private String		deleteType;

	/** Temporary document corresponding to upload file */
	private DocumentTo	document;

}
