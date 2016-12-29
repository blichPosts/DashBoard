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
		
		System.out.println("         vendor_name year month  dom_mou   overage_mou   dom_msgs   dom_data   roam_mou   roam_data  roam_msgs");
		
		for (int i = 0; i < linesAmount; i++){
			
			//System.out.println("line # " + i);
			
			String lineToBeSplited = linesOfFile.get(i+2);
			String[] splitedline = splitLine(lineToBeSplited);
		
			UsageOneMonth usageOneMonth = new UsageOneMonth();
			usageOneMonth.setVendorName(linesOfFile.get(0));         // vendor_name 
			usageOneMonth.setOrdinalYear(splitedline[4]);            // ordinal_year
			usageOneMonth.setOrdinalMonth(splitedline[5]);           // ordinal_month
			usageOneMonth.setDomesticVoice(splitedline[19]);         // domestic_mou
			usageOneMonth.setDomesticOverageVoice(splitedline[20]);  // domestic_overage_mou
			usageOneMonth.setDomesticMessages(splitedline[21]);      // domestic_messages
			usageOneMonth.setDomesticDataUsageKb(splitedline[22]);   // domestic_data_usage_kb
			usageOneMonth.setRoamingVoice(splitedline[23]);          // roaming_mou
			usageOneMonth.setRoamingDataUsageKb(splitedline[24]);    // roaming_data_usage_kb
			usageOneMonth.setRoamingMessages(splitedline[25]);       // roaming_messages
			
			listValues.add(usageOneMonth);
			
			System.out.print("Row " + (i+1) + ":  | ");
			System.out.print(listValues.get(i).getVendorName() + "  | ");            // vendor_name 
			System.out.print(listValues.get(i).getOrdinalYear() + "  | ");           // ordinal_year
			System.out.print(listValues.get(i).getOrdinalMonth() + "  | ");          // ordinal_month
			System.out.print(listValues.get(i).getDomesticVoice() + "  | ");         // domestic_mou
			System.out.print(listValues.get(i).getDomesticOverageVoice() + "  | ");  // domestic_overage_mou
			System.out.print(listValues.get(i).getDomesticMessages() + "  | ");      // domestic_messages
			System.out.print(listValues.get(i).getDomesticDataUsageKb() + "  | ");   // domestic_data_usage_kb
			System.out.print(listValues.get(i).getRoamingVoice() + "  | ");          // roaming_mou
			System.out.print(listValues.get(i).getRoamingDataUsageKb() + "  | ");    // roaming_data_usage_kb
			System.out.print(listValues.get(i).getRoamingMessages() + "  | ");       // roaming_messages
			System.out.println("");
			
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
