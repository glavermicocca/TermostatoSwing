package thermostat.bean;

public class Values {
	private double temperature;
	private boolean toggleButton;
	
	public Values(double temperature, boolean toggleButton) {
		super();
		this.temperature = temperature;
		this.toggleButton = toggleButton;
	}

	public boolean isToggleButton() {
		return toggleButton;
	}

	public void setToggleButton(boolean toggleButton) {
		this.toggleButton = toggleButton;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	public void addOneDegree()
	{
		this.temperature = this.temperature + 0.5;
	}
	
	public void decrementOneDegree()
	{
		this.temperature = this.temperature - 0.5;
	}
	
}
