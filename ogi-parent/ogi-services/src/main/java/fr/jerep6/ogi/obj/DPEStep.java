package fr.jerep6.ogi.obj;

public class DPEStep {

	private Integer	num;
	private Integer	min;
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
