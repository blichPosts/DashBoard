package testSuiteUsage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;
import usage.UsageKPITilesActions;


public class UsagePageTestValuesFromFile extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
 		//CommonTestStepActions.switchToContentFrame();
		// Initialization of month selector - we may want to call this method from somewhere else, or just when the month selector is needed
		// I've put it here to make sure that it gets initialized and that will not error 
		//CommonTestStepActions.initializeMonthSelector();
	}
	
	
	@Test
	public static void UsagePageTestValuesFromFileTest() throws Exception
	{
		
		String vendor = "";
		//vendor = "AT&T Mobility";
		//vendor = "Etisalat";
		//vendor = "O2 UK";
		vendor = "Rogers";

		
		String path = "D:/Documents/CMD Dashboard/CreateFilesProgram/ ";
		String file = vendor + ".txt";
		String completePath = path + file;
		
		System.out.println("Path: " + completePath);
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
			
		// #1 Select Vendor View 
		UsageHelper.selectVendorView();
		
		
		// #2 Read data from file
		//String[][] valuesFromFile = ReadFilesHelper.getDataFromSpreadsheet(completePath);
		List<UsageOneMonth> valuesFromFile = ReadFilesHelper.getDataFromSpreadsheet(completePath);
		
			
		// #3 Select only one vendor
		CommonTestStepActions.UnSelectAllVendors();
		CommonTestStepActions.selectOneVendor(vendor);
		
		
		String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
		
		//String[] oneMonthData;
		UsageOneMonth oneMonthData;
		String year =  "";
		String month = "";
		String monthYear = "";
		String domesticVoiceUsage;
		String domesticVoiceOverageUsage;
		String domesticMessagesUsage;
		String domesticDataUsage;
		String roamingDataUsage;
		
		int indexMonth = 0;
		
		do {
		
			oneMonthData = valuesFromFile.get(indexMonth);  //valuesFromFile[indexMonth];   
			
			/*
			 * This has been modified on the source file. Month is the month listed on the file, it doesn't refer to the previous month.
			 * E.g.: “9/1/2016” now refers to September instead of August  
			 * if(oneMonthData[1].equals("1")){
				month = "12";
				year = Integer.toString(Integer.parseInt(oneMonthData[0]) - 1);
			}else{
				month = Integer.toString((Integer.parseInt(oneMonthData[1]) - 1));
				year =  oneMonthData[0];
			}*/
			
			// The following two lines replace the commented above. Will need to find out which version is the correct....
			month = oneMonthData.getOrdinalMonth();
			year =  oneMonthData.getOrdinalYear();
			
			monthYear = CommonTestStepActions.convertMonthNumberToName(month, year);
			System.out.println("Month Year: " + monthYear);
			
			// #4 Select month on month/year selector
			CommonTestStepActions.selectMonthYearPulldown(monthYear);
			
			Thread.sleep(2000);
			
			domesticVoiceUsage = oneMonthData.getDomesticVoice();     //oneMonthData[2];
			domesticVoiceOverageUsage = oneMonthData.getDomesticOverageVoice();    //oneMonthData[3];
			domesticMessagesUsage = oneMonthData.getDomesticMessages();   //oneMonthData[4];
			domesticDataUsage = oneMonthData.getDomesticDataUsageKb();   //oneMonthData[5];
			roamingDataUsage = oneMonthData.getRoamingDataUsageKb();   //oneMonthData[7];
							
			// #5 Compare the values displayed on the KPIs to the values from spreadsheet
			UsageKPITilesActions.verifyKPItileValues(domesticVoiceUsage, domesticVoiceOverageUsage, domesticMessagesUsage, domesticDataUsage, roamingDataUsage);
			
			// Get the values needed to calculate the 3 month Rolling Averages, and the Trending values
			// ONLY if there's data for two months before the current month. 
			// E.g.: Last month with data: January 2016, then March 2016 is the last month that will have the three month and trending values calculated
			// ** TO DO ** might want to calculate the 6 month Rolling Average too
			if(indexMonth < valuesFromFile.size()-2){
				
				List<UsageOneMonth> valuesForTrendingValue = new ArrayList<UsageOneMonth>();
				
				// Adds the current month values to the list
				valuesForTrendingValue.add(valuesFromFile.get(indexMonth));
				// Adds the previous month values to the list
				valuesForTrendingValue.add(valuesFromFile.get(indexMonth+1));
				// Adds values from two months ago to the list
				valuesForTrendingValue.add(valuesFromFile.get(indexMonth+2));
			
				// #6 
				UsageKPITilesActions.verifyTrendingValues(valuesForTrendingValue);
				
			}
			
			indexMonth++;
			
		} while (!monthYear.equals(lastMonthListedMonthSelector));
		
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
