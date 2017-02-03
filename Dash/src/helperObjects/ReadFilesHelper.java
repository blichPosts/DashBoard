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
	
	 
	
}
