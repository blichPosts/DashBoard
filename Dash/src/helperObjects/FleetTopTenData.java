package helperObjects;

public class FleetTopTenData {

	private String serviceNumber;
	private double value;
	

	public FleetTopTenData() { }
	
	public FleetTopTenData(String serviceNumber, double value) {
		
		this.serviceNumber = serviceNumber;
		this.value = value;
		
	}
	
	
	public String getServiceNumber() {
		return serviceNumber;
	}
	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
}