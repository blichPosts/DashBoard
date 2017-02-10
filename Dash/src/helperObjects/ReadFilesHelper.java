package helperObjects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class ReadFilesHelper {

	// E.g. path: "D:/Documents/CMD Dashboard/CreateFilesProgram/AT&T Mobility.txt"
	
	
	// Reads the data needed for the "Fleet Manager Dashboard" tests
	public static List<UsageOneMonth> getDataFromSpreadsheet(String filePath) throws IOException{
		
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
