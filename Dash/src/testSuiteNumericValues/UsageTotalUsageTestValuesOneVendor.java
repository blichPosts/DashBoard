package testSuiteNumericValues;

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



public class UsageTotalUsageTestValuesOneVendor extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test 
	public static void UsageTotalUsageTestValuesOneVendorTest() throws Exception
	{

		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		
		// Get all the vendor names listed on the PoV section
		for (WebElement vendor: vendors) {
			vendorNames.add(vendor.getText());
		}
		
		
		String path = UsageHelper.path;

		// Run the test for each vendor 
		for (String vendorSelected: vendorNames) {
			
			String vendor = vendorSelected;
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendor);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
						
			CommonTestStepActions.GoToUsagePageDetailedWait();
				
			// #1 Select Vendor View 
			UsageHelper.selectVendorView();
			
			
			// #2 Read data from file
			List<UsageOneMonth> valuesFromFile = ReadFilesHelper.getDataFromSpreadsheet(completePath);
			
				
			// #3 Select only one vendor
			CommonTestStepActions.UnSelectAllVendors();
			CommonTestStepActions.selectOneVendor(vendor);
			
			String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
			
			UsageOneMonth oneMonthData;
			String year =  "";
			String month = "";
			String monthYearToSelect = "";
						
			int indexMonth = 0;
			
			do {
			
				// Get the data for the selected vendor and the month indicated by indexMonth
				oneMonthData = valuesFromFile.get(indexMonth);   
				
				String[] monthYear = UsageHelper.getMonthYearToSelect(oneMonthData);
				month = monthYear[0];
				year = monthYear[1];
				
				monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
				System.out.println("Month Year: " + monthYearToSelect);
				
				// #4 Select month on month/year selector
				CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
				
				Thread.sleep(2000);
				
				// #5 Verify that the values displayed on the tooltips of Total Usage charts are the same as the ones read from file  
				
				UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryVoice);
				
//				TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, oneMonthData, UsageHelper.categoryVoice);
//				Thread.sleep(1000);
//				
//				TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, oneMonthData, UsageHelper.categoryVoice);
//				Thread.sleep(1000);				
//				
//				UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryData);
//				
//				TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, oneMonthData, UsageHelper.categoryData);
//				Thread.sleep(1000);
//				
//				TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, oneMonthData, UsageHelper.categoryData);
//				Thread.sleep(1000);				
//				
//				UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryMessages);
//				
//				TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, oneMonthData, UsageHelper.categoryMessages);
//				Thread.sleep(1000);
//				
//				TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, oneMonthData, UsageHelper.categoryMessages);
				Thread.sleep(1000);
								
				indexMonth++;
				
			} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
						
		}
	
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Total Usage values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
