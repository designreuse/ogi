package fr.jerep6.ogi.obj;

/**
 * Dimension for photos
 * 
 * @author jerep6
 */
public class PhotoDimension {
	public static final PhotoDimension	THUMB	= new PhotoDimension("thumb", 150, 100);
	public static final PhotoDimension	MEDIUM	= new PhotoDimension("medium", 800, 600);
	public static final PhotoDimension	BIG		= new PhotoDimension("big", 2000, 1000);

	/** Short name for dimention (thumb, big ...) */
	private final String				name;
	private final Integer				width;
	private final Integer				height;

	public PhotoDimension(Integer width, Integer height) {
		this(null, width, height);
	}

	public PhotoDimension(String name, Integer width, Integer height) {
		super();
		this.name = name;
		this.width = width;
		this.height = height;
	}

	public Integer getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public Integer getWidth() {
		return width;
	}

}