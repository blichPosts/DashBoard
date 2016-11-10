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
	
	
	
	
	// path "D:/Documents/CMD Dashboard/CreateFilesProgram/AT&T Mobility.txt"
	
	public static String[][] getDataFromSpreadsheet(String filePath) throws IOException{
		
		List<String> linesOfFile = getRowsfromFile(filePath);
		
		int linesAmount = linesOfFile.size() - 2; 
		
		String [][] values = new String[linesAmount][9]; // # rows it's variable for each vendor - one for each line found in the file
												 		 // 9 columns - one for each value needed for Usage page tests 	
		
		for (int i = 0; i < linesAmount; i++){
			
			//System.out.println("line # " + i);
			
			String lineToBeSplited = linesOfFile.get(i+2);
			String [] splitedline = splitLine(lineToBeSplited);
			
			values[i][0] = splitedline[4];   // ordinal_year
			values[i][1] = splitedline[5];   // ordinal_month
			values[i][2] = splitedline[19];  // domestic_mou
			values[i][3] = splitedline[20];  // domestic_overage_mou
			values[i][4] = splitedline[21];  // domestic_messages
			values[i][5] = splitedline[22];  // domestic_data_usage_kb
			values[i][6] = splitedline[23];  // roaming_mou
			values[i][7] = splitedline[24];  // roaming_data_usage_kb
			values[i][8] = splitedline[25];  // roaming_messages 
			
		}
		
		
		for (int i = 0; i < linesAmount; i++){
			
			System.out.print("Row " + (i+1) + ": ");
			
			for (int j = 0; j < 9; j++){
				
				System.out.print(values[i][j] + "  | ");
				
			}
			
			System.out.println("");
		}
		
		return values;
		
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
