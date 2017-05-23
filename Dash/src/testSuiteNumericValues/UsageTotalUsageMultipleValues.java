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
import usage.TotalUsageValues;



public class UsageTotalUsageMultipleValues extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void UsageTotalUsageMultipleValuesTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		Thread.sleep(2000);
		
		// Reload Fleet data
		ReadFilesHelper.reloadFleetData();
		Thread.sleep(2000);

		// Wait for countries and vendors to be loaded on PoV section
		WaitForElementPresent(By.cssSelector(".tdb-povGroup>.tdb-povGroup"), ExtremeTimeout);
		
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
			
		String path = UsageHelper.path;
		int amountOfVendors = 10;

		if (amountOfVendors > vendorNames.size()) {
			amountOfVendors = vendorNames.size();
		}
		
		System.out.println("Amount of Vendors Selected: " + amountOfVendors);
		
		// #1 Select Vendor View and Unselect all vendors  
		UsageHelper.selectVendorView();
		CommonTestStepActions.UnSelectAllVendors();
		
		Thread.sleep(2000);
		
		List<List<UsageOneMonth>> listVendorsSelectedData = new ArrayList<>();
		
		// Run the test for each vendor 
		for(int i = 0; i < amountOfVendors; i++){
			
			String vendor = vendorNames.get(i);
			String vendorSelected = vendorNames.get(i);
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendorSelected);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
						
			// #2 Read data from file
			List<UsageOneMonth> valuesFromFileOneVendor = ReadFilesHelper.getJsonDataExpenseUsage(vendor);  // getDataFromSpreadsheet(completePath);
			listVendorsSelectedData.add(valuesFromFileOneVendor);
				
			// #3 Select one vendor
			CommonTestStepActions.selectOneVendor(vendor);
			
		}
		
		String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
		String monthYearToSelect = "";
					
		int indexMonth = 0;
	
		List<String> monthsToSelect = UsageHelper.getMonthListUnifiedForVendorsSelected(listVendorsSelectedData);
		
		do {
		
			// List listOneMonthData will have the data for a specific month, for all the vendors previously selected
			List<UsageOneMonth> listOneMonthData = new ArrayList<>();
			
			for(List<UsageOneMonth> values: listVendorsSelectedData){
				
				int indexMonthForVendorSelected = 0;
				boolean dataFoundForMonth = false;
				
				while(indexMonthForVendorSelected < values.size() && !dataFoundForMonth){
					
					String monthYear = CommonTestStepActions.convertMonthNumberToName(values.get(indexMonthForVendorSelected).getOrdinalMonth(), values.get(indexMonthForVendorSelected).getOrdinalYear()); 
					
					if(monthsToSelect.get(indexMonth).equals(monthYear)){
 
						listOneMonthData.add(values.get(indexMonthForVendorSelected));
						dataFoundForMonth = true;
						
					}
						
					indexMonthForVendorSelected++;
				}
					
				
			}
			
			// Vendors are listed in alphabetical order in the vertical axis
			List<UsageOneMonth> listOneMonthSortedByVendor = UsageHelper.sortVendorsAlphabetically(listOneMonthData);
			
			
			// #4 Select month on month/year selector
			CommonTestStepActions.selectMonthYearPulldown(monthsToSelect.get(indexMonth));
			System.out.println("monthYear: " + monthsToSelect.get(indexMonth)); 
			
			Thread.sleep(2000);
			
			// #5 Verify that the values displayed on the tooltips of Total Usage charts are the same as the ones read from file  
			
			UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryVoice);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, listOneMonthSortedByVendor, UsageHelper.categoryVoice);
			Thread.sleep(2000);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, listOneMonthSortedByVendor, UsageHelper.categoryVoice);
			Thread.sleep(2000);				
			
			UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryData);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, listOneMonthSortedByVendor, UsageHelper.categoryData);
			Thread.sleep(2000);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, listOneMonthSortedByVendor, UsageHelper.categoryData);
			Thread.sleep(2000);				
			
			UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryMessages);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, listOneMonthSortedByVendor, UsageHelper.categoryMessages);
			Thread.sleep(2000);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, listOneMonthSortedByVendor, UsageHelper.categoryMessages);
			Thread.sleep(2000);
						
			indexMonth++;

			
		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector) && indexMonth < monthsToSelect.size());
		
				
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
