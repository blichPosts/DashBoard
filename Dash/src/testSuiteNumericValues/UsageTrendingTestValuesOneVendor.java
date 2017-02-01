package testSuiteNumericValues;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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


public class UsageTrendingTestValuesOneVendor extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		login();
		// MainLogin();
		// CommonTestStepActions.switchToContentFrame();

	}


	@Test
	public static void UsageTrendingTestValuesOneVendorTest() throws Exception
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
		//String vendor = "AT&T Mobility";
		//String vendor = "Tangoe, Inc.";
		//String vendor = "Verizon Wireless";
		
		String path = UsageHelper.path;

		// Run the test for each vendor 
		for(String vendorSelected: vendorNames){
			
			String vendor = vendorSelected;
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendor);
			
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
			
			
			// #4 Select month on month/year selector
			// Month to be selected on pulldown needs to be one of the months for which there's data in the source file
			String year =  "";
			String month = "";
			String monthYearToSelect = "";
			
			String[] monthYear = UsageHelper.getMonthYearToSelect(oneMonthData);
			month = monthYear[0];
			year = monthYear[1];
			
			monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
			System.out.println("Month Year: " + monthYearToSelect);
			
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			Thread.sleep(2000);
			
			
			// #5 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file
			// Note: Only the first month with data is selected for each vendor, since no matter which month is selected the same info
			// will be displayed on the Usage Trending charts 
			
			try{
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
				
				UsageTrending.verifyUsageTrendingChartTooltipOneVendor(UsageHelper.usageTrendingDomesticChart, valuesFromFile, UsageHelper.categoryVoice);
				Thread.sleep(2000);
				
				UsageTrending.verifyUsageTrendingChartTooltipOneVendor(UsageHelper.usageTrendingRoamingChart, valuesFromFile, UsageHelper.categoryVoice);
				Thread.sleep(2000);				
			/*	
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
				
				UsageTrending.verifyUsageTrendingChartTooltipOneVendor(UsageHelper.usageTrendingDomesticChart, valuesFromFile, UsageHelper.categoryData);
				Thread.sleep(2000);
				
				UsageTrending.verifyUsageTrendingChartTooltipOneVendor(UsageHelper.usageTrendingRoamingChart, valuesFromFile, UsageHelper.categoryData);
				Thread.sleep(2000);				
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
				
				UsageTrending.verifyUsageTrendingChartTooltipOneVendor(UsageHelper.usageTrendingDomesticChart, valuesFromFile, UsageHelper.categoryMessages);
				Thread.sleep(2000);
				
				UsageTrending.verifyUsageTrendingChartTooltipOneVendor(UsageHelper.usageTrendingRoamingChart, valuesFromFile, UsageHelper.categoryMessages);
				Thread.sleep(2000);
			*/	
			} catch(NullPointerException e){
				
				System.out.println("chart not found");
				
			}
						
		}
	
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
