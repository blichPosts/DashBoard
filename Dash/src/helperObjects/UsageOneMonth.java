package helperObjects;

public class UsageOneMonth {

	private String vendorName;
	private String ordinalYear;
	private String ordinalMonth;
	private String invoiceMonth;
	private String domesticVoice;
	private String domesticOverageVoice;
	private String domesticMessages;
	private String domesticDataUsageKb;
	private String roamingVoice;
	private String roamingDataUsageKb;
	private String roamingMessages; 
	
	
	// this constructor will be used to create and initialize the months that are missing on the file for the vendor selected 
	public UsageOneMonth(String vendorName, String ordinalYear, String ordinalMonth){
		
		this.vendorName = vendorName;
		this.ordinalYear = ordinalYear;
		this.ordinalMonth = ordinalMonth;
		this.invoiceMonth = "";
		this.domesticVoice = "0";
		this.domesticOverageVoice = "0";
		this.domesticMessages = "0";
		this.domesticDataUsageKb = "0";
		this.roamingVoice = "0";
		this.roamingDataUsageKb = "0";
		this.roamingMessages = "0"; 
		
	}
	
	
	public UsageOneMonth() {
		// TODO Auto-generated constructor stub
	}


	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getOrdinalYear() {
		return ordinalYear;
	}
	public void setOrdinalYear(String ordinalYear) {
		this.ordinalYear = ordinalYear;
	}
	public String getOrdinalMonth() {
		return ordinalMonth;
	}
	public void setOrdinalMonth(String ordinalMonth) {
		this.ordinalMonth = ordinalMonth;
	}
	public String getInvoiceMonth() {
		return invoiceMonth;
	}
	public void setInvoiceMonth(String invoiceMonth) {
		this.invoiceMonth = invoiceMonth;
	}
	public String getDomesticVoice() {
		return domesticVoice;
	}
	public void setDomesticVoice(String domesticVoice) {
		this.domesticVoice = domesticVoice;
	}
	public String getDomesticOverageVoice() {
		return domesticOverageVoice;
	}
	public void setDomesticOverageVoice(String domesticOverageVoice) {
		this.domesticOverageVoice = domesticOverageVoice;
	}
	public String getDomesticMessages() {
		return domesticMessages;
	}
	public void setDomesticMessages(String domesticMessages) {
		this.domesticMessages = domesticMessages;
	}
	public String getDomesticDataUsageKb() {
		return domesticDataUsageKb;
	}
	public void setDomesticDataUsageKb(String domesticDataUsageKb) {
		this.domesticDataUsageKb = domesticDataUsageKb;
	}
	public String getRoamingVoice() {
		return roamingVoice;
	}
	public void setRoamingVoice(String roamingVoice) {
		this.roamingVoice = roamingVoice;
	}
	public String getRoamingDataUsageKb() {
		return roamingDataUsageKb;
	}
	public void setRoamingDataUsageKb(String roamingDataUsageKb) {
		this.roamingDataUsageKb = roamingDataUsageKb;
	}
	public String getRoamingMessages() {
		return roamingMessages;
	}
	public void setRoamingMessages(String roamingMessages) {
		this.roamingMessages = roamingMessages;
	}

}
