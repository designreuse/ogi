package fr.jerep6.ogi.transfert;

import lombok.Getter;
import lombok.ToString;

/**
 * Response for jQuery-File-Upload
 * 
 * @author jerep6
 */
@Getter
@ToString
public class FileUpload {
	public static class Builder {
		private String	name;
		private Long	size;
		private String	type;
		private String	url;
		private String	thumbnailUrl;
		private String	deleteUrl;
		private String	deleteType;

		public FileUpload build() {
			FileUpload fileUpload = new FileUpload();
			fileUpload.name = name;
			fileUpload.size = size;
			fileUpload.type = type;
			fileUpload.url = url;
			fileUpload.thumbnailUrl = thumbnailUrl;
			fileUpload.deleteUrl = deleteUrl;
			fileUpload.deleteType = deleteType;
			return fileUpload;
		}

		public Builder deleteType(String deleteType) {
			this.deleteType = deleteType;
			return this;
		}

		public Builder deleteUrl(String deleteUrl) {
			this.deleteUrl = deleteUrl;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder size(Long size) {
			this.size = size;
			return this;
		}

		public Builder thumbnailUrl(String thumbnailUrl) {
			this.thumbnailUrl = thumbnailUrl;
			return this;
		}

		public Builder type(String type) {
			this.type = type;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}
	}

	private String	name;
	private Long	size;
	/** Mime type */
	private String	type;
	private String	url;
	private String	thumbnailUrl;
	private String	deleteUrl;

	/** Type of HTTP method to delete file (DELETE, GET, POST ....) */
	private String	deleteType;
}
