package helperObjects;

public class HierarchyTopTenData {

	private String serviceId;
	private String serviceNumber;
	private String employeeId;
	private String companyEmployeeId;
	private String employeeFirstname;
	private String employeeLastname;
	private String departmentId;
	private String departmentName;
	private String type;
	private double value;
	

	public HierarchyTopTenData() { }
	
	// Constructor for Top Ten Employee Data
	public HierarchyTopTenData(String serviceId, String serviceNumber, String employeeId, String companyEmployeeId, String employeeFirstname, 
			String employeeLastname, String type, double value) {
		
		this.serviceId = serviceId;
		this.serviceNumber = serviceNumber;
		this.employeeId = employeeId;
		this.companyEmployeeId = companyEmployeeId;
		this.employeeFirstname = employeeFirstname;
		this.employeeLastname = employeeLastname;
		this.type = type;
		this.value = value;
		
	}
	
	// Constructor for Top Ten Department Data
	public HierarchyTopTenData(String serviceId, String serviceNumber, String departmentId, String departmentName, String type, double value) {
		
		this.serviceId = serviceId;
		this.serviceNumber = serviceNumber;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.type = type;
		this.value = value;
		
	}
	
	// Constructor for Top Ten Average Data
	public HierarchyTopTenData(String type, double value) {
		
		this.type = type;
		this.value = value;
		
	}
	
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceNumber() {
		return serviceNumber;
	}
	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getCompanyEmployeeId() {
		return companyEmployeeId;
	}
	public void setCompanyEmployeeId(String companyEmployeeId) {
		this.companyEmployeeId = companyEmployeeId;
	}
	public String getEmployeeFirstname() {
		return employeeFirstname;
	}
	public void setEmployeeFirstname(String employeeFirstname) {
		this.employeeFirstname = employeeFirstname;
	}
	public String getEmployeeLastname() {
		return employeeLastname;
	}
	public void setEmployeeLastname(String employeeLastname) {
		this.employeeLastname = employeeLastname;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
}