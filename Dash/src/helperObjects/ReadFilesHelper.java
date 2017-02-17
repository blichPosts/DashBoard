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
	
	
	public static void startCollectingData() {
		
		js.executeScript("__TANGOE__setShouldCaptureTestData(true)");
		
	}
	
	
	// Get the data for the KPI tiles and Trending chart
	public static List<HierarchyTrendData> getJsonDataTrend(boolean drillDown, int hierarchyNum) throws JSONException, AWTException, InterruptedException{
		
		HierarchyTrendData trendDataOneMonth = new HierarchyTrendData();
		List<HierarchyTrendData> listValues = new ArrayList<HierarchyTrendData>();
	  
	    // Click on tile map to generate the call to get data 
	    if (drillDown) {
	    	HierarchyHelper.drillDownOnHierarchy();
	    }
	    
	    String hierarchyLevel = "";
	    
	    if (hierarchyNum == 1) {
	    
	    	hierarchyLevel = "PRIMARY";
	    	
	    } else if (hierarchyNum == 2) {
	    	
	    	hierarchyLevel = "SECONDARY";
	    	
	    } else if (hierarchyNum == 3) {
	    	
	    	hierarchyLevel = "TERTIARY";
	    	
	    }
	    	
	    String trendData = (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy." + hierarchyLevel + ".trend.payload.rows')");
	   

	    ShowText(trendData);
	   
	    // Get the rows with data
//	    String trendData = trendDataTmp.split("\"rows\":")[1];
	   
	    // Convert the String with data into a JSONArray
	    JSONArray array = new JSONArray(trendData); 

	    for (int i = 0; i < array.length(); i++){
	    	
	    	JSONObject jsonObj  = array.getJSONObject(i);
	    	trendDataOneMonth = new HierarchyTrendData();
	        
	    	trendDataOneMonth.setId(jsonObj.getString("id"));
	    	trendDataOneMonth.setName(jsonObj.getString("name"));
	    	trendDataOneMonth.setOrdinalYear(Long.toString(jsonObj.getLong("ordinal_year")));
	    	trendDataOneMonth.setOrdinalMonth(Long.toString(jsonObj.getLong("ordinal_month")));
	    	trendDataOneMonth.setNumberOfInvoices(Long.toString(jsonObj.getLong("no_of_invoices")));
	    	trendDataOneMonth.setNumberOfLines(Long.toString(jsonObj.getLong("no_of_lines")));
	    	trendDataOneMonth.setNumberOfAccounts(Long.toString(jsonObj.getLong("no_of_accounts")));
	    	trendDataOneMonth.setNumberOfInvoicesRollup(Long.toString(jsonObj.getLong("no_of_invoices_rollup")));
	    	trendDataOneMonth.setNumberOfLinesRollup(Long.toString(jsonObj.getLong("no_of_lines_rollup")));
	    	trendDataOneMonth.setNumberOfAccountsRollup(Long.toString(jsonObj.getLong("no_of_accounts_rollup")));
	    	trendDataOneMonth.setCurrencyCode(jsonObj.getString("currency_code"));
	    	trendDataOneMonth.setTotalExpense(Double.toString(jsonObj.getDouble("total_expense_ex")));
	    	trendDataOneMonth.setOptimizableExpense(Double.toString(jsonObj.getDouble("optimizable_expense_ex")));
	    	trendDataOneMonth.setRoamingExpense(Double.toString(jsonObj.getDouble("roaming_expense_ex")));
	    	trendDataOneMonth.setTotalExpenseRollup(Double.toString(jsonObj.getDouble("total_expense_rollup_ex")));
	    	trendDataOneMonth.setOptimizableExpenseRollup(Double.toString(jsonObj.getDouble("optimizable_expense_rollup_ex")));
	    	trendDataOneMonth.setRoamingExpenseRollup(Double.toString(jsonObj.getDouble("roaming_expense_rollup_ex")));
	    	
	        listValues.add(trendDataOneMonth);
			
	    }
	    
		return listValues;
	
	}
	
	
	
	// Get the data for Top Ten chart
	public static List<HierarchyTopTenData> getJsonDataTopTen(boolean drillDown, int category, int hierarchyNum) throws JSONException, AWTException, InterruptedException{
		
		
		HierarchyTopTenData topTenDataOneMonth = new HierarchyTopTenData();
		List<HierarchyTopTenData> listValues = new ArrayList<HierarchyTopTenData>();
	  
		
		String hierarchyLevel = "";
	    
	    if (hierarchyNum == 1) {
	    
	    	hierarchyLevel = "PRIMARY";
	    	
	    } else if (hierarchyNum == 2) {
	    	
	    	hierarchyLevel = "SECONDARY";
	    	
	    } else if (hierarchyNum == 3) {
	    	
	    	hierarchyLevel = "TERTIARY";
	    	
	    }
		
		
		String categoryForJS = "";
		
		if (category == HierarchyHelper.categoryTotal) {
		
			categoryForJS = "TOTAL_EXPENSE";
			
		} else if (category == HierarchyHelper.categoryOptimizable) {
			
			categoryForJS = "OPTIMIZABLE_EXPENSE";
			
		} else if (category == HierarchyHelper.categoryRoaming) {
			
			categoryForJS = "ROAMING_EXPENSE";
			
		}
		

	    // Click on tile map to generate the call to get data 
	    if (drillDown) {
	    	HierarchyHelper.drillDownOnPoV();
	    }
	    
	    // E.g.: hierarchy.PRIMARY.topUsers.EXPENSE.TOTAL_EXPENSE'
	    String topTenData = (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy." + hierarchyLevel + ".topUsers.EXPENSE." + categoryForJS + ".payload.rows')");
	   
	    ShowText(topTenData);
	   
	    // Get the rows with data
//	    String topTenData = topTenDataTmp.split("\"rows\":")[1];
	   
	    // Convert the String with data into a JSONArray
	    JSONArray array = new JSONArray(topTenData); 

	    for (int i = 0; i < array.length(); i++){
	    	
	    	JSONObject jsonObj  = array.getJSONObject(i);
	    	topTenDataOneMonth = new HierarchyTopTenData();
	        
	    	String type = jsonObj.getString("type");
	    	
	    	switch (type) {
	    	
	    		case "EMPLOYEE":
	    			topTenDataOneMonth = new HierarchyTopTenData(jsonObj.getString("service_id"), jsonObj.getString("service_number"), jsonObj.getString("employee_id"),
	    					jsonObj.getString("company_employee_id"), jsonObj.getString("employee_firstname"), jsonObj.getString("employee_lastname"), 
	    					jsonObj.getString("type"), jsonObj.getDouble("value"));
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
			
//			System.out.print("Row " + (i+1) + ":  | ");
//			System.out.print(listValues.get(i).getVendorName() + "  | "); 
//			System.out.print(listValues.get(i).getInvoiceMonth() + "  | ");
//			System.out.print(listValues.get(i).getOrdinalYear() + "  | ");
//			System.out.print(listValues.get(i).getOrdinalMonth() + "  | ");
//			System.out.print(listValues.get(i).getDomesticVoice() + "  | ");
//			System.out.print(listValues.get(i).getDomesticOverageVoice() + "  | ");
//			System.out.print(listValues.get(i).getDomesticMessages() + "  | ");
//			System.out.print(listValues.get(i).getDomesticDataUsageKb() + "  | ");
//			System.out.print(listValues.get(i).getRoamingVoice() + "  | ");
//			System.out.print(listValues.get(i).getRoamingDataUsageKb() + "  | ");
//			System.out.print(listValues.get(i).getRoamingMessages() + "  | ");
//			System.out.println("");
			
		}
		
		return listValues;
		
	}


	public static List<String> getRowsfromFile(String filePath) throws IOException{
		
		Path path = Paths.get(filePath);
		Stream<String> rows = Files.lines(path);
		List<String> lines = new ArrayList<String>();
		
		rows.forEach(s ->lines.add((String)s));
		
		for(String line: lines){	
			//System.out.println("line: " + line);
		}
		
		rows.close();
		
		return lines;
		
	}
	
	
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


	// Reads the data needed for the "Hierarchy Dashboard" tests
	public static List<HierarchyTrendData> getHierarchyTrendData(String filePath) throws IOException {

		List<String> linesOfFile = getRowsfromFile(filePath);
		boolean rowsLineRead = false;
		
		HierarchyTrendData trendDataOneMonth = new HierarchyTrendData();
		List<HierarchyTrendData> listValues = new ArrayList<HierarchyTrendData>();
		
//		System.out.println("File to be read:" + filePath);
		
		for (String s: linesOfFile) {
			
//			System.out.println("Line :" + s);
			
			if (s.contains("rows")) {
				rowsLineRead = true;
			}	
			
			if (rowsLineRead && !s.trim().equals("{") && !s.trim().equals("},")) {
				
				switch (returnName(s)) {
				
					case "id":
					
						trendDataOneMonth.setId(returnValue(s));
						break;
					
					case "name":
					
						trendDataOneMonth.setName(returnValue(s));
						break;
					
					case "ordinal_year":
					
						trendDataOneMonth.setOrdinalYear(returnValue(s));
						break;
					
					case "ordinal_month":
										
						trendDataOneMonth.setOrdinalMonth(returnValue(s));
						break;
					
					case "no_of_invoices":
					
						trendDataOneMonth.setNumberOfInvoices(returnValue(s));
						break;
					
					case "no_of_lines":
					
						trendDataOneMonth.setNumberOfLines(returnValue(s));
						break;
					
					case "no_of_accounts":
					
						trendDataOneMonth.setNumberOfAccounts(returnValue(s));
						break;
					
					case "no_of_invoices_rollup":
						
						trendDataOneMonth.setNumberOfInvoicesRollup(returnValue(s));
						break;
					
					case "no_of_lines_rollup":
					
						trendDataOneMonth.setNumberOfLinesRollup(returnValue(s));
						break;
					
					case "no_of_accounts_rollup":
					
						trendDataOneMonth.setNumberOfAccountsRollup(returnValue(s));
						break;
					
					case "currency_code":
					
						trendDataOneMonth.setCurrencyCode(returnValue(s));
						break;
					
					case "total_expense_ex":
					
						trendDataOneMonth.setTotalExpense(returnValue(s));
						break;
					
					case "optimizable_expense_ex":
					
						trendDataOneMonth.setOptimizableExpense(returnValue(s));
						break;
					
					case "roaming_expense_ex":
					
						trendDataOneMonth.setRoamingExpense(returnValue(s));
						break;
					
					case "total_expense_rollup_ex":
					
						trendDataOneMonth.setTotalExpenseRollup(returnValue(s));
						break;
					
					case "optimizable_expense_rollup_ex":
					
						trendDataOneMonth.setOptimizableExpenseRollup(returnValue(s));
						break;
					
					case "roaming_expense_rollup_ex":
					
						trendDataOneMonth.setRoamingExpenseRollup(returnValue(s));
						break;
					
					default:
					
//						System.out.println("not a value");
						break;
					
				}
				
			}
			
			if (rowsLineRead && (s.trim().equals("},") || s.trim().equals("]"))) {
				
				listValues.add(trendDataOneMonth);
				trendDataOneMonth = new HierarchyTrendData();
			}
			
		}
		
		
		
		for (HierarchyTrendData h: listValues) {
			
			System.out.println("id: " + h.getId()); 
			System.out.println("name: " + h.getName());
			System.out.println("ordinal_year: " + h.getOrdinalYear());
			System.out.println("ordinal_month: " + h.getOrdinalMonth());
			System.out.println("no_of_invoices: " + h.getNumberOfInvoices());
			System.out.println("no_of_lines: " + h.getNumberOfLines());
			System.out.println("no_of_accounts: " + h.getNumberOfAccounts());
			System.out.println("no_of_invoices_rollup: " + h.getNumberOfInvoicesRollup());
			System.out.println("no_of_lines_rollup: " + h.getNumberOfLinesRollup());
			System.out.println("no_of_accounts_rollup: " + h.getNumberOfAccountsRollup());
			System.out.println("currency_code: " + h.getCurrencyCode());
			System.out.println("total_expense_ex: " + h.getTotalExpense());
			System.out.println("optimizable_expense_ex: " + h.getOptimizableExpense());
			System.out.println("roaming_expense_ex: " + h.getRoamingExpense());
			System.out.println("total_expense_rollup_ex: " + h.getTotalExpenseRollup());
			System.out.println("optimizable_expense_rollup_ex: " + h.getOptimizableExpenseRollup());
			System.out.println("roaming_expense_rollup_ex: " + h.getRoamingExpenseRollup());
			
		}
		
		
		
		int linesAmount = linesOfFile.size() - 2; 
		
		// This list will contain one UsageOneMonth object per month 

		
//		for (int i = 0; i < linesAmount; i++){
//			
//			//System.out.println("line # " + i);
//			
//			String lineToBeSplited = linesOfFile.get(i+2);
//			String[] splitedline = splitLine(lineToBeSplited);
//		
//			HierarchyTrendData trendData = new HierarchyTrendData();
//		
//			trendData.setId("...");
//			trendData.setName("...");
//			trendData.setOrdinalYear("...");
//			trendData.setOrdinalMonth("...");
//			
//			trendData.setNumberOfInvoices("...");
//			trendData.setNumberOfLines("...");
//			trendData.setNumberOfAccounts("...");
//			trendData.setNumberOfInvoicesRollup("...");
//			trendData.setNumberOfLinesRollup("...");
//			trendData.setNumberOfAccountsRollup("...");
//			trendData.setCurrencyCode("...");
//			trendData.setTotalExpense("...");
//			trendData.setOptimizableExpense("...");
//			trendData.setRoamingExpense("...");
//			trendData.setTotalExpenseRollup("...");
//			trendData.setOptimizableExpenseRollup("...");
//			trendData.setRoamingExpenseRollup("...");
//			
//			listValues.add(trendData);
			
//			System.out.print("Row " + (i+1) + ":  | ");
//			System.out.print(listValues.get(i).getVendorName() + "  | "); 
//			System.out.print(listValues.get(i).getInvoiceMonth() + "  | ");
//			System.out.print(listValues.get(i).getOrdinalYear() + "  | ");
//			System.out.print(listValues.get(i).getOrdinalMonth() + "  | ");
//			System.out.print(listValues.get(i).getDomesticVoice() + "  | ");
//			System.out.print(listValues.get(i).getDomesticOverageVoice() + "  | ");
//			System.out.print(listValues.get(i).getDomesticMessages() + "  | ");
//			System.out.print(listValues.get(i).getDomesticDataUsageKb() + "  | ");
//			System.out.print(listValues.get(i).getRoamingVoice() + "  | ");
//			System.out.print(listValues.get(i).getRoamingDataUsageKb() + "  | ");
//			System.out.print(listValues.get(i).getRoamingMessages() + "  | ");
//			System.out.println("");
			
//		}
		
		return listValues;
		
	}
		
	
	
	public static String returnName(String jsonItem) {
		
		return jsonItem.split(":")[0].trim().replace("\"", "");
		
	}
		
	
	public static String returnValue(String jsonItem) {
		
		jsonItem = jsonItem.trim();
		return (jsonItem.split("\":")[1].trim().replace("\"", "").replace(",", ""));
//		return (jsonItem.substring(jsonItem.trim().indexOf(":") + 1,jsonItem.length() - 1)).trim().replace("\"","");
		
	}
	 
	
}
