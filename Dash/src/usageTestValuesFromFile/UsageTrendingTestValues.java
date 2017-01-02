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
import usage.UsageTrending;


public class UsageTrendingTestValues extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		//CommonTestStepActions.switchToContentFrame();
		// Initialization of month selector - we may want to call this method from somewhere else, or just when the month selector is needed
		// I've put it here to make sure that it gets initialized and that will not error 
		CommonTestStepActions.initializeMonthSelector();
	}


	@Test
	public static void UsageTrendingTestValuesTest() throws Exception
	{

		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}
		

		//String vendor = "Telstra Australia";
		//String vendor = "Vivo Brazil";
		//String vendor = "Rogers";
		//String vendor = "Telcel Mexico";
		//String vendor = "SingTel Singapore";
		//String vendor = "Etisalat";
		//String vendor = "O2 UK";
		//String vendor = "Vodafone United Kingdom";
		String vendor = "AT&T Mobility";
		//String vendor = "Tangoe, Inc.";
		//String vendor = "Verizon Wireless";
		
		String path = UsageHelper.path;

		// Run the test for each vendor 
		//for(String vendorSelected: vendorNames){
			
		//	String vendor = vendorSelected;
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendor);  //(vendorSelected);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
			
			System.out.println("Path: " + completePath);
			
			System.out.println("  **  RUNNING KPI TILE TEST FOR VENDOR: " + vendor + "  **");
			
			CommonTestStepActions.GoToUsagePageDetailedWait();
				
			// #1 Select Vendor View 
			UsageHelper.selectVendorView();
			
			
			// #2 Read data from file
			List<UsageOneMonth> valuesFromFileTmp = ReadFilesHelper.getDataFromSpreadsheet(completePath);
			//System.out.println("valuesFromFileTmp size: " + valuesFromFileTmp.size());  
			
			// Some vendors might not have information for all last 13 months; in that case the value displayed on the Usage Trending chart is zero
			// So to have a valid value to compare with, the info for those missing months (on source file) is created and values are set to zero.  
			List<UsageOneMonth> valuesFromFile = UsageHelper.addMissingMonthsForVendor(valuesFromFileTmp);
			//System.out.println("valuesFromFile size: " + valuesFromFile.size());
			
			UsageOneMonth oneMonthData;
			oneMonthData = valuesFromFileTmp.get(0);
			
			
			// #3 Select only one vendor
			CommonTestStepActions.UnSelectAllVendors();
			CommonTestStepActions.selectOneVendor(vendor);
			
			String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
			
			String year =  "";
			String month = "";
			String monthYear = "";
						
			int indexMonth = 0;
			
			// for each vendor repeat the test 3 times, (for 3 different months) just in case. There's no point on verifying all the months
			// since the chart already shows the data for 13 months, no matter which month has been selected. 
			//do {
			
				
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
				// Month to be selected on pulldown needs to be one of the months for which there's data in the source file
				CommonTestStepActions.selectMonthYearPulldown(monthYear);
				Thread.sleep(2000);
				
				
				
				
				// #5 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file 
				
				try{
					
					UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, valuesFromFile, UsageHelper.categoryVoice);
					Thread.sleep(2000);
					UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, valuesFromFile, UsageHelper.categoryVoice);
					Thread.sleep(2000);				
					/*UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, valuesFromFile, UsageHelper.categoryData);
					Thread.sleep(2000);
					UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, valuesFromFile, UsageHelper.categoryData);
					Thread.sleep(2000);				
					UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, valuesFromFile, UsageHelper.categoryMessages);
					Thread.sleep(2000);
					UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, valuesFromFile, UsageHelper.categoryMessages);
					Thread.sleep(2000);
					*/
				} catch(NullPointerException e){
					
					System.out.println("chart not found");
					
				}
				
				indexMonth++;
				
			//} while (indexMonth < 3 && !monthYear.equals(lastMonthListedMonthSelector));
						
		//}
	
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Usage Trending values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
	
	
}
