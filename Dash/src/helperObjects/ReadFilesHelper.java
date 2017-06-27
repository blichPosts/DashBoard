package helperObjects;

import java.awt.AWTException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;

import Dash.BaseClass;


public class ReadFilesHelper extends BaseClass {


	static JavascriptExecutor js = (JavascriptExecutor)driver;
	
	
	// After this call is made, the data displayed on the UI can be obtained by an AJAX call
	public static void startCollectingData() {
		
		js.executeScript("__TANGOE__setShouldCaptureTestData(true)");
		
	}

	// Reloads the fleet data 
	public static void reloadFleetData() {
		
		js.executeScript("__TANGOE__reloadFleetData()");
		
	}
	

	// Get the data for Expenses and Usage pages - Fleet Dashboard
	public static List<UsageOneMonth> getJsonDataExpenseUsage(String vendor) throws Exception{
		
		UsageOneMonth usageOneMonth = new UsageOneMonth();
		List<UsageOneMonth> listValues = new ArrayList<UsageOneMonth>();
	    
		String usageExpenseData = (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('fleet.expenseUsage.payload.rows')");
				
	    // ShowText(usageExpenseData);
	   
	    // Convert the String with data into a JSONArray
	    JSONArray array = new JSONArray(usageExpenseData); 

	    for (int i = 0; i < array.length(); i++){
	    	
	    	JSONObject jsonObj  = array.getJSONObject(i);
	    	usageOneMonth = new UsageOneMonth();
	        
	    	if (jsonObj.getString("vendor_name").equals(vendor)) {
	    		
	    		usageOneMonth.setVendorName(jsonObj.getString("vendor_name"));
	    		usageOneMonth.setCountry(jsonObj.getString("country"));
	    		usageOneMonth.setInvoiceMonth(usageOneMonth.formatInvoiceMonth(jsonObj.getString("invoice_month")));
				usageOneMonth.setOrdinalYear(Integer.toString(jsonObj.getInt("ordinal_year")));
				usageOneMonth.setOrdinalMonth(Integer.toString(jsonObj.getInt("ordinal_month")));
				
				usageOneMonth.setDomesticVoice(Double.toString(jsonObj.getDouble("domestic_mou")));
				usageOneMonth.setDomesticOverageVoice(Double.toString(jsonObj.getDouble("domestic_overage_mou")));
				usageOneMonth.setDomesticMessages(Double.toString(jsonObj.getDouble("domestic_messages")));
				usageOneMonth.setDomesticDataUsageKb(Double.toString(jsonObj.getDouble("domestic_data_usage_kb")));
				usageOneMonth.setRoamingVoice(Double.toString(jsonObj.getDouble("roaming_mou")));
				usageOneMonth.setRoamingDataUsageKb(Double.toString(jsonObj.getDouble("roaming_data_usage_kb")));
				usageOneMonth.setRoamingMessages(Double.toString(jsonObj.getDouble("roaming_messages")));
				
				usageOneMonth.setNumberOfInvoices(Long.toString(jsonObj.getLong("no_of_invoices")));
				usageOneMonth.setNumberOfLines(Long.toString(jsonObj.getLong("no_of_lines")));
				usageOneMonth.setTotalSubscriberCharges(Double.toString(jsonObj.getDouble("total_subscriber_charges_ex")));
				usageOneMonth.setVoiceCharges(Double.toString(jsonObj.getDouble("voice_charges_ex")));
				usageOneMonth.setDataCharges(Double.toString(jsonObj.getDouble("data_charges_ex")));
				usageOneMonth.setMessagesCharges(Double.toString(jsonObj.getDouble("messaging_charges_ex")));
				usageOneMonth.setEquipmentCharges(Double.toString(jsonObj.getDouble("equipment_charges_ex")));
				usageOneMonth.setTaxCharges(Double.toString(jsonObj.getDouble("tax_charges_ex")));
				usageOneMonth.setRoamingMsgCharges(Double.toString(jsonObj.getDouble("roaming_msg_charges_ex")));
				usageOneMonth.setRoamingDataCharges(Double.toString(jsonObj.getDouble("roaming_data_charges_ex")));
				usageOneMonth.setRoamingVoiceCharges(Double.toString(jsonObj.getDouble("roaming_voice_charges_ex")));
				usageOneMonth.setTotalAccountLevelCharges(Double.toString(jsonObj.getDouble("total_account_level_charges_ex")));
				usageOneMonth.setRoamingCharges(Double.toString(jsonObj.getDouble("roaming_charges_ex")));
				usageOneMonth.setOtherCharges(Double.toString(jsonObj.getDouble("other_charges_ex")));
				usageOneMonth.setTotalCharge(Double.toString(jsonObj.getDouble("total_charge_ex")));
	    		
				listValues.add(usageOneMonth);
				
	    	}
	    	
	    }
	    
		return listValues;
	
	}
	
	
	
	// Get the data for the KPI tiles and Trending chart - Cost Center Dashboard
	public static List<HierarchyTrendData> getJsonDataTrend(String hierarchyId) throws JSONException, AWTException, InterruptedException{
		
		HierarchyTrendData trendDataOneMonth = new HierarchyTrendData();
		List<HierarchyTrendData> listValues = new ArrayList<HierarchyTrendData>();
	    	    	
	    String trendData = (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy." + hierarchyId + ".trend.payload.rows')");
	   
	    // ShowText(trendData);
		   
	    // Convert the String with data into a JSONArray
	    JSONArray array = new JSONArray(trendData); 

	    for (int i = 0; i < array.length(); i++){
	    	
	    	JSONObject jsonObj = array.getJSONObject(i);
	    	trendDataOneMonth = new HierarchyTrendData();
	        
	    	trendDataOneMonth.setId(jsonObj.getString("id"));
	    	trendDataOneMonth.setName(jsonObj.getString("name"));
	    	trendDataOneMonth.setOrdinalYear(Long.toString(jsonObj.getLong("ordinal_year")));
	    	trendDataOneMonth.setOrdinalMonth(Long.toString(jsonObj.getLong("ordinal_month")));
	    	trendDataOneMonth.setTotalNumberOfInvoices(Long.toString(jsonObj.getLong("total_no_of_invoices")));
	    	trendDataOneMonth.setNumberOfLines(Long.toString(jsonObj.getLong("no_of_lines")));
	    	trendDataOneMonth.setNumberOfLinesRollup(Long.toString(jsonObj.getLong("no_of_lines_rollup")));
	    	trendDataOneMonth.setCurrencyCode(jsonObj.getString("currency_code"));
	    	trendDataOneMonth.setTotalExpense(Double.toString(jsonObj.getDouble("total_expense_ex")));
	    	trendDataOneMonth.setOptimizableExpense(Double.toString(jsonObj.getDouble("optimizable_expense_ex")));
	    	trendDataOneMonth.setRoamingExpense(Double.toString(jsonObj.getDouble("roaming_expense_ex")));
	    	trendDataOneMonth.setTotalExpenseRollup(Double.toString(jsonObj.getDouble("total_expense_rollup_ex")));
	    	trendDataOneMonth.setOptimizableExpenseRollup(Double.toString(jsonObj.getDouble("optimizable_expense_rollup_ex")));
	    	trendDataOneMonth.setRoamingExpenseRollup(Double.toString(jsonObj.getDouble("roaming_expense_rollup_ex")));
	    	trendDataOneMonth.setHasData(jsonObj.getBoolean("has_data"));
	    	
	        listValues.add(trendDataOneMonth);
			
	    }
	    
		return listValues;
	
	}
	
	
	
	// Get the data for the ancestors - Cost Center Dashboard
	public static List<AncestorsInfo> getJsonDataAncestors(String hierarchyId) throws JSONException, AWTException, InterruptedException{
		
		AncestorsInfo ancestorsDataOneMonth = new AncestorsInfo();
		List<AncestorsInfo> listValues = new ArrayList<AncestorsInfo>();
	    	    	
	    String ancestorsData = (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy." + hierarchyId + ".trend.payload.ancestorsInfo')");
	    	   
	    // ShowText(ancestorsData);
		   
	    // Convert the String with data into a JSONArray
	    JSONArray array = new JSONArray(ancestorsData); 

	    for (int i = 0; i < array.length(); i++){
	    	
	    	JSONObject jsonObj  = array.getJSONObject(i);
	    	ancestorsDataOneMonth = new AncestorsInfo();
	        
	    	JSONArray tempJSONarray = jsonObj.getJSONArray("ancestors");
	    	List<String> ancestorsNames = new ArrayList<>();
	    	
	    	for (int j = 0; j < tempJSONarray.length(); j++) {
	    	
	    		JSONObject jsonObjTmp  = tempJSONarray.getJSONObject(j);
	    		ancestorsNames.add(jsonObjTmp.getString("name"));

	    	}
	    	
	    	ancestorsDataOneMonth.setOrdinalYear(Long.toString(jsonObj.getLong("ordinal_year")));
	    	ancestorsDataOneMonth.setOrdinalMonth(Long.toString(jsonObj.getLong("ordinal_month")));
	    	ancestorsDataOneMonth.setAncestorsNames(ancestorsNames);	
	    		    	
	        listValues.add(ancestorsDataOneMonth);
			
	    }
	    
		return listValues;
	
	}
	
	
	
	
	// Get the data for Top Ten chart - Cost Center Dashboard
	public static List<HierarchyTopTenData> getJsonDataTopTen(int category, String hierarchyId) throws JSONException, AWTException, InterruptedException{
		
		
		HierarchyTopTenData topTenDataOneMonth = new HierarchyTopTenData();
		List<HierarchyTopTenData> listValues = new ArrayList<HierarchyTopTenData>();
	  		
		String categoryForJS = "";
		
		if (category == HierarchyHelper.categoryTotal) {
		
			categoryForJS = "TOTAL_EXPENSE";
			
		} else if (category == HierarchyHelper.categoryOptimizable) {
			
			categoryForJS = "OPTIMIZABLE_EXPENSE";
			
		} else if (category == HierarchyHelper.categoryRoaming) {
			
			categoryForJS = "ROAMING_EXPENSE";
			
		}
		
	    
	    // E.g.: hierarchy.PRIMARY.topUsers.EXPENSE.TOTAL_EXPENSE'
	    String topTenData = (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy." + hierarchyId + ".topUsers.EXPENSE." + categoryForJS + ".payload.rows')");
	   
	    // ShowText(topTenData);
	   
	    // Convert the String with data into a JSONArray
	    JSONArray array = new JSONArray(topTenData); 

	    for (int i = 0; i < array.length(); i++){
	    	
	    	JSONObject jsonObj  = array.getJSONObject(i);
	    	topTenDataOneMonth = new HierarchyTopTenData();
	        
	    	String type = jsonObj.getString("type");
	    	
	    	switch (type) {
	    	
	    		case "EMPLOYEE":
	    			
	    			topTenDataOneMonth = getEmployeeData(jsonObj);
	    			break;
	    			
	    		case "DEPARTMENT":
	    			topTenDataOneMonth = new HierarchyTopTenData(jsonObj.getString("service_id"), jsonObj.getString("service_number"), jsonObj.getString("department_id"),
	    					jsonObj.getString("department_name"), jsonObj.getString("type"), jsonObj.getDouble("value"));
	    			break;

	    		case "AVERAGE":
	    			topTenDataOneMonth = new HierarchyTopTenData(jsonObj.getString("type"), jsonObj.getDouble("value"));
	    			break;
	    	
	    	}
	    	
	        listValues.add(topTenDataOneMonth);
			
	    }
	    
		return listValues;
	
	}
	
	
	
	private static HierarchyTopTenData getEmployeeData(JSONObject jsonObj) throws JSONException {
		
		HierarchyTopTenData data = new HierarchyTopTenData();
		String companyEmployeeId = "";
		String employeeFirstName = "";
		String employeeLastName = "";
		
		try {
			
			companyEmployeeId = jsonObj.getString("company_employee_id");
			
		} catch (JSONException e) {
			
		}
	
		try {
			
			employeeFirstName = jsonObj.getString("employee_firstname");
			
		} catch (JSONException e) {
			
		}
		
		try {
			
			employeeLastName = jsonObj.getString("employee_lastname");
			
		} catch (JSONException e) {
			
		}
		
		data = new HierarchyTopTenData(jsonObj.getString("service_id"), jsonObj.getString("service_number"), jsonObj.getString("employee_id"),
				companyEmployeeId, employeeFirstName, employeeLastName, jsonObj.getString("type"), jsonObj.getDouble("value"));
		
		return data;
		
	}
	
	
	
	
	// Get the data for Top Ten chart - Fleet Dashboard
	public static List<FleetTopTenData> getJsonDataTopTenFleet(int chartId, int category) throws JSONException, AWTException, InterruptedException{
		
		
		FleetTopTenData topTenDataOneMonth = new FleetTopTenData();
		List<FleetTopTenData> listValues = new ArrayList<FleetTopTenData>();
	  
		String categoryForJS = "";
		String chartNameForJS = "";
		
		if (chartId == GeneralTopTenHelper.expenseChart) {
			
			chartNameForJS = "EXPENSE";
			
			if (category == GeneralTopTenHelper.categoryExpenseAll) {
				
				categoryForJS = "ALL";
				
			} else if (category == GeneralTopTenHelper.categoryExpenseVoice) {
				
				categoryForJS = "VOICE";
				
			} else if (category == GeneralTopTenHelper.categoryExpenseData) {
				
				categoryForJS = "DATA";
				
			} else if (category == GeneralTopTenHelper.categoryExpenseMessages) {
				
				categoryForJS = "MESSAGES";
				
			} else if (category == GeneralTopTenHelper.categoryExpenseRoaming) {
				
				categoryForJS = "ROAMING";
				
			}
			
		} else if (chartId == GeneralTopTenHelper.domesticUsageChart) {
			
			chartNameForJS = "DOMESTIC_USAGE";
			
			if (category == GeneralTopTenHelper.categoryDomesticUsageVoice) {
				
				categoryForJS = "VOICE";
				
			} else if (category == GeneralTopTenHelper.categoryDomesticUsageVoiceOverage) {
				
				categoryForJS = "VOICE_OVERAGE";
				
			} else if (category == GeneralTopTenHelper.categoryDomesticUsageData) {
				
				categoryForJS = "DATA";
				
			} else if (category == GeneralTopTenHelper.categoryDomesticUsageMessages) {
				
				categoryForJS = "MESSAGES";
				
			} 
		
		} else if (chartId == GeneralTopTenHelper.roamingUsageChart) {
			
			chartNameForJS = "ROAMING_USAGE";
			
			if (category == GeneralTopTenHelper.categoryRoamingUsageVoice) {
				
				categoryForJS = "VOICE";
				
			} else if (category == GeneralTopTenHelper.categoryRoamingUsageData) {
				
				categoryForJS = "DATA";
				
			} else if (category == GeneralTopTenHelper.categoryRoamingUsageMessages) {
				
				categoryForJS = "MESSAGES";
				
			} 
		
		}
	
		
		try {
		
			// E.g.: fleet.topUsers.EXPENSE.ALL.payload.rows'
			String script = "return __TANGOE__getCapturedTestDataAsJSON('fleet.topUsers." + chartNameForJS + "." + categoryForJS + ".payload.rows')";
			String topTenData = (String) js.executeScript(script);

			// ShowText(topTenData);
	   	
		    // Convert the String with data into a JSONArray
		    JSONArray array = new JSONArray(topTenData); 

		    for (int i = 0; i < array.length(); i++){
		    	
		    	JSONObject jsonObj  = array.getJSONObject(i);
		    	topTenDataOneMonth = new FleetTopTenData(jsonObj.getString("service_number"), jsonObj.getDouble("value"));
		        listValues.add(topTenDataOneMonth);
				
		    }

		} catch (NullPointerException e) {
			
			// ShowText("Ajax call returned no data.");
			
		}
	    	    
		return listValues;
	
	}
	
	
	public static String getHierarchyValue() throws JSONException {
		
		String topTenData = (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy')");
		
		String[] tmpValues = topTenData.split(":"); 
		
		String value = tmpValues[0].replace("\"", "").replace("{", "").trim(); 
		
		return value;
		
	}
	
	
	// ***********************************
	// *** OLD - NOT USED ANYMORE ***
	
	// Reads the data needed for the "Fleet Manager Dashboard" tests
	public static List<UsageOneMonth> getDataFromSpreadsheet(String filePath) throws IOException{
		
		// E.g. path: "D:/Documents/CMD Dashboard/CreateFilesProgram/AT&T Mobility.txt"
		List<String> linesOfFile = getRowsfromFile(filePath);
		
		int linesAmount = linesOfFile.size() - 2; 
		
		// This list will contain one UsageOneMonth object per month 
		List<UsageOneMonth> listValues = new ArrayList<UsageOneMonth>();
		
		for (int i = 0; i < linesAmount; i++){
			
			//System.out.println("line # " + i);
			
			String lineToBeSplited = linesOfFile.get(i+2);
			String[] splitedline = splitLine(lineToBeSplited);
		
			UsageOneMonth dataOneMonth = new UsageOneMonth();
		
			dataOneMonth.setVendorName(linesOfFile.get(0));
			dataOneMonth.setInvoiceMonth(splitedline[3]);
			dataOneMonth.setOrdinalYear(splitedline[4]);
			dataOneMonth.setOrdinalMonth(splitedline[5]);
			
			dataOneMonth.setDomesticVoice(splitedline[19]);
			dataOneMonth.setDomesticOverageVoice(splitedline[20]);
			dataOneMonth.setDomesticMessages(splitedline[21]);
			dataOneMonth.setDomesticDataUsageKb(splitedline[22]);
			dataOneMonth.setRoamingVoice(splitedline[23]);
			dataOneMonth.setRoamingDataUsageKb(splitedline[24]);
			dataOneMonth.setRoamingMessages(splitedline[25]);
			
			dataOneMonth.setNumberOfInvoices(splitedline[6]);
			dataOneMonth.setNumberOfLines(splitedline[7]);
			dataOneMonth.setTotalSubscriberCharges(splitedline[9]);
			dataOneMonth.setVoiceCharges(splitedline[10]);
			dataOneMonth.setDataCharges(splitedline[11]);
			dataOneMonth.setMessagesCharges(splitedline[12]);
			dataOneMonth.setEquipmentCharges(splitedline[13]);
			dataOneMonth.setTaxCharges(splitedline[14]);
			dataOneMonth.setRoamingMsgCharges(splitedline[15]);
			dataOneMonth.setRoamingDataCharges(splitedline[16]);
			dataOneMonth.setRoamingVoiceCharges(splitedline[17]);
			dataOneMonth.setTotalAccountLevelCharges(splitedline[18]);
			dataOneMonth.setRoamingCharges(splitedline[26]);
			dataOneMonth.setOtherCharges(splitedline[27]);
			dataOneMonth.setTotalCharge(splitedline[28]);
			
			listValues.add(dataOneMonth);
			
		}
		
		return listValues;
		
	}


	// *** OLD - NOT USED ANYMORE ***
	public static List<String> getRowsfromFile(String filePath) throws IOException{
		
		Path path = Paths.get(filePath);
		Stream<String> rows = Files.lines(path);
		List<String> lines = new ArrayList<String>();
		
		rows.forEach(s ->lines.add((String)s));
		rows.close();
		
		return lines;
		
	}
	
	// *** OLD - NOT USED ANYMORE ***
	public static String[] splitLine(String lineToBeSplited){
		
		String itemsOfLine[];
				 		
		itemsOfLine = lineToBeSplited.split(";;");
		
		//System.out.println("Items #: " + itemsOfLine.length);  // 31, the last item is empty, must be discarded 
		
		for(int i = 0; i < itemsOfLine.length; i++){
			
			itemsOfLine[i] = itemsOfLine[i].trim();
			//System.out.println("Item: " + itemsOfLine[i]);
		}
		
		return itemsOfLine;
	}

}
