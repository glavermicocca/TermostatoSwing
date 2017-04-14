package thermostat.bean;

public class Sensor {

	private Values values;
	private double currentUmidity;
	private double currentTemperature;
	
	public Sensor(Values values) {
		super();
		this.values = values;
	}
	
	public double getCurrentUmidity() {
		return currentUmidity;
	}

	public void setCurrentUmidity(String currentUmidity) {
		this.currentUmidity = Double.parseDouble(currentUmidity);
	}

	public double getCurrentTemperature() {
		return currentTemperature;
	}

	public void setCurrentTemperature(String currentTemperature) {
		this.currentTemperature = Double.parseDouble(currentTemperature);
	}

	public boolean isHigher() {
		return (currentTemperature < values.getTemperature());
	}
}
