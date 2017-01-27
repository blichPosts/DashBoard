package helperObjects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;



public class ReadFilesHelper {

	
	/*public static void main(String args[]) throws IOException{
		
	}
	*/
	
	
	// E.g. path: "D:/Documents/CMD Dashboard/CreateFilesProgram/AT&T Mobility.txt"
	
	public static List<UsageOneMonth> getDataFromSpreadsheet(String filePath) throws IOException{
		
		List<String> linesOfFile = getRowsfromFile(filePath);
		
		int linesAmount = linesOfFile.size() - 2; 
		
		// This list will contain one UsageOneMonth object per month 
		List<UsageOneMonth> listValues = new ArrayList<UsageOneMonth>();
		
//		System.out.println("         vendor_name   invoice_month   year month  dom_mou   overage_mou   dom_msgs   dom_data   roam_mou   roam_data  roam_msgs");
		
		for (int i = 0; i < linesAmount; i++){
			
			//System.out.println("line # " + i);
			
			String lineToBeSplited = linesOfFile.get(i+2);
			String[] splitedline = splitLine(lineToBeSplited);
		
			UsageOneMonth dataOneMonth = new UsageOneMonth();
			dataOneMonth.setVendorName(linesOfFile.get(0));         // vendor_name
			dataOneMonth.setInvoiceMonth(splitedline[3]);			 // invoice_month
			dataOneMonth.setOrdinalYear(splitedline[4]);            // ordinal_year
			dataOneMonth.setOrdinalMonth(splitedline[5]);           // ordinal_month
			dataOneMonth.setDomesticVoice(splitedline[19]);         // domestic_mou
			dataOneMonth.setDomesticOverageVoice(splitedline[20]);  // domestic_overage_mou
			dataOneMonth.setDomesticMessages(splitedline[21]);      // domestic_messages
			dataOneMonth.setDomesticDataUsageKb(splitedline[22]);   // domestic_data_usage_kb
			dataOneMonth.setRoamingVoice(splitedline[23]);          // roaming_mou
			dataOneMonth.setRoamingDataUsageKb(splitedline[24]);    // roaming_data_usage_kb
			dataOneMonth.setRoamingMessages(splitedline[25]);       // roaming_messages
			
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
//			System.out.print(listValues.get(i).getVendorName() + "  | ");            // vendor_name 
//			System.out.print(listValues.get(i).getInvoiceMonth() + "  | ");          // invoice_month
//			System.out.print(listValues.get(i).getOrdinalYear() + "  | ");           // ordinal_year
//			System.out.print(listValues.get(i).getOrdinalMonth() + "  | ");          // ordinal_month
//			System.out.print(listValues.get(i).getDomesticVoice() + "  | ");         // domestic_mou
//			System.out.print(listValues.get(i).getDomesticOverageVoice() + "  | ");  // domestic_overage_mou
//			System.out.print(listValues.get(i).getDomesticMessages() + "  | ");      // domestic_messages
//			System.out.print(listValues.get(i).getDomesticDataUsageKb() + "  | ");   // domestic_data_usage_kb
//			System.out.print(listValues.get(i).getRoamingVoice() + "  | ");          // roaming_mou
//			System.out.print(listValues.get(i).getRoamingDataUsageKb() + "  | ");    // roaming_data_usage_kb
//			System.out.print(listValues.get(i).getRoamingMessages() + "  | ");       // roaming_messages
//			System.out.println("");
			
		}
		
		return listValues;
		
	}


	
	public static List<String> getRowsfromFile(String filePath) throws IOException{
		
		Path path = Paths.get(filePath);
		Stream<String> rows = Files.lines(path);
		
		List<String> lines = new ArrayList<String>();
		
		rows.forEach(s ->lines.add((String)s));
					
		//System.out.println("count: " +  lines.size());
		
		for(String line: lines){	
			//System.out.println("line: " + line);
		}
		
		rows.close();
		
		return lines;
		
	}
	
	
	public static String[] splitLine(String lineToBeSplited){
		
		
		String itemsOfLine[];
		//String lineWithNoSpaces = lineToBeSplited.replaceAll("          ", ";"); 
		
		//System.out.println("lineWithNoSpaces: " + lineWithNoSpaces); 
				 		
		itemsOfLine = lineToBeSplited.split(";;");    //lineWithNoSpaces.split(";");
		//itemsOfLine = line.get(2).split(" ");
		
		//System.out.println("Items #: " + itemsOfLine.length);  // 31, the last item is empty, must be discarded 
		
		for(int i = 0; i < itemsOfLine.length; i++){
			
			itemsOfLine[i] = itemsOfLine[i].trim();
			//System.out.println("Item: " + itemsOfLine[i]);
		}
		
		return itemsOfLine;
	}
	
	 
	
}
