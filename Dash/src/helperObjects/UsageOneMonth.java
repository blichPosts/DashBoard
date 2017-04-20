package helperObjects;

public class UsageOneMonth {

	private String vendorName;
	private String ordinalYear;
	private String ordinalMonth;
	private String invoiceMonth;
	
	// Usage data
	private String domesticVoice;
	private String domesticOverageVoice;
	private String domesticMessages;
	private String domesticDataUsageKb;
	private String roamingVoice;
	private String roamingDataUsageKb;
	private String roamingMessages; 
	
	// Expenses data
	private String numberOfInvoices;
	private String numberOfLines;
	private String totalSubscriberCharges;
	private String voiceCharges;
	private String dataCharges;
	private String messagesCharges;
	private String equipmentCharges; 
	private String taxCharges;
	private String roamingMsgCharges;
	private String roamingDataCharges;
	private String roamingVoiceCharges;
	private String totalAccountLevelCharges;
	private String roamingCharges;
	private String otherCharges;
	private String totalCharge;
	
	
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
		
		this.numberOfInvoices = "0";
		this.numberOfLines = "0";
		this.totalSubscriberCharges = "0";
		this.voiceCharges = "0";
		this.dataCharges = "0";
		this.messagesCharges = "0";
		this.equipmentCharges = "0"; 
		this.taxCharges = "0";
		this.roamingMsgCharges = "0";
		this.roamingDataCharges = "0";
		this.roamingVoiceCharges = "0";
		this.totalAccountLevelCharges = "0";
		this.roamingCharges = "0";
		this.otherCharges = "0";
		this.totalCharge = "0";
		
	}
	
	public UsageOneMonth() {}

	
	public String formatInvoiceMonth(String originalInvoiceMonth) {
		
		if (originalInvoiceMonth.contains(" ")) {
			
			String tempInvMonth = originalInvoiceMonth.split(" ")[0];
			
			String year = tempInvMonth.split("-")[0];
			String month = tempInvMonth.split("-")[1];
			String day = tempInvMonth.split("-")[2];
			
			if (month.startsWith("0"))
				month = month.replace("0", "");
			
			return month + "/" + day + "/" + year; 
			
		} else {
			
			return originalInvoiceMonth;
			
		}
				
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
	public String getNumberOfInvoices() {
		return numberOfInvoices;
	}
	public void setNumberOfInvoices(String numberOfInvoices) {
		this.numberOfInvoices = numberOfInvoices;
	}
	public String getNumberOfLines() {
		return numberOfLines;
	}
	public void setNumberOfLines(String numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	public String getTotalSubscriberCharges() {
		return totalSubscriberCharges;
	}
	public void setTotalSubscriberCharges(String totalSubscriberCharges) {
		this.totalSubscriberCharges = totalSubscriberCharges;
	}
	public String getVoiceCharges() {
		return voiceCharges;
	}
	public void setVoiceCharges(String voiceCharges) {
		this.voiceCharges = voiceCharges;
	}
	public String getDataCharges() {
		return dataCharges;
	}
	public void setDataCharges(String dataCharges) {
		this.dataCharges = dataCharges;
	}
	public String getMessagesCharges() {
		return messagesCharges;
	}
	public void setMessagesCharges(String messagesCharges) {
		this.messagesCharges = messagesCharges;
	}
	public String getEquipmentCharges() {
		return equipmentCharges;
	}
	public void setEquipmentCharges(String equipmentCharges) {
		this.equipmentCharges = equipmentCharges;
	}
	public String getTaxCharges() {
		return taxCharges;
	}
	public void setTaxCharges(String taxCharges) {
		this.taxCharges = taxCharges;
	}
	public String getRoamingMsgCharges() {
		return roamingMsgCharges;
	}
	public void setRoamingMsgCharges(String roamingMsgCharges) {
		this.roamingMsgCharges = roamingMsgCharges;
	}
	public String getRoamingDataCharges() {
		return roamingDataCharges;
	}
	public void setRoamingDataCharges(String roamingDataCharges) {
		this.roamingDataCharges = roamingDataCharges;
	}
	public String getRoamingVoiceCharges() {
		return roamingVoiceCharges;
	}
	public void setRoamingVoiceCharges(String roamingVoiceCharges) {
		this.roamingVoiceCharges = roamingVoiceCharges;
	}
	public String getTotalAccountLevelCharges() {
		return totalAccountLevelCharges;
	}
	public void setTotalAccountLevelCharges(String totalAccountLevelCharges) {
		this.totalAccountLevelCharges = totalAccountLevelCharges;
	}
	public String getRoamingCharges() {
		return roamingCharges;
	}
	public void setRoamingCharges(String roamingCharges) {
		this.roamingCharges = roamingCharges;
	}
	public String getOtherCharges() {
		return otherCharges;
	}
	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
	}
	public String getTotalCharge() {
		return totalCharge;
	}
	public void setTotalCharge(String totalCharge) {
		this.totalCharge = totalCharge;
	}
	
}
