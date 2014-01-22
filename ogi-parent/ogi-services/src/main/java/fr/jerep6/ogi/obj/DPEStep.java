package fr.jerep6.ogi.obj;

/**
 * A step is a dpe class. A, B, C ...
 * 
 * @author jerep6 20 janv. 2014
 */
public class DPEStep {

	/** Numéro du step (1 = lettre A, 2= lettre B ...) */
	private Integer	num;
	/** Minimal value for the step */
	private Integer	min;
	/** Nombre de valeurs que compte la classe du dpe */
	private Integer	plage;

	public DPEStep(Integer num, Integer min, Integer plage) {
		super();
		this.num = num;
		this.min = min;
		this.plage = plage;
	}

	public Integer getMin() {
		return min;
	}

	public Integer getNum() {
		return num;
	}

	public Integer getPlage() {
		return plage;
	}

	@Override
	public String toString() {
		return "DPEStep [num=" + num + ", min=" + min + ", plage=" + plage + "]";
	}

}
