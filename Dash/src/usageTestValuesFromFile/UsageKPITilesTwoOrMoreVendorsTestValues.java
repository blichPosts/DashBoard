package usageTestValuesFromFile;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;
import usage.UsageKPITilesActions;

public class UsageKPITilesTwoOrMoreVendorsTestValues extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
 		// CommonTestStepActions.switchToContentFrame();
		// Initialization of month selector - we may want to call this method from somewhere else, or just when the month selector is needed
		// I've put it here to make sure that it gets initialized and that will not error 
		// CommonTestStepActions.initializeMonthSelector();
	}
	
	
	@Test
	public static void UsageKPITilesTwoOrMoreVendorsTestValuesTest() throws Exception
	{
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}
	
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
		
		String path = UsageHelper.path;
		int amountOfVendors = 12;
		
		// #1 Select Vendor View and Unselect all vendors 
		UsageHelper.selectVendorView();
		CommonTestStepActions.UnSelectAllVendors();
		
		List<List<UsageOneMonth>> listVendorsSelectedData = new ArrayList<>();
		
		// Run the test for each vendor 
		for(int i = 0; i < amountOfVendors; i++){
			
			String vendor = vendorNames.get(i);
			String vendorSelected = vendorNames.get(i);
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendorSelected);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
			
			System.out.println("Path: " + completePath);
			
			System.out.println("  **  RUNNING KPI TILE TEST FOR VENDOR: " + vendor + "  **");
			
			// #2 Read data from file
			List<UsageOneMonth> valuesFromFileOneVendor = ReadFilesHelper.getDataFromSpreadsheet(completePath);
			listVendorsSelectedData.add(valuesFromFileOneVendor);
				
			// #3 Select one vendor
			CommonTestStepActions.selectOneVendor(vendor);
			
		}
			
		String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
		
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
		
		List<UsageOneMonth> valuesSummarizedVendors = UsageHelper.summarizeDataVendorsSelected(listVendorsSelectedData);
		
		
		do {
		
			oneMonthData = valuesSummarizedVendors.get(indexMonth);   
			
			 // This has been modified on the source file. Month is the month listed on the file, it doesn't refer to the previous month.
			 // E.g.: “9/1/2016” now refers to September instead of August  
			 // if(oneMonthData[1].equals("1")){
//				month = "12";
//				year = Integer.toString(Integer.parseInt(oneMonthData[0]) - 1);
//			}else{
//				month = Integer.toString((Integer.parseInt(oneMonthData[1]) - 1));
//				year =  oneMonthData[0];
//			}
			
			// The following two lines replace the commented above. Will need to find out which version is the correct....
			month = oneMonthData.getOrdinalMonth();
			year =  oneMonthData.getOrdinalYear();
			
			monthYear = CommonTestStepActions.convertMonthNumberToName(month, year);
			System.out.println("Month Year: " + monthYear);
			
			// #4 Select month on month/year selector
			CommonTestStepActions.selectMonthYearPulldown(monthYear);
			
			Thread.sleep(2000);
			
			domesticVoiceUsage = oneMonthData.getDomesticVoice();
			domesticVoiceOverageUsage = oneMonthData.getDomesticOverageVoice();
			domesticMessagesUsage = oneMonthData.getDomesticMessages();
			domesticDataUsage = oneMonthData.getDomesticDataUsageKb();
			roamingDataUsage = oneMonthData.getRoamingDataUsageKb();
							
			// #5 Compare the values displayed on the KPIs to the values from spreadsheet
			UsageKPITilesActions.verifyKPItileValues(domesticVoiceUsage, domesticVoiceOverageUsage, domesticMessagesUsage, domesticDataUsage, roamingDataUsage);
			
			
			// #6 Calculate 3 Month Rolling Averages and Trending Percentage
			// Get the values needed to calculate the 3 month Rolling Averages, and the Trending values
			// ONLY if there's data for two months before the current month. 
			// E.g.: Last month with data: January 2016, then March 2016 is the last month that will have the 3 month rolling average and trending values calculated
			if(indexMonth < valuesSummarizedVendors.size()-2){
				
				List<UsageOneMonth> valuesForTrendingValue = new ArrayList<UsageOneMonth>();
				
				// Adds the current month values to the list
				valuesForTrendingValue.add(valuesSummarizedVendors.get(indexMonth));
				// Adds the previous month values to the list
				valuesForTrendingValue.add(valuesSummarizedVendors.get(indexMonth+1));
				// Adds values from 2 months ago to the list
				valuesForTrendingValue.add(valuesSummarizedVendors.get(indexMonth+2));
			 
				UsageKPITilesActions.verifyThreeMonthRollingAverageAndTrendingValues(valuesForTrendingValue);
				
			}
			
			
			// #7 Calculate 6 Month Rolling Averages
			// Get the values needed to calculate the 6 month Rolling Averages
			// ONLY if there's data for five months before the current month. 
			// E.g.: Last month with data: January 2016, then June 2016 is the last month that will have the 6 month rolling average calculated
			if(indexMonth < valuesSummarizedVendors.size()-5){
				
				List<UsageOneMonth> valuesForSixMonthAverage = new ArrayList<UsageOneMonth>();
				
				// Adds the current month values to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth));
				// Adds the previous month values to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+1));
				// Adds values from 2 months ago to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+2));
				// Adds values from 3 months ago to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+3));
				// Adds values from 4 months ago to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+4));
				// Adds values from 5 months ago to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+5));
				
				UsageKPITilesActions.verifySixMonthRollingAverage(valuesForSixMonthAverage);
				
			}
			
			indexMonth++;
			
		} while (!monthYear.equals(lastMonthListedMonthSelector));
		
		Thread.sleep(2000);		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for KPI tiles values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

	
	
}
