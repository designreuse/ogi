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
		private String	url;
		private String	thumbnailUrl;
		private String	deleteUrl;
		private String	deleteType;

		public FileUpload build() {
			return new FileUpload(this);
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

		public Builder url(String url) {
			this.url = url;
			return this;
		}
	}

	private final String	name;
	private final Long		size;
	private final String	url;
	private final String	thumbnailUrl;
	private final String	deleteUrl;

	/** Type of HTTP method to delete file (DELETE, GET, POST ....) */
	private final String	deleteType;

	private FileUpload(Builder builder) {
		name = builder.name;
		size = builder.size;
		url = builder.url;
		thumbnailUrl = builder.thumbnailUrl;
		deleteUrl = builder.deleteUrl;
		deleteType = builder.deleteType;
	}
}
